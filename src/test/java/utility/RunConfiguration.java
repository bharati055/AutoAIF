package utility;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import elementRepository.ElementFactory;

public abstract class RunConfiguration{
	 CommonFunctions comFunc = 	new CommonFunctions();
	 
	 @Parameters({"browserType"})
	 @BeforeSuite(alwaysRun=true)
	 public void runLevel_setup(String browserType){
		 System.out.println("Inside setup - @BeforeSuite");
		 comFunc.envSetup(browserType);
	 }
	 
	 @BeforeClass(alwaysRun=true)
	 public void testLevel_setup(){
		 System.out.println("Inside setup - @BeforeClass");
		 BrowserFactory.getBrowser(comFunc.tempXML_readNode("browser"));
		 ElementFactory.getRepository();
		 Log.startTestCase(comFunc.getCurrentTestName());
	 }
	 
	 @AfterClass(alwaysRun=true)
	 public void testLevel_tearDown(){
		 System.out.println("Inside setup - @AfterClass");
		 Reporting.endCurrentTest(false);
		 Log.endTestCase(comFunc.getCurrentTestName());
	 }
	 
	 @AfterSuite(alwaysRun=true)
	 public void runLevel_tearDown(){
		 System.out.println("Inside setup - @AfterSuite");
		 Reporting.endExecution();
	 }
}

