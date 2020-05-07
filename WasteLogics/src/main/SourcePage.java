package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourcePage {
    private WebDriver driver;
    private String orderIdXpath = "//tbody[@class='   gl-1 ']//td[@class='cal'][1]";

    public SourcePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int getIndexOfOrder(String orderId) {
        int index = 0;
        List<WebElement> elements = driver.findElements(By.xpath(orderIdXpath));
        for (WebElement element : elements) {
            if (!element.getText().equals(orderId)) {
                index++;
            }
            else {
                break;
            }
        }
        return index;
    }

    public String getCompanyName(int orderIndex) {
        String xpath = String.format("(//tbody[@class='gl-0 tgl eo ui-selectee'])[%d]//td[@class='cal'][3]", orderIndex);
        return getElementFromTable(xpath);
    }

    public String getInvoiceAddress(int orderIndex) {
        String xpath = String.format("(//tbody[@class='   gl-1 '])[%d]//td[@colspan='3']", orderIndex);
        return getElementFromTable(xpath);
    }

    public String getElementFromTable(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        return element.getText().trim();
    }

    public Map<String, String> getGrades(int orderIndex) {
        String xpathOfGradeLine = "/following-sibling::tbody[@class='   gl-2 '][following::tbody[@class='   gl-1 ']]";
        String xpathOfValue = "*/td[@class='car']";
        return getElementsMap(orderIndex, xpathOfGradeLine, xpathOfValue);
    }

    public Map<String, String> getPrices(int orderIndex) {
        String xpathOfPriceLine = "/following-sibling::tbody[@class='   gl-3 '][following::tbody[@class='   gl-1 ']]";
        String xpathOfValue = "*/td[@class='car'][5]";
        return getElementsMap(orderIndex, xpathOfPriceLine, xpathOfValue);
    }

    public Map<String, String> getElementsMap(int orderIndex, String xpathOfLine, String xpathOfValue) {
        String xpathOfParentElement = String.format("(%s)[%d]/ancestor::tbody", orderIdXpath, orderIndex + 1);
        String xpathOfReportElements = xpathOfParentElement + xpathOfLine;
        List<WebElement> rowsList = driver.findElements(By.xpath(xpathOfReportElements));
        Map<String, String> elementsMap = new HashMap<String, String>();
        for (WebElement row : rowsList) {
            String key = row.findElement(By.xpath("*/td[@class='cal fc ']")).getText().trim();
            String value = row.findElement(By.xpath(xpathOfValue)).getText().trim();
            elementsMap.put(key, value);
        }
        return elementsMap;
    }
}
