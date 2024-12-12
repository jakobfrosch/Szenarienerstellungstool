package at.jku.win.masterarbeit.szenarienerstellungstool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import at.jku.win.masterarbeit.util.MotherfeatureFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Choose_Variations_terminal {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Choose_Variations_CLI <openScenarioFile> <documentationFile> <outputFolder> [options]");
            System.out.println("Options:");
            System.out.println("  --varyWeather         Vary weather conditions");
            System.out.println("  --varyStartPosition   Vary starting positions");
            System.out.println("  --varyWaypoints       Vary waypoints");
            System.out.println("  --varyVehicles        Vary vehicles");
            System.out.println("  --varyAgression       Vary aggression");
            System.out.println("  --varySpeed           Vary speed");
            System.out.println("  --switchTrueFalse     Switch true/false values");
            return;
        }

        String openScenarioPath = args[0];
        String documentationPath = args[1];
        String outputFolderPath = args[2];

        boolean varyWeather = false;
        boolean varyStartPosition = false;
        boolean varyWaypoints = false;
        boolean varyVehicles = false;
        boolean varyAgression = false;
        boolean varySpeed = false;
        boolean switchTrueFalse = false;

        for (int i = 3; i < args.length; i++) {
            switch (args[i]) {
                case "--varyWeather":
                    varyWeather = true;
                    break;
                case "--varyStartPosition":
                    varyStartPosition = true;
                    break;
                case "--varyWaypoints":
                    varyWaypoints = true;
                    break;
                case "--varyVehicles":
                    varyVehicles = true;
                    break;
                case "--varyAgression":
                    varyAgression = true;
                    break;
                case "--varySpeed":
                    varySpeed = true;
                    break;
                case "--switchTrueFalse":
                    switchTrueFalse = true;
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    return;
            }
        }

        // Starte die Variationen
        startVariations(openScenarioPath, documentationPath, outputFolderPath, varyWeather, varyStartPosition, varyWaypoints, varyVehicles, varyAgression, varySpeed, switchTrueFalse);
    }

    private static void startVariations(String openScenarioPath, String documentationPath, String outputFolderPath,
                                        boolean varyWeather, boolean varyStartPosition, boolean varyWaypoints, boolean varyVehicles,
                                        boolean varyAgression, boolean varySpeed, boolean switchTrueFalse) {
        // Loggen der Variationen
        System.out.println("Starting variations with the following settings:");
        System.out.println("OpenScenario File: " + openScenarioPath);
        System.out.println("Documentation File: " + documentationPath);
        System.out.println("Output Folder: " + outputFolderPath);
        System.out.println("Vary Weather: " + varyWeather);
        System.out.println("Vary Start Position: " + varyStartPosition);
        System.out.println("Vary Waypoints: " + varyWaypoints);
        System.out.println("Vary Vehicles: " + varyVehicles);
        System.out.println("Vary Aggression: " + varyAgression);
        System.out.println("Vary Speed: " + varySpeed);
        System.out.println("Switch True/False: " + switchTrueFalse);

        Element rootElement = null;
        try {
            File inputFile = new File(openScenarioPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<MotherfeatureFeature> openScenarioRequired = FeatureModelAdaptions.fillOpenScenario_required(documentationPath);
        FeatureModel fm = FeatureModelCreation.fillCreateFeatureModel(rootElement, "?xml version=\"1.0\"?", openScenarioRequired);
        List<IFeatureStructure> list = new ArrayList<>();
        list.add(fm.getStructure().getRoot());

        if (varyWeather) {
            fm = FeatureModelAdaptions.adaptFeatureModelWeather_add(fm);
        }
        if (varySpeed) {
            fm = FeatureModelAdaptions.adaptFeatureModelAcceleration_add(fm);
        }
        if (switchTrueFalse) {
            fm = FeatureModelAdaptions.adaptFeatureModelSwitchTrueFalse_add(fm);
        }
        if (varyStartPosition) {
            fm = FeatureModelAdaptions.adaptFeatureModelPosition_add(fm);
        }
        if (varyWaypoints) {
            fm = FeatureModelAdaptions.adaptFeatureModelWaypoints_add(fm);
        }
        if (varyVehicles) {
            fm = FeatureModelAdaptions.adaptFeatureModelVehicles_add(fm);
        }
        if (varyAgression) {
            fm = FeatureModelAdaptions.adaptFeatureModelAgression_add(fm);
        }

        fm = FeatureModelAdaptions.addConstraints(fm);

        FeatureModelAdaptions.saveXMLFeatureModel(outputFolderPath + "/" + extractFileName(openScenarioPath) + "_Adapted_FM.xml", fm);

        //ArrayList<FeatureModel> featureModelList = FeatureModelAdaptions.adaptFeatureModelCreateList(fm, outputFolderPath + "/Adapted/" + extractFileName(openScenarioPath));

        System.out.println("Variations successfully generated and saved to: " + outputFolderPath);
    }

    private static String extractFileName(String filePath) {
        File file = new File(filePath);
        String fileNameWithExtension = file.getName();
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');

        if (lastDotIndex != -1) {
            return fileNameWithExtension.substring(0, lastDotIndex);
        } else {
            return fileNameWithExtension;
        }
    }
}
