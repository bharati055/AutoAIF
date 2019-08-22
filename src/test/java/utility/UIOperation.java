package utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import elementRepository.ElementFactory;

public class UIOperation {
	
	private WebDriver driver; 
	private CommonFunctions comFunc = new CommonFunctions();
	
	public UIOperation(){
		driver = BrowserFactory.getBrowser(comFunc.tempXML_readNode("browser"));
	}
	
	/**Launch application
	 * @return
	 */
	public boolean launchApplication(){
		driver.get(comFunc.getEnvData("AppURL"));
		driver.manage().window().maximize();
		try{
			System.out.println(driver.getCurrentUrl());
			return true;
		}catch (Exception e){
			return false;
		}
	}
	
	/**Click web element when element is passed
	 * @param driver
	 * @param ele
	 * @return
	 */
	public boolean clickElement(WebElement ele){
		Log.info("UIOperation>> ClickElement. Element Name - " + this.getElementName(ele));
		
		if(this.elementExist(ele)){
			
			ele.click();
			return true;
		}else{
			Reporting.reportStep(false, "Click Element", "Element is clicked - " + this.getElementName(ele), "Failed to click", true);
			return false;
		}
	}
	
	/**Click web element when element name as a string is passed
	 * @param driver
	 * @param eleName
	 * @return
	 */
	public boolean clickElement(String eleName){
		Log.info("UIOperation>> ClickElement. Element Name - " + eleName);
		WebElement ele = ElementFactory.getElement(eleName);
		if(this.elementExist(ele)){
			ele.click();
			return true;
		}else{
			Reporting.reportStep(false, "Click Element", "Element is clicked - " + eleName, "Failed to click", true);
			return false;
		}
	}
	
	/** Click element and validate element when webElements are passed
	 * @param driver
	 * @param ele
	 * @param eleToValidate
	 * @return
	 */
	public boolean clickElement(WebElement ele,WebElement eleToValidate){
		Log.info("UIOperation>> ClickElement. Element Name - " + this.getElementName(ele));
		if(this.elementExist(ele)){
			ele.click();
					if (this.elementExist(eleToValidate)){
						return true;
					}else{
						Reporting.reportStep(false, "Click Element", "Element is clicked - " + this.getElementName(ele), "Failed to click", true);
						return false;
					}
		}else{
			return false;
		}
	}
	
	/** Click element and validate element when webElements are passed
	 * @param driver
	 * @param ele
	 * @param eleToValidate
	 * @return
	 */
	public boolean clickElement(String eleName ,String eleNameToValidate){
		Log.info("UIOperation>> ClickElement. Element Name - " + eleName);
		
		WebElement ele = ElementFactory.getElement(eleName);
		WebElement eleToValidate = ElementFactory.getElement(eleNameToValidate);
		if(this.elementExist(ele)){
			ele.click();
					if (this.elementExist(eleToValidate)){
						return true;
					}else{
						Reporting.reportStep(false, "Click Element", "Element is clicked - " + eleName, "Failed to click", true);
						return false;
					}
		}else{
			return false;
		}
	}
	
	/**Set test fields with the value when element is passed
	 * @param driver
	 * @param ele
	 * @param strValue
	 * @return
	 */
	public boolean setText(WebElement ele,String strValue){
		Log.info("UIOperation>> SetText. Element Name - " + this.getElementName(ele));
		
		if(this.elementExist(ele)){
			ele.sendKeys(strValue);
			return true;
		}else{
			Reporting.reportStep(false, "Set field", "Test is set in field - " + this.getElementName(ele), "Failed to set text", true);
			return false;
		}
	}
	
	/**Set test fields with the value when element name is passed
	 * @param driver
	 * @param ele
	 * @param strValue
	 * @return
	 */
	public boolean setText(String eleName,String strValue){
		Log.info("UIOperation>> SetText. Element Name - " + eleName);
		
		WebElement ele = ElementFactory.getElement(eleName);
		if(this.elementExist(ele)){
			ele.sendKeys(strValue);
			return true;
		}else{
			Reporting.reportStep(false, "Set field", "Test is set in field - " + eleName, "Failed to set text", true);
			return false;
		}
	}
	
	/** method to set password when webelement is passed
	 * @param ele
	 * @param strValue
	 * @return
	 */
	public boolean setPassword(WebElement ele,String strValue){
		Log.info("UIOperation>> SetPassword. Element Name - " + this.getElementName(ele));
		
		if(this.elementExist(ele)){
			ele.sendKeys(CommonFunctions.decryptPassword(strValue));
			return true;
		}else{
			Reporting.reportStep(false, "Set password field", "Password is set in - " + this.getElementName(ele), "Failed to set password", true);
			return false;
		}
	}
	
	/** method to set password when webelement name is passed
	 * @param ele
	 * @param strValue
	 * @return
	 */
	public boolean setPassword(String eleName,String strValue){
		Log.info("UIOperation>> SetPassword. Element Name - " + eleName);
		WebElement ele = ElementFactory.getElement(eleName);
		
		if(this.elementExist(ele)){
			ele.sendKeys(strValue);
			return true;
		}else{
			Reporting.reportStep(false, "Set password field", "Password is set in - " + eleName, "Failed to set password", true);
			return false;
		}
	}
	
	/**Validate element exists
	 * @param driver
	 * @param ele
	 * @return
	 */
	public boolean elementExist(WebElement ele){
		Log.info("UIOperation>> Element Exist. Element Name - " + this.getElementName(ele));
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(ele));
		if(ele.isDisplayed()){
			return true;
		}else{
			Reporting.reportStep(false, "Element Exist", "Element exists - " + this.getElementName(ele), "Element Does not exist.", true);
			return false;
		}
		
	}
	
	/**Validate element exists
	 * @param driver
	 * @param ele
	 * @return
	 */
	public boolean elementExist(String eleName){
		Log.info("UIOperation>> Element Exist. Element Name - " + eleName);
		
		WebElement ele = ElementFactory.getElement(eleName);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(ele));
		if(ele.isDisplayed()){
			return true;
		}else{
			Reporting.reportStep(false, "Element Exist", "Element exists - " + eleName, "Element Does not exist.", true);
			return false;
		}
		
	}
	
	/**Capture screen shot for test run
	 * @param driver
	 */
	public String captureScreenNSave(){
		Log.info("UIOperation>> captureScreenNSave. Element Name - ");
		
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String strDestFile=null;
		
		Date myDate = new Date();
		String strDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myDate);
		strDateTime = strDateTime.replace(":", "");
		String strTestName = comFunc.getCurrentTestName();
		
		strDestFile = "ScrnSht_"+strTestName+strDateTime+ ".png";
		
		try {
			FileUtils.copyFile(srcFile, new File("src//test//resources//temp//screenshot//" + strDestFile ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Reporting.reportStep(false, "ScreenCapture", "Done", "Failed. Reason- "+e.toString(), true);
			e.printStackTrace();
		} 
		return strDestFile;
		
	}
	
	/** return tag text
	 * @param ele
	 * @return
	 */
	public String getElementName(WebElement ele){
		//Log.info("UIOperation>> getElementName");
		return ele.getText();
		//return "";
		
	}

	/** This method waits for element for certain timeout
	 * @param ele
	 * @return
	 */
	public boolean waitForElement(WebElement ele, int timeoutInSeconds){
		Log.info("UIOperation>> waitForElement. Element Name - " + this.getElementName(ele));
		
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		
		wait.until(ExpectedConditions.visibilityOf(ele));
		if(ele.isDisplayed()){
			return true;
		}else{
			//Reporting.reportStep(false, "Element Exist", "Element exists - " + this.getElementName(ele), "Element Does not exist.", true);
			return false;
		}
		
	}
	
	/**Validate element exists
	 * @param driver
	 * @param ele
	 * @return
	 */
	public boolean waitForElement(String eleName, int timeoutInSeconds){
		Log.info("UIOperation>> waitForElement. Element Name - " + eleName);
		
		WebElement ele = ElementFactory.getElement(eleName);
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(ele));
		if(ele.isDisplayed()){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**Perform mouse hover over webElement
	 * @param ele
	 */
	public void mouseHover(WebElement ele){
		Log.info("UIOperation>> waitForElement. Element Name - " + this.getElementName(ele));
		if(ele.isDisplayed()){
			Actions action = new Actions(driver);
			action.moveToElement(ele).perform();
		}else{
			Reporting.reportStep(false, "Element mouse hover", "Element - "+this.getElementName(ele), "Element Does not exist.", true);
			
		}
	}
	
	/**Return WebDriver for new window
	 * @param pageTitle
	 * @return
	 */
	public WebDriver switchBrowserByTitle(String pageTitle){
		WebDriver newDriver = null;
		Set<String> allHandles =  driver.getWindowHandles();
		for (String handle1 : allHandles) {
			
        	newDriver = driver.switchTo().window(handle1);
        	if(newDriver.getTitle().equalsIgnoreCase(pageTitle)){
        		break;
        		
        	}else{
        		Reporting.reportStep(false,"Switch Driver","Success","Failed - no window found with title - "+pageTitle,true);
        	}

        }
		return newDriver;
	}
	
	/**returns page title
	 * @return
	 */
	public String getPageTitle(){
		return driver.getTitle();
	}
	
	/**returns page title
	 * @return
	 */
	public String getPageURL(){
		return driver.getCurrentUrl();
	}
	
	public boolean acceptPopupAlert(){
		
		 try 
		    { 
			 driver.switchTo().alert().accept();
		        return true; 
		    }   // try 
		    catch (NoAlertPresentException Ex) 
		    { 
		        return false; 
		    }  
	}
}	