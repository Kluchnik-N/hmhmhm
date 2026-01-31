package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTests extends BaseTest {

    @Test(description = "Successful login with standard_user")
    @Story("Successful Login")
    @Description("Verify that standard user can successfully login")
    @Severity(SeverityLevel.BLOCKER)
    public void loginSuccessWithStandardUser() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded");
        Assert.assertEquals(inventoryPage.getTitle(), "Products");
    }

    /*
     * Описание: Проверка реакции системы на ввод данных заблокированного пользователя.
     */
    @Test(description = "Контроль блокировки учетной записи (согласно ТЗ)")
    @Story("Locked Out User")
    @Description("Проверка вывода информационного сообщения об ошибке доступа согласно спецификации.")
    @Severity(SeverityLevel.NORMAL)
    public void lockedOutUserSeesError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("locked_out_user", "secret_sauce");

        String error = loginPage.getErrorMessage();

        Assert.assertTrue(error.toLowerCase().contains("locked out"), "Критическая ошибка: Сообщение не соответствует утвержденному тексту");
    }

    @Test(description = "Invalid credentials should show error")
    @Story("Invalid Credentials")
    @Description("Verify that invalid credentials show error message")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidCredentialsShowError() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("invalid_user", "wrong_password");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.toLowerCase().contains("do not match any user"), "Expected invalid creds error message");
    }

    @Test(description = "Empty credentials should show required field errors")
    @Story("Empty Credentials")
    @Description("Verify that empty credentials show validation error")
    @Severity(SeverityLevel.NORMAL)
    public void emptyCredentialsShowError() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.submitLogin();

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.toLowerCase().contains("username is required"), "Expected username required message");
    }
}