package Page;

import base.Base;
import base.DriverContext;
import org.openqa.selenium.WebDriver;
import utilities.ExcelUtil;
import utilities.ScreenShotUtil;

import java.util.concurrent.TimeUnit;

public class Page1 {

    public void googleChrome() {
        WebDriver driver = DriverContext.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get("https://www.google.com.hk");
        driver.manage().deleteAllCookies();
        Base.delay(5);
        ScreenShotUtil.addScreenShot();
    }

//    public void typeing() {
//        constantWebElement.userIDTextField.sendKeys(ExcelUtil.getValue("userName"));
//        Base.delay(5);
//        ScreenShotUtil.addScreenShot();
//    }


}
