package szenarienerstellungstool;

import java.util.ArrayList;
import java.util.List;

public class ElementsAndChildren {
    private String feature;
    private ArrayList<String> children;
    public ElementsAndChildren(ArrayList<String> motherfeature,String feature) {
        this.setFeature(feature);
        this.setChildren(motherfeature);
    }

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public ArrayList<String> getMotherfeature() {
		return children;
	}

	public void setChildren(ArrayList<String> motherfeature) {
		this.children = motherfeature;
	}
    
}