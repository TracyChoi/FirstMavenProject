package test;


import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.InetAddress;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
//		monochrome = true,
//		plugin = { "pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" },
//		plugin = { "pretty", "json:target/cucumber-reports/Cucumber.json" },
//		plugin = { "json:target/cucumber-reports/Cucumber.json" },
        plugin = {
                "pretty",
                "json:output/json-report/cucumber.json",
//				"com.epam.reportportal.cucumber.ScenarioReporter",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        features = "src/feature",
        glue = { "stepdefinition" },
        tags =  "@RT"
)
public class TestRun extends AbstractTestNGCucumberTests {
    public static ITestContext itc;


    @BeforeSuite
    public void beforeSuite(ITestContext itc) throws Exception {
        this.itc = itc;

        try {
            if (InetAddress.getAllByName("AIA.BIZ").length==0){
                throw new Exception("Not in AIA internal network, tests aborted");
            }
        } catch (Exception e) {
            throw new Exception("Not in AIA internal network, tests aborted");
        }
    }

    @AfterSuite
    public void afterClass() {
//		ExtentService.getInstance().setSystemInfo("Hostname", "abc");
//		ExtentService.getInstance().setSystemInfo("IP", "123.45.67.89");
    }

    @BeforeClass
    public static void setup() throws Exception{
    }

//	@Before
//	public void beforeScenario(){
//		extent.createTest("Z");
//	}

//	@BeforeSuite
//	public void beforeSuite(ITestContext itc) throws Exception {
//		this.itc = itc;
//	}
//
//	@BeforeClass
//	public static void setup() {
////		Properties props = System.getProperties();
////		props.setProperty("extent.reporter.klov.start","true");
//
//		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
//		extentProperties.setKlovServerUrl("http://hkeqxuwapp030:80");
//		// specify project
//		// ! you must specify a project, other a "Default project will be used"
//		extentProperties.setKlovProjectName("iShop_Dev");
//		// you must specify a reportName otherwise a default timestamp will be used
//		extentProperties.setKlovReportName("TestReport");
//
//		// Mongo DB Configuration
//		extentProperties.setMongodbHost("http://hkeqxuwapp030");
//		extentProperties.setMongodbPort(27017);
//		extentProperties.setMongodbDatabase("klov");
//	}

//	@Override
//	@DataProvider(parallel = true)
//	public Object[][] features() {
//		Object[][] a = super.features();
//		return a;
//	}

    @AfterSuite
    public void afterClass(ITestContext itc) throws Exception {

        String srcPath= ExtentService.getScreenshotFolderName().replace("/"+ExtentService.getScreenshotReportRelatvePath(),"");
        copyFolder(Paths.get("output/json-report/"),Paths.get(srcPath+ File.separator +"json-report"));
        if(itc.getCurrentXmlTest().getParameter("jiraUploadFlag").equalsIgnoreCase("true")){
            System.out.println("******Start Jira Upload********");
            jiraUpload();
        }else{
            System.out.println("******Jira Upload Skipped********");
            System.out.println(itc.getCurrentXmlTest().getParameter("jiraUploadFlag"));
        }
    }

    private void jiraUpload(){
        try {
//			String resultJson=String.format("\"%s\\output\\%s\\json-report\\cucumber.json\"",System.getProperty("user.dir"),System.getProperty("currReportFolder"));
            String resultJson=String.join(File.separator,System.getProperty("user.dir"),"output",System.getProperty("currReportFolder"),"json-report","cucumber.json");
            System.out.println(resultJson);
//			String infoJson="\""+System.getProperty("user.dir")+"\\resource\\jiraInfo.json\"";
            String infoJson=String.join(File.separator,System.getProperty("user.dir"),"resource","jiraInfo.json");
            String cmd = String.format("curl -u PnCBot:PnCAdmin! -X POST -F info=@%s -F result=@%s http://aiahk-jira.aia.biz/rest/raven/1.0/import/execution/cucumber/multipart",
                    infoJson,
                    resultJson);
//			System.out.println(cmd);

//			Runtime rt = Runtime.getRuntime();
            ProcessBuilder PB = new ProcessBuilder("curl","-u","E110607:AIAAmb03","-X","POST","-F","info=@"+infoJson,"-F","result=@"+resultJson,"http://aiahk-jira.aia.biz/rest/raven/1.0/import/execution/cucumber/multipart");
            System.out.println(PB.command());
            PB.redirectErrorStream(true);
            Process process = PB.start();
//
//			Process proc = rt.exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private void copy(Path source, Path dest) {
        try {
            Files.deleteIfExists(dest);
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
