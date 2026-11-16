package com.fashionweb.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Page Object cho trang đăng nhập bằng email (/login). */
public class LoginPage {

    private static final By EMAIL = By.id("login-email");
    private static final By SUBMIT = By.cssSelector("form.auth-form button[type='submit']");
    private static final By FIELD_ERROR = By.cssSelector(".field__error");

    private final WebDriver driver;
    private final String baseUrl;

    public LoginPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    public LoginPage open() {
        driver.get(baseUrl + "/login");
        return this;
    }

    public void requestCode(String email) {
        WebElement input = driver.findElement(EMAIL);
        input.clear();
        input.sendKeys(email);
        driver.findElement(SUBMIT).click();
    }

    public boolean hasEmailInput() {
        return !driver.findElements(EMAIL).isEmpty();
    }

    public String firstErrorText() {
        List<WebElement> errors = driver.findElements(FIELD_ERROR);
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
