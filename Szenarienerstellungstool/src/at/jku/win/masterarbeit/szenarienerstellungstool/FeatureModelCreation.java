package at.jku.win.masterarbeit.szenarienerstellungstool;


import org.w3c.dom.*;

import at.jku.win.masterarbeit.util.MotherfeatureFeature;
import at.jku.win.masterarbeit.util.MotherfeatureFeatureLevelMand;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureProperty;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import java.util.ArrayList;
import java.util.List;

public class FeatureModelCreation {
	public static int c=0;
   
    public static ArrayList<MotherfeatureFeatureLevelMand> fillCreateArraylist(Node node, String parentName, int level) {
    	ArrayList<MotherfeatureFeatureLevelMand> ar = new ArrayList();
    	ar.add(new MotherfeatureFeatureLevelMand(null,parentName,level,true));
    	ar=createArraylist(node,parentName, level, ar);
    	return ar;
    }

    public static ArrayList<MotherfeatureFeatureLevelMand> createArraylist(Node node, String parentName, int level,ArrayList<MotherfeatureFeatureLevelMand> ar) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            ar.add(new MotherfeatureFeatureLevelMand(parentName,node.getNodeName(),level,true));
            if (node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                for (int k = 0; k < attributes.getLength(); k++) {
                    Node attribute = attributes.item(k);
                    ar.add(new MotherfeatureFeatureLevelMand(node.getNodeName(),""+attribute.getNodeName()+"|"+attribute.getNodeValue(),level,true));
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
    public static FeatureModel fillCreateFeatureModel(Node node, String parentName, List<MotherfeatureFeature> openScenario_required) {
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();   
		FeatureModel fm = new FeatureModel("Test");
    	Feature f = factory.createFeature(fm, node.getNodeName());
    	fm=createFeatureModel(node,parentName,f,0,fm,0,openScenario_required);
    	return fm;
    }
    public static FeatureModel createFeatureModel(Node node, String parentName,IFeature motherfeature, int level,FeatureModel fm, int counter,List<MotherfeatureFeature> openScenario_required) {
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
            		
            		//System.out.println("!!!!!!!!!!!!!!"+node.getNodeName()+"|||"+ openScenario_required.get(i).getFeature()+"!! "+parentName+"||| "+ openScenario_required.get(i).getMotherfeature());
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
                    		
                    		//System.out.println("!!!!!!!!!!!!!!"+attribute.getNodeName()+"|||"+ openScenario_required.get(i).getFeature()+"!! "+node.getNodeName()+"||| "+ openScenario_required.get(i).getMotherfeature());
                    	}
                    }
                    Feature f2=factory.createFeature(fm, "#"+c+" "+attribute.getNodeName()+"|"+attribute.getNodeValue());
                    //System.out.println(attribute.getNodeValue());
                    if (attribute.getNodeValue().equals("")||attribute.getNodeValue().equals("0")) {
                    	f2.getStructure().setHidden(true);
                    }
                    c++;
                    fm.addFeature(f2);
                    IFeatureStructure f2Structure=f2.getStructure();                    
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