package ru.netology.ibank.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public DashboardPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();
        // Проверяем, что после ввода кода открылась страница Dashboard
        webdriver().shouldHave(url("http://localhost:9999/dashboard"));
        return new DashboardPage();
    }
}