package utilities;

import base.DriverContext;
//import com.aiatss.uplifttest.constant.ConstantFile;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class WebDriverUtil {



	public static synchronized void openUrl(String url) {
		WebDriver driver = DriverContext.getDriver();
		driver.get(url);
	}

	public static synchronized void scroll2elementPresent(String xPath) {
		WebDriver driver = DriverContext.getDriver();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);",driver.findElement(By.xpath(xPath)));
	}

	public static synchronized void scrollUp2elementPresent(String xPath) {
		WebDriver driver = DriverContext.getDriver();
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);",driver.findElement(By.xpath(xPath)));
	}

	public static synchronized void uploadTheFile(String uploadBtn, String fileLocation, String fileName) {
		String js = "arguments[0].style.visibility = 'visible';";
		WebDriver driver = DriverContext.getDriver();
		WebElement upload = driver.findElement(By.xpath(uploadBtn));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript(js,upload);
		String uploadTheFile = System.getProperty("user.dir") + File.separator + "resource" + File.separator + fileLocation + File.separator + fileName.trim();
		System.out.println(uploadTheFile);
		upload.sendKeys(uploadTheFile);
		System.out.println("Chose File");
		ScreenShotUtil.addScreenShot();
	}


	public boolean isFileDownloaded(String downloadPath, String fileName) {
		boolean flag = false;
		File dir = new File(downloadPath);
		File[] dir_contents = dir.listFiles();

		for (int i = 0; i < dir_contents.length; i++) {
			if (dir_contents[i].getName().contains(fileName))
				flag = true;
		}

		return flag;

	}


	public static synchronized boolean waitElementVisible(String xPath, int timeout) {
		WebDriver driver = DriverContext.getDriver();
		boolean visible = false;
		int i = 0;
		while(!visible){
			i++;
			WebDriverUtil.waitForSeconds(1);

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
		WebDriver driver = DriverContext.getDriver();
		driver.findElements(By.xpath(xPath)).stream().filter(WebElement::isDisplayed).findFirst().get().click();
	}

	public static synchronized void submitInputForm(String xPath) {
		WebDriver driver = DriverContext.getDriver();
		driver.findElements(By.xpath(xPath)).stream().filter(WebElement::isDisplayed).findFirst().get().submit();
	}

	public static synchronized String getText(String xPath) {
		WebDriver driver = DriverContext.getDriver();
		return driver.findElement(By.xpath(xPath)).getText();
	}

	public static synchronized void setListValue(List<String> list, String value) {
		WebDriver driver = DriverContext.getDriver();
		boolean flag = false;
		for(String elementName : list){
			try{
				driver.findElement(By.xpath(elementName)).clear();
				WebDriverUtil.setValue(elementName, value);
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
		WebDriver driver = DriverContext.getDriver();
		driver.findElement(By.xpath(xPath)).clear();
		driver.findElement(By.xpath(xPath)).sendKeys(value);
	}

	public static synchronized void setValue(String xPath, Keys value) {
		WebDriver driver = DriverContext.getDriver();
		driver.findElement(By.xpath(xPath)).sendKeys(value);
	}

	public static synchronized void selectByLabel(String xPath, String value, boolean exactMatch) {
		WebDriver driver = DriverContext.getDriver();
		Select select = new Select(driver.findElement(By.xpath(xPath)));
		try {
			select.selectByVisibleText(value);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("\nAvailable options:"+select.getOptions().stream().map(el->el.getAttribute("text")).collect(Collectors.joining(" | "))+"\n"+"Target options:"+value);
		}
	}

	public static synchronized void selectByValue(String xPath, String value, boolean exactMatch) {
		WebDriver driver = DriverContext.getDriver();
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
		WebDriver driver = DriverContext.getDriver();
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
		WebDriver driver = DriverContext.getDriver();
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
		WebDriver driver = DriverContext.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String command = "document.getElementById(\"" + id + "\").removeAttribute(\"readonly\")";
		executor.executeScript(command);
	}

	public static synchronized void sendKeysByJS(String id, String text){
		WebDriver driver = DriverContext.getDriver();
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
		WebDriver driver = DriverContext.getDriver();
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
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
//		WebDriver driver = DriverContext.getDriver();
//
//		if(WebDriverUtil.isElementVisible(By.xpath(clickableXpath), ConstantFile.TIMEOUT5)) {
//			if ("UNCHECK".equalsIgnoreCase(expectedValue)) {
//				if ("0".equalsIgnoreCase(driver.findElement(By.xpath(statusFlagXpath)).getAttribute("value"))) {
//					WebDriverUtil.clickElement(clickableXpath);
//				}
//			}
//
//			if ("CHECK".equalsIgnoreCase(expectedValue)) {
//				if (!"0".equalsIgnoreCase(driver.findElement(By.xpath(statusFlagXpath)).getAttribute("value"))) {
//					WebDriverUtil.clickElement(clickableXpath);
//				}
//			}
//		}
//	}

	public static synchronized void setCheckbox(boolean expectValue, String xpath){
		WebDriver driver = DriverContext.getDriver();
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

	public static synchronized void selectTodayCalendar(String yearXpath,String monthXpath,String dayXpath){
		WebDriver driver = DriverContext.getDriver();
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date currentDate = new Date();
		String nowDateTime = formatDate.format(currentDate);
		String[] nowDateList = nowDateTime.split("/");
		String nowDay = nowDateList[0];
		String nowMonth = nowDateList[1];
		String nowYear = nowDateList[2];

		String nowYearXpath = String.format((yearXpath), nowYear);
		driver.findElement(By.xpath(nowYearXpath)).click();
		String nowMonthXpath = String.format((monthXpath), nowMonth);
		driver.findElement(By.xpath(nowMonthXpath)).click();
		String nowDayXpath = String.format((dayXpath), nowDay);
		driver.findElement(By.xpath(nowDayXpath)).click();
	}


	public static synchronized void selectFromDayCalendar(String yearXpath,String monthXpath,String dayXpath){
		WebDriver driver = DriverContext.getDriver();
		String fromDateField = ExcelUtil.getValue("fromDate");
		String[] fromDateList = fromDateField.split("/");

		String fromMonth = fromDateList[0];
		String fromDay = fromDateList[1];
		String fromYear = fromDateList[2];
		String nowYearXpath = String.format((yearXpath), fromYear);
		driver.findElement(By.xpath(nowYearXpath)).click();
		String nowMonthXpath = String.format((monthXpath), fromMonth);
		driver.findElement(By.xpath(nowMonthXpath)).click();
		String nowDayXpath = String.format((dayXpath), fromDay);
		driver.findElement(By.xpath(nowDayXpath)).click();
	}










}

