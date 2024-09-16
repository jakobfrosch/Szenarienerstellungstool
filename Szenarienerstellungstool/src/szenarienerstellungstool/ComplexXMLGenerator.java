package szenarienerstellungstool;

import java.io.File;
import java.util.ArrayList;

public class ComplexXMLGenerator {
    public static void main(String[] args) {
        ArrayList<TwoStrings2> ar = new ArrayList<>();
        ar.add(new TwoStrings2("<employees>", null));
        ar.add(new TwoStrings2("<employee>", "<employees>"));
        ar.add(new TwoStrings2("<id>1</id>", "<employee>"));
        ar.add(new TwoStrings2("<name>John Doe</name>", "<employee>"));
        ar.add(new TwoStrings2("</employee>", "<employees>"));
        // Add more elements as needed

        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            generateXML(xmlBuilder, ar, 0);

            // Write the content into an XML file
            String xmlContent = xmlBuilder.toString();
            File file = new File("output.xml");
            java.nio.file.Files.write(file.toPath(), xmlContent.getBytes());

            System.out.println("XML File generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateXML(StringBuilder xmlBuilder, ArrayList<TwoStrings2> ar, int index) {
        if (index >= ar.size()) {
            return;
        }

        TwoStrings2 currentTag = ar.get(index);
        String currentTagContent = currentTag.getTag();

        // Indentation based on the hierarchy level
        int indentation = index * 2;

        // Append indentation
        for (int i = 0; i < indentation; i++) {
            xmlBuilder.append(" ");
        }

        // Append the current tag
        xmlBuilder.append(currentTagContent).append("\n");

        // Continue processing the remaining tags
        generateXML(xmlBuilder, ar, index + 1);

        // Process the child tags recursively if this is not a closing tag
        if (!currentTagContent.startsWith("</")) {
            int nextIndex = index + 1;
            while (nextIndex < ar.size()) {
                TwoStrings2 nextTag = ar.get(nextIndex);
                // Check if the next tag is a child of the current tag
                if (currentTagContent.equals(nextTag.getParent())) {
                    generateXML(xmlBuilder, ar, nextIndex);
                    nextIndex++; // Move to the next tag after the processed children
                } else {
                    break; // The next tag is not a child of the current tag
                }
            }

            // Append the closing tag
            for (int i = 0; i < indentation; i++) {
                xmlBuilder.append(" ");
            }
            xmlBuilder.append("</").append(currentTagContent.substring(1)).append(">\n");
        }
    }
}

class TwoStrings2 {
    private String tag;
    private String parent;

    public TwoStrings2(String tag, String parent) {
        this.tag = tag;
        this.parent = parent;
    }

    public String getTag() {
        return tag;
    }

    public String getParent() {
        return parent;
    }
}
