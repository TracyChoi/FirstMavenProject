package stepdefinition;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

import base.DriverContext;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.service.ExtentService;
import driver.MyChromeDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import test.TestRun;
import utilities.ExcelUtil;
import base.Base;
import utilities.ScreenShotUtil;

public class Hook extends Base {
    private static ExtentTest feature,scenarios;
    private static String featureName="";

    @Before
    public void beforeScenario(Scenario scenario) throws IOException {
        ScreenShotUtil.setScenario(scenario);
        String testResultFolder = ExtentService.getScreenshotFolderName().split("/")[1];
        System.setProperty("currReportFolder", testResultFolder);
        Collection<String> tags = scenario.getSourceTagNames();
        String jiraTaskName = null;
        for(String tag: tags){
            System.out.println(tag);
        }
        Base.setCaseID(scenario.getName());
        // System.out.println("This will run before the Scenario");
        System.out.println("-----------------------------------");
        System.out.println("Starting - " + scenario.getName());
        System.out.println("-----------------------------------");

//		String[] path = scenario.getId().split("/");
//		String currentFeature=path[path.length-1].replace(".feature","");
//		if(!currentFeature.equals(featureName)){
//			featureName=currentFeature;
//			feature = TestRun.extent.createTest(Feature.class,featureName);
//		}
//		scenarios = feature.createNode(String.valueOf(Scenario.class), "Jeff returns a faulty microwave");

        if (scenario.getName().equals("AIAPartner (Regression) Admin Portal Login - IFA>9 staff login") || scenario.getName().equals("AIAPartner (Regression) Admin Portal Login - IFA=6 staff login") || scenario.getName().equals("AIAPartner (Regression) Admin Portal Login - IFA=1 staff login")){
            String driverPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "driver" + File.separator + "chromedriverNew.exe";
            System.setProperty("webdriver.chrome.driver", driverPath);
            System.out.println("-----------------------------------");
            System.out.println("incognito mode");
            System.out.println("-----------------------------------");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            DriverContext.setDriver (new MyChromeDriver(options));
        }else{
            setCapabilities();
        }






        ExtentService.getInstance().setSystemInfo("Hostname", InetAddress.getLocalHost().getHostName());
        ExtentService.getInstance().setSystemInfo("Test Script Version", "v0.1");
        ExtentService.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
        ExtentService.getInstance().setSystemInfo("Browser", DriverContext.getDriver().getCapabilities().getBrowserName());
        ExtentService.getInstance().setSystemInfo("Browser Version", DriverContext.getDriver().getCapabilities().getVersion());

        ExcelUtil.readExcel(scenario.getName());
    }

//	@AfterStep
//	public void doSomethingAfterStep(Scenario scenario){
//		scenarios.createNode(Given.class,"AAA").pass("pass");
//	}

    @After
    public void afterScenario(Scenario scenario) {
        Long height = (Long) DriverContext.getDriver().executeScript("return Math.max(document.body.scrollHeight,1000)");
        System.out.println("****Page height : "+height);
        DriverContext.getDriver().manage().window().setSize(new Dimension(1920, Math.toIntExact(height)));
        if(scenario.getStatus().toString().equalsIgnoreCase("failed")){
            ScreenShotUtil.saveScreenShotForStep("error");
        }
        DriverContext.getDriver().quit();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {

        if ("FAILED".equals(scenario.getStatus().toString())) {
            ScreenShotUtil.saveScreenShotForStep("error");
        }

    }


}
