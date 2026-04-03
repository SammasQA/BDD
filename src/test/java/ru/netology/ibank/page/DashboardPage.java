package ru.netology.ibank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    // Конструктор ждёт загрузки страницы (появление хотя бы одной карты)
    public DashboardPage() {
        cards.first().shouldBe(visible);
    }

    private SelenideElement getCardByMaskedNumber(String maskedNumber) {
        return cards.findBy(text(maskedNumber));
    }

    public int getCardBalance(String maskedNumber) {
        SelenideElement card = getCardByMaskedNumber(maskedNumber);
        String text = card.getText();
        return extractBalance(text);
    }

    public TransferPage clickTransferButton(String receiverMaskedNumber) {
        SelenideElement card = getCardByMaskedNumber(receiverMaskedNumber);
        card.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public void verifyCardBalance(String maskedNumber, int expectedBalance) {
        SelenideElement card = getCardByMaskedNumber(maskedNumber);
        card.shouldBe(visible);
        String expectedText = balanceStart + expectedBalance + balanceFinish;
        card.shouldHave(text(expectedText));
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        if (start == -1 || finish == -1) {
            throw new IllegalStateException("Не удалось найти баланс в тексте: " + text);
        }
        String balanceStr = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(balanceStr);
    }
}