package ru.netology.ibank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    // Селектор для карт (может потребоваться уточнить)
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    // Получить баланс по индексу карты (0-based)
    public int getCardBalance(int index) {
        String text = cards.get(index).text();
        return extractBalance(text);
    }

    // Получить баланс по идентификатору data-test-id (если есть)
    public int getCardBalance(String cardId) {
        SelenideElement cardElement = $("[data-test-id='" + cardId + "']");
        String text = cardElement.text();
        return extractBalance(text);
    }

    // Нажать "Пополнить" для карты по индексу
    public TransferPage clickTransferButton(int index) {
        SelenideElement cardElement = cards.get(index);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    // Нажать "Пополнить" для карты по data-test-id
    public TransferPage clickTransferButton(String cardId) {
        SelenideElement cardElement = $("[data-test-id='" + cardId + "']");
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    // Вырезать баланс из текста элемента
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