package at.jku.win.masterarbeit.szenarienerstellungstool;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import at.jku.win.masterarbeit.util.MotherfeatureFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.io.*;
import java.util.Scanner;
public class Choose_Variations extends JFrame {

	private JTextField inputOpenScenarioField;
    private JTextField inputDocumentationField;
    private JTextField outputFolderField;
    private JCheckBox weatherCheckbox;
    private JCheckBox startPositionCheckbox;
    private JCheckBox waypointsCheckbox;
    private JCheckBox vehiclesCheckbox;
    private JCheckBox agressionCheckbox;
    private JCheckBox speedCheckbox;
    private JCheckBox switchTrueFalseCheckbox;

    public Choose_Variations() {
        super("OpenScenario Feature Variation");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        JPanel fileSelectionPanel = createFileSelectionPanel();
        add(fileSelectionPanel, BorderLayout.NORTH);

        JPanel featureVariationPanel = createFeatureVariationPanel();
        add(featureVariationPanel, BorderLayout.CENTER);

        JPanel outputFolderPanel = createOutputFolderPanel();
        add(outputFolderPanel, BorderLayout.SOUTH);

        JButton startButton = new JButton("Start Variationen");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startVariations();
            }
        });
        add(startButton, BorderLayout.SOUTH);

        // JFrame-Einstellungen
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel openScenarioLabel = new JLabel("Ausgangs OpenSCENARIO (.xosc):");
        inputOpenScenarioField = new JTextField();
        JButton openScenarioButton = new JButton("Auswählen");
        openScenarioButton.addActionListener(e -> selectFile(inputOpenScenarioField, "xosc"));

        JLabel documentationLabel = new JLabel("OpenSCENARIO Dokumentation (.xml):");
        inputDocumentationField = new JTextField();
        JButton documentationButton = new JButton("Auswählen");
        documentationButton.addActionListener(e -> selectFile(inputDocumentationField, "xml"));

        JLabel outputFolderLabel = new JLabel("Zielordner:");
        outputFolderField = new JTextField();
        JButton outputFolderButton = new JButton("Auswählen");
        outputFolderButton.addActionListener(e -> selectFolder(outputFolderField));

        panel.add(openScenarioLabel);
        panel.add(inputOpenScenarioField);
        panel.add(openScenarioButton);

        panel.add(documentationLabel);
        panel.add(inputDocumentationField);
        panel.add(documentationButton);

        panel.add(outputFolderLabel);
        panel.add(outputFolderField);
        panel.add(outputFolderButton);

        return panel;
    }

    private JPanel createFeatureVariationPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel featureVariationsLabel = new JLabel("Feature Variationen");
        panel.add(featureVariationsLabel);

        weatherCheckbox = new JCheckBox("Wetter");
        startPositionCheckbox = new JCheckBox("Ausgangsposition");
        waypointsCheckbox = new JCheckBox("Waypoints");
        vehiclesCheckbox = new JCheckBox("Vehicles");
        agressionCheckbox = new JCheckBox("Agression");
        speedCheckbox = new JCheckBox("Beschleunigung");
        switchTrueFalseCheckbox = new JCheckBox("vertausche Ja/Nein");
        panel.add(weatherCheckbox);
        panel.add(startPositionCheckbox);
        panel.add(waypointsCheckbox);
        panel.add(vehiclesCheckbox);
        panel.add(agressionCheckbox);
        panel.add(speedCheckbox);
        panel.add(switchTrueFalseCheckbox);

        return panel;
    }

    private JPanel createOutputFolderPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private void startVariations() {
        String openScenarioPath = inputOpenScenarioField.getText();
        String documentationPath = inputDocumentationField.getText();
        String outputFolderPath = outputFolderField.getText();
        deleteDirectoryContents(outputFolderPath+"/Adapted");
        boolean varyWeather = weatherCheckbox.isSelected();
        boolean varyStartPosition = startPositionCheckbox.isSelected();
        boolean varyWaypoints = waypointsCheckbox.isSelected();
        boolean varyVehicles = vehiclesCheckbox.isSelected();
        boolean varyAgression = agressionCheckbox.isSelected();
        boolean varySpeed = speedCheckbox.isSelected();
        boolean switchTrueFalse = switchTrueFalseCheckbox.isSelected();
        
        System.out.println("OpenScenario Pfad: " + openScenarioPath);
        System.out.println("Dokumentations Pfad: " + documentationPath);
        System.out.println("Zielordner: " + outputFolderPath);
        System.out.println("Feature Variationen:");
        System.out.println("Wetter: " + varyWeather);
        System.out.println("Ausgangspositionen: " + varyStartPosition);
        System.out.println("Andere Positionen: " + varyWaypoints);
        System.out.println("Andere Vehicles: " + varyVehicles);
        System.out.println("Andere Agression: " + varyAgression);
        System.out.println("Speed: " + varySpeed);
        Element rootElement=null;
        try {
            File inputFile = new File(openScenarioPath);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MotherfeatureFeature> openScenario_required =FeatureModelAdaptions.fillOpenScenario_required(documentationPath);
		FeatureModel fm=FeatureModelCreation.fillCreateFeatureModel(rootElement, "?xml version=\"1.0\"?",openScenario_required);
		List<IFeatureStructure> list=new ArrayList<IFeatureStructure>();
        list.add(fm.getStructure().getRoot());
		StringBuilder xmlBuilder = new StringBuilder();
		
		xmlBuilder.append("<?xml version=\"1.0\"?>\n");
		xmlBuilder = OpenScenarioGenerator.generateOpenScenarioFromFmRecursively(fm, list, xmlBuilder, 0);
		
		if (varyWeather) {
			fm=FeatureModelAdaptions.adaptFeatureModelWeather_add(fm);
		}
		if (varySpeed) {
			fm=FeatureModelAdaptions.adaptFeatureModelAcceleration_add(fm);
		}
		if (switchTrueFalse) {
			fm=FeatureModelAdaptions.adaptFeatureModelSwitchTrueFalse(fm);
		}
		if (varyStartPosition) {
			fm=FeatureModelAdaptions.adaptFeatureModelPosition_add(fm);
		}
		if (varyWaypoints) {
			fm=FeatureModelAdaptions.adaptFeatureModelWaypoints_add(fm);
		}
		if (varyVehicles) {
			fm=FeatureModelAdaptions.adaptFeatureModelVehicles_add(fm);
		}
		if (varyAgression) {
			fm=FeatureModelAdaptions.adaptFeatureModelAgression_add(fm);
		}
		FeatureModelAdaptions.saveXMLFeatureModel(outputFolderPath+"/"+extractFileName(openScenarioPath)+"_Adapted_FM.xml",fm);
		ArrayList<FeatureModel> featureModelList = FeatureModelAdaptions.adaptFeatureModelCreateList(fm, outputFolderPath+"/Adapted/"+extractFileName(openScenarioPath));
		
		
		
		

        // Hier wird der Zielordner geöffnet
        try {
            Desktop.getDesktop().open(new File(outputFolderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        SwingUtilities.invokeLater(() -> {
            FileSelectionWindow fileSelectionWindow = new FileSelectionWindow(outputFolderPath);
            fileSelectionWindow.setVisible(true);
        });
    }
    private static String extractFileName(String filePath) {
        File file = new File(filePath);
        String fileNameWithExtension = file.getName();
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');

        if (lastDotIndex != -1) {
            // Extrahiere den Dateinamen ohne Erweiterung
            return fileNameWithExtension.substring(0, lastDotIndex);
        } else {
            // Falls keine Erweiterung vorhanden ist
            return fileNameWithExtension;
        }
    }

    private void selectFile(JTextField textField, String extension) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "OpenSCENARIO Dateien (*." + extension + ")", extension);
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().endsWith("." + extension)) {
                textField.setText(selectedFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Datei mit der Erweiterung ." + extension);
            }
        }
    }

    private void selectFolder(JTextField textField) {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = folderChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFolder = folderChooser.getSelectedFile().getAbsolutePath();
            textField.setText(selectedFolder);
        }
    }
    private static void deleteDirectoryContents(String directoryPath) {
        // Den Ordner als File-Objekt initialisieren
        File directory = new File(directoryPath);
        
        // Alle Dateien und Verzeichnisse im Verzeichnis auflisten
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Wenn es ein Unterverzeichnis ist, rekursiv löschen
                    deleteDirectoryContents(file.getAbsolutePath());
                }
                // Datei oder leeres Verzeichnis löschen
                boolean success = file.delete();
                if (!success) {
                    System.out.println("Konnte Datei oder Verzeichnis nicht löschen: " + file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Choose_Variations());
    }
    public static void openLogFile(File logFile) {
        try {
            // Befehl zum Öffnen der Datei (für Windows, Linux/Mac benötigt möglicherweise andere Befehle)
            String command = "notepad.exe " + logFile.getAbsolutePath();
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileSelectionWindow extends JFrame {

    private JTable filesTable;
    private DefaultTableModel tableModel;
    private JButton executeButton;

    public FileSelectionWindow(String outputFolderPath) {
        super("Dateien auswählen");

        // Layout-Manager einstellen (hier: BorderLayout)
        setLayout(new BorderLayout());

        // Initialisiere die Tabelle und den Ausführen-Knopf
        initializeTable(outputFolderPath);

        // JFrame-Einstellungen
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeTable(String outputFolderPath) {
    	
        String[] columnNames = {"Dateiname"};
        tableModel = new DefaultTableModel(columnNames, 0);
        filesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(filesTable);
        add(scrollPane, BorderLayout.CENTER);

        executeButton = new JButton("Ausgewählte Ausführen");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSelectedFiles(outputFolderPath);
            }
        });
        add(executeButton, BorderLayout.SOUTH);

        // Lese die Dateinamen aus dem Zielordner und fülle die Tabelle
        
        fillTableWithFileNames(outputFolderPath+"/Adapted");
    }

    private void fillTableWithFileNames(String folderPath) {
        tableModel.setRowCount(0);

        // Füge die Dateinamen aus dem Zielordner zur Tabelle hinzu (ohne Dateien, die mit "." beginnen)
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && !file.getName().startsWith(".")) {
                    tableModel.addRow(new Object[]{file.getName()});
                }
            }
        }
    }
    
    private void executeSelectedFiles(String outputFolderPath) {
        if (filesTable.getSelectedRowCount() > 0) {
            int[] selectedRows = filesTable.getSelectedRows();
            List<String> selectedFiles = new ArrayList<>();
            for (int row : selectedRows) {
                selectedFiles.add((String) tableModel.getValueAt(row, 0));
            }
            //FileNotFoundError: [Errno 2] No such file or directory: 'C:\\Users\\stefan\\Documents\\OpenSCENARIO_output/AdaptedPedestrianCrossingFront6precipitationType_rain_intensity_10_Adapted.xosc'

            List<String> command = new ArrayList<>();
            command.add("C:\\Users\\stefan\\PycharmProjects\\Python_BeamNG\\.venv\\Scripts\\python.exe");
            command.add("C:\\Users\\stefan\\PycharmProjects\\Python_BeamNG\\start_scenario_direct.py");
            command.add(outputFolderPath+"\\Adapted\\");
            command.addAll(selectedFiles);
            System.out.println("Selected Files" + selectedFiles);
            System.out.println("JF Selected File: "+selectedFiles.get(0));
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(outputFolderPath));

            try {
                Process process = processBuilder.start();

                // Lese die Ausgabe des Python-Skripts
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);  // Gib die Ausgabe in der Java-Konsole aus
                }

                // Lese die Fehlerausgabe des Python-Skripts (optional)
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);  // Gib die Fehlerausgabe in der Java-Konsole aus
                }

                // Warte auf das Ende des Python-Skripts
                int exitCode = process.waitFor();
                System.out.println("Python-Skript beendet mit Exit-Code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie mindestens eine Datei aus.");
        }
    }


    
}