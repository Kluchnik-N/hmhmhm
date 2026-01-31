package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

@Epic("E-Commerce")
@Feature("Purchase Flow")
public class PurchaseTests extends BaseTest {

    @Test(description = "Successful purchase of a single product")
    @Story("Single Product Purchase")
    @Description("Verify complete purchase flow with a single product")
    @Severity(SeverityLevel.BLOCKER)
    public void purchaseSingleProduct() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded");
        inventoryPage.addFirstProductToCart();

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemsCount(), 1, "Cart should contain 1 item");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.continueToOverview();
        Assert.assertEquals(checkoutPage.getCartItemsCount(), 1, "Overview should show 1 item");

        CheckoutCompletePage completePage = checkoutPage.finish();
        Assert.assertTrue(completePage.isLoaded(), "Complete page should be loaded");
        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!",
                "Success message should be displayed");
    }

    @Test(description = "Successful purchase of multiple products")
    @Story("Multiple Products Purchase")
    @Description("Verify complete purchase flow with multiple products")
    @Severity(SeverityLevel.CRITICAL)
    public void purchaseMultipleProducts() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded");
        inventoryPage.addProductsToCart(3);
        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemsCount(), 3, "Cart should contain 3 items");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("Jane", "Smith", "54321");
        checkoutPage.continueToOverview();
        Assert.assertEquals(checkoutPage.getCartItemsCount(), 3, "Overview should show 3 items");

        CheckoutCompletePage completePage = checkoutPage.finish();
        Assert.assertTrue(completePage.isLoaded(), "Complete page should be loaded");
        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!",
                "Success message should be displayed");
    }

    @Test(description = "Purchase with price calculation verification")
    @Story("Price Calculation")
    @Description("Verify price calculations including subtotal, tax and total")
    @Severity(SeverityLevel.NORMAL)
    public void purchaseWithPriceVerification() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded");
        inventoryPage.addProductsToCart(2);

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemsCount(), 2, "Cart should contain 2 items");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("Test", "User", "11111");
        checkoutPage.continueToOverview();

        double subtotal = checkoutPage.getSubtotal();
        double tax = checkoutPage.getTax();
        double total = checkoutPage.getTotal();

        Assert.assertTrue(subtotal > 0, "Subtotal should be greater than 0");
        Assert.assertTrue(tax > 0, "Tax should be greater than 0");
        Assert.assertTrue(total > 0, "Total should be greater than 0");
        
        double calculatedTotal = subtotal + tax;
        Assert.assertTrue(Math.abs(total - calculatedTotal) < 0.01,
                "Total should equal subtotal + tax. Expected: " + calculatedTotal + ", Actual: " + total);

        CheckoutCompletePage completePage = checkoutPage.finish();
        Assert.assertTrue(completePage.isLoaded(), "Complete page should be loaded");
        Assert.assertEquals(completePage.getSuccessMessage(), "Thank you for your order!",
                "Success message should be displayed");
    }
}

