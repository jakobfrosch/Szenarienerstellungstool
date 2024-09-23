package at.jku.win.masterarbeit.szenarienerstellungstool;
import java.util.ArrayList;
import java.util.List;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;

public class OpenScenarioGenerator {
	public static ArrayList<StringBuilder> stringBuilderList = new ArrayList<>();

    public static StringBuilder generateOpenScenarioFromFmRecursively(IFeatureModel fm, List<IFeatureStructure> list, StringBuilder xml, int counter) {
        for (IFeatureStructure fs : list) {
            IFeature f = fs.getFeature();
            String featureName = removePrefix(f.getName());
            if (!f.getName().contains("|")) {
            	appendIndent(xml, counter);
                if (f.getStructure().hasChildren()) {
                    xml.append("<").append(featureName);
                    List<IFeatureStructure> childFeatures = f.getStructure().getChildren();
                    for (IFeatureStructure childFs : childFeatures) {
                        IFeature childFeature = childFs.getFeature();
                        String childFeatureName = childFeature.getName();

                        if (childFeatureName.contains("|")) {
                            String[] parts = childFeatureName.split("\\|");
                            String attributeName = removePrefix(parts[0]);
                            String attributeValue = removePrefix(parts.length > 1 ? parts[1] : "");
                            xml.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
                        }
                    }
                    if (childFeatures.stream().anyMatch(childFs -> !childFs.getFeature().getName().contains("|"))) {
                        xml.append(">\n");
                        List<IFeatureStructure> newlist = new ArrayList<>(fs.getChildren());
                        generateOpenScenarioFromFmRecursively(fm, newlist, xml, counter+1); // Hier den Counter ändern
                        appendIndent(xml, counter); // Fügt die richtige Einrückung vor dem schließenden Tag hinzu
                        xml.append("</").append(featureName).append(">\n");
                    } else {
                        xml.append("/>\n");
                    }
                } else {
                    xml.append("<").append(featureName).append("/>\n");
                    generateOpenScenarioFromFmRecursively(fm, fs.getChildren(), xml, counter + 1);
                }
            }
        }
        return xml;
    }
    public static void appendIndent(StringBuilder xml, int count) {
        for (int i = 0; i < count; i++) {
            xml.append("  ");
        }
    }
    public static String removePrefix(String string) {
        int index = string.indexOf(" ");
        if (index != -1) {
            string = string.substring(index + 1);
        }
        return string;
    }
}
