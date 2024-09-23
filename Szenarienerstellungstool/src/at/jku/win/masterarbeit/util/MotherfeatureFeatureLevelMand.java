package at.jku.win.masterarbeit.util;

import java.util.ArrayList;
import java.util.List;

public class MotherfeatureFeatureLevelMand {
    private String feature;
    private String motherfeature;
    private int level;
    private boolean mandatory;
    public MotherfeatureFeatureLevelMand(String motherfeature,String feature,int level,boolean mandatory) {
        this.setFeature(feature);
        this.setMotherfeature(motherfeature);
        this.setLevel(level);
        this.setMandatory(mandatory);
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

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

    
}