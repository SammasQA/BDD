package ru.netology.ibank.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.ibank.page.*;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private static final String sutUrl = "http://localhost:9999";
    private static final String login = "vasya";
    private static final String password = "qwerty123";
    private static final String code = "12345";


    private static final String card1Number = "5559 0000 0000 0001";
    private static final String card2Number = "5559 0000 0000 0002";

    private static Process sutProcess;

    @BeforeAll
    static void startSut() throws IOException {

        String jarPath = "artifacts/app-ibank-build-for-testers.jar";
        sutProcess = new ProcessBuilder("java", "-jar", jarPath).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void stopSut() {
        if (sutProcess != null) {
            sutProcess.destroy();
        }
    }

    @BeforeEach
    void loginAndGoToDashboard() {
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));

        open(sutUrl);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(login, password);
        DashboardPage dashboardPage = verificationPage.validVerify(code);
        webdriver().shouldHave(url(sutUrl + "/dashboard"));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        DashboardPage dashboardPage = new DashboardPage();

        int balanceCard1Before = dashboardPage.getCardBalance(0); // отправитель
        int balanceCard2Before = dashboardPage.getCardBalance(1); // получатель

        int transferAmount = 3000;


        TransferPage transferPage = dashboardPage.clickTransferButton(1);

        dashboardPage = transferPage.transfer(transferAmount, card1Number);

        int balanceCard1After = dashboardPage.getCardBalance(0);
        int balanceCard2After = dashboardPage.getCardBalance(1);

        assertEquals(balanceCard1Before - transferAmount, balanceCard1After,
                "Баланс карты-отправителя должен уменьшиться");
        assertEquals(balanceCard2Before + transferAmount, balanceCard2After,
                "Баланс карты-получателя должен увеличиться");
    }


}