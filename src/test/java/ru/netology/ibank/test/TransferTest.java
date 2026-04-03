package ru.netology.ibank.test;

import org.junit.jupiter.api.*;
import ru.netology.ibank.data.DataHelper;
import ru.netology.ibank.page.*;

import static com.codeborne.selenide.Selenide.open;

public class TransferTest {

    private static final String sutUrl = "http://localhost:9999";
    private DashboardPage dashboardPage;   // сохраняем логин

    @BeforeEach
    void loginAndGoToDashboard() {
        open(sutUrl);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(
                DataHelper.getValidLogin(),
                DataHelper.getValidPassword()
        );

        dashboardPage = verificationPage.validVerify(DataHelper.getValidVerificationCode());
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        String senderCard = DataHelper.getFirstCardMaskedNumber();
        String receiverCard = DataHelper.getSecondCardMaskedNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderCard);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverCard);

        int transferAmount = Math.min(balanceSenderBefore / 2, 5000);

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverCard);
        dashboardPage = transferPage.transfer(transferAmount, DataHelper.getFirstCardNumber());

        dashboardPage.verifyCardBalance(senderCard, balanceSenderBefore - transferAmount);
        dashboardPage.verifyCardBalance(receiverCard, balanceReceiverBefore + transferAmount);
    }

    @Test
    @Disabled("Дефект")
    void shouldNotTransferMoreThanBalance() {
        String senderCard = DataHelper.getFirstCardMaskedNumber();
        String receiverCard = DataHelper.getSecondCardMaskedNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderCard);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverCard);

        int transferAmount = balanceSenderBefore + 1000;

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverCard);
        dashboardPage = transferPage.transfer(transferAmount, DataHelper.getFirstCardNumber());

        dashboardPage.verifyCardBalance(senderCard, balanceSenderBefore);
        dashboardPage.verifyCardBalance(receiverCard, balanceReceiverBefore);
    }
}