package szenarienerstellungstool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Savepoint;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.sat4j.specs.TimeoutException;

import de.ovgu.featureide.fm.attributes.base.IExtendedFeatureModel;
import de.ovgu.featureide.fm.attributes.base.impl.ExtendedFeatureModel;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureProperty;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FactoryManager;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureStructure;
import de.ovgu.featureide.fm.core.color.FeatureColor;
import de.ovgu.featureide.fm.core.conf.IFeatureGraph;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.io.IFeatureGraphFormat;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

import java.io.File;
import java.io.IOException;
import java.util.List;



import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
public class Test7 {
	
	public static void main(String[] args) {
        ArrayList<TwoStrings> ar=new ArrayList();
        ArrayList<TwoStrings> ar2=new ArrayList();
        Element rootElement=null;
        
        try {
            File inputFile = new File("/Users/jakobfroschauer/Downloads/scenario_runner-master/srunner/examples/PedestrianCrossingFront.xosc");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            rootElement = doc.getDocumentElement();
            System.out.println("Wie ist die ArrayList abgespeichert?");
            //XMLParser.printTree(rootElement, null, 0);

            ar=XMLParser.fillCreateArraylist(rootElement, "?xml version=\"1.0\"?", 0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        IFeatureModel fm = fillFeatureModel(ar);
        fm.setSourceFile(Paths.get("/Users/jakobfroschauer/Desktop/FM/text.xml"));
        System.out.println("Wie sieht das Feature Model aus?");
        System.out.println(fm);
        ar2=XMLParser.fillCreateArraylist(fm);
        /*
        for(TwoStrings i : ar2) {
        	for (int j=0;j<i.getLevel(); j++) {
        		System.out.print("  ");
        	}
        	System.out.print("Motherfeature: "+i.getMotherfeature()+"; feature: "+i.getFeature()+"\n");
        }
        */
        System.out.println("-----XML Version generiert aus XML -> ArrayList -> FeatureModel -> ArrayList----");
        
        System.out.print(XMLGenerator.generateXML3(ar2));
        List<IFeatureStructure> list=new ArrayList();
        list.add(fm.getStructure().getRoot());
        StringBuilder xmlBuilder = new StringBuilder();
        
        xmlBuilder = XMLGenerator.generateXMLrec(fm, list, xmlBuilder, 0);
        System.out.println("-----XML Version generiert aus XML -> ArrayList -> FeatureModel (rekursiv)----");
        //System.out.print(xmlBuilder.toString());
        fm.setSourceFile(Paths.get("/Users/jakobfroschauer/Desktop/FM/text.xml"));
        FeatureColor fc;
        XmlFeatureModelFormat fmXmlFormat = new XmlFeatureModelFormat();        
        
		String xmlString=fmXmlFormat.write(fm);
		System.out.println(xmlString);
		String filePath = "/Users/jakobfroschauer/Desktop/FM/text.xml";
		try {
            // Open a BufferedWriter to write the XML string to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            
            // Write the XML string to the file
            writer.write(xmlString);
            
            // Close the writer
            writer.close();
            
            System.out.println("XML data saved to file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
		FeatureModel fm2=XMLParser.fillCreateFeatureModel(rootElement, "?xml version=\"1.0\"?");
        System.out.println(fm2.toString());
		XmlFeatureModelFormat fmXmlFormat2 = new XmlFeatureModelFormat();        
        
		String xmlString2=fmXmlFormat2.write(fm2);
		//System.out.println(xmlString);
		String filePath2 = "/Users/jakobfroschauer/Desktop/FM/text2.xml";
		List<IFeatureStructure> list2=new ArrayList();
        list.add(fm2.getStructure().getRoot());
		StringBuilder xmlBuilder2 = new StringBuilder();
		xmlBuilder2 = XMLGenerator.generateXMLrec(fm2, list2, xmlBuilder2, 0);
		System.out.print(xmlBuilder.toString());
		XMLGenerator.saveXmlToFile(xmlBuilder2.toString(), "FeatureModel.xml");
		try {
            // Open a BufferedWriter to write the XML string to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2));
            
            // Write the XML string to the file
            writer.write(xmlString2);
            
            // Close the writer
            writer.close();
            
            System.out.println("XML data saved to file: " + filePath2);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
        
	}
	
	public static IFeatureModel fillFeatureModel(ArrayList<TwoStrings> ar) {
		//System.out.println("JF: "+ ar.get(0).getFeature()+ar.get(0).getMotherfeature());
		final IFeatureModel fm = new FeatureModel(ar.get(0).getFeature());
		final IFeatureModelStructure fms=fm.getStructure();
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		final IFeatureModelStructure fmStructure = fm.getStructure();
		IConstraint c1=factory.createConstraint(fm, null);
		IExtendedFeatureModel x;
		IFeatureGraphFormat xy;
		IFeatureGraph xyz;

		
		
		
		//xyza=fm;
		
		
		int counter = 0;
		for (TwoStrings i : ar) {
			Feature f = factory.createFeature(fm, i.getFeature());
			
			//f.setName("#"+f.getInternalId()+i.getFeature());
			
			IFeatureProperty fp=f.getProperty();
			fp.setDescription(""+i.getLevel());
			fm.addFeature(f);
			
        	IFeatureStructure fStructure = f.getStructure();
            if (i.getMotherfeature() == null) {
            	fmStructure.setRoot(fStructure);
            	
            }
            else {
            	/*
            	ArrayList<IFeature> fx = new ArrayList();
            	for (IFeature f1 : fm.getFeatures()) {
            		if (f1.getName().contains(i.getMotherfeature())) {
            			fx.add(f);
            		}
            	}
            	IFeature mf=findHighestNumber(fx,f.getInternalId());
            	IFeatureStructure mfStructure = mf.getStructure();
            	System.out.println("mfStructure "+mfStructure.getFeature().getName());

            	mfStructure.addChild(fStructure);
            	
            	//fm.getFeatures()
            	 * 
            	 
            	int internalid=0;
            	if (counter>2) {
		        	for (int x=1; x<=f.getInternalId();x++) {
		        		if (fStructure.getParent().getFeature().getName()==ar.get(x).getMotherfeature()) {
		        			internalid=(int) f.getInternalId();
		        			System.out.println("haha"+internalid);
		        		}
		        	}
		        	*/
		        	fm.getFeature(i.getMotherfeature()).getStructure().addChild(fStructure);
		        	fStructure.setMandatory(i.isMandatory());
		        }
		    //}
            counter++;
		}
		return fm;
	}
	public static IFeature findHighestNumber(ArrayList<IFeature> list,long max) {
        long highest = Long.MIN_VALUE; // Anfangswert mit dem kleinsten Integer-Wert
        int id = 0;
        //System.out.println("max: "+max);
        for (IFeature f : list) {
            if (f.getInternalId() >= highest && f.getInternalId() <= max) {
                highest = Math.toIntExact(f.getInternalId());
            }
        }
        for (IFeature f : list) {
            if (f.getInternalId() == highest) {
            	//System.out.println("test"+f.getName());
                return f; 
            }
        }
		return null;
        
        
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
