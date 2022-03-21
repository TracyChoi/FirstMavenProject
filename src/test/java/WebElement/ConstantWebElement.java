package WebElement;

import base.BasePage;
import constant.xpath_Locator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class ConstantWebElement extends BasePage {

    @FindAll({
            @FindBy(xpath = xpath_Locator.searchTypingBar)
    })
    public WebElement searchTypingBar;

    @FindAll({
            @FindBy(xpath = xpath_Locator.wirteSomeText)
    })
    public WebElement wirteSomeText;



}
