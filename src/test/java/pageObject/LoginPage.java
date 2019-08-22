package pageObject;
import org.openqa.selenium.WebElement;
import elementRepository.ElementFactory;
import utility.CommonFunctions;
import utility.Log;
import utility.Reporting;
import utility.UIOperation;

public class LoginPage{
	
	UIOperation uiop = new UIOperation();
	ServicePage servicePage = new ServicePage();
	
   //Page object method for - username
   public WebElement username(){
     Log.info("Element Page Object >> username");
     return ElementFactory.getElement("username");
   }

   //Page object method for - password
   public WebElement password(){
     Log.info("Element Page Object >> password");
     return ElementFactory.getElement("password");
   }

   //Page object method for - loginbutton
   public WebElement loginbutton(){
     Log.info("Element Page Object >> loginbutton");
     return ElementFactory.getElement("loginbutton");
   }
   
   /** Login method. validates element after login
 * @param loginID
 * @param loginPassword
 * @param eleToValidate
 * @return
 */
   public boolean login(){
	   
	
	   CommonFunctions comFunc = 	new CommonFunctions();   
	   	   
	   uiop.setText(this.username(), comFunc.getEnvData("LoginID"));
	   uiop.setText(this.password(), comFunc.getEnvData("Password"));
	   uiop.clickElement(this.loginbutton());
	   
 	   if(uiop.elementExist(servicePage.repairService())){
 		  Reporting.reportStep(true, "Login to the application.", "Loged in successfully.", "operation passed", false);
		   return true;
	   }else{
		   Reporting.reportStep(true, "Login to the application.", "Loged in successfully.", "operation failed", true);
		   return false;
	   }
	   
	   
   }

}
