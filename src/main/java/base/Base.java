package base;

import com.aventstack.extentreports.gherkin.model.Scenario;
//import driver.MyChromeDriver;

import java.io.File;
import java.io.IOException;

import driver.MyChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import test.TestRun;

import org.openqa.selenium.Proxy;

public class Base {
    public static Scenario scenario;

    private static String caseID;
    public static String platform;
    private static long start_time;

    public static void setStartTime() {
        start_time = System.currentTimeMillis();
    }

    public static String getRunTime() {
        long run_time = System.currentTimeMillis() - start_time;
        long currentMS = run_time % 1000;
        long totalSeconds = run_time / 1000;
        long currentSecond = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;
        long currentMinute = totalMinutes % 60;

        String use_time = String.valueOf(currentMinute) + "m" + String.valueOf(currentSecond) + "."
                + String.valueOf(currentMS) + "s";
        return use_time;
    }

    public static String getCaseID() {
        return caseID;
    }

    public static void setCaseID(String caseID) {
        Base.caseID = caseID;
    }

    public static void setCapabilities() throws IOException {
//		String driverPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "driver" + File.separator + "chromedriver91.exe";
        String driverPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "driver" + File.separator + "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);

        ITestContext itc = TestRun.itc;
//		String headlessFlag = itc.getCurrentXmlTest().getParameter("headlessFlag");
        String headlessFlag = "True";

        if("".equalsIgnoreCase(headlessFlag) || "True".equalsIgnoreCase(headlessFlag)) {
            ChromeOptions options = new ChromeOptions();

            //options.addArguments("--proxy-server=10.7.192.136:10938");
//			options.addArguments("disable-gpu");
//			options.addArguments("--remote-debugging-port=9222");
//			options.addArguments("window-size=1920,1080");
//			options.addArguments("--headless");

            DriverContext.setDriver (new MyChromeDriver(options));
        }else{

            ChromeOptions options = new ChromeOptions();

            Proxy proxy = new Proxy();
            proxy.setProxyType(Proxy.ProxyType.PAC);
            proxy.setProxyAutoconfigUrl("http://pac.zscalertwo.net/aia.com/AIA_HK_PROD.pac");
            options.setProxy(proxy);
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("disable-gpu");
            options.addArguments("window-size=1920,1080");
            options.addArguments("--headless");

            DriverContext.setDriver (new MyChromeDriver(options));

        }
    }

    public static void delay(long delaySec) {
        try {
            Thread.sleep(delaySec * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void renameReportFolder() {
        String oldPath = System.getProperty("user.dir") + File.separator + "output" + File.separator + "report.html";
        String newPath = System.getProperty("user.dir") + File.separator + "output" + File.separator + "screenshots" + File.separator + caseID + File.separator + "report.html";
        File newFile = new File(newPath);
        new File(oldPath).renameTo(newFile);
        System.out.println("report renamed to " + caseID + "_report.html");
    }
}
