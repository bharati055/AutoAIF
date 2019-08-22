package utility;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class BrowserFactory {
	private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
	private static CommonFunctions comFunc = new  CommonFunctions();
	/*
	 * Factory method for getting browsers
	 */
	public static WebDriver getBrowser(String browserType) {
		WebDriver driver = null;
		Log.info("Function Execution: BrowserFactory>>getBrowser for - "+browserType);
		
		switch (browserType.toLowerCase()) {
		case "firefox":
			
			driver = drivers.get("firefox");
			
			if (driver == null) {
				Log.info("Browser Initiation for - "+browserType);
				driver = new FirefoxDriver();
				drivers.put("firefox", driver);
			}else{	
				Log.info("Function Execution: BrowserFactory>>getBrowser for. Firefox driver found in factory");
			}
			break;
		case "ie":
			
			driver = drivers.get("ie");
			if (driver == null) {
				Log.info("Browser Initiation for - "+browserType);
				System.setProperty("webdriver.ie.driver","src\\test\\resources\\webDtiver\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				drivers.put("ie", driver);
			}
			break;
		case "chrome":
			
			driver = drivers.get("chrome");
			if (driver == null) {
				Log.info("Browser Initiation for - "+browserType);
				System.setProperty("webdriver.chrome.driver","src\\test\\resources\\webDtiver\\ChromeDriver.exe");
				driver = new ChromeDriver();
				drivers.put("chrome", driver);
			}
			break;
		}
		return driver;
	}
 
	/**
	 * 
	 */
	public static void closeAllDriver() {
		Log.info("Function Execution: BrowserFactory>>closeAllDriver");
		
		for (String key : drivers.keySet()) {
			drivers.get(key).close();
			//drivers.get(key).quit();
		}
		drivers.clear();
	}

	/**Launch Application
	 * @param url
	 * @param expectedPageTitle
	 * @return
	 */
	public static boolean launchApp(String url,String expectedPageTitle){
		
		Log.info("Function Execution: BrowserFactory>>launchApp");
		Log.info("Launch Browser call for url - "+url + ". Expected title - "+expectedPageTitle );
		
		WebDriver driver = BrowserFactory.getBrowser(comFunc.tempXML_readNode("browser"));
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		System.out.println("inside launch brower function");
		if(driver.getTitle().equals(expectedPageTitle)){
			return true;
		}else{
			Reporting.reportStep(false, "Launch URL - " +url, "Expected page title - " + expectedPageTitle, "Validation failed. Actual title - "+driver.getTitle(), true);
			return false;
		}
	}
}
