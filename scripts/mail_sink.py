#!/usr/bin/env python3
"""SMTP giả cho máy local: nhận thư và ghi ra file, không gửi đi đâu.
Cách dùng: python3 scripts/mail_sink.py <file_ghi_thu> [port=2525]"""
import socketserver
import sys

OUTPUT = sys.argv[1]
PORT = int(sys.argv[2]) if len(sys.argv) > 2 else 2525


class Handler(socketserver.StreamRequestHandler):
    def reply(self, text):
        self.wfile.write((text + "\r\n").encode())

    def handle(self):
        self.reply("220 mail-sink")
        while line := self.rfile.readline():
            command = line.decode(errors="replace").strip().upper()
            if command.startswith("DATA"):
                self.reply("354 end with .")
                data = []
                while (row := self.rfile.readline()) and row.rstrip(b"\r\n") != b".":
                    data.append(row)
                with open(OUTPUT, "ab") as out:
                    out.write(b"".join(data) + b"\n=====END=====\n")
                self.reply("250 OK")
            elif command.startswith("QUIT"):
                self.reply("221 bye")
                return
            else:
                self.reply("250 OK")


socketserver.ThreadingTCPServer.allow_reuse_address = True
with socketserver.ThreadingTCPServer(("127.0.0.1", PORT), Handler) as server:
    server.serve_forever()
