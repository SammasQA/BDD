package ru.netology.ibank.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");   // отправитель
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");


    public DashboardPage transfer(int amount, String toCardNumber) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(toCardNumber);
        transferButton.click();
        return new DashboardPage();
    }
}