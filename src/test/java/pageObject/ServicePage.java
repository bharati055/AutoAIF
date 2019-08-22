package pageObject;
import org.openqa.selenium.WebElement;
import elementRepository.ElementFactory;
import utility.Log;
import utility.Reporting;
import utility.UIOperation;

public class ServicePage{
	
	UIOperation uiop = new UIOperation();
	ServiceDescPage serviceDescPage = new ServiceDescPage();
		
   //Page object method for - repairService
   public WebElement repairService(){
     Log.info("Element Page Object >> repairService");
     return ElementFactory.getElement("repairService");
   }
   
   /**clicks repair service
 * @return boolean
 */
public boolean clickRepairService(){
	   uiop.clickElement(this.repairService());
	   
	   if(uiop.elementExist(serviceDescPage.name())){
		   Reporting.reportStep(true, "click RepairService button.", "Clicked successfully.", "operation passed", false);
		   return true;
	   }else{
		   Reporting.reportStep(true, "click RepairService button.", "Clicked successfully.", "operation failed", true);
		   return false;
	   }
		   
   }

}
