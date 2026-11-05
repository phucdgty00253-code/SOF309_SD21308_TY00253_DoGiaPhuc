#!/usr/bin/env python3
"""Add the Saltair logo to square product images without modifying the raw files."""
from __future__ import annotations
import argparse
from pathlib import Path
from PIL import Image, ImageOps


def mean_luminance(image: Image.Image) -> float:
    gray = ImageOps.grayscale(image)
    return sum(gray.resize((32, 32)).get_flattened_data()) / (32 * 32)


def foreground_ratio(image: Image.Image, box: tuple[int, int, int, int]) -> float:
    patch = image.crop(box).convert("RGB").resize((48, 48))
    pixels = list(patch.get_flattened_data())
    foreground = sum(1 for r, g, b in pixels if (abs(r - 244) + abs(g - 244) + abs(b - 244)) > 54)
    return foreground / len(pixels)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("input_dir", type=Path, help="Folder containing raw product images")
    parser.add_argument("output_dir", type=Path, help="Folder for logo-composited images")
    parser.add_argument("logo", type=Path, help="Black transparent PNG logo")
    parser.add_argument("--logo-white", type=Path, help="White transparent PNG logo")
    parser.add_argument("--opacity", type=float, default=0.85, help="Logo opacity (default: 0.85)")
    args = parser.parse_args()
    if not 0 <= args.opacity <= 1:
        parser.error("--opacity must be between 0 and 1")
    if not args.input_dir.is_dir():
        parser.error(f"input folder does not exist: {args.input_dir}")
    if not args.logo.is_file():
        parser.error(f"logo file does not exist: {args.logo}")

    black_logo = Image.open(args.logo).convert("RGBA")
    white_logo = Image.open(args.logo_white).convert("RGBA") if args.logo_white else None
    images = sorted(p for p in args.input_dir.rglob("*") if p.suffix.lower() in {".jpg", ".jpeg", ".png"})
    if not images:
        parser.error("no images found in input folder")
    args.output_dir.mkdir(parents=True, exist_ok=True)
    moved, used_white = [], []
    for source in images:
        with Image.open(source) as opened:
            base = ImageOps.fit(opened.convert("RGB"), (1200, 1200), method=Image.Resampling.LANCZOS)
        w, h = base.size
        logo_w = round(w * 0.12)
        margin = round(w * 0.05)
        logo = black_logo
        ratio = logo_w / logo.width
        scaled = logo.resize((logo_w, round(logo.height * ratio)), Image.Resampling.LANCZOS)
        scaled_alpha = scaled.getchannel("A").point(lambda a: round(a * args.opacity))
        scaled.putalpha(scaled_alpha)
        lw, lh = scaled.size
        positions = {
            "right": (w - margin - lw, h - margin - lh),
            "left": (margin, h - margin - lh),
        }
        boxes = {side: (x, y, x + lw, y + lh) for side, (x, y) in positions.items()}
        scores = {side: foreground_ratio(base, box) for side, box in boxes.items()}
        side = "right"
        if scores["right"] > 0.12 and scores["left"] + 0.08 < scores["right"]:
            side = "left"
            moved.append(source.name)
        x, y = positions[side]
        if mean_luminance(base.crop(boxes[side])) < 110 and white_logo is not None:
            white = white_logo.resize((logo_w, round(white_logo.height * logo_w / white_logo.width)), Image.Resampling.LANCZOS)
            white.putalpha(white.getchannel("A").point(lambda a: round(a * args.opacity)))
            scaled = white
            used_white.append(source.name)
        canvas = base.convert("RGBA")
        canvas.alpha_composite(scaled, (x, y))
        dest = args.output_dir / source.relative_to(args.input_dir).with_suffix(".jpg")
        dest.parent.mkdir(parents=True, exist_ok=True)
        canvas.convert("RGB").save(dest, "JPEG", quality=85, optimize=True, progressive=True)
    print(f"Processed {len(images)} product images")
    print(f"Moved to lower-left to avoid product overlap: {', '.join(moved) if moved else 'none'}")
    print(f"Used white logo for dark placement area: {', '.join(used_white) if used_white else 'none'}")

if __name__ == "__main__":
    main()

