package com.fashionweb.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Lớp cha cho mọi test giao diện (Selenium).
 * Chỉ chạy khi gọi {@code mvn test -Pui} và web đang chạy ở {@link #BASE_URL}.
 * Đổi địa chỉ: {@code mvn test -Pui -DbaseUrl=http://localhost:8080/saltair}.
 */
@Tag("ui")
public abstract class BaseUITest {

    protected static final String BASE_URL =
            System.getProperty("baseUrl", "http://localhost:8080/saltair");

    protected WebDriver driver;

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getenv("CI")) || Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
