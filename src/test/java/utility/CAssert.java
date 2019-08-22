package utility;

import org.testng.Assert;

public class CAssert extends Assert{
	
	public static boolean cAssertEquals(String str1,String str2){
		
		if(str1!=null &&str1!=null){
			if(str1.equals(str2)){
				Log.info("Assertion Successfull. Value1 - "+str1+". Value2 - "+str2);
				return true;
			}else{
				Reporting.reportStep(false, "Compare Strings", "Compare - "+str1+" with - "+str2, "Comparison failed.", true);
				return false;
			}
		}else{
			Reporting.reportStep(false, "Compare Strings", "Compare - "+str1+" with - "+str2, "Failed. Either of the inputs were null.", true);
			return false;
		}
	}
	
	public static boolean cAssertEquals(Integer str1,Integer str2){
		if(str1!=0 &&str1!=0){
			if(str1==str2){
				Log.info("Assertion Successfull. Value1 - "+str1+". Value2 - "+str2);
				return true;
				
			}else{
				Reporting.reportStep(false, "Compare integers", "Compare - "+str1+" with - "+str2, "Comparison failed.", true);
				return false;
			}
		}else{
			Reporting.reportStep(false, "Compare integers", "Compare - "+str1+" with - "+str2, "Failed. Either of the inputs were null.", true);
			return false;
		}
	}
	
	public static boolean cAssertEquals(boolean booleanValue1,boolean booleanValue2){
		
			if(booleanValue1==booleanValue2){
				Log.info("Assertion Successfull. Value1 - "+booleanValue1+". Value2 - "+booleanValue2);
				return true;
			}else{
				Reporting.reportStep(false, "Compare boolean", "Compare - "+booleanValue1+" with - "+booleanValue2, "Comparison failed.", true);
				return false;
			}
		
	}
	
	public static boolean cAssertNotEquals(String str1,String str2){
		if(str1!=null &&str1!=null){
			if(!str1.equals(str2)){
				Log.info("Assertion not equals- Successfull. Value1 - "+str1+". Value2 - "+str2);
				return true;
			}else{
				Reporting.reportStep(false, "Compare Strings for not equal", "Compare - "+str1+" with - "+str2, "Comparison failed.", true);
				return false;
			}
		}else{
			Reporting.reportStep(false, "Compare Strings for not equal", "Compare - "+str1+" with - "+str2, "Failed. Either of the inputs were null.", true);
			return false;
		}
	}
	
	public static boolean cAssertNotEquals(Integer str1,Integer str2){
		if(str1!=0 &&str1!=0){
			if(str1==str2){
				Log.info("Assertion not equals- Successfull. Value1 - "+str1+". Value2 - "+str2);
				return true;
			}else{
				Reporting.reportStep(false, "Compare Integers for not equal", "Compare - "+str1+" with - "+str2, "Comparison failed.", true);
				return false;
			}
		}else{
			Reporting.reportStep(false, "Compare Integers for not equal", "Compare - "+str1+" with - "+str2, "Failed. Either of the inputs were null.", true);
			return false;
		}
	}
	
	public static boolean cAssertNotEquals(boolean booleanValue1,boolean booleanValue2){
		
			if(booleanValue1==booleanValue2){
				Log.info("Assertion not equals- Successfull. Value1 - "+booleanValue1+". Value2 - "+booleanValue2);
				return true;
			}else{
				Reporting.reportStep(false, "Compare booleans for not equal", "Compare - "+booleanValue1+" with - "+booleanValue2, "Comparison failed.", true);
				return false;
			}
		
	}

}
