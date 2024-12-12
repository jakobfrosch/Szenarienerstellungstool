package at.jku.win.masterarbeit.szenarienerstellungstool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.Constraint;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;
import de.ovgu.featureide.fm.core.functional.Functional;
import java.io.File;
import java.io.IOException;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.RenamingsManager;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationAnalyzer;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.ITWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.RandomConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.twise.TWiseConfigurationGenerator;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.w3c.dom.*;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.job.monitor.ConsoleMonitor;
import de.ovgu.featureide.fm.core.job.monitor.IMonitor;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import at.jku.win.masterarbeit.util.MotherfeatureFeature;
import javax.xml.parsers.*;
import java.io.*;
public class FeatureModelAdaptions {
	static Collection<IFeature> addFeatures = new ArrayList<>();
	public static FeatureModelFormula featureModel;
	public static Configuration configuration;
	public static ConfigurationAnalyzer configurationAnalyzer;
	public static ArrayList<Object> undefinedFeatures = new ArrayList<Object>();
	public static ArrayList<Object> manuallySelectedFeatures = new ArrayList<Object>();
	public static ArrayList<Object> automaticallySelectedFeatures = new ArrayList<Object>();
	public static ArrayList<Object> automaticallyDeselectedFeatures = new ArrayList<Object>();
	public static List<String> selectedFeatures = new ArrayList<String>();
	private static final Logger logger = Logger.getLogger(FeatureModelAdaptions.class.getName());
	public static ArrayList<MotherfeatureFeature> fillOpenScenario_required(String source) {
		ArrayList<MotherfeatureFeature> openScenario_required = new ArrayList<MotherfeatureFeature>();
		try {
		      File fXmlFile = new File(source);
		      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		      Document doc = dBuilder.parse(fXmlFile);
		      doc.getDocumentElement().normalize();
		      NodeList nList = doc.getElementsByTagName("*");
		      int counter=0;
		      for (int temp = 0; temp < nList.getLength(); temp++) {
		        Element element = (Element) nList.item(temp);
		        
		        if (element.hasAttribute("use") && element.getAttribute("use").equals("required")) {
		        	try {
		        		openScenario_required.add(new MotherfeatureFeature(element.getParentNode().getAttributes().getNamedItem("name").getNodeValue(),element.getAttribute("name")));
		        	}
		        	catch (NullPointerException e) {
		        		logger.severe("!! " + element.getAttribute("name") + " " + true + temp +" "+ counter);
		            }
		        	counter++;
		        }
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		return openScenario_required;
	}
	
	public static FeatureModel addFeature(FeatureModel fm, IFeatureStructure motherStructure, String name) {
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		IFeature f=factory.createFeature(fm, name);
		IFeatureStructure fStructure=f.getStructure();
		motherStructure.addChild(fStructure);
		return fm;
	}
	public static FeatureModel adaptFeatureModelWeather_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null &&test[i].toString().contains("Weather")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				weatherfs=fs.getChildren();
				for(IFeatureStructure j:weatherfs) {
					if (j.getFeature().getName().contains("Precipitation")) {	
						for(IFeatureStructure k:j.getChildren()) {
							String[] parts = k.getFeature().getName().split("\\|");
                            String attributeName = parts[0];
                            String[] parts2 = attributeName.split("\\s", 2);
							if (k.getFeature().getName().contains("precipitationType")) {
								if (!k.getFeature().getName().contains("rain")) {
									fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|rain");
									fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|"+parts[1]);
									k.getFeature().setName(attributeName+"|alt");
									k.setAlternative();
								}
								//fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|rain");
								//fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|snow");
								//fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|hail");
								//k.setAlternative();
							}
							else if (k.getFeature().getName().contains("intensity")) {
								String attributeName_old = parts[1];
								fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|"+attributeName_old);
								fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|1");
								fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|0");
								Random random = new Random();
						        double randomNumber = random.nextDouble();
						        fm=addFeature(fm, k, parts2[0]+"_4 "+parts2[1]+"|"+String.format(Locale.US, "%.2f", randomNumber));
						        randomNumber = random.nextDouble();
						        fm=addFeature(fm, k, parts2[0]+"_5 "+parts2[1]+"|"+String.format(Locale.US, "%.2f", randomNumber));
								k.setAlternative();
								k.getFeature().setName(attributeName+"|alt");
							}
						}
					}
					else if (j.getFeature().getName().contains("Fog")) {
						for(IFeatureStructure k:j.getChildren()) {
							String[] parts = k.getFeature().getName().split("\\|");
	                        String attributeName = parts[0];
	                        String[] parts2 = attributeName.split("\\s", 2);
							if (k.getFeature().getName().contains("visualRange")) {
								String attributeName_old = parts[1];
								fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|"+attributeName_old);
								fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|500000.0");
								fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|1000.0");
								Random random = new Random();
								int randomNumber = random.nextInt(1000000 - 1000 + 1) + 1000;
						        fm=addFeature(fm, k, parts2[0]+"_4 "+parts2[1]+"|"+randomNumber);
						        randomNumber = random.nextInt(1000000 - 1000 + 1) + 1000;
						        fm=addFeature(fm, k, parts2[0]+"_5 "+parts2[1]+"|"+randomNumber);
								k.setAlternative();
								k.getFeature().setName(attributeName+"|alt");
							}
						}
					}
				}
			}
		}
		return fm;
	}
	public static FeatureModel adaptFeatureModelPosition_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> positionfs;
		ArrayList<String> addedFeatureNames = new ArrayList<String>();
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null && test[i].toString().contains("TeleportAction")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				positionfs=fs.getChildren();
				String fullName;
				for(IFeatureStructure j:positionfs) {
					String[] parts = j.getChildren().get(0).getFeature().getName().split(" ");
                    String attributeName = parts[1];
                    String attributeValue = parts[0];
					String fName1=attributeValue+"_1 "+attributeName;
                    String fName2=attributeValue+"_2 "+attributeName;
                    j.setAlternative();
                    final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
                    IFeature newf=factory.createFeature(fm, fName1);
            		IFeatureStructure fStructure=newf.getStructure();
            		String[] partName=null;
            		String[] partTwoName=null;
            		double parsedNumber2=0;
            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
            			fullName = childfeature.getFeature().getName();
            			partName = fullName.split(" ");
            			partTwoName = partName[1].split("\\|");
            			if (!partName[1].contains("z")) {
            				double randomNumber = 0.5 + Math.random();
            				parsedNumber2 = Double.parseDouble(partTwoName[1])+randomNumber;
                			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_1 "+partTwoName[0]+"|"+parsedNumber2);
                			IFeatureStructure newStruct = newf2_1.getStructure();
                			if (childfeature.isMandatory()) {
                				newStruct.setMandatory(true);
                			}
                			fStructure.addChild(newStruct);
            			}
            			else {
            				parsedNumber2 = Double.parseDouble(partTwoName[1]);
                			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_1 "+partTwoName[0]+"|"+parsedNumber2);
                			IFeatureStructure newStruct = newf2_1.getStructure();
                			if (childfeature.isMandatory()) {
                				newStruct.setMandatory(true);
                			}
                			fStructure.addChild(newStruct);
            			}
            		}
         
            		fullName = j.getChildren().get(0).getFeature().getName();
        			partName = fullName.split(" ");
            		j.getChildren().get(0).getFeature().setName(partName[0]+"_0 "+partName[1]);
            		j.addChild(fStructure);
                    IFeature newf2=factory.createFeature(fm, fName2);
            		IFeatureStructure fStructure2=newf2.getStructure();
            		
            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
            			fullName = childfeature.getFeature().getName();
            			partName = fullName.split(" ");
            			partTwoName = partName[1].split("\\|");
            			if (!partName[1].contains("z")) {
            				double randomNumber = 0.5 + Math.random();
            				parsedNumber2 = Double.parseDouble(partTwoName[1])-randomNumber;
                			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_2 "+partTwoName[0]+"|"+parsedNumber2);
                			IFeatureStructure newStruct2 = newf2_1.getStructure();
                			if (childfeature.isMandatory()) {
                				newStruct2.setMandatory(true);
                			}
                			fStructure2.addChild(newStruct2);
            			}
            			else {
            				parsedNumber2 = Double.parseDouble(partTwoName[1]);
                			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_2 "+partTwoName[0]+"|"+parsedNumber2);
                			IFeatureStructure newStruct2 = newf2_1.getStructure();
                			if (childfeature.isMandatory()) {
                				newStruct2.setMandatory(true);
                			}
                			fStructure2.addChild(newStruct2);
            			}
            		}
            		j.addChild(fStructure2);
                    addedFeatureNames.add(fName1);
                    addedFeatureNames.add(fName2);
					j.setAlternative();
				}
			}
		}
		Collection<IFeature> features2 = fm.getFeatures();
		
		test=features2.toArray();
		for (int i=0; i<features2.size(); i++) {
			for (String featureName:addedFeatureNames) {
				if (test[i]!=null && test[i].toString().contains(featureName)) {
					f=fm.getFeature(test[i].toString());
					fs=f.getStructure();
					positionfs=fs.getChildren();
				}
			}
			
		}
		return fm;
	}

	public static FeatureModel adaptFeatureModelWaypoints_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> positionfs;
		ArrayList<String> addedFeatureNames = new ArrayList<String>();
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null && test[i].toString().contains("Waypoint")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				positionfs=fs.getChildren();
				String fullName;
				String[] parts = null;
				for(IFeatureStructure j:positionfs) {
					if (j.getFeature().getName().contains("Position")) {
						parts = j.getChildren().get(0).getFeature().getName().split(" ");
	                    String attributeName = parts[1];
	                    String attributeValue = parts[0];
						String fName1=attributeValue+"_1 "+attributeName;
	                    String fName2=attributeValue+"_2 "+attributeName;
	                    j.setAlternative();
	                    final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
	                    IFeature newf=factory.createFeature(fm, fName1);
	            		IFeatureStructure fStructure=newf.getStructure();
	            		String[] partName=null;
	            		String[] partTwoName=null;
	            		double parsedNumber2=0;
	            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
	            			fullName = childfeature.getFeature().getName();
	            			partName = fullName.split(" ");
	            			partTwoName = partName[1].split("\\|");
	            			parsedNumber2 = Double.parseDouble(partTwoName[1])+1;
	            			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_1 "+partTwoName[0]+"|"+parsedNumber2);
	            			IFeatureStructure newStruct = newf2_1.getStructure();
	            			if (childfeature.isMandatory()) {
	            				newStruct.setMandatory(true);
	            			}
	            			fStructure.addChild(newStruct);
	            			
	            		}
	         
	            		fullName = j.getChildren().get(0).getFeature().getName();
	        			partName = fullName.split(" ");
	            		j.getChildren().get(0).getFeature().setName(partName[0]+"_0 "+partName[1]);
	            		j.addChild(fStructure);
	                    IFeature newf2=factory.createFeature(fm, fName2);
	            		IFeatureStructure fStructure2=newf2.getStructure();
	            		
	            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
	            			fullName = childfeature.getFeature().getName();
	            			partName = fullName.split(" ");
	            			partTwoName = partName[1].split("\\|");
	            			parsedNumber2 = Double.parseDouble(partTwoName[1])-1;
	            			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_2 "+partTwoName[0]+"|"+parsedNumber2);
	            			IFeatureStructure newStruct2 = newf2_1.getStructure();
	            			if (childfeature.isMandatory()) {
	            				newStruct2.setMandatory(true);
	            			}
	            			fStructure2.addChild(newStruct2);
	            		}
	            		j.addChild(fStructure2);
	                    addedFeatureNames.add(fName1);
	                    addedFeatureNames.add(fName2);
						j.setAlternative();
					}
					
				}
			}
		}
		Collection<IFeature> features2 = fm.getFeatures();
		
		test=features2.toArray();
		for (int i=0; i<features2.size(); i++) {
			for (String featureName:addedFeatureNames) {
				if (test[i]!=null && test[i].toString().contains(featureName)) {
					f=fm.getFeature(test[i].toString());
					fs=f.getStructure();
					positionfs=fs.getChildren();
				}
			}
			
		}
		return fm;
	}
	public static FeatureModel adaptFeatureModelAcceleration_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> positionfs;
		ArrayList<String> addedFeatureNames = new ArrayList<String>();
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null && test[i].toString().contains("Vehicle")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				positionfs=fs.getChildren();
				String fullName;
				String[] parts = null;
				for(IFeatureStructure j:positionfs) {
					if (j.getFeature().getName().contains("Performance")) {
						parts = j.getChildren().get(0).getFeature().getName().split(" ");
	                    String attributeName = parts[1];
	                    String attributeValue = parts[0];
						String fName1=attributeValue+"_1 "+attributeName;
	                    String fName2=attributeValue+"_2 "+attributeName;
	                    j.setAlternative();
	                    final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
	                    IFeature newf=factory.createFeature(fm, fName1);
	            		IFeatureStructure fStructure=newf.getStructure();
	            		String[] partName=null;
	            		String[] partTwoName=null;
	            		double parsedNumber2=0;
	            		
	            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
	            			if (childfeature.getFeature().getName().contains("maxAcceleration")) {
	            				fullName = childfeature.getFeature().getName();
		            			partName = fullName.split(" ");
		            			partTwoName = partName[1].split("\\|");
		            			String[] parts2 = attributeName.split("\\s", 2);
		            			parsedNumber2 = Double.parseDouble(partTwoName[1])+1;
		            			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_1 "+partTwoName[0]+"|"+parsedNumber2);
		            			IFeatureStructure newStruct = newf2_1.getStructure();
		            			if (childfeature.isMandatory()) {
		            				newStruct.setMandatory(true);
		            			}
		            			fStructure.addChild(newStruct);	
	            			}
	            		}
	            		fullName = j.getChildren().get(0).getFeature().getName();
	        			partName = fullName.split(" ");
	            		j.getChildren().get(0).getFeature().setName(partName[0]+"_0 "+partName[1]);
	            		j.addChild(fStructure);
	                    IFeature newf2=factory.createFeature(fm, fName2);
	            		IFeatureStructure fStructure2=newf2.getStructure();
	            		
	            		for (IFeatureStructure childfeature : j.getChildren().get(0).getFeature().getStructure().getChildren()) {
	            			if (childfeature.getFeature().getName().contains("maxAcceleration")) {
		            			fullName = childfeature.getFeature().getName();
		            			partName = fullName.split(" ");
		            			partTwoName = partName[1].split("\\|");
		            			parsedNumber2 = Double.parseDouble(partTwoName[1])-1;
		            			IFeature newf2_1=factory.createFeature(fm, partName[0]+"_2 "+partTwoName[0]+"|"+parsedNumber2);
		            			IFeatureStructure newStruct2 = newf2_1.getStructure();
		            			if (childfeature.isMandatory()) {
		            				newStruct2.setMandatory(true);
		            			}
		            			fStructure2.addChild(newStruct2);
	            			}
	            		}
	            		j.addChild(fStructure2);
	                    addedFeatureNames.add(fName1);
	                    addedFeatureNames.add(fName2);
						j.setAlternative();
					}
					
				}
			}
		}
		Collection<IFeature> features2 = fm.getFeatures();
		
		test=features2.toArray();
		for (int i=0; i<features2.size(); i++) {
			for (String featureName:addedFeatureNames) {
				if (test[i]!=null && test[i].toString().contains(featureName)) {
					f=fm.getFeature(test[i].toString());
					fs=f.getStructure();
					positionfs=fs.getChildren();
				}
			}
			
		}
		return fm;
	}
	public static FeatureModel adaptFeatureModelAcceleration_add2(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size()-1; i++) {
			if (test[i]!=null &&test[i].toString().contains("Vehicle")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				weatherfs=fs.getChildren();
				for(IFeatureStructure j:weatherfs) {
					if (j.getFeature().getName().contains("Performance")) {	
						for(IFeatureStructure k:j.getChildren()) {
							String[] parts = k.getFeature().getName().split("\\|");
                            String attributeName = parts[0];
                            String[] parts2 = attributeName.split("\\s", 2);
							if (k.getFeature().getName().contains("maxAcceleration")) {
								if (!parts[1].equals("0")) {
									Random random = new Random();
							        //double randomNumber = random.nextDouble();
							        int randomValue = 100 + random.nextInt(301);
							        fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|"+parts[1]);
									fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|"+randomValue);
									randomValue = 100 + random.nextInt(301);
									fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|"+randomValue);
									k.getFeature().setName(attributeName+"|alt");
									k.setAlternative();
								}
								//fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|rain");
								//fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|snow");
								//fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|hail");
								//k.setAlternative();
							}
						}
					}
				}
			}
		}
		return fm;
	}
	public static FeatureModel adaptFeatureModelSpeed_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size()-1; i++) {
			if (test[i]!=null &&test[i].toString().contains("Vehicle")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				weatherfs=fs.getChildren();
				for(IFeatureStructure j:weatherfs) {
					if (j.getFeature().getName().contains("Performance")) {	
						for(IFeatureStructure k:j.getChildren()) {
							String[] parts = k.getFeature().getName().split("\\|");
                            String attributeName = parts[0];
                            String[] parts2 = attributeName.split("\\s", 2);
							if (k.getFeature().getName().contains("maxSpeed")) {
								if (!parts[1].equals("0")) {
									Random random = new Random();
									int randomValue = 30 + random.nextInt(121);
							        fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|"+parts[1]);
									fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|"+randomValue);
									randomValue = 30 + random.nextInt(121);
									fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|"+randomValue);
									k.getFeature().setName(attributeName+"|alt");
									k.setAlternative();
								}
							}
						}
					}
				}
			}
		}
		return fm;
	}
	public static FeatureModel adaptFeatureModelVehicles_add(FeatureModel fm) {
        Collection<IFeature> features = fm.getFeatures();
        List<IFeatureStructure> entityFs;
        IFeature f;
        IFeatureStructure fs;
        RenamingsManager rm=fm.getRenamingsManager();
        Object[] test = features.toArray();
        for (int i = 0; i < fm.getFeatures().size(); i++) {
            if (test[i] != null && test[i].toString().contains(" Entities")) {
            	f = fm.getFeature(test[i].toString());
                fs = f.getStructure();
                entityFs = new ArrayList<>(fs.getChildren());
                List<IFeatureStructure> newScenarioObjects = new ArrayList<>();
                int x=1;
                for (IFeatureStructure j : entityFs) {
                    if (j.getFeature().getName().contains(" ScenarioObject")) {
                    	j.getFeature().setName(generateNewAltFeatureName(j.getFeature().getName(),0));
                    	addFeatures.add(j.getFeature());
                    	rm.renameFeature(j.getFeature().getName(), generateNewAltFeatureName(j.getFeature().getName(),0));
                        IFeatureStructure newScenarioObject = cloneFeatureStructure(fm,j,false,x);
                        x++;
                        newScenarioObjects.add(newScenarioObject);
                        addFeatures.add(newScenarioObject.getFeature());
                 
                    }
                }
                for (IFeatureStructure newScenarioObject : newScenarioObjects) {
                    fs.addChild(newScenarioObject);
                    addFeatures.add(newScenarioObject.getFeature());
                }
                fs.setOr();
            }
            if (test[i] != null && test[i].toString().endsWith(" Actions")) {
            	f = fm.getFeature(test[i].toString());  
                fs = f.getStructure();
                entityFs = new ArrayList<>(fs.getChildren());
                List<IFeatureStructure> newScenarioObjects = new ArrayList<>();
                int x = 1;
                
                for (IFeatureStructure j : entityFs) {
                    if (j.getFeature().getName().contains(" Private")) {
                    	j.getFeature().setName(generateNewAltFeatureName(j.getFeature().getName(),0));
                    	addFeatures.add(j.getFeature());
                    	rm.renameFeature(j.getFeature().getName(), generateNewAltFeatureName(j.getFeature().getName(),0));
                        IFeatureStructure newScenarioObject = cloneFeatureStructure(fm,j,false,x);
                        x++;
                        newScenarioObjects.add(newScenarioObject);
                        addFeatures.add(newScenarioObject.getFeature());
                    }
                }
                for (IFeatureStructure newScenarioObject : newScenarioObjects) {
                    fs.addChild(newScenarioObject);
                    addFeatures.add(newScenarioObject.getFeature());
                }
                fs.setOr();
            }
        }
        for (IFeature feature : addFeatures) {
            fm.addFeature(feature);
        }
        addFeatures=new ArrayList<>();
        return fm;
    }
	public static FeatureModel addConstraints(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		
        List<IFeatureStructure> entityFs;
        String constraintName1;
        IFeature f;
        IFeatureStructure fs;
        Object[] test = features.toArray();
		for (int i = 0; i < fm.getFeatures().size(); i++) {
            if (test[i] != null && test[i].toString().contains(" ScenarioObject")) {
            	f = fm.getFeature(test[i].toString());
                fs = f.getStructure();
                entityFs = new ArrayList<>(fs.getChildren());
                for (IFeatureStructure j : entityFs) {
                    if (j.getFeature().getName().contains(" name")) {
                    	constraintName1 = j.getFeature().getName();
                    	for (int x = 0; x < fm.getFeatures().size(); x++) {
                    		if(test[x]!= null && test[x].toString().contains("entityRef")) {
                    			int index = test[x].toString().lastIndexOf("|");
                    			int index2 = constraintName1.lastIndexOf("|");
                    			if (test[x].toString().substring(index+1).equals(constraintName1.substring(index2+1))) {
                    				final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
                                    Node constraintNode = new Implies(new Literal(constraintName1), new Literal(test[x].toString()));
                                    Constraint iC= factory.createConstraint(fm, constraintNode);
                                    fm.addConstraint(iC);
                                    logger.info("Added Constraint: "+constraintName1+" -> "+test[x].toString());
                                    constraintNode = new Implies(new Literal(test[x].toString()), new Literal(constraintName1));
                                    iC= factory.createConstraint(fm, constraintNode);
                                    fm.addConstraint(iC);
                                    logger.info("Added Constraint: "+test[x].toString()+" -> "+constraintName1);
                                    
                    			}
                    		}
                    	}
                    }
                }
            }
		}
		
		return fm;
	}
    private static IFeatureStructure cloneFeatureStructure(FeatureModel fm, IFeatureStructure original, Boolean changeName, int x) {
    	final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
        IFeature originalFeature = original.getFeature();
        String originalName = originalFeature.getName();
        if ((originalName.contains("name")||originalName.contains("entityRef"))&&(original.getParent().getFeature().getName().contains("ScenarioObject") || original.getParent().getFeature().getName().endsWith("Private"))) {
        	originalName = originalName+"_add";
        }
        String newName = generateNewAltFeatureName(originalName,x);
        IFeature clonedFeature=factory.createFeature(fm, newName);
        clonedFeature.setName(newName);
		IFeatureStructure clonedStructure = clonedFeature.getStructure();
		clonedStructure.getFeature().setName(newName);
		if (original.isMandatory()) {
			clonedStructure.setMandatory(true);
		}
        for (IFeatureStructure child : original.getChildren()) {
            IFeatureStructure clonedChild = cloneFeatureStructure(fm,child,changeName,x);
            clonedStructure.addChild(clonedChild);
            addFeatures.add(clonedChild.getFeature());
        }

        return clonedStructure;
    }
    private static String generateNewAltFeatureName(String originalName,int x) {
        String[] parts = originalName.split(" ");
        if (originalName.contains("_alt_")) {
        	return parts[0] +x+" " + parts[1];
        }
        if (parts.length > 1) {
            return parts[0] + "_alt_"+x+" " + parts[1];
        } else {
            return originalName + "_alt_"+x;
        }
    }
    public static int vehicleCounter(FeatureModel fm) {
    	Collection<IFeature> features = fm.getFeatures();
        List<IFeatureStructure> entityFs;
        int counter = 0;
        IFeature f;
        IFeatureStructure fs;
        Object[] test = features.toArray();
    	for (int i = 0; i < fm.getFeatures().size(); i++) {
            if (test[i] != null && test[i].toString().contains(" Entities")) {
                f = fm.getFeature(test[i].toString());
                fs = f.getStructure();
                entityFs = new ArrayList<>(fs.getChildren());
                for (IFeatureStructure j : entityFs) {
                    if (j.getFeature().getName().contains(" ScenarioObject")) {
                    	counter++;
                    }
                }
            }
    	}
    	return counter;
    }
	public static FeatureModel adaptFeatureModelAgression_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		boolean aggressionflag = false;
		for (int i=0; i<fm.getFeatures().size()-1; i++) {
			if (test[i]!=null &&test[i].toString().contains("Properties")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				weatherfs=fs.getChildren();
				for(IFeatureStructure j:weatherfs) {
					if (j.getFeature().getName().contains("Property")) {
						for(IFeatureStructure k:j.getChildren()) {
							if (k.getFeature().getName().contains("aggression")) {
								aggressionflag = true;
							}
						}
						if (aggressionflag) {
							for(IFeatureStructure k:j.getChildren()) {
								String[] parts = k.getFeature().getName().split("\\|");
	                            String attributeName = parts[0];
	                            String[] parts2 = attributeName.split("\\s", 2);
								if (k.getFeature().getName().contains("value")) {
									Random random = new Random();
							        double randomValue = 0.1 + (1.0 - 0.1) * random.nextDouble();
									fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|0.5");
									fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|0.1");
									fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|1");
									fm=addFeature(fm, k, parts2[0]+"_4 "+parts2[1]+"|"+randomValue);
									k.setAlternative();
									k.getFeature().setName(attributeName+"|alt");
								}
							}
						}
						aggressionflag=false;

					}
				}
			}
		}
		return fm;
	}
	public static FeatureModel adaptFeaturechangeSettings(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size()-1; i++) {
			if (test[i]!=null &&test[i].toString().contains("Weather")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				weatherfs=fs.getChildren();
				for(IFeatureStructure j:weatherfs) {
					if (j.getFeature().getName().contains("Precipitation")) {
						
						for(IFeatureStructure k:j.getChildren()) {
							String[] parts = k.getFeature().getName().split("\\|");
                            String attributeName = parts[0];
                            
                            String attributeValue = parts.length > 1 ? parts[1] : "";
							if (k.getFeature().getName().contains("precipitationType")) {
								if (attributeValue.equals("rain")) {
									attributeValue="snow";
								}
								else {
									attributeValue="rain";
								}
							}
							if (k.getFeature().getName().contains("intensity")) {
								attributeValue="0.5";
								
							}
							k.getFeature().setName(attributeName+"|"+attributeValue);
						}
						
					}
				}
			}
		}
		
		return fm;
	}
	static FeatureModel adaptFeatureModelSwitchTrueFalse_add(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null &&(test[i].toString().contains("|false")||test[i].toString().contains("|true"))) {
				f=fm.getFeature(test[i].toString());
				fs = f.getStructure();
				String[] parts = f.getName().split("\\|");
                String attributeName = parts[0];
                IFeature newf1=factory.createFeature(fm, attributeName+"|true");
                IFeature newf2=factory.createFeature(fm, attributeName+"|false");
                
                newf1.getStructure().setParent(f.getStructure());
                newf2.getStructure().setParent(f.getStructure());
                fm=addFeature(fm, fs, attributeName+"|true");
                fm=addFeature(fm, fs, attributeName+"|false");
                fs.setAlternative();
                f.setName(attributeName+"|alt");
			}
		}
		return fm;
	}
	
	public static void createRandomConfig(FeatureModel fm, String file, int maxFeatureModels) {
		FeatureModelFormula formula = new FeatureModelFormula(fm);
		CNF cnf = formula.getCNF();
		logger.info("Used Random");
	    RandomConfigurationGenerator generator = new RandomConfigurationGenerator(cnf, maxFeatureModels);
	    IMonitor<List<LiteralSet>> monitor = new ConsoleMonitor<>();
	    List<LiteralSet> literalSets = null;
	    try {
	        literalSets = generator.execute(monitor);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    int counter = 0;
	    for (LiteralSet literalSet : literalSets) {
	        Configuration configuration = new Configuration(formula);
	        selectedFeatures = new ArrayList<>();
	        for (int literal : literalSet.getLiterals()) {
	            int variableIndex = Math.abs(literal) - 1;
	            Collection<SelectableFeature> sfs = configuration.getFeatures();
		        ArrayList<SelectableFeature> sfs_arraylist = new ArrayList<SelectableFeature>(sfs);
		        SelectableFeature sf=sfs_arraylist.get(variableIndex);
		        
	            if (sf != null && !sf.getName().contains("|alt")) {
	                if (literal > 0) {
	                    configuration.setManual(sf, Selection.SELECTED);
	                    if (!sf.getFeature().getStructure().isMandatorySet()) {
	                    	selectedFeatures.add(sf.getName());
	                    }
	                    else {
	                    	
	                    }
	                } else {
	                    configuration.setManual(sf, Selection.UNSELECTED);
	                }
	            }
	        }
	        logger.info("Konfiguration " + counter + ": ");
	        logger.info("Ausgewählte Features: " + selectedFeatures);
	        IFeatureModel newFM = FeatureModelAdaptions.pruneFeatureModelByConfiguration(configuration);
	        FeatureModelAdaptions.saveXMLScenarioI(file+"_"+counter+"_Random.xosc", newFM);
	        counter++;
	    }
	}
	public static boolean checkValidity(FeatureModel fm) {
		FeatureModelFormula formula = new FeatureModelFormula(fm);
		if (fm != null) {
			formula = new FeatureModelFormula(fm);
			final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
			analyzer.analyzeFeatureModel(null);
			if(analyzer.isValid(null)) {
				logger.info("Feature model is valid");
                return true;
			}
			else {
				logger.info("Feature model is not valid");
                return false;
			}
		}
		return false;
		
	}
	public static void createTWiseConfig(FeatureModel fm, String file, int tint, int maxFeatureModels) {
		FeatureModelFormula formula = new FeatureModelFormula(fm);
		CNF cnf = formula.getCNF();
		logger.info("Used T-Value: "+tint);
		ITWiseConfigurationGenerator generator = new TWiseConfigurationGenerator(cnf, tint, maxFeatureModels);
		IMonitor<List<LiteralSet>> monitor = new ConsoleMonitor<>();
        List<LiteralSet> literalSets = null;
        List<String> selectedFeatures = new ArrayList<>();
		try {
			literalSets = generator.execute(monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int counter = 0;
		for (LiteralSet literalSet : literalSets) {
		    Configuration configuration = new Configuration(formula);
		    for (int literal : literalSet.getLiterals()) {
		        int variableIndex = Math.abs(literal) - 1;
		        Collection<SelectableFeature> sfs = configuration.getFeatures();
		        ArrayList<SelectableFeature> sfs_arraylist = new ArrayList<SelectableFeature>(sfs);
		        SelectableFeature sf=sfs_arraylist.get(variableIndex);
		        if (sf!= null && sf.getName()!=null&& !sf.getName().contains("|alt")) {
		        	if (literal > 0) {
			            configuration.setManual(sf, Selection.SELECTED);
			            if (!sf.getFeature().getStructure().isMandatorySet()) {
	                    	selectedFeatures.add(sf.getName());
	                    }
			        } else {
			            configuration.setManual(sf, Selection.UNSELECTED);
			        }
		        }
		    }
		    logger.info("Konfiguration " + counter + ": ");
	        logger.info("Ausgewählte Features: " + selectedFeatures);
		    IFeatureModel newFM = FeatureModelAdaptions.pruneFeatureModelByConfiguration(configuration);
		    FeatureModelAdaptions.saveXMLScenarioI(file+"_"+counter+"_Twise.xosc", newFM);
		    counter++;
		}
	}
	public static FeatureModel setFmToMandatory (FeatureModel fm){
		FeatureModel fmtemp=fm.clone();
        for (IFeature feature : fmtemp.getFeatures()) {
        	if(!feature.getStructure().isRoot()) {
        		if(feature.getStructure().getParent().isAlternative()||feature.getStructure().getParent().isOr()) {
        			feature.getStructure().setMandatory(false);
        		}
        		else {
                    feature.getStructure().setMandatory(true);
                }
        	}
        	else {
        		feature.getStructure().setMandatory(true);
        	}
        }
        return fmtemp;
	}
	public static void saveXMLScenarioI(String filePath, IFeatureModel fm) {
		StringBuilder xmlBuilder = new StringBuilder();
		List<IFeatureStructure> list=new ArrayList<IFeatureStructure>();
        list.add(fm.getStructure().getRoot());
		xmlBuilder.append("<?xml version=\"1.0\"?>\n");
		xmlBuilder = OpenScenarioGenerator.generateOpenScenarioFromFmRecursively(fm, list, xmlBuilder, 0);
		String xmlString=xmlBuilder.toString();
		try {
            Files.write(Paths.get(filePath), xmlString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	private static List<SelectableFeature> getSelectableFeatures() {
		final List<SelectableFeature> features = Functional.toList(Functional.filter(configuration.getFeatures(),
				feature -> feature.getFeature().getStructure().isConcrete()
						&& !feature.getFeature().getStructure().hasHiddenParent()));
		Collections.sort(features, (SelectableFeature o1, SelectableFeature o2) -> {
			if (o1.getAutomatic() == Selection.UNDEFINED) {
				if (o2.getAutomatic() == Selection.UNDEFINED) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return -1;
				}
			} else {
				if (o2.getAutomatic() == Selection.UNDEFINED) {
					return 1;
				} else {
					return o1.getName().compareTo(o2.getName());
				}
			}
		});
		return features;
	}
	public static void saveXMLFeatureModel(String filePath, FeatureModel fm) {
		XmlFeatureModelFormat fmXmlFormat = new XmlFeatureModelFormat();
		fmXmlFormat.setFeatureNameValidator(fmXmlFormat);

		String xmlString=fmXmlFormat.write(fm);
		try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(xmlString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	static void updateLists() {
		undefinedFeatures.clear();
		manuallySelectedFeatures.clear();
		automaticallyDeselectedFeatures.clear();
		automaticallySelectedFeatures.clear();
		
		configurationAnalyzer.update(true, null);
		final List<SelectableFeature> features = getSelectableFeatures();

		for (SelectableFeature feature : features) {
			switch (feature.getSelection()) {
			case SELECTED:
				if (feature.getAutomatic() != Selection.UNDEFINED) {
					automaticallySelectedFeatures.add(feature);
				} else {
					manuallySelectedFeatures.add(feature);
				}
				break;
			case UNDEFINED:
				undefinedFeatures.add(feature);
				break;
			case UNSELECTED:
				if (feature.getAutomatic() != Selection.UNDEFINED) {
					automaticallyDeselectedFeatures.add(feature);
				}
				break;
			default:
				break;
			}
		}
	}
	public static IFeatureModel pruneFeatureModelByConfiguration(Configuration configuration) {
	    IFeatureModel originalFeatureModel = configuration.getFeatureModel();
	    IFeatureModel prunedFeatureModel = originalFeatureModel.clone();
	    ArrayList<IFeature> deleteFeatures = new ArrayList<>();
	    Collection<IFeature> selectedFeatures = configuration.getSelectedFeatures();
	    Iterator<IFeature> featureIterator = prunedFeatureModel.getFeatures().iterator();
	    while (featureIterator.hasNext()) {
	        IFeature feature = featureIterator.next();

	        if (!selectedFeatures.contains(feature)) {
	        	deleteFeatures.add(feature);
	        }
	    }
	    for (IFeature feature : deleteFeatures) {
	    	prunedFeatureModel.deleteFeature(feature);
	    }
	    return prunedFeatureModel;
	}
}
