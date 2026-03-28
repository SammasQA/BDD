package ru.netology.ibank.test;

import org.junit.jupiter.api.*;
import ru.netology.ibank.data.DataHelper;
import ru.netology.ibank.page.*;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


        int senderIndex = 0;
        int receiverIndex = 1;
        String senderCardNumber = DataHelper.getFirstCardNumber();
        String receiverCardNumber = DataHelper.getSecondCardNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderIndex);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverIndex);

        int transferAmount = Math.min(balanceSenderBefore / 2, 5000);

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverIndex);
        dashboardPage = transferPage.transfer(transferAmount, senderCardNumber);

        int balanceSenderAfter = dashboardPage.getCardBalance(senderIndex);
        int balanceReceiverAfter = dashboardPage.getCardBalance(receiverIndex);

        assertEquals(balanceSenderBefore - transferAmount, balanceSenderAfter,
                "Баланс карты-отправителя должен уменьшиться на сумму перевода");
        assertEquals(balanceReceiverBefore + transferAmount, balanceReceiverAfter,
                "Баланс карты-получателя должен увеличиться на сумму перевода");
    }

    @Test
    @Disabled("Дефект")
    void shouldNotTransferMoreThanBalance() {
        DashboardPage dashboardPage = new DashboardPage();

        int senderIndex = 0;
        int receiverIndex = 1;
        String senderCardNumber = DataHelper.getFirstCardNumber();

        int balanceSenderBefore = dashboardPage.getCardBalance(senderIndex);
        int balanceReceiverBefore = dashboardPage.getCardBalance(receiverIndex);

        int transferAmount = balanceSenderBefore + 1000;

        TransferPage transferPage = dashboardPage.clickTransferButton(receiverIndex);
        dashboardPage = transferPage.transfer(transferAmount, senderCardNumber);

        int balanceSenderAfter = dashboardPage.getCardBalance(senderIndex);
        int balanceReceiverAfter = dashboardPage.getCardBalance(receiverIndex);

        assertEquals(balanceSenderBefore, balanceSenderAfter,
                "Баланс карты-отправителя не должен измениться при превышении суммы перевода");
        assertEquals(balanceReceiverBefore, balanceReceiverAfter,
                "Баланс карты-получателя не должен измениться при превышении суммы перевода");
    }
}