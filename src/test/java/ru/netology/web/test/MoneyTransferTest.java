package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        var LoginPage = new LoginPage();
        var autoInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(autoInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(autoInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

    }

    @Test
    void transferValid() {
        var firstCardNumber = getOneCardNumber();
        var twoCardNumber = getTwoCardNumber();
        var oneCardBalance = dashboardPage.getCardBalance(0);
        var twoCardBalance = dashboardPage.getCardBalance(1);
        var amount = DataHelper.generateValidAmount(oneCardBalance);
        var expectedBalanceOneCard = oneCardBalance - amount;
        var expectedBalanceTwoCard = twoCardBalance + amount;
        var transferPages = dashboardPage.selectCardToTransfer(twoCardNumber);
        dashboardPage = transferPages.setRefill(amount,getOneCardNumber());
        var actualBalanceOneCard = dashboardPage.getCardBalance(0);
        var actualBalanceTwoCard = dashboardPage.getCardBalance(1);
        assertEquals(expectedBalanceOneCard, actualBalanceOneCard);
        assertEquals(expectedBalanceTwoCard ,actualBalanceTwoCard);
    }
    @Test
    void transferInvalid() {
        var firstCardNumber = getOneCardNumber();
        var twoCardNumber = getTwoCardNumber();
        var oneCardBalance = dashboardPage.getCardBalance(0);
        var twoCardBalance = dashboardPage.getCardBalance(1);
        var amount = DataHelper.generateInvalidAmount(twoCardBalance);
        var transferPages = dashboardPage.selectCardToTransfer(getOneCardNumber());
        transferPages.refill(amount,getTwoCardNumber());
        transferPages.setErrorNotification("Ошибка");
        var actualBalanceOneCard = dashboardPage.getCardBalance(0);
        var actualBalanceTwoCard = dashboardPage.getCardBalance(1);
        assertEquals(oneCardBalance, actualBalanceOneCard);
        assertEquals(twoCardBalance, actualBalanceTwoCard);
    }
}
