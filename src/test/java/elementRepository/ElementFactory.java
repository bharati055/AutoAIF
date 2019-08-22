package elementRepository;


import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utility.BrowserFactory;
import utility.CommonFunctions;
import utility.Log;
import utility.Reporting;

public class ElementFactory {
	
	private static FileInputStream repoFile;
	public static WebElement ele=null;
	private static Map<String, Properties> eleRepo = new HashMap<String, Properties>();
	static CommonFunctions comFunc = new CommonFunctions();
	
	/**Catches the repository object for future use
	 * @return
	 */
	public static Properties getRepository(){
		Log.info("Function Execution: ElementFactory>>getRepository");
		
		Properties propObj = null;
		try {
			
			propObj = eleRepo.get("repository");
			if(propObj==null){
				repoFile = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\java\\elementRepository\\element.properties");
				propObj = new Properties();
				propObj.load(repoFile);
				Log.info("Function Execution: ElementFactory>>getRepository>> Repository is catched.");
			}
			
			eleRepo.put("repository", propObj);
			return propObj;
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Reporting.reportStep(false, "Get Repository.", "Repository is catched", "Operation failed. Reason - "+e.toString(), false);
			
			e.printStackTrace();
			return  propObj;
		}

	}
	
	/**Returns WebElement for element name
	 * @param eleName
	 * @return
	 */
	public static WebElement getElement(String eleName){
		
		Log.info("Function Execution: ElementFactory>>getElement");
		
		WebDriver driver = BrowserFactory.getBrowser(comFunc.tempXML_readNode("browser"));
		Properties propObj = ElementFactory.getRepository();
		String readLine = propObj.getProperty(eleName);
		String splitLine[] = readLine.split(";");
	
		String strLocatorType = splitLine[0];
		String strLocatorValue = splitLine[1];
			
			//System.out.println("Element Factory - Locator type - "+splitLine[0]);
			//System.out.println("Element Factory - Locator value - "+splitLine[1]);
			
		try{
			switch(strLocatorType.toLowerCase()){
				case "name":
					ele = driver.findElement(By.name(strLocatorValue));
					break;
				case "id":
					ele = driver.findElement(By.id(strLocatorValue));
					break;
				case "xpath":
					ele = driver.findElement(By.xpath(strLocatorValue));
					break;
				case "classname":
					ele = driver.findElement(By.className(strLocatorValue));
					break;	
				case "cssselector":
					ele = driver.findElement(By.cssSelector(strLocatorValue));
					break;	
				default:
					System.out.println("locator type invalid");
					Reporting.reportStep(false, "GetElement", "Element Found in Repo", "Element - "+eleName+" not found in repository.", false);
			}	
			return ele;
		}catch(Exception e){
			
			Log.info(e.toString());
			Reporting.reportStep(false, "GetElement", "Element Found in Repo", "Error Occured - "+e.toString(), false);
			return ele;
		}
		
	}
}
