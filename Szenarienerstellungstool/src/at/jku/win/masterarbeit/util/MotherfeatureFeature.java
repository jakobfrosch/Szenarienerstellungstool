package at.jku.win.masterarbeit.util;


public class MotherfeatureFeature {
    private String motherfeature;
    private String feature;
    public MotherfeatureFeature(String motherfeature,String feature) {
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