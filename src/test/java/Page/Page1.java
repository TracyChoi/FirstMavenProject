package Page;

import WebElement.ConstantWebElement;
import base.BasePage;
import base.DriverContext;
import constant.xpath_Locator;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import utilities.ScreenShotUtil;
import utilities.WebDriverUtil;
import org.testng.Assert;
import java.util.concurrent.TimeUnit;
import static base.Base.delay;



public class Page1 extends BasePage {

    private final ConstantWebElement webElement = new ConstantWebElement();
    private final constant.xpath_Locator xpathLocator = new xpath_Locator();

    WebDriver driver = DriverContext.getDriver();
    public void CypressDocPage() {
//        WebDriver driver = DriverContext.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get("https://docs.cypress.io/guides/references/assertions#Chai");
        driver.manage().deleteAllCookies();
        delay(1);
        ScreenShotUtil.addScreenShot();
    }

    public void helloWorld() {
        System.out.println("i am here");
        WebDriverUtil.clickElement(xpathLocator.searchBar);
        webElement.searchTypingBar.sendKeys("Hello World");
        webElement.searchTypingBar.sendKeys(Keys.ENTER);
        delay(2);
        ScreenShotUtil.addScreenShot();
    }

    public void clickFirstChoice() {
        WebDriverUtil.clickElement(xpathLocator.helloWorldFirstChoice);
        delay(2);
        ScreenShotUtil.addScreenShot();
    }

    public void checktxtFileMsg() {
        delay(2);
        Assert.assertTrue(webElement.wirteSomeText.isDisplayed());
        ScreenShotUtil.addScreenShot();
    }




}
