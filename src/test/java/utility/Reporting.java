package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Reporting {
	
	private static UIOperation uiop = new UIOperation();
	/** report every step for pass or fail with/without screenshot
	 * @param blnStatus
	 * @param strDesc
	 * @param strExpected
	 * @param strActual
	 * @param blnCaptureScreen
	 * @param driver
	 */
	public static void reportStep(boolean blnStatus,String strDesc,String strExpected, String strActual,boolean blnCaptureScreen){
		Log.info("Function Execution - Reporting>>reportStep");

		if(blnStatus){
			Log.info("Step PASSED. "+"StepDesc - " + strDesc+ ". StepExpected - "+strExpected+". StepAtual - "+strActual);
			Reporting.printLine("Pass");
			Reporting.printLine(strDesc);
			Reporting.printLine(strExpected);
			Reporting.printLine(strActual);
			//take screenshot if boolean is true
			if(blnCaptureScreen){
				String strScreenShotPath = uiop.captureScreenNSave();
				Reporting.printLine(strScreenShotPath);
			}
			Reporting.printLine("\n");
			
		}
		else{
			Log.fatal("Step FAILED. "+"StepDesc - " + strDesc+ ". StepExpected - "+strExpected+". StepAtual - "+strActual);
			Reporting.printLine("Fail");
			Reporting.printLine(strDesc);
			Reporting.printLine(strExpected);
			Reporting.printLine(strActual);
			//take screenshot and end run
			String strScreenShotPath = uiop.captureScreenNSave();
			Reporting.printLine(strScreenShotPath);
			Reporting.printLine("\n");
			//end Run
			Reporting.endCurrentTest(false);
			Reporting.endExecution();
			
		}
		
	}
	
	/**Prints the result
	 * @param strLine
	 */
	private static void printLine(String strLine){
		System.out.println(strLine);
	}
	

	
	/**End Current Test
	 * @param driver
	 */
	public static void endCurrentTest(boolean takeScrnShot){
		Log.info("Function Execution - Reporting>>endCurrentTest");

		if(takeScrnShot){
			uiop.captureScreenNSave();
		}
		BrowserFactory.closeAllDriver();
	}
	
	/**End Execution
	 * @param driver
	 */
	public static void endExecution(){
		Log.info("Function Execution - Reporting>>endExecution");
		Reporting repo = new Reporting();
		repo.generateHTML();
		BrowserFactory.closeAllDriver();
		//System.exit(0);
	}
	
	/**
	 * Creates HTML format report by Parsing result XML from TestNG
	 */
	public void generateHTML(){
		Log.info("Function Execution - Reporting>>generateHTML");
		SendEmail sendEmail = new SendEmail();
		CommonFunctions comFunc = new CommonFunctions();
		Element eElement,eStatusElement;
		String strStatus=null;
		NodeList TestNodes=null;
		Node methodNode=null;
		
		String strProjName = "AutomationTestResult";
				strProjName = comFunc.getEnvData("projectName");	
		Properties maildata = sendEmail.readPropertyFile("\\src\\test\\resources\\environmentData\\eMailConfig.properties");
		String strUser = maildata.getProperty("username");
		
		if(strUser.contains("@")){
			String[] strSplit = strUser.split("@");
			strUser = 	strSplit[0];
		}
				
		try {
			File resultFile = new File("src\\test\\resources\\temp\\" + strProjName + "_resultFile.html");
			if(resultFile.exists()){
				resultFile.delete();
			}
			
			OutputStream htmlfile = new FileOutputStream(resultFile);
			PrintStream printhtml = new PrintStream(htmlfile);
			
			File fXmlFile = new File("test-output\\testng-results.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList headerNodes = doc.getElementsByTagName("suite");
			String startTime = null;
			String endTime = null;
			String duration = null;
			double iDuration = 0;
			//for (int i = 0; i < headerNodes.getLength(); i++) {
				Node headerNode = headerNodes.item(0);
				if (headerNode.getNodeType() == Node.ELEMENT_NODE) {
				    eElement = (Element) headerNode;
					startTime = eElement.getAttribute("started-at");
					endTime = eElement.getAttribute("finished-at");
					duration = eElement.getAttribute("duration-ms");
					iDuration = Double.parseDouble(duration)/1000;
				}
			//}	
			
			NodeList methodNodes = doc.getElementsByTagName("test-method");
			int passCount = 0;
			int failCount = 0;
			for (int i = 0; i < methodNodes.getLength(); i++) {
				methodNode = methodNodes.item(i);
				if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) methodNode;
					
					if(!eElement.getAttribute("is-config").equals("true")){	
						if(eElement.getAttribute("status").equals("PASS"))
							passCount+= 1;
						else
							failCount+= 1;
					}	
				}
			}	
			
			NodeList nList = doc.getElementsByTagName("class");
			int testCount = nList.getLength();

			printhtml.println("<HTML>");
			printhtml.println("<HEAD>");
			printhtml.println("<META NAME='GENERATOR' Content='Microsoft Visual Studio 6.0'>");
			printhtml.println("<TITLE>" + strProjName + "</TITLE>");
			printhtml.println("</HEAD>");
			printhtml.println("<BODY>");
			printhtml.println("<STRONG><FONT face=bold color=#3e3535 size=5><CENTER>" + strProjName + "</CENTER></STRONG>");
			printhtml.println("<STRONG><HR size=5 width=100% color = #3399CC></STRONG>");
			printhtml.println("<CENTER><TABLE borderColor=#817339 cellSpacing=4 cellPadding=5 border=1 width=100%><TBODY><TR>");
			printhtml.println("<TD align=middle colSpan=8 width = 100%><FONT face=Tahoma color=#150517 size=5>Test Execution Summary</FONT></TD></TR>");
			printhtml.println("<TR bgcolor= #3399CC align=middle><TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Release Version</FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Executed by </FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Execution Started </FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Execution Ended</FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Total Execution Time (in Seconds) </FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Total Test Cases</FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Test Cases Passed</FONT></STRONG></TD>");
			printhtml.println("<TD><STRONG><FONT face=Tahoma size=2 color = #ffffff>Test Cases Failed</FONT></STRONG></TD></TR>");
			printhtml.println("<TR bgcolor =  #FFFFCC align=middle><TD><FONT face=Tahoma size=2>"+"Release-2016.01"+"</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>" + strUser + "</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>" + startTime + "</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>" + endTime + "</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>" + iDuration + "</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>" + testCount + "</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>"+ passCount +"</FONT></TD>");
			printhtml.println("<TD><FONT face=Tahoma size=2>"+ failCount +"</FONT></TD></TR></TABLE></CENTER>");
			printhtml.println("</BR>");
			printhtml.println("<TABLE cellSpacing=4 cellPadding=5 align=center border=1 width=100%>");
			printhtml.println("<TR style='BACKGROUND-COLOR: #3399CC' align=Center ><TD ><STRONG><FONT size=2 color=#ffffff>Test Case ID</FONT></STRONG></TD><TD ><STRONG><FONT size=2 color=#ffffff>Description</FONT></STRONG></TD><TD ><STRONG><FONT size=2 color=#ffffff>Status</FONT></STRONG></TD></Font></TR>");
			
			
			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					eElement = (Element) nNode;
					System.out.println("inside - get attribbute for test details");
					String strTestName = eElement.getAttribute("name");
					String strTestDesc = eElement.getAttribute("name");
					
					TestNodes = eElement.getElementsByTagName("test-method");
					
					for (int j = 0; j < TestNodes.getLength(); j++) {
						methodNode = TestNodes.item(j);
						if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
							eStatusElement = (Element) methodNode;
							
							if(!eStatusElement.getAttribute("is-config").equals("true")){
								 strStatus= eStatusElement.getAttribute("status");
								break;
							}	
						}
					}	
					
					printhtml.println("<TR>");
					printhtml.println("<TD bgcolor= #FFFFCC ><FONT face=Tahoma size=2>"+ strTestName +"</FONT></TD>");
					printhtml.println("<TD bgcolor= #FFFFCC ><FONT face=Tahoma size=2>"+ strTestDesc +"</FONT></TD>");
					printhtml.println("<TD bgcolor= #FFFFCC><B><FONT COLOR={2}  face=Tahoma size=2>" + strStatus + "</B></FONT></TD>");
					printhtml.println("</TR>");
				}
			}
			printhtml.println("</TABLE>");
			printhtml.println("</BODY>");
			printhtml.println("</HTML>");
		
			printhtml.close();
			htmlfile.close();
			
			//Sending email
			sendEmail.execute();
			
		} catch (Exception e) {
			System.out.println("Error caused in HTML report creation. Error Message - "+e.toString());
			e.printStackTrace();
		}
	}

	public static void main(String[] a){
		Reporting repo = new Reporting();
		repo.generateHTML();
	}

}
