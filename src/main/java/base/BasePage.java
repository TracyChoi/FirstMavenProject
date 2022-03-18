package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import utilities.ExcelUtil;
import utilities.ScreenShotUtil;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {
    protected static ChromeDriver driver = DriverContext.getDriver();

    public BasePage() {
        PageFactory.initElements(DriverContext.getDriver(), this);
    }


}
