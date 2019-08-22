package pageObject;
import org.openqa.selenium.WebElement;
import elementRepository.ElementFactory;
import utility.Log;
import utility.Reporting;
import utility.UIOperation;

public class ServiceDescPage{
	
	UIOperation uiop = new UIOperation();
	
   //Page object method for - name
   public WebElement name(){
     Log.info("Element Page Object >> name");
     return ElementFactory.getElement("name");
   }

   //Page object method for - email
   public WebElement email(){
     Log.info("Element Page Object >> email");
     return ElementFactory.getElement("email");
   }

   //Page object method for - contact
   public WebElement contact(){
     Log.info("Element Page Object >> contact");
     return ElementFactory.getElement("contact");
   }

   //Page object method for - address
   public WebElement address(){
     Log.info("Element Page Object >> address");
     return ElementFactory.getElement("address");
   }

   //Page object method for - description
   public WebElement description(){
     Log.info("Element Page Object >> description");
     return ElementFactory.getElement("description");
   }

   //Page object method for - submitbutton
   public WebElement submitbutton(){
     Log.info("Element Page Object >> submitbutton");
     return ElementFactory.getElement("submitbutton");
   }
   
   public boolean submitServiceReqForm(String strName,String strEmail, String strContact,String strAddress, String Desc){
	   uiop.setText(this.name(), strName);
	   uiop.setText(this.email(), strEmail);
	   uiop.setText(this.contact(), strContact);
	   uiop.setText(this.address(), strAddress);
	   uiop.setText(this.description(), Desc);
	   
	   uiop.clickElement(this.submitbutton());
	   
	   boolean blnFlag = uiop.acceptPopupAlert();
	   
	   if (blnFlag){
		   Reporting.reportStep(true, "Submit service request form.", "form submited successfully.", "operation passed", false);
		   return true;
	   }else{
		   Reporting.reportStep(true, "Submit service request form.", "form submited successfully.", "operation failed", true);
		   return false;
	   }
	   
   }

}
