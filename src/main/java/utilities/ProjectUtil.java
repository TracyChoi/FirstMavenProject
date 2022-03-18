package utilities;

import base.DriverContext;
//import constant.ConstantFile;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ProjectUtil {
	
	public static synchronized void openUrl(String url) {
		ChromeDriver driver = DriverContext.getDriver();
		driver.get(url);
	}



	public static synchronized boolean waitElementVisible(String xPath, int timeout) {
		ChromeDriver driver = DriverContext.getDriver();
		boolean visible = false;
		int i = 0;
		while(!visible){
			i++;
			ProjectUtil.waitForSeconds(1);

			try {
				visible = (new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))) != null);
			}catch (Exception e){
			}

			if(visible || i == 2){
				break;
			}
		}
		return visible;
	}

	public static synchronized void clickElement(String xPath) {
		ChromeDriver driver = DriverContext.getDriver();
		driver.findElements(By.xpath(xPath)).stream().filter(e->e.isDisplayed()).findFirst().get().click();
	}

	public static synchronized String getText(String xPath) {
		ChromeDriver driver = DriverContext.getDriver();
		return driver.findElement(By.xpath(xPath)).getText();
	}

    public static synchronized void setListValue(List<String> list, String value) {
		ChromeDriver driver = DriverContext.getDriver();
        boolean flag = false;
        for(String elementName : list){
            try{
				driver.findElementByXPath(elementName).clear();
                ProjectUtil.setValue(elementName, value);
                flag = true;
				break;
            }catch (Exception e){
            }

        }

		if(!flag){
			throw new RuntimeException("Fail to set value");
		}
    }
	
	public static synchronized void setValue(String xPath, String value) {
		ChromeDriver driver = DriverContext.getDriver();
		driver.findElement(By.xpath(xPath)).clear();
		driver.findElement(By.xpath(xPath)).sendKeys(value);
	}

	public static synchronized void setValue(String xPath, Keys value) {
		ChromeDriver driver = DriverContext.getDriver();
		driver.findElement(By.xpath(xPath)).sendKeys(value);
	}
	
	public static synchronized void selectByLabel(String xPath, String value, boolean exactMatch) {
		ChromeDriver driver = DriverContext.getDriver();
		Select select = new Select(driver.findElement(By.xpath(xPath)));
		select.selectByVisibleText(value);
	}
	
	public static synchronized void selectByValue(String xPath, String value, boolean exactMatch) {
		ChromeDriver driver = DriverContext.getDriver();
		Select select = new Select(driver.findElement(By.xpath(xPath)));
		select.selectByValue(value);
	}

	public static synchronized boolean isElementVisible(By by,int timeout){
		WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(),timeout);
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
   public static synchronized void waitForSeconds(double waitTime){

        try {
            Thread.sleep((long) waitTime * 1000);
        } catch (InterruptedException e) {
//            throw new TestingException("Hit error during wait", e);
        }
    }
	
   
    public static synchronized boolean waitLoadingComplete(int timeout){
		waitForSeconds(1);
		ChromeDriver driver = DriverContext.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long)js.executeScript("return jQuery.active") == 0);
				}
				catch (Exception e) {
					// no jQuery present
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return js.executeScript("return document.readyState")
						.toString().equals("complete");
			}
		};
		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}
    
    public static synchronized void clickUsingJS(String xpath) {
		ChromeDriver driver = DriverContext.getDriver();
		WebElement element = driver.findElement(By.xpath(xpath));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click()", element);
	}
    
	public static String getRandomName(){
		Random random=new Random();
		StringBuffer sb = new StringBuffer();

		for(int i=0; i<6; i++){
			long result=0;
			result=Math.round(Math.random()*25+65);
			sb.append(String.valueOf((char)result));
		}

		return sb.toString();
	}
	
	public static synchronized void removeReadOnly(String id){
		ChromeDriver driver = DriverContext.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String command = "document.getElementById(\"" + id + "\").removeAttribute(\"readonly\")";
		executor.executeScript(command);
	}
	
	public static synchronized void sendKeysByJS(String id, String text){
		ChromeDriver driver = DriverContext.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String command = "document.getElementById(\"" + id + "\").value=\""+ text + "\"";
		executor.executeScript(command);
	}
	
	public static synchronized boolean isNullOrEmpty(String value){
		return (null == value || "".equals(value));
	}
	
	public static synchronized void waitElementClickable(String xpath, int timeout){
		WebDriverWait driverWait = new WebDriverWait(DriverContext.getDriver(), timeout);
		driverWait.until(ExpectedConditions.elementToBeClickable (By.xpath(xpath)));
	}


	public static synchronized BigDecimal toBigDecimal(String value){
		System.out.println("toBigDecimal value = " + value);

		if(value.indexOf(",") != -1){
			value = value.replace(",", "");
		}

		if(isNullOrEmpty(value)){
			System.out.println("toBigDecimal value is Null or empty, default as 0");
			return new BigDecimal(0);
		}else{
			System.out.println("toBigDecimal value NOT Null or empty");
			return new BigDecimal(value);
		}
	}

	public static synchronized String calculateDOB(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.YEAR, -18);

		return sdf.format(calendar.getTime());
	}


	public static synchronized void moveToElement(String xpath){
		ChromeDriver driver = DriverContext.getDriver();
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElementByXPath(xpath)).perform();
	}

	public static synchronized String getTitle(){
		return DriverContext.getDriver().getTitle();
	}

	public static synchronized String getUrl(){
		return DriverContext.getDriver().getCurrentUrl();
	}

	public static synchronized boolean isSameValue(BigDecimal bigDecimal1, BigDecimal bigDecimal2){
		return (bigDecimal1.compareTo(bigDecimal2) == 0);
	}

//	public static synchronized void handleCheckBox(String expectedValue, String statusFlagXpath, String clickableXpath){
//		ChromeDriver driver = DriverContext.getDriver();
//
//		if(AIAPartnerUtil.isElementVisible(By.xpath(clickableXpath), ConstantFile.TIMEOUT5)) {
//			if ("UNCHECK".equalsIgnoreCase(expectedValue)) {
//				if ("0".equalsIgnoreCase(driver.findElementByXPath(statusFlagXpath).getAttribute("value"))) {
//					AIAPartnerUtil.clickElement(clickableXpath);
//				}
//			}
//
//			if ("CHECK".equalsIgnoreCase(expectedValue)) {
//				if (!"0".equalsIgnoreCase(driver.findElementByXPath(statusFlagXpath).getAttribute("value"))) {
//					AIAPartnerUtil.clickElement(clickableXpath);
//				}
//			}
//		}
//	}

	public static synchronized void setCheckbox(boolean expectValue, String xpath) {
		ChromeDriver driver = DriverContext.getDriver();
		WebElement box = driver.findElement(By.xpath(xpath));
		if (box.isSelected()!=expectValue){
			box.click();
		}
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum.replaceAll("[A-Za-z$\\\\s]+",""));
		} catch (NumberFormatException nfe) {
			System.out.println("Checking String ["+strNum+"] is not numeric");
			return false;
		}
		return true;
	}

	public static String changeMonthFromNum(String text) {
		String result = null;
		//if (language.equalsIgnoreCase("English")||language.equalsIgnoreCase("Simple Chinese") ) {
		switch (text) {
			case "01":
				result = "Jan";
				break;
			case "02":
				result = "Feb";
				break;
			case "03":
				result = "Mar";
				break;
			case "04":
				result = "Apr";
				break;
			case "05":
				result = "May";
				break;
			case "06":
				result = "Jun";
				break;
			case "07":
				result = "Jul";
				break;
			case "08":
				result = "Aug";
				break;
			case "09":
				result = "Sep";
				break;
			case "10":
				result = "Oct";
				break;
			case "11":
				result = "Nov";
				break;
			case "12":
				result = "Dec";
				break;

		}
		return result;
	}
	
}
	
