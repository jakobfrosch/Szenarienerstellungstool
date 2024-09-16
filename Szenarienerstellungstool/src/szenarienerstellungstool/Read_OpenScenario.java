package szenarienerstellungstool;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Read_OpenScenario {
  public static void main(String argv[]) {
    try {
      File fXmlFile = new File("/Users/jakobfroschauer/Documents/OpenSCENARIO.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("*");
      int counter=0;
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Element element = (Element) nList.item(temp);
        
        if (element.hasAttribute("use") && element.getAttribute("use").equals("required")) {
        	//System.out.println(element.getAttribute("name") + " true");
        	try {
        		System.out.println(element.getParentNode().getAttributes().getNamedItem("name").getNodeValue() + " " + element.getAttribute("name") + " " + true+ temp+" "+ counter);
        	}
        	catch (NullPointerException e) {
        		System.out.println("!! " + element.getAttribute("name") + " " + true + temp +" "+ counter);
            }
        	counter++;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}