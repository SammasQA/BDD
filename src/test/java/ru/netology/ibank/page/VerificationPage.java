package ru.netology.ibank.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public DashboardPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();

        return new DashboardPage();
    }
}