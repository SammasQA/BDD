package ru.netology.ibank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(int index) {
        String text = cards.get(index).text();
        return extractBalance(text);
    }

    public int getCardIndexByNumber(String cardNumber) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).text().contains(cardNumber)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Карта с номером " + cardNumber + " не найдена");
    }

    public TransferPage clickTransferButton(int index) {
        SelenideElement cardElement = cards.get(index);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
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

    public void verifyTransfer(int fromIndex, int toIndex, int amount,
                               int expectedFromBefore, int expectedToBefore) {
        int balanceFromAfter = getCardBalance(fromIndex);
        int balanceToAfter = getCardBalance(toIndex);

        assertEquals(expectedFromBefore - amount, balanceFromAfter,
                "Баланс карты-отправителя должен уменьшиться на сумму перевода");
        assertEquals(expectedToBefore + amount, balanceToAfter,
                "Баланс карты-получателя должен увеличиться на сумму перевода");
    }
}