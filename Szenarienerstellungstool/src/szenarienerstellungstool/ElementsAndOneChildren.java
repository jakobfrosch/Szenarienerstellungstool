package szenarienerstellungstool;


public class ElementsAndOneChildren {
    private String motherfeature;
    private String feature;
    public ElementsAndOneChildren(String motherfeature,String feature) {
        this.setFeature(feature);
        this.setMotherfeature(motherfeature);
    }

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getMotherfeature() {
		return motherfeature;
	}

	public void setMotherfeature(String motherfeature) {
		this.motherfeature = motherfeature;
	}
    
}