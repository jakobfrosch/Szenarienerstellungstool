package at.jku.win.masterarbeit.szenarienerstellungstool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.ConfigFormatManager;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

import java.io.File;
import java.io.IOException;


import org.w3c.dom.*;

import at.jku.win.masterarbeit.util.MotherfeatureFeature;

import javax.xml.parsers.*;
import java.io.*;
public class FeatureModelAdaptions {
	
	public static void main(String[] args) {
        Element rootElement=null;
        
        try {
            File inputFile = new File("C:\\Users\\stefan\\Documents\\PedestrianCrossingFront.xosc");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MotherfeatureFeature> openScenario_required =fillOpenScenario_required("C:\\Users\\stefan\\Documents\\OpenSCENARIO.xml");
        //System.out.print(openScenario_required.toString());
		FeatureModel fm=FeatureModelCreation.fillCreateFeatureModel(rootElement, "?xml version=\"1.0\"?",openScenario_required);
		saveXMLFeatureModel("C:\\Users\\stefan\\Documents\\OpenSCENARIO_output\\PedestrianCrossingFront_FM.xml",fm);
		saveXMLScenario("C:\\Users\\stefan\\Documents\\OpenSCENARIO_output\\PedestrianCrossingFront_unchanged.xml",fm);
		  
		List<IFeatureStructure> list=new ArrayList();
        list.add(fm.getStructure().getRoot());
		StringBuilder xmlBuilder = new StringBuilder();
		
		xmlBuilder.append("<?xml version=\"1.0\"?>\n");
		xmlBuilder = OpenScenarioGenerator.generateOpenScenarioFromFmRecursively(fm, list, xmlBuilder, 0);
		FeatureModel fmAdapted=adaptFeatureModelWeather_add(fm);
		fmAdapted=adaptFeatureModelAcceleration(fmAdapted);
		fmAdapted=adaptFeatureModelSwitchTrueFalse(fmAdapted);
		saveXMLFeatureModel("C:\\Users\\stefan\\Documents\\OpenSCENARIO_output\\PedestrianCrossingFront_Adapted_FM.xml",fmAdapted);
		saveXMLScenario("C:\\Users\\stefan\\Documents\\OpenSCENARIO_output\\PedestrianCrossingFront_weather_Adapted.xosc",fmAdapted);
		saveXMLScenario("C:\\Users\\stefan\\Documents\\OpenSCENARIO_output\\PedestrianCrossingFront_weather_Adapted.xml",fmAdapted);
		
	}
	public static List<MotherfeatureFeature> fillOpenScenario_required(String source) {
		List<MotherfeatureFeature> openScenario_required = new ArrayList();
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
		        	//System.out.println(element.getAttribute("name") + " true");
		        	try {
		        		openScenario_required.add(new MotherfeatureFeature(element.getParentNode().getAttributes().getNamedItem("name").getNodeValue(),element.getAttribute("name")));
		        		//System.out.println(element.getParentNode().getAttributes().getNamedItem("name").getNodeValue() + " " + element.getAttribute("name") + " " + true+ temp+" "+ counter);
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
		return openScenario_required;
	}
	
	public static FeatureModel addFeature(FeatureModel fm, IFeatureStructure motherStructure, String name) {
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		IFeature f=factory.createFeature(fm, name);
		IFeatureStructure fStructure=f.getStructure();
		motherStructure.addChild(fStructure);
		System.out.println("Added "+f.getName()+"with motherFeature "+motherStructure.getFeature().getName());
		return fm;
	}
	public static FeatureModel adaptFeatureModelWeather_add(FeatureModel fm) {
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
						int counter=0;
						
						for(IFeatureStructure k:j.getChildren()) {
							//System.out.println(counter);
							counter++;
							String[] parts = k.getFeature().getName().split("\\|");
							//System.out.println(k.getFeature().getName());
                            String attributeName = parts[0];
                            //System.out.println("p1: "+parts[1]);
                            
                            String attributeValue = parts.length > 1 ? parts[1] : "";
                           
                            String[] parts2 = attributeName.split("\\s", 2);
							if (k.getFeature().getName().contains("precipitationType")) {
								String attributeName_old = parts[1];
								fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|rain");
								fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|snow");
								fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|hail");
								k.setAlternative();
							}
							else if (k.getFeature().getName().contains("intensity")) {
								String attributeName_old = parts[1];
								fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|"+attributeName_old);
								fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|1");
								fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|0");
								k.setAlternative();
								
							}
							k.getFeature().setName(attributeName+"|alt");
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
				//System.out.println("positionfs: "+positionfs.toString());
				for(IFeatureStructure j:positionfs) {
					
					//System.out.println("j.getFeature().getName() "+j.getFeature().getName());
					//System.out.println("j.getFeature().getName()2 "+j.getChildren().get(0).getFeature().getName());
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
				//System.out.println("f: "+f.getName());
				fs=f.getStructure();
				positionfs=fs.getChildren();
				String fullName;
				//System.out.println("positionfs: "+positionfs.toString());
				String[] parts = null;
				for(IFeatureStructure j:positionfs) {
					//System.out.println("j.getFeature().getName()1 "+j.getFeature().getName());
				}
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
				//System.out.println("f: "+f.getName());
				fs=f.getStructure();
				positionfs=fs.getChildren();
				String fullName;
				//System.out.println("positionfs: "+positionfs.toString());
				String[] parts = null;
				for(IFeatureStructure j:positionfs) {
					//System.out.println("j.getFeature().getName()1 "+j.getFeature().getName());
				}
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
	public static FeatureModel adaptFeatureModelVehicles_add_old(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		IFeature f;
		IFeatureStructure fs;
		IFeatureStructure fs_clone;
		Object[] test=features.toArray();
		
		for (int i=0; i<fm.getFeatures().size(); i++) {
			if (test[i]!=null && test[i].toString().contains(" Vehicle")) {
				f=fm.getFeature(test[i].toString());
				//System.out.println("f: "+f.getName());
				fs=f.getStructure();
				fs_clone=fs.clone(f);
				fs_clone.setParent(fs.getParent());
				fs_clone.getParent().setAlternative();
			
			}
		}
				
		return fm;
	}
	public static FeatureModel adaptFeatureModelVehicles_add(FeatureModel fm) {
        Collection<IFeature> features = fm.getFeatures();
        List<IFeatureStructure> entityFs;
        
        IFeature f;
        IFeatureStructure fs;
        Object[] test = features.toArray();
        //System.out.println("vehicleCounter: "+vehicleCounter(fm));
        for (int i = 0; i < fm.getFeatures().size(); i++) {
            if (test[i] != null && test[i].toString().contains(" Entities")) {
                f = fm.getFeature(test[i].toString());
                fs = f.getStructure();
                entityFs = new ArrayList<>(fs.getChildren());

                // Liste für neue ScenarioObjects erstellen
                List<IFeatureStructure> newScenarioObjects = new ArrayList<>();

                for (IFeatureStructure j : entityFs) {
                    if (j.getFeature().getName().contains(" ScenarioObject")) {
                        // Neue Featurestruktur für das zusätzliche ScenarioObject erstellen
                        IFeatureStructure newScenarioObject = cloneFeatureStructure(fm,j);
                        
                        newScenarioObjects.add(newScenarioObject);
                    }
                }

                // Neue ScenarioObjects zur Entities-Struktur hinzufügen
                for (IFeatureStructure newScenarioObject : newScenarioObjects) {
                    System.out.println("Adding new ScenarioObject: " + newScenarioObject.getFeature().getName());
                    fs.addChild(newScenarioObject);
                }

                // Markieren Sie die Entities-Struktur als alternativ
                fs.setAlternative();
            }
        }
        //System.out.println("vehicleCounter2: "+vehicleCounter(fm));

        return fm;
    }

    private static IFeatureStructure cloneFeatureStructure(FeatureModel fm, IFeatureStructure original) {
    	final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
        IFeature originalFeature = original.getFeature();
        String originalName = originalFeature.getName();
        String newName = generateNewFeatureName(originalName);
        IFeature clonedFeature=factory.createFeature(fm, newName);
        clonedFeature.setName(newName);
		IFeatureStructure clonedStructure = clonedFeature.getStructure();
		clonedStructure.getFeature().setName(newName);
		if (original.isMandatory()) {
			clonedStructure.setMandatory(true);
		}
        for (IFeatureStructure child : original.getChildren()) {
            IFeatureStructure clonedChild = cloneFeatureStructure(fm,child);
            clonedStructure.addChild(clonedChild);
        }

        return clonedStructure;
    }

    private static String generateNewFeatureName(String originalName) {
        String[] parts = originalName.split(" ");
        if (parts.length > 1) {
            return parts[0] + "_add " + parts[1];
        } else {
            return originalName + "_add";
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

                // Liste für neue ScenarioObjects erstellen
                List<IFeatureStructure> newScenarioObjects = new ArrayList<>();

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
						int counter=0;
						for(IFeatureStructure k:j.getChildren()) {
							if (k.getFeature().getName().contains("aggression")) {
								aggressionflag = true;
							}
						}
						if (aggressionflag) {
							for(IFeatureStructure k:j.getChildren()) {
								//System.out.println(counter);
								counter++;
								String[] parts = k.getFeature().getName().split("\\|");
								//System.out.println(k.getFeature().getName());
	                            String attributeName = parts[0];
	                            //System.out.println("p1: "+parts[1]);
	                            
	                            String attributeValue = parts.length > 1 ? parts[1] : "";
	                           
	                            String[] parts2 = attributeName.split("\\s", 2);
								if (k.getFeature().getName().contains("value")) {
									String attributeName_old = parts[1];
									fm=addFeature(fm, k, parts2[0]+"_1 "+parts2[1]+"|0.5");
									fm=addFeature(fm, k, parts2[0]+"_2 "+parts2[1]+"|0.1");
									fm=addFeature(fm, k, parts2[0]+"_3 "+parts2[1]+"|0.9");
									k.setAlternative();
								}
								//k.getFeature().setName(attributeName+"|alt");
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
		IFeatureModelStructure fms=fm.getStructure();
		Collection<IFeature> features = fm.getFeatures();
		Collection<IFeature> featurepreorder = fms.getFeaturesPreorder();
		List<IFeatureStructure> weatherfs;
		List<IFeatureStructure> precipitationfs;
		ConfigFormatManager cfm;
		
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
	public static FeatureModel adaptFeatureModelAcceleration(FeatureModel fm) {
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
                            
                            String attributeValue = parts.length > 1 ? parts[1] : "";
							
							if (k.getFeature().getName().contains("Acceleration")) {
								int attributeInt;
								attributeInt=Integer.parseInt(attributeValue)*2;
								
								attributeValue=""+attributeInt;
								
							}
							k.getFeature().setName(attributeName+"|"+attributeValue);
						}
						
					}
				}
			}
		}
		
		return fm;
	}
	static FeatureModel adaptFeatureModelSwitchTrueFalse(FeatureModel fm) {
		Collection<IFeature> features = fm.getFeatures();
		List<IFeatureStructure> weatherfs;		
		IFeature f;
		IFeatureStructure fs;
		Object[] test=features.toArray();
		for (int i=0; i<fm.getFeatures().size()-1; i++) {
			if (test[i]!=null &&test[i].toString().contains("|false")) {
				f=fm.getFeature(test[i].toString());
				fs=f.getStructure();
				
				String[] parts = f.getName().split("\\|");
                String attributeName = parts[0];
                String attributeValue = parts.length > 1 ? parts[1] : "";
                
                if (attributeValue.equals("false")) {
                	attributeValue="true";
                }
                
                f.setName(attributeName+"|"+attributeValue);
			}
			else {
				if (test[i]!=null &&test[i].toString().contains("|true")) {
					f=fm.getFeature(test[i].toString());
					fs=f.getStructure();
					
					String[] parts = f.getName().split("\\|");
	                String attributeName = parts[0];
	                String attributeValue = parts.length > 1 ? parts[1] : "";
	                
	                if (attributeValue.equals("true")) {
	                	attributeValue="false";
	                }
	                
	                f.setName(attributeName+"|"+attributeValue);
				}
			}
		}
		return fm;
	}
	static FeatureModel removeFeature (FeatureModel fm, IFeatureStructure fs, IFeatureStructure motherFs) {
		FeatureModel fmtemp=fm;
		//System.out.println("FM: "+fm);
		//System.out.println("Name: "+fs.getFeature().getName());
		Collection<IFeature> test = fmtemp.getStructure().getFeaturesPreorder();
		for (IFeature tes:test) {
			if (tes.getName().equals(fs.getFeature().getName())) {
				IFeatureStructure fs2=tes.getStructure();
				IFeatureStructure motherFs2 = fs2.getParent();
				motherFs2.removeChild(fs2);
				//System.out.println("remmo: "+fs2.getFeature().getName());
			}
		}
		return fmtemp;
	}
	static ArrayList<FeatureModel> adaptFeatureModelCreateList(FeatureModel fm, String file) {
		Collection<IFeature> features = fm.getFeatures();
		ArrayList<IFeature> featuresArayList = new ArrayList<>(features);
		List<IFeatureStructure> featuresArayList2 = null;
		List<String> stringList = new ArrayList<>();
		List<String> stringList_add = new ArrayList<>();
		List<String> stringList2 = new ArrayList<>();
		List<List<String>> allGroups = new ArrayList<>();
		List<IFeatureStructure> fsList = new ArrayList<IFeatureStructure>();
		ArrayList<String> changesList = new ArrayList<>();

		FeatureModel fmtemp=fm;
		ArrayList<FeatureModel> featureList = new ArrayList<FeatureModel>();

		for (int i=0; i<featuresArayList.size(); i++) {
			//System.out.println("yyy_yyy: "+featuresArayList.get(i).getName());
			if (featuresArayList.get(i).getName().contains("_add ")) {
				stringList_add.add(featuresArayList.get(i).getName());
				//System.out.println("xxx_xxx: "+featuresArayList.get(i).getName());
			}
			if (featuresArayList.get(i).getStructure().isAlternative()) {
				featuresArayList2=featuresArayList.get(i).getStructure().getChildren();
				for (int a=0; a<featuresArayList2.size();a++) {
					fsList.add(featuresArayList2.get(a));
					//System.out.println("jj_jj: "+featuresArayList2.get(a));
					//if (!featuresArayList2.get(a).getFeature().toString().contains("_add ")) {
						stringList.add(featuresArayList2.get(a).getFeature().toString());
					//}
				}
			}
		}
		//System.out.println("featuresArayList.size(): "+featuresArayList.size());
		for (int i=0; i<featuresArayList.size(); i++) {
			
			for (int j=0; j<stringList.size();j++) {
				stringList2.add(stringList.get(j).contains("_") ? stringList.get(j).substring(0, stringList.get(j).indexOf('_')) : stringList.get(j));
			}
			
			ArrayList<String>stringList2remdup = removeDuplicates(stringList2); //List of alternative Groups 
			for (int j=0; j<stringList2remdup.size(); j++) {
				//System.out.println("featuresArayList.get(i).getName(): "+featuresArayList.get(i).getName());
				if (featuresArayList.get(i).getName().contains(stringList2remdup.get(j))) {
					ArrayList<String> temp=new ArrayList<String>();
					for (int k=0; k<stringList.size();k++) {
						if (stringList.get(k).contains(stringList2remdup.get(j))) {
							//System.out.println(stringList.get(k));
							temp.add(stringList.get(k));
						}
					}
					//System.out.println("temp: "+temp);
					allGroups.add(temp);
				}
			}
		}
		List<List<String>> combinations = combineGroups(allGroups);
		int counter=0;
		IFeatureStructure delFSMother=null;
		ArrayList<IFeatureStructure> deleteFeatureStructureList = new ArrayList<IFeatureStructure>();
		String temp="";
		//System.out.println("combinations.size: "+combinations.size());
		for (List<String> combination : combinations) {
			//System.out.println("combination: "+combination);
			for (int j=0; j<combination.size(); j++) {
				for (int i=0; i<featuresArayList.size(); i++) {
					//System.out.println("1: "+featuresArayList.get(i).getName().substring(0, featuresArayList.get(i).getName().indexOf(' ')));
					//System.out.println(combination.get(j));
					//System.out.println("2: "+combination.get(j).substring(0, combination.get(j).indexOf('_')));
					String comboPart = combination.get(j).contains("_") ? 
						    combination.get(j).substring(0, combination.get(j).indexOf('_')) : 
						    combination.get(j);
					
					if (featuresArayList.get(i).getName().substring(0, featuresArayList.get(i).getName().indexOf(' ')).contains(combination.get(j).substring(0, combination.get(j).indexOf('_')))) {
						fmtemp=fm.clone();
						//System.out.println("fmtemp=fm.clone();");
						for (IFeatureStructure delf : fsList) {
							//System.out.println("delf");
							//System.out.println(delf.getFeature().getName()+" comb: "+ combination.get(j)+" delfFeature"+delf.getFeature().getName());
							if (!delf.getFeature().getName().contains("|")&&combination.get(j).equals(delf.getFeature().getName())) {
								//System.out.println("combi: "+combination.get(j)+" zu "+delf.getFeature().getName());
								for (IFeatureStructure delf2 : fsList) {
									if (!combination.get(j).equals(delf2.getFeature().getName())&&combination.get(j).replaceFirst("_\\d+", "").equals(delf2.getFeature().getName().replaceFirst("_\\d+", ""))){
										deleteFeatureStructureList.add(delf2);
										//System.out.println("todelete: "+delf2.getFeature().getName());
									}
									else if (combination.get(j).equals(delf2.getFeature().getName())) {
										String featureChildString = "";
										for (IFeatureStructure featureChild : delf2.getChildren()) {
											featureChildString = featureChildString+featureChild.getFeature().getName()+", ";
										}
										String featureName = delf2.getFeature().getName();
										featureName = (featureName.indexOf(' ') != -1) ? featureName.substring(featureName.indexOf(' ') + 1) : "";
										String motherFeatureName = delf2.getParent().getParent().getFeature().getName();
										motherFeatureName = (motherFeatureName.indexOf(' ') != -1) ? motherFeatureName.substring(motherFeatureName.indexOf(' ') + 1) : "";
										String motherMotherFeatureName = delf2.getParent().getParent().getParent().getFeature().getName();
										motherMotherFeatureName = (motherMotherFeatureName.indexOf(' ') != -1) ? motherMotherFeatureName.substring(motherMotherFeatureName.indexOf(' ') + 1) : "";
										String motherMotherMotherFeatureName = delf2.getParent().getParent().getParent().getParent().getFeature().getName();
										motherMotherMotherFeatureName = (motherMotherMotherFeatureName.indexOf(' ') != -1) ? motherMotherMotherFeatureName.substring(motherMotherMotherFeatureName.indexOf(' ') + 1) : "";
										changesList.add(""+counter+": "+featureName+","+featureChildString+", ("+motherFeatureName+", "+motherMotherFeatureName+", "+motherMotherMotherFeatureName+")");
									}
								}
							}
							
							else if (delf.getFeature().getName().contains("|")&&combination.get(j).equals(delf.getFeature().getName())) {
								//System.out.println("combi2: "+combination.get(j)+" zu "+delf.getFeature().getName());
								for (IFeatureStructure delf2 : fsList) {
									if (combination.get(j).equals(delf2.getFeature().getName())){
										delf2.getParent().getFeature().setName(delf2.getFeature().getName());
										String featureName = delf2.getFeature().getName();
										featureName = (featureName.indexOf(' ') != -1) ? featureName.substring(featureName.indexOf(' ') + 1) : "";
										String motherFeatureName = delf2.getParent().getParent().getFeature().getName();
										motherFeatureName = (motherFeatureName.indexOf(' ') != -1) ? motherFeatureName.substring(motherFeatureName.indexOf(' ') + 1) : "";
										String motherMotherFeatureName = delf2.getParent().getParent().getParent().getFeature().getName();
										motherMotherFeatureName = (motherMotherFeatureName.indexOf(' ') != -1) ? motherMotherFeatureName.substring(motherMotherFeatureName.indexOf(' ') + 1) : "";
										String motherMotherMotherFeatureName = delf2.getParent().getParent().getParent().getParent().getFeature().getName();
										motherMotherMotherFeatureName = (motherMotherMotherFeatureName.indexOf(' ') != -1) ? motherMotherMotherFeatureName.substring(motherMotherMotherFeatureName.indexOf(' ') + 1) : "";
										changesList.add(""+counter+": "+featureName+", ("+motherFeatureName+", "+motherMotherFeatureName+", "+motherMotherMotherFeatureName+")");
										
									}
								}
								
							}
							//System.out.println("delFSMother: "+delFSMother.getFeature().getName());
							//deleteFeatureStructureList.add(delFSMother);
						}
						if((j+1)%combination.size() == 0) {
							//System.out.println("remove: ");
							for(IFeatureStructure deleteFeatureStructure:deleteFeatureStructureList) {
								//System.out.println(deleteFeatureStructure.getFeature().getName());
								//System.out.println("removed: "+deleteFeatureStructure.getFeature().getName()+" Mother: "+deleteFeatureStructure.getParent().getFeature().getName());
								fmtemp=removeFeature(fmtemp, deleteFeatureStructure, deleteFeatureStructure.getParent());
							}
							deleteFeatureStructureList=new ArrayList<IFeatureStructure>();
							featureList.add(fmtemp);
							String combinat="";
							for (String comb:combination) {
								combinat=combinat+comb;
							}
							//System.out.println("test: "+file+counter+combination.get(j).split(" ")[1]);
							//System.out.println(temp.split(" ")[1].replace("|", "_")+"_Adapted.xosc");
							saveXMLScenario(file+"_"+counter+"_Adapted.xosc",fmtemp);
							counter++;
							//System.out.println("Counter: "+counter);
						}
						temp=combination.get(j).replaceFirst("_\\d+", "");
					}
		        }
			}
		}
		for (String cl:changesList) {
			//System.out.println(cl);
		}
		return featureList;
	}
	private static List<List<String>> combineGroups(List<List<String>> allGroups) {
        List<List<String>> combinations = new ArrayList<>();
        combineGroupsRecursively(allGroups, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void combineGroupsRecursively(List<List<String>> allGroups, int currentIndex, List<String> currentCombination, List<List<String>> combinations) {
        //System.out.println("allGroups.size(): "+allGroups.size());
    	if (currentIndex == allGroups.size()) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        List<String> currentGroup = allGroups.get(currentIndex);

        for (String value : currentGroup) {
            currentCombination.add(value);
            combineGroupsRecursively(allGroups, currentIndex + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

	private static ArrayList<String> removeDuplicates(List<String> stringList2) {
        HashSet<String> set = new HashSet<>(stringList2);
        return new ArrayList<>(set);
    }
	public static void saveXMLScenarios(String filePath, FeatureModel fm) {
		StringBuilder xmlBuilder = new StringBuilder();
		List<IFeatureStructure> list=new ArrayList();
        list.add(fm.getStructure().getRoot());
		xmlBuilder.append("<?xml version=\"1.0\"?>\n");
		xmlBuilder = OpenScenarioGenerator.generateOpenScenarioFromFmRecursively(fm, list, xmlBuilder, 0);
		String xmlString=xmlBuilder.toString();
		
		try {
            Files.write(Paths.get(filePath), xmlString.getBytes());
            //System.out.println("Scenario XML erfolgreich gespeichert in: "+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void saveXMLScenario(String filePath, FeatureModel fm) {
		StringBuilder xmlBuilder = new StringBuilder();
		List<IFeatureStructure> list=new ArrayList();
        list.add(fm.getStructure().getRoot());
		xmlBuilder.append("<?xml version=\"1.0\"?>\n");
		//for (int i = 0; i<fm.getFeatureOrderList().size(); i++) {
			//System.out.println("fm.getFeatureOrderList().get(i): "+fm.getFeatureOrderList().get(i));
		//}
		xmlBuilder = OpenScenarioGenerator.generateOpenScenarioFromFmRecursively(fm, list, xmlBuilder, 0);
		String xmlString=xmlBuilder.toString();
		//System.out.println("fullXML: "+xmlString);
		try {
            Files.write(Paths.get(filePath), xmlString.getBytes());
            //System.out.println("Scenario XML erfolgreich gespeichert in: "+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void saveXMLFeatureModel(String filePath, FeatureModel fm) {
		XmlFeatureModelFormat fmXmlFormat = new XmlFeatureModelFormat();  
		String xmlString=fmXmlFormat.write(fm);    
		try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(xmlString);
            writer.close();
            //System.out.println("Feature Model XML erfolgreich gespeichert in: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
