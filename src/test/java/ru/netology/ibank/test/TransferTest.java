package ru.netology.ibank.test;

import org.junit.jupiter.api.*;
import ru.netology.ibank.data.DataHelper;
import ru.netology.ibank.page.*;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class TransferTest {

    private static final String sutUrl = "http://localhost:9999";

    @BeforeEach
    void loginAndGoToDashboard() {
        open(sutUrl);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(
                DataHelper.getValidLogin(),
                DataHelper.getValidPassword()
        );
        verificationPage.validVerify(DataHelper.getValidVerificationCode());
        webdriver().shouldHave(url(sutUrl + "/dashboard"));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        DashboardPage dashboardPage = new DashboardPage();

        String senderCard = DataHelper.getFirstCardMaskedNumber();
        String receiverCard = DataHelper.getSecondCardMaskedNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderCard);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverCard);

        int transferAmount = Math.min(balanceSenderBefore / 2, 5000);

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverCard);
        // В TransferPage метод transfer теперь принимает номер карты-отправителя
        dashboardPage = transferPage.transfer(transferAmount, DataHelper.getFirstCardNumber());


        dashboardPage.verifyCardBalance(senderCard, balanceSenderBefore - transferAmount);
        dashboardPage.verifyCardBalance(receiverCard, balanceReceiverBefore + transferAmount);
    }

    @Test
    @Disabled("Дефект")
    void shouldNotTransferMoreThanBalance() {
        DashboardPage dashboardPage = new DashboardPage();

        String senderCard = DataHelper.getFirstCardMaskedNumber();
        String receiverCard = DataHelper.getSecondCardMaskedNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderCard);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverCard);

        int transferAmount = balanceSenderBefore + 1000;

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverCard);
        dashboardPage = transferPage.transfer(transferAmount, DataHelper.getFirstCardNumber());

        // Балансы не должны измениться
        dashboardPage.verifyCardBalance(senderCard, balanceSenderBefore);
        dashboardPage.verifyCardBalance(receiverCard, balanceReceiverBefore);
    }
}