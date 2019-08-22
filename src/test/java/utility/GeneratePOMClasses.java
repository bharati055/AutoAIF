package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GeneratePOMClasses {

	public void generate(){
			BufferedReader bReader = null;
			try {
				
				String strLine;
				String strClassName=null;
				String strEleName;
				boolean blnMentodEnd = false;
				
				bReader = new BufferedReader(new FileReader("src\\test\\java\\elementRepository\\element.properties"));
				
				while ((strLine = bReader.readLine()) != null) {
					
					if(strLine.contains("#Class-")){
						
						if(blnMentodEnd){
							FileWriter fw = new FileWriter(strClassName,true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write("}");bw.newLine();
							bw.close();
							
						}
						
						blnMentodEnd=true;
						String[] strSplit = strLine.split("-");
						
						if(!strSplit[1].equalsIgnoreCase("end")){
							
								strClassName = "src//test//resources//temp//" + strSplit[1] + ".java";
								File file = new File(strClassName);
				
								// if file doesnt exists, then create it
								if (!file.exists()) {
									file.createNewFile();
								}
								
								FileWriter fw = new FileWriter(strClassName,true);
								BufferedWriter bw = new BufferedWriter(fw);
								
								bw.write("package pageObject;");bw.newLine();
								bw.write("import org.openqa.selenium.WebElement;");bw.newLine();
								bw.write("import elementRepository.ElementFactory;");bw.newLine();
								bw.write("import utility.Log;");bw.newLine();
								bw.newLine();
								bw.write("public class " + strSplit[1] +"{");bw.newLine();
								bw.close();
						}
					 }
					
					else{ 
						
					    String[] strSplit2 = strLine.split("=");
						strEleName = strSplit2[0];
						
						FileWriter fw = new FileWriter(strClassName,true);
						BufferedWriter bw = new BufferedWriter(fw);
						
						bw.write("   //Page object method for - "+strEleName);bw.newLine();
						bw.write("   public WebElement " + strEleName + "(){");bw.newLine();
						bw.write("     Log.info(\"Element Page Object >> "+strEleName+"\");");bw.newLine();
						bw.write("     return ElementFactory.getElement(\"" + strEleName + "\");");bw.newLine();
						bw.write("   }\n");bw.newLine();
						bw.close();
					
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if (bReader != null)bReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}	
	}
	
	public static void main(String[] a){
		GeneratePOMClasses genPOM = new GeneratePOMClasses();
		genPOM.generate();
		System.out.println("POM Generation Finished!!");
	}
}	