package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class CommonFunctions {
	
	/**Returns data from environment file
	 * @param paramName
	 * @return
	 */
	public String getEnvData(String paramName){
		//System.out.println("before reading env data file");
		Log.info("Reading environment data file for param - " +paramName);
		System.out.println("Reading environment data file for param - " +paramName);
		File xlfile = new File("src\\test\\resources\\environmentData\\EnvironmentData.xlsx");
		
		boolean dataFound=false;
		String paramVal = null;
		
		try{
			FileInputStream xlStream = new FileInputStream(xlfile);
			
			Workbook xlwb = new XSSFWorkbook(xlStream);
			Sheet xlws = xlwb.getSheet("Sheet1");
			
			int rownum = xlws.getLastRowNum();
			for(int i=1;i<=rownum;i++){
				String data = xlws.getRow(i).getCell(0).toString();
				if(data.equals(paramName)){
					paramVal = xlws.getRow(i).getCell(1).toString();
					dataFound= true;
				}
			}
			xlwb.close();
		
		}catch (IOException e){
			e.printStackTrace();
		}
		if(!dataFound){
			System.out.println("element - " + paramName + " detail not found in repository");
			Reporting.reportStep(false, "Reading Env data file. Param - "+paramName, "Value found", "Failed", false);
		}
		Log.info("Reading environment data file for param - " +paramName+". Value - "+paramVal);
		return paramVal;
		
	}
	
	/**
	 * Clears the Temp folder
	 */
	public void clearTempFolder(){
		
		System.out.println("Function execution: CommonFunctions >> clearTempFolder");
		File index = new File("src//test//resources//temp//screenshot");
		try {
			FileUtils.cleanDirectory(index);
			new File("src//test//resources//temp//screenshot").mkdir();
			
			this.deleteFile("src//test//resources//temp//runTimeParam.xml");
			
			String strProjName = this.getEnvData("projectName");
			System.out.println("Filename - "+"src//test//resources//temp//"+strProjName + "_resultFile.html");
			this.deleteFile("src//test//resources//temp//"+strProjName + "_resultFile.html");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Reporting.reportStep(false, "Function - clear Temp folder.", "Cleared temp.", "Operation failed.", false);
		}
	
	}
	
	/**
	 * Delete  file
	 */
	public void deleteFile(String filePath){
		System.out.println("Function execution: CommonFunctions >> deleteFile");
		try{
	    		File file = new File(filePath);
	        	if(file.exists()){
	        		file.delete();
	    		}
        		
    	}catch(Exception e){
    		e.printStackTrace();
    		Reporting.reportStep(false, "Function - Log file setup.", "Setup successfull.", "Operation failed.", false);
    	}
		
    	
	}
	
	/**
	 * Delete log file and create new
	 */
	public void logFileSetup(){
		System.out.println("Function execution: CommonFunctions >> logFileSetup");
		try{
	    		File file = new File("src//test//resources//temp//logfile.log");
	        	if(file.exists()){
	        		FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();
	    		}else{
	    			//System.out.println("Delete operation failed.");
	    		}
	        	
	        	//File newfile = new File("src//test//resources//temp//logfile.log");
	        	file.createNewFile();
        		
    	}catch(Exception e){
    		e.printStackTrace();
    		Reporting.reportStep(false, "Function - Log file setup.", "Setup successfull.", "Operation failed.", false);
    	}
		
    	
	}
	
	/**
	 * @param browserType
	 */
	public void envSetup(String browserType){
		System.out.println("Function execution: CommonFunctions >> envSetup");
		this.clearTempFolder();
		this.logFileSetup();
		this.createTempXML();
		this.tempXML_addNode("browser", browserType);
		
		
		/*BrowserFactory.getBrowser(browserType);
		ElementFactory.getRepository();*/
	}
	
	/**
	 * @return
	 */
	public String getCurrentTestName(){
		Log.info("Function execution: CommonFunctions >> getCurrentTestName");
		return Object.class.getName();
	}
	
	/**Find string in the passed table
	 * @param eleTable
	 * @param strToFind
	 * @return
	 */
	public boolean findInTable(WebElement eleTable,String strToFind){
		boolean blnFound = false;
		String cellContent="";
		List<WebElement> rowsEle = eleTable.findElements(By.tagName("tr"));
		
		for(WebElement rowElement:rowsEle){

			List<WebElement> colsEle = rowElement.findElements(By.tagName("td"));
			for(WebElement colElement:colsEle){
		    	  cellContent = colElement.getText();
		    	  System.out.println(cellContent);
					if(strToFind.equals(cellContent)){
						blnFound= true;
						break;
					}else{
						Reporting.reportStep(false, "Find in table", "Processed", "Failed. String - "+strToFind+" not found in table.", true);
					}
			}		
		 }
		
		return blnFound;
	}
	
	/** Parse table and get the specified cell content
	 * @param eleTable
	 * @param irow
	 * @param icol
	 * @return
	 */
	public String getCellContent(WebElement eleTable,int irow,int icol){
		String strCellValue = null;
		
		List<WebElement> rowsEle = eleTable.findElements(By.tagName("tr"));
		if(rowsEle.size()<=irow-1){
			List<WebElement> colsEle = rowsEle.get(irow-1).findElements(By.tagName("td"));
			if(colsEle.size()<=icol-1){
				strCellValue = colsEle.get(icol-1).toString();	
			}else{
				Reporting.reportStep(false, "Get table content", "Processed", "Failed. column value passed is incorrect.", true);
			}
		}else{
			Reporting.reportStep(false, "Get table content", "Processed", "Failed. row value passed is incorrect.", true);
		}
		return strCellValue;
	}
	
	/** Get password de-crypted at runtime
	 * @param encryptedPassword
	 * @return
	 */
	public static String decryptPassword(String encryptedPassword) {
		String decryptedPassword;
		byte[] decryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);
		decryptedPassword = new String(decryptedPasswordBytes);
		return decryptedPassword;
	}
	
	/**Get password encrypted
	 * @param strPassword
	 * @return
	 */
	public static String encryptPassword(String strPassword) {
		return Base64.getEncoder().encodeToString(strPassword.getBytes());
	}
	
	
	/**
	 * Create temporary XML file for runtime variables
	 */
	public void createTempXML() {
		  Log.info("Creating Runtime parameter XML");
		  try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("TestRun");
			doc.appendChild(rootElement);

			// RunTimeParameters elements
			Element staff = doc.createElement("RunTimeParameters");
			rootElement.appendChild(staff);
			// RunTimeParameters elements
			Element suiteDetails = doc.createElement("SuiteDetails");
			rootElement.appendChild(suiteDetails);
			
			// RunTimeParameters elements
			Element relVersion = doc.createElement("ReleaseVersion");
			suiteDetails.appendChild(relVersion);			
			// RunTimeParameters elements
			Element TesterName = doc.createElement("TesterName");
			suiteDetails.appendChild(TesterName);	
			// RunTimeParameters elements
			Element ExecutionStartTime = doc.createElement("ExecutionStartTime");
			suiteDetails.appendChild(ExecutionStartTime);	
			// RunTimeParameters elements
			Element ExecutionEndTime = doc.createElement("ExecutionEndTime");
			suiteDetails.appendChild(ExecutionEndTime);				
			// RunTimeParameters elements
			Element TeastCases = doc.createElement("TeastCases");
			suiteDetails.appendChild(TeastCases);	
			// RunTimeParameters elements
			Element PassCount = doc.createElement("PassCount");
			suiteDetails.appendChild(PassCount);	
			// RunTimeParameters elements
			Element FailCount = doc.createElement("FailCount");
			suiteDetails.appendChild(FailCount);	
			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("src//test//resources//temp//runTimeParam.xml"));
		
			transformer.transform(source, result);

		  } catch (Exception e) {
			Reporting.reportStep(false, "Creating run time parameter XML", "Success", "Failed. Error - "+e.toString(), false);
		    e.printStackTrace();
		  }
	}
	
	/**Write temp XML file
	 * @param nodeName
	 * @param nodeValue
	 */
	public void tempXML_addNode(String nodeName,String nodeValue) {
		Log.info("Writing Runtime parameter XML. Node name-"+nodeName+". Node value - "+nodeValue);
		   try {
			String filepath = "src//test//resources//temp//runTimeParam.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			//Node company = doc.getFirstChild();
			// Get the staff element by tag name directly
			Node runTimeParameters = doc.getElementsByTagName("RunTimeParameters").item(0);


			// append a new node to staff
			Element browser = doc.createElement(nodeName);
			browser.appendChild(doc.createTextNode(nodeValue));
			runTimeParameters.appendChild(browser);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);


		   } catch (Exception e) {
			   Reporting.reportStep(false, "Adding parameter to run time parameter XML", "Success", "Failed. Error - "+e.toString(), false);
			   e.printStackTrace();	   
		   } 
		}
	
	/**Get runtime Param value
	 * @param strNodeName
	 * @return
	 */
	public String tempXML_readNode(String strNodeName) {
		String strNodeValue = null;
	    try {

		File fXmlFile = new File("src//test//resources//temp//runTimeParam.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
				
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("RunTimeParameters");
		
			for (int temp = 0; temp < nList.getLength(); temp++) {
	
				Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		                
						Element eElement = (Element) nNode;
						strNodeValue = eElement.getElementsByTagName(strNodeName).item(0).getTextContent();
					}
			}
		    } catch (Exception e) {
		    	Reporting.reportStep(false, "Reading parameter from run time parameter XML. Node - "+strNodeName, "Success", "Failed. Error - "+e.toString(), false);
				e.printStackTrace();	
		    }
	    return strNodeValue;
	  }

	
	/**Write temp XML file
	 * @param nodeName
	 * @param nodeValue
	 */
	public void tempXML_addValue(String nodeName,String nodeValue) {
		Log.info("Writing Runtime parameter XML. Node name-"+nodeName+". Tester - "+nodeValue);
		   try {
			String filepath = "src//test//resources//temp//runTimeParam.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			Node ReleaseVersion = doc.getElementsByTagName(nodeName).item(0);
			ReleaseVersion.appendChild(doc.createTextNode(nodeValue));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);


		   } catch (Exception e) {
			   Reporting.reportStep(false, "Adding value to node in temp XML", "Success", "Failed. Error - "+e.toString(), false);
			   e.printStackTrace();	   
		   } 
		}
	
	/**tempXML_addSuiteInfo
	 * @param releaseVersion
	 * @param tester
	 */
	public void tempXML_StartSuit(String releaseVersion,String tester) {
		Log.info("tempXML_addSuiteInfo. releaseVersion-"+releaseVersion+". Tester - "+tester);
		this.tempXML_addValue("ReleaseVersion", releaseVersion);
		this.tempXML_addValue("TesterName", tester);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		this.tempXML_addValue("ExecutionStartTime", dateFormat.format(date));
		
	}
	
	/**Assing testInfo
	 * @param testID
	 * @param testDescription
	 * @param status
	 */
	public void tempXML_StartTest(String testID,String testDescription,String status) {
		
		try {
			String filepath = "src//test//resources//temp//runTimeParam.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			Node root = doc.getElementsByTagName("TestRun").item(0);
			
			Element testCase = doc.createElement("TestCase");
			root.appendChild(testCase);
			
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue(testID);
			testCase.setAttributeNode(attr);
			// set attribute to staff element
			 attr = doc.createAttribute("description");
			attr.setValue(testDescription);
			testCase.setAttributeNode(attr);
			// set attribute to staff element
			 attr = doc.createAttribute("status");
			attr.setValue(status);
			testCase.setAttributeNode(attr);
			
				
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);


		   } catch (Exception e) {
			   Reporting.reportStep(false, "Adding value to node in temp XML", "Success", "Failed. Error - "+e.toString(), false);
			   e.printStackTrace();	   
		   } 
		
	}
	
	public void tempXML_EndTest() {

		int passCount = 0;
		int failCount = 0;
		
	    try {

		File fXmlFile = new File("src//test//resources//temp//runTimeParam.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
				
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("TestCase");
		
			for (int temp = 0; temp < nList.getLength(); temp++) {
	
				Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		                
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("status").equals("PASS")){
							passCount+=1;
						}else{
							failCount+=1;
						}
						
					}
			}
			
			this.tempXML_addValue("TeastCases", Integer.toString(passCount+failCount));
			this.tempXML_addValue("PassCount", Integer.toString(passCount));
			this.tempXML_addValue("FailCount", Integer.toString(failCount));
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			this.tempXML_addValue("ExecutionEndTime", dateFormat.format(date));
			
	    } catch (Exception e) {
	    	Reporting.reportStep(false, "tempXML_EndTest::Calcuating pass n fail count", "Success", "Failed. Error - "+e.toString(), false);
			e.printStackTrace();	
	    }
	    
	  }
	
	
	
	public static void main(String[] a){
		CommonFunctions c =new CommonFunctions();
		c.tempXML_EndTest();
	}

}
