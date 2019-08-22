package testCase;
import org.testng.annotations.Test;

import pageObject.*;
import utility.*;

public class TC01_SubmitServiceRequest_Repairservices extends RunConfiguration {

	
	UIOperation uiop = new UIOperation();
	LoginPage loginPage = new LoginPage();
	ServicePage servicePage = new ServicePage();
	ServiceDescPage serviceDescPage = new ServiceDescPage();
	
	 @Test(groups = {"sanity"})
	  public void Login_Validation_01() {
		  
		  System.out.println("TC01_SubmitServiceRequest_Repairservices");
		  
		  //login
		  uiop.launchApplication();
		  loginPage.login();
		  
		  //Service page
		  servicePage.clickRepairService();
		  
		  //Service request page
		  serviceDescPage.submitServiceReqForm("admin", "admin@automationtoolhelp.com", "9999000111", "101 Cherry St.", "Please contact for service.");
	 
	 } 
 
}
