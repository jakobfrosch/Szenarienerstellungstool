package szenarienerstellungstool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.sat4j.specs.TimeoutException;

import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureProperty;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;

import java.io.File;
import java.io.IOException;
import java.util.List;



import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
public class Test6 {

	public static void main(String[] args) {
        ArrayList<TwoStrings> ar=new ArrayList();
        ArrayList<TwoStrings> ar2=new ArrayList();
        try {
            File inputFile = new File("/Users/jakobfroschauer/Downloads/scenario_runner-master/srunner/examples/PedestrianCrossingFront.xosc");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            //String fileName = inputFile.getName();
            //int dotIndex = fileName.indexOf('.');
            
            //String resultString=fileName;
            // Überprüfe, ob ein Punkt vorhanden ist und schneide ab dem Punkt ab
            //if (dotIndex != -1) {
            //    resultString = fileName.substring(0, dotIndex);
            //}
            //System.out.println(resultString);
            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            
            // Auslesen der wichtigsten Features als Baumstruktur
            
            XMLParser.printTree(rootElement, null, 1);
            System.out.println("Test");
            ar=XMLParser.fillCreateArraylist(rootElement, "?xml version=\"1.0\"?", 0);
            System.out.println("finished");
        } catch (Exception e) {
        	System.out.println("Catch");
            e.printStackTrace();
        }
        
        IFeatureModel fm2 = fillFeatureModel(ar);
        System.out.println(fm2.toString());
        printPossibleSolutions(fm2);
        System.out.println("---------");
        
        ar2=XMLParser.fillCreateArraylist(fm2);
        System.out.println("---------");
        for(TwoStrings i : ar2) {
        	System.out.println("Motherfeature: "+i.getMotherfeature()+"; feature: "+i.getFeature());
        }
        System.out.println("-----Structure----");
        ArrayList<IntString> arintstring=new ArrayList();
        for (IFeature f : fm2.getFeatures()) {
        	arintstring.add(new IntString(f.getName(),f.getInternalId(), 0));
        	//System.out.println(f.getName()+"xxx"+f.getInternalId());
        }
        sortListById(arintstring);
        for (IntString is : arintstring) {
        	System.out.println(is.getName()+"  "+is.getId());
        }
        System.out.println("-----XML----");
        
        System.out.print(XMLGenerator.generateXML(ar2));
        //createXMLFileFromFeatureModel(fm2);

	}
	
	/*
	public static void createXMLFileFromArrayList(ArrayList<TwoStrings> ar){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        
	        
            
            for (TwoStrings i : ar) {
            	if (i.getMotherfeature()==null) {
            		Element rootElement = doc.createElement(ar.get(0).getFeature());
                    doc.appendChild(rootElement);
                    for (TwoStrings j : ar) {
                    	//if 
                    }
            	}
            	
            }
	        
	        
	        
	        
	        
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	*/
	public static IFeatureModel fillFeatureModel(ArrayList<TwoStrings> ar) {
		//System.out.println("JF: "+ ar.get(0).getFeature()+ar.get(0).getMotherfeature());
		final IFeatureModel fm = new FeatureModel(ar.get(0).getFeature());
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		final IFeatureModelStructure fmStructure = fm.getStructure();
		IConstraint c1=factory.createConstraint(fm, null);
		for (TwoStrings i : ar) {
			Feature f = factory.createFeature(fm, i.getFeature());
			IFeatureProperty fp=f.getProperty();
			fp.setDescription(""+i.getLevel());
			fm.addFeature(f);
        	IFeatureStructure fStructure = f.getStructure();
            if (i.getMotherfeature() == null) {
            	System.out.println("JF2: "+ i.getFeature()+i.getMotherfeature());
            	//System.out.println("JF3: "+ fStructure.getFeature().getName());
            	fmStructure.setRoot(fStructure);
            }
            else {
            	fm.getFeature(i.getMotherfeature()).getStructure().addChild(fStructure);
            	fStructure.setMandatory(i.isMandatory());
            }
        }
		return fm;
	}
	public static void printPossibleSolutions(IFeatureModel fm) {
		final Configuration conf = new Configuration(fm);
		//conf.setManual("OverrideControllerValueAction", Selection.SELECTED);
        System.out.println("Can be valid: " + conf.canBeValid());
        System.out.println("Solutions: " + conf.number());
        List<List<String>> solutions = null;
		try {
			solutions = conf.getSolutions(Long.valueOf(conf.number()).intValue());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
        System.out.println("Possible solutions so far:");
        for (final List<String> solution : solutions) {
        	System.out.println(solution);
        }
	}
	public static void sortListById(List<IntString> list) {
        Collections.sort(list, new Comparator<IntString>() {
            @Override
            public int compare(IntString item1, IntString item2) {
                return Long.compare(item1.getId(), item2.getId());
            }
        });
    }

}
