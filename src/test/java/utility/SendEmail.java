package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;



public class SendEmail {
	
	static CommonFunctions comFunc = new CommonFunctions();
	
	public static void main(String[] a){
		SendEmail sendEmail = new SendEmail();
		sendEmail.execute();
	}
	
		
	 /**
	 * call the sender function with required credentials
	 */
	public void execute()
	 {
		Properties maildata = this.readPropertyFile("\\src\\test\\resources\\environmentData\\eMailConfig.properties");
		
		if(maildata.getProperty("sendEmail").equalsIgnoreCase("y")){
		
			String reportFilePath = "\\src\\test\\resources\\temp\\"+ comFunc.getEnvData("projectName") +"_resultFile.html";
			String[] to = maildata.getProperty("to").split(",");
			String[] cc = {};
			String[] bcc = {};
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			
			String subject = maildata.getProperty("subject")+" - "+comFunc.getEnvData("projectName") +" - " +dateFormat.format(cal.getTime());
			
			if (!maildata.getProperty("cc").equals("") && maildata.getProperty("cc") != null) {
				cc = maildata.getProperty("cc").split(",");
			}
			if (!maildata.getProperty("bcc").equals("") && maildata.getProperty("bcc") != null) {
				bcc = maildata.getProperty("bcc").split(",");
			}
	
			this.prepareEmail(maildata.getProperty("username"), maildata.getProperty("password"),
					 to, cc, bcc, maildata.getProperty("host"), maildata.getProperty("port"),
					 subject, maildata.getProperty("messageBody"), reportFilePath);
		}else{
			System.out.println("Sending Email is disable fron eMailConfig.property file.");
			Log.info("Sending Email is disable fron eMailConfig.property file.");
		}
	}

	 
	/**
	 * @param propFile
	 * @return
	 */
	public Properties readPropertyFile(String propFile){
		Log.info("Function Execution: SendEmail>>readPropertyFile");
		Properties propObj = null;
		
		try {
			FileInputStream repoFile = new FileInputStream(System.getProperty("user.dir") + propFile);
				propObj = new Properties();
				propObj.load(repoFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Reporting.reportStep(false, "Get PropertyFile OBJ. File - "+propFile, "Property file returned.", "Operation failed. Reason - "+e.toString(), false);
			
			e.printStackTrace();
			return  propObj;
		}	
		return propObj;
		
	}
	
	 /**Prepare and send the email
	 * @param userName
	 * @param password
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param host
	 * @param port
	 * @param subject
	 * @param body
	 * @param attahcmentPath
	 */
	public void prepareEmail(String userName,String password, 
									 String[] to,String[] cc,String[] bcc,String host,String port,
									 String subject,String messageBody, String attahcmentPath) {
			
		
		    Properties props = new Properties();
		    props.put("mail.smtp.host", host);
		    props.put("mail.smtp.port", port);
		
		    Session session = Session.getInstance(props,
		            new javax.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(userName, password);
		                }
		            });
		
		    try {
		
		        Message message = new MimeMessage(session);
		        message.setFrom(new InternetAddress(userName));
		        
		        for(int i=0;i<to.length;i++){
		        	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
	        	}
	        	for(int i=0;i<cc.length;i++){
	        		message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
	        	}
	        	for(int i=0;i<bcc.length;i++){
	        		message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
	        	}
	        	message.saveChanges();
		        
		        message.setSubject(subject);
		        Multipart multipart = new MimeMultipart();
	       
		       MimeBodyPart messageBodyPart = new MimeBodyPart();
		      
		       try{
			       StringWriter writer = new StringWriter();
			       IOUtils.copy(new FileInputStream(new File(System.getProperty("user.dir") + attahcmentPath)), writer);
	
			       messageBodyPart.setContent(writer.toString(), "text/html");
		       }catch(Exception e){
		    	   Reporting.reportStep(false, "SendEmail", "report is written in email", "operation Failed. Error - "+e.toString(), false);
		       }
		       multipart.addBodyPart(messageBodyPart);
		       //messageBodyPart.setText(messageBody+"\n\nRegards\nAutomation Team");
		 	       
		       MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			   String file = System.getProperty("user.dir") + attahcmentPath;
		      // String file = path;
		       String fileName = "Automation run report.html";
		       DataSource source = new FileDataSource(file);
		       messageBodyPart1.setDataHandler(new DataHandler(source));
		       messageBodyPart1.setFileName(fileName);
		
		        multipart.addBodyPart(messageBodyPart1);
		        message.setContent(multipart);
		
		        System.out.println("Emailing the Test Report....");
		
		        Transport.send(message);
		
		        System.out.println("Email Sent successfully!!");
		
		    } catch (MessagingException e) {
		        e.printStackTrace();
		    }
		  }
}
