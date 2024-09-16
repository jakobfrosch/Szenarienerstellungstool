package szenarienerstellungstool;


import org.w3c.dom.*;

import de.ovgu.featureide.fm.attributes.base.IExtendedFeatureModel;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureProperty;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureStructure;
import de.ovgu.featureide.fm.core.conf.IFeatureGraph;
import de.ovgu.featureide.fm.core.io.IFeatureGraphFormat;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
	public static int c=0;
    public static void main(String[] args) {
        try {
            File inputFile = new File("/Users/jakobfroschauer/Downloads/scenario_runner-master/srunner/examples/PedestrianCrossingFront.xosc");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            String fileName = inputFile.getName();
            int dotIndex = fileName.indexOf('.');
            String resultString=fileName;
            // Überprüfe, ob ein Punkt vorhanden ist und schneide ab dem Punkt ab
            if (dotIndex != -1) {
                resultString = fileName.substring(0, dotIndex);
            }
            //System.out.println(resultString);
            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            // Auslesen der wichtigsten Features als Baumstruktur
            printTree(rootElement, resultString, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void printTree(Node node, String parentName, int level) {
    	//String fileName = inputFile.getName();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            for (int j = 0; j < level; j++) {
                System.out.print("  "); // Einrückung für die Baumstruktur
            }
            System.out.print("[" + (parentName != null ? parentName : "null") + "] ");
            System.out.print(node.getNodeName());

            
            // Auslesen der Attribute und Anzeige, falls vorhanden
            if (node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                for (int k = 0; k < attributes.getLength(); k++) {
                    Node attribute = attributes.item(k);
                    System.out.println();
                    for (int j = 0; j < level+1; j++) {
                        System.out.print("  "); // Einrückung für die Baumstruktur
                    }
                    System.out.print("[" + node.getNodeName() + "] ");
                    System.out.print(attribute.getNodeName()+"|"+attribute.getNodeValue());
                }
            }
			
            System.out.println();

            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                printTree(childNode, node.getNodeName(), level + 1); // Rekursiver Aufruf für die Kindknoten
            }
        }
    }
    public static ArrayList<TwoStrings> fillCreateArraylist(Node node, String parentName, int level) {
    	ArrayList<TwoStrings> ar = new ArrayList();
    	ar.add(new TwoStrings(null,parentName,level,true));
    	ar=createArraylist(node,parentName, level, ar);
    	return ar;
    }

    public static ArrayList<TwoStrings> createArraylist(Node node, String parentName, int level,ArrayList<TwoStrings> ar) {
    	//ar.add(new TwoStrings(null,parentName,true));
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            ar.add(new TwoStrings(parentName,node.getNodeName(),level,true));
            if (node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                for (int k = 0; k < attributes.getLength(); k++) {
                    Node attribute = attributes.item(k);
                    ar.add(new TwoStrings(node.getNodeName(),""+attribute.getNodeName()+"|"+attribute.getNodeValue(),level,true));
                }
            }

            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                createArraylist(childNode, node.getNodeName(), level + 1,ar);
            }
        }
        return ar;
    }
    public static ArrayList<TwoStrings> fillCreateArraylist(IFeatureModel fm) {
    	ArrayList<TwoStrings> ar = new ArrayList();
    	ar=createArraylist(fm, ar);
    	return ar;
    }
    public static ArrayList<TwoStrings> createArraylist(IFeatureModel fm,ArrayList<TwoStrings> ar) {
    	IFeatureModelStructure fms=fm.getStructure();
    	
    	for (IFeature f : fms.getFeaturesPreorder()) {
    		
    		//System.out.println(f.getProperty().getDescription());
    		try {
    			
    			ar.add(new TwoStrings(f.getStructure().getParent().getFeature().getName(),f.getName(),Integer.valueOf(f.getProperty().getDescription()),true));
    		}
    		catch (Exception e) {
    			ar.add(new TwoStrings(null,f.getName(),0,true));
			}
    		
    	}
    	
    	
    	
    	return ar;
    }
    public static FeatureModel fillCreateFeatureModel(Node node, String parentName, List<ElementsAndOneChildren> openScenario_required) {
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();   
		FeatureModel fm = new FeatureModel("Test");
		//IFeatureModelStructure fmStructure=fm.getStructure();
    	Feature f = factory.createFeature(fm, node.getNodeName());
    	//IFeatureStructure fStructure=f.getStructure();
    	//fmStructure.setRoot(fStructure);
    	//NodeList childNodes = node.getChildNodes();
    	fm=createFeatureModel(node,parentName,f,0,fm,0,openScenario_required);
    	
    	
    	return fm;
    }
    public static FeatureModel createFeatureModel (Node node, String parentName,IFeature motherfeature, int level,FeatureModel fm, int counter,List<ElementsAndOneChildren> openScenario_required) {
    	
		final IFeatureModelStructure fms=fm.getStructure();
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		final IFeatureModelStructure fmStructure = fm.getStructure();
    	if (node.getNodeType() == Node.ELEMENT_NODE) {
            Feature f = factory.createFeature(fm, "#"+c+" "+node.getNodeName());
            c++;
            IFeatureStructure fStructure = f.getStructure();
            IFeatureProperty fp=f.getProperty();
            
			
			if (counter==0 && level == 0) {
    			fmStructure.setRoot(fStructure);
    		}
			if (f.getName().contains("FileHeader")) {
				f.getStructure().setHidden(true);
			}
			if (node.getChildNodes().getLength()==0 && !node.hasAttributes()) {
				f.getStructure().setHidden(true);
			}
			fm.addFeature(f);
			counter++;
			boolean required = false;
			for (int i = 0; i < openScenario_required.size(); i++) {
            	
            	if (node.getNodeName().equals(openScenario_required.get(i).getFeature())) {
            		required=true;
            		
            		System.out.println("!!!!!!!!!!!!!!"+node.getNodeName()+"|||"+ openScenario_required.get(i).getFeature()+"!! "+parentName+"||| "+ openScenario_required.get(i).getMotherfeature());
            	}
            }
            if (node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                
                for (int k = 0; k < attributes.getLength(); k++) {
                	required=false;
                    Node attribute = attributes.item(k);
                    for (int i = 0; i < openScenario_required.size(); i++) {
                    	
                    	if (attribute.getNodeName().equals(openScenario_required.get(i).getFeature())&&node.getNodeName().equals(openScenario_required.get(i).getMotherfeature())) {
                    		required=true;
                    		
                    		System.out.println("!!!!!!!!!!!!!!"+attribute.getNodeName()+"|||"+ openScenario_required.get(i).getFeature()+"!! "+node.getNodeName()+"||| "+ openScenario_required.get(i).getMotherfeature());
                    	}
                    }
                    Feature f2=factory.createFeature(fm, "#"+c+" "+attribute.getNodeName()+"|"+attribute.getNodeValue());
                    System.out.println(attribute.getNodeValue());
                    if (attribute.getNodeValue().equals("")||attribute.getNodeValue().equals("0")) {
                    	f2.getStructure().setHidden(true);
                    	
                    }
                    c++;
                    fm.addFeature(f2);
                    IFeatureStructure f2Structure=f2.getStructure();
                    
                    //if (f2Structure.getParent().getFeature().getName().endsWith(parentName))
                    
            		if (required) {
                		f2Structure.setMandatory(true);
                		required=false;
            		}
            		else {
            			f2Structure.setMandatory(false);
            		}
                    
                    fStructure.addChild(f2Structure);
                    counter++;
                    
                }
            }
            //System.out.println(motherfeature.getName());
            
            motherfeature.getStructure().addChild(fStructure);
            fStructure.setMandatory(false);
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
            	motherfeature=f;
                Node childNode = childNodes.item(i);
                counter++;
                createFeatureModel(childNode, motherfeature.getName(),motherfeature, level + 1,fm,counter,openScenario_required); // Rekursiver Aufruf für die Kindknoten
                fStructure.setMandatory(false);
                
            }
        }
    	
    	return fm;
    }
}
/*

 */