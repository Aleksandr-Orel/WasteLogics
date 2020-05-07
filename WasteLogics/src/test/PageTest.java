package test;
import io.github.bonigarcia.wdm.WebDriverManager;
import main.SourcePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import java.util.Map;

public class PageTest {
    private final String TARGET_COMPANY = "TEST CUSTOMER";
    private final String TARGET_INVOICE_ADDRESS = "TEST ADDRESS, TEST TOWN, 111111";
    private final String TARGET_GRADE = "Mixed Municipal Waste";
    private final String TARGET_WEIGHT = "0.460 T";
    private final String TARGET_FLAT_CHARGE_PRICE = "£100.00";
    private final String TARGET_PER_TONNE_PRICE = "£4.60";
    private final String TARGET_ITEM_PRICE = "£110.10";
    private final String TARGET_ORDER_ID = "146566";
    private int orderIndex;
    private WebDriver driver;
    private Map<String, String> prices;
    private Map<String, String> grades;

    @BeforeTest
    public void openBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File file = new File("Company invoices - Waste Logics.mhtml");
        String filePath = file.getAbsolutePath();
        driver.get(filePath);
    }

    @Test
    public void testSourcePage() {
        SourcePage sourcePage = new SourcePage(driver);
        orderIndex = sourcePage.getIndexOfOrder(TARGET_ORDER_ID);
        prices = sourcePage.getPrices(orderIndex);
        grades = sourcePage.getGrades(orderIndex);
        String company = sourcePage.getCompanyName(orderIndex);
        String invoiceAddress = sourcePage.getInvoiceAddress(orderIndex);

        Assert.assertEquals(TARGET_COMPANY, company, "company");
        Assert.assertEquals(TARGET_INVOICE_ADDRESS, invoiceAddress, "invoice address");
        Assert.assertTrue(grades.containsKey(TARGET_GRADE));
        Assert.assertTrue(grades.containsValue(TARGET_WEIGHT));
        Assert.assertEquals(TARGET_FLAT_CHARGE_PRICE, prices.get("Flat charge"), "flat charge price");
        Assert.assertEquals(TARGET_PER_TONNE_PRICE, prices.get("per tonne"), "per tonne price");
        Assert.assertEquals(TARGET_ITEM_PRICE, prices.get("Item"), "item price");
    }

    @AfterTest
    public void closeBrowser() {
        driver.quit();
        driver = null;
    }
}

