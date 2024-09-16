package szenarienerstellungstool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.FeatureStructure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.FileWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

public class XMLGenerator {
	public static Stack stack=new Stack();
	public static ArrayList<StringBuilder> stringBuilderList = new ArrayList<>();
    public static void main(String[] args) {
        // Hier ersetzen Sie "yourList" durch Ihre tatsächliche ArrayList mit TwoStrings-Objekten
        List<TwoStrings> yourList = new ArrayList<>();
        // ... füllen Sie die Liste hier ...

        String generatedXML = generateXML(yourList);
        //System.out.println(generatedXML);
    }

    public static String generateXML(List<TwoStrings> list) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\"?>\n");
        xmlBuilder.append("<OpenSCENARIO>\n");

        Map<String, String> attributeMap = new HashMap<>();
        String currentMotherFeature = null;
        int indentationLevel = 1;

        for (TwoStrings item : list) {
            String motherFeature = item.getMotherfeature();
            String feature = item.getFeature();
            String[] parts = feature.split("\\|");

            if (feature.contains("|")) {
                for (int i = 0; i < parts.length - 1; i += 2) {
                    attributeMap.put(parts[i], parts[i + 1]);
                }
            } else {
                if (currentMotherFeature != null && !currentMotherFeature.equals(feature)) {
                    closeElement(xmlBuilder, currentMotherFeature, attributeMap, indentationLevel);
                    indentationLevel--;
                    attributeMap.clear();
                }
                currentMotherFeature = feature;
                openElement(xmlBuilder, currentMotherFeature, indentationLevel);
                indentationLevel++;
            }
        }

        closeElement(xmlBuilder, currentMotherFeature, attributeMap, indentationLevel - 1);
        xmlBuilder.append("</OpenSCENARIO>");
        return xmlBuilder.toString();
    }
    
    public static String generateXML2(List<TwoStrings> list) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\"?>\n");
        xmlBuilder.append("<OpenSCENARIO>\n");

        Map<String, String> attributeMap = new HashMap<>();
        String currentMotherFeature = null;
        int indentationLevel = 1;

        for (TwoStrings item : list) {
            String motherFeature = item.getMotherfeature();
            String feature = item.getFeature();
            String[] parts = feature.split("\\|");

            if (feature.contains("|")) {
                for (int i = 0; i < parts.length - 1; i += 2) {
                    attributeMap.put(parts[i], parts[i + 1]);
                }
            } else {
                if (currentMotherFeature != null && !currentMotherFeature.equals(feature)) {
                    closeElement(xmlBuilder, currentMotherFeature, attributeMap, item.getLevel());
                    
                    attributeMap.clear();
                }
                currentMotherFeature = feature;
                openElement(xmlBuilder, currentMotherFeature, item.getLevel());
            }
        }

        closeElement(xmlBuilder, currentMotherFeature, attributeMap, indentationLevel);
        xmlBuilder.append("</OpenSCENARIO>");
        return xmlBuilder.toString();
    }
    public static StringBuilder generateXMLrec(IFeatureModel fm,List<IFeatureStructure> list,StringBuilder xml,int counter) {
    	IFeatureModelStructure fms=fm.getStructure();
    	for (int i=0; i<list.size();i++) {
    		//System.out.println("list.size: "+list.size());
    		//System.out.println(i+" "+list.get(i).getFeature().getName());
    		IFeatureStructure fs=list.get(i).getFeature().getStructure();
    		
    		for (int j=0; j<=counter; j++) {
    			xml.append(" ");
    		}
        	xml.append("<"+fs.getFeature().getName()+">\n");
        	//System.out.println("done");
        	counter++;
        	List<IFeatureStructure> newlist=fs.getChildren();
        	List<IFeatureStructure> nnewlist=new ArrayList();
        	for (IFeatureStructure y:newlist) {
        		//System.out.println("J: "+y.getFeature().getName());
        		nnewlist.add(y);
        	}
        	xml=generateXMLrec(fm,nnewlist,xml,counter);
        	for (int j=0; j<=counter; j++) {
    			xml.append(" ");
    		}
        	xml.append("</"+fs.getFeature().getName()+">\n");
    	}
    	
    	
		return xml;
    	
    }
    public static StringBuilder generateXMLrecRemovePrefix(IFeatureModel fm,List<IFeatureStructure> list,StringBuilder xml,int counter) {
    	IFeatureModelStructure fms=fm.getStructure();
    	String one=" ";
    	String two=" ";
    	boolean hasChildren=false;
    	int pcount=0;
    	Map<String, String> attributeMap = new HashMap<>();
    	for (int i=0; i<list.size();i++) {
    		IFeatureStructure fs=list.get(i).getFeature().getStructure();
    		IFeature f=fs.getFeature();
    		for (int j=0; j<counter; j++) {
    			xml.append(" ");
    		}
    		if (!f.getName().contains("|")) {
	    		if (f.getStructure().hasChildren()) {
		    		if (f.getStructure().getFirstChild().getFeature().getName().contains("|")) {
		    			xml.append("<"+removePrefix(fs.getFeature().getName())+" ");
		    			List<IFeatureStructure> childFeatures = f.getStructure().getChildren();
		    			System.out.println(childFeatures);
		    			System.out.println(childFeatures.size());
		    			for (int j=0; j<childFeatures.size();j++) {
		    				if (childFeatures.get(j).getFeature().getName().contains("|")) {
		    					pcount++;
			    				String[] parts = childFeatures.get(j).getFeature().getName().split("\\|");
			    				one=parts[0];
			    				if (parts.length>1) {
			    					two=parts[1];
			    				}
				                xml.append(removePrefix(one)).append("=\"").append(removePrefix(two)).append("\" ");
				                one =" ";
				                two = " ";
		    				}
		    				else {
		    					hasChildren=true;
		    				}
		    			}
		    			xml.setLength(xml.length() - 1); // Remove the extra space at the end
		    			if (!hasChildren) {
		    				xml.append("/>\n");
		    			}
		    			else {
		    				xml.append(">\n");
		    			}
		    			List<IFeatureStructure> newlist=fs.getChildren();
			        	List<IFeatureStructure> nnewlist=new ArrayList();
			        	for (IFeatureStructure y:newlist) {
			        		nnewlist.add(y);
			        	}
			        	xml=generateXMLrecRemovePrefix(fm,nnewlist,xml,counter-pcount);
			        	if (hasChildren) {
			        		xml.append("</"+removePrefix(fs.getFeature().getName())+">\n");
			        	}
			        	pcount=0;
			        	hasChildren=false;
		    		}
		    		else {
			        	xml.append("<"+removePrefix(fs.getFeature().getName())+">\n");
			        	List<IFeatureStructure> newlist=fs.getChildren();
			        	List<IFeatureStructure> nnewlist=new ArrayList();
			        	for (IFeatureStructure y:newlist) {
			        		nnewlist.add(y);
			        	}
			        	xml=generateXMLrecRemovePrefix(fm,nnewlist,xml,counter+1);
			        	for (int j=0; j<counter; j++) {
			    			xml.append(" ");
			    		}
			        	xml.append("</"+removePrefix(fs.getFeature().getName())+">\n");
		    		}
		    	} else {
		    		for (int j=0; j<counter; j++) {
		    			xml.append(" ");
		    		}
		    		xml.append("<"+removePrefix(fs.getFeature().getName())+"/>\n");
		        	List<IFeatureStructure> newlist=fs.getChildren();
		        	List<IFeatureStructure> nnewlist=new ArrayList();
		        	for (IFeatureStructure y:newlist) {
		        		nnewlist.add(y);
		        	}
		        	xml=generateXMLrecRemovePrefix(fm,nnewlist,xml,counter+1);	    		
		    	}
	    	}
    	}
		return xml;
    }
    public static StringBuilder generateXMLRecursively(IFeatureModel fm, List<IFeatureStructure> list, StringBuilder xml, int counter) {
        IFeatureModelStructure fms = fm.getStructure();
        //System.out.println("rec: "+fm.toString());
        for (IFeatureStructure fs : list) {
            IFeature f = fs.getFeature();
            String featureName = removePrefix(f.getName());
            
            //System.out.println("featureName: "+f.getName());

            if (!f.getName().contains("|")) {
            	appendIndent(xml, counter);
                if (f.getStructure().hasChildren()) {
                    xml.append("<").append(featureName);
                    List<IFeatureStructure> childFeatures = f.getStructure().getChildren();
                    int pcount = 0;

                    for (IFeatureStructure childFs : childFeatures) {
                        IFeature childFeature = childFs.getFeature();
                        String childFeatureName = childFeature.getName();

                        if (childFeatureName.contains("|")) {
                            pcount++;
                            String[] parts = childFeatureName.split("\\|");
                            String attributeName = removePrefix(parts[0]);
                            String attributeValue = removePrefix(parts.length > 1 ? parts[1] : "");
                            xml.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
                            
                        }
                    }
        

                    if (childFeatures.stream().anyMatch(childFs -> !childFs.getFeature().getName().contains("|"))) {
                    	//System.out.println();
                        xml.append(">\n");
                        List<IFeatureStructure> newlist = new ArrayList<>(fs.getChildren());
                        generateXMLRecursively(fm, newlist, xml, counter+1); // Hier den Counter ändern
                        appendIndent(xml, counter); // Fügt die richtige Einrückung vor dem schließenden Tag hinzu
                        xml.append("</").append(featureName).append(">\n");
                    } else {
                        xml.append("/>\n");
                    }
                } else {
                    xml.append("<").append(featureName).append("/>\n");
                    generateXMLRecursively(fm, fs.getChildren(), xml, counter + 1);
                }
            }
        }

        return xml;
    }
    
    public static ArrayList<StringBuilder> generateXMLsRecursively(IFeatureModel fm, List<IFeatureStructure> list, StringBuilder xml, int counter) {
        IFeatureModelStructure fms = fm.getStructure();
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder = XMLGenerator.generateXMLRecursively(fm, list, xml, 0);
        

        return stringBuilderList;
    }



    public static void appendIndent(StringBuilder xml, int count) {
        for (int i = 0; i < count; i++) {
            xml.append("  ");
        }
    }

    public static String generateXML3(List<TwoStrings> list) {
        StringBuilder xmlBuilder = new StringBuilder();
        //xmlBuilder.append("<?xml version=\"1.0\"?>\n");
        //xmlBuilder.append("<OpenSCENARIO>\n");

        Map<String, String> attributeMap = new HashMap<>();
        String currentMotherFeature = null;

        for (int i=0; i<list.size()-1;i++) {
            String motherFeature = list.get(i).getMotherfeature();
            String feature = list.get(i).getFeature();
            String[] parts = feature.split("\\|");

            if (feature.contains("|")) {
            	
                for (int j = 0; j < parts.length - 1; j += 2) {
                    attributeMap.put(parts[j], parts[j + 1]);
                }
                
            } else {
                if (currentMotherFeature != null) {
                    closeElement(xmlBuilder, currentMotherFeature, attributeMap,list.get(i).getLevel());
                    //System.out.println(list.get(i+1).getLevel()+", "+list.get(i).getLevel());
                    if (list.get(i).getLevel()<list.get(i+1).getLevel()) {
                		for (int k=0;k<list.get(i+1).getLevel()-list.get(i).getLevel();k++) {
                			//System.out.println(list.get(i-1).getLevel()-list.get(i).getLevel());
                			indent(xmlBuilder, list.get(i).getLevel());
                			xmlBuilder.append("</"+stack.pop()+">");
                			xmlBuilder.append("\n");
                		}
                		
                	}
                }
                currentMotherFeature = feature;
                attributeMap.clear();
                //openElement(xmlBuilder, currentMotherFeature,list.get(i).getLevel());
                indent(xmlBuilder, list.get(i).getLevel());
                xmlBuilder.append("<").append(currentMotherFeature);
                //System.out.println("JF: "+currentMotherFeature);
                if (!list.get(i+1).getFeature().contains("|")) {
                	stack.push(currentMotherFeature);
                }
                
                
               
            }
        }
        if (!attributeMap.isEmpty()) {
        	//stack.deletes(element);
            xmlBuilder.append(" ");
            for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
                xmlBuilder.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
            }
            xmlBuilder.setLength(xmlBuilder.length() - 1); // Remove the extra space at the end
            xmlBuilder.append("/>\n");
        }
        else if (attributeMap != null) {
            xmlBuilder.append(">\n");
        }
        //closeElement(xmlBuilder, currentMotherFeature, attributeMap,0);
        //xmlBuilder.append("</OpenSCENARIO>");
        return xmlBuilder.toString();
    }

    public static void openElement(StringBuilder xmlBuilder, String element, int indentationLevel) {
        indent(xmlBuilder, indentationLevel);
        xmlBuilder.append("<").append(element);
        //System.out.println("JF: "+element);
        stack.push(element);
    }

    public static void closeElement(StringBuilder xmlBuilder, String element, Map<String, String> attributes, int indentationLevel) {
        if (!attributes.isEmpty()) {
        	//stack.deletes(element);
            xmlBuilder.append(" ");
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                xmlBuilder.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
            }
            xmlBuilder.setLength(xmlBuilder.length() - 1); // Remove the extra space at the end
        }
        if (element != null) {
            xmlBuilder.append("/>\n");
        } else {
            //xmlBuilder.append("</OpenSCENARIO>\n");
        }
    }

    public static void indent(StringBuilder xmlBuilder, int indentationLevel) {
        for (int i = 0; i < indentationLevel; i++) {
            xmlBuilder.append("  ");
        }
    }
    public static String removePrefix(String string) {
            
        int index = string.indexOf(" ");
        if (index != -1) {
            string = string.substring(index + 1);
        }
        return string;
    }
    
    public static void saveXmlToFile(String xmlContent, String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create a DOM element from the XML content
            Element root = doc.createElement("root");
            doc.appendChild(root);
            root.appendChild(doc.createTextNode(xmlContent));

            // Write the DOM content to a file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(filePath));
            transformer.transform(source, result);

            System.out.println("XML content saved to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String escapeXml(String input) {
        return input.replace("&amp;","&")
                    .replace("&lt;","<")
                    .replace("&gt;",">")
                    .replace("&quot;","\"")
                    .replace("&apos;","'");
    }







}
