package szenarienerstellungstool;

import java.util.ArrayList;
import java.util.List;

import org.sat4j.specs.TimeoutException;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
public class Test5 {

	public static void main(String[] args) {
		ArrayList<TwoStrings> ar = filltwostrings();
        IFeatureModel fm = fillArrayList(ar);
        System.out.println(fm.toString());
        printPossibleSolutions(fm);
    

	}
	public static ArrayList<TwoStrings> filltwostrings() {
		// Baumstruktur erstellen und mit String-Werten befüllen
		ArrayList<TwoStrings> ar = new ArrayList<>();
		ar.add(new TwoStrings(null,"FM_AEB_Testing_Scenarios",0,true));
		ar.add(new TwoStrings("FM_AEB_Testing_Scenarios","Template",0,true));
		ar.add(new TwoStrings("Template","Simple_Crossing",0,true));
		ar.add(new TwoStrings("Template","NCAP_AEB_CVN",0,true));
		ar.add(new TwoStrings("FM_AEB_Testing_Scenarios","E4",0,true));
		ar.add(new TwoStrings("E4","Ego",0,true));
		ar.add(new TwoStrings("Ego","Speed",0,true));
		ar.add(new TwoStrings("Speed","Speed_50",0,true));
		ar.add(new TwoStrings("Speed","Speed_55",0,true));
		ar.add(new TwoStrings("Speed","Speed_60",0,true));
		ar.add(new TwoStrings("E4","Traffic",0,true));
		ar.add(new TwoStrings("Ego","Trailer|5",0,true));
		ar.add(new TwoStrings("Traffic","Crossing_Obj",0,true));
		ar.add(new TwoStrings("Traffic","Crossing_Path",0,true));
		ar.add(new TwoStrings("Crossing_Obj","Animals",0,false));
		ar.add(new TwoStrings("Animals","Bear",0,true));
		ar.add(new TwoStrings("Animals","Cat",0,true));
		ar.add(new TwoStrings("Animals","Moose",0,true));
		ar.add(new TwoStrings("Crossing_Obj","Bicycle",0,false));
		ar.add(new TwoStrings("Bicycle","Cyclist_Male_01",0,true));
		ar.add(new TwoStrings("Bicycle","Cyclist_Male_01_ChildCarrier_01",0,true));
		return ar;
	}
	public static IFeatureModel fillArrayList(ArrayList<TwoStrings> ar) {
		final IFeatureModel fm = new FeatureModel("CarScenario");
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		final IFeatureModelStructure fmStructure = fm.getStructure();
		
		for (TwoStrings i : ar) {
			Feature f = factory.createFeature(fm, i.getFeature());
			fm.addFeature(f);
        	IFeatureStructure fStructure = f.getStructure();
            if (i.getMotherfeature() == null) {
            	fmStructure.setRoot(fStructure);
            }
            else {
            	fm.getFeature(i.getMotherfeature()).getStructure().addChild(fStructure);
            	fStructure.setMandatory(i.isMandatory());
            }
        }
		return fm;
	}
	public static void printPossibleSolutions(IFeatureModel fm) {
		final Configuration conf = new Configuration(fm);
        System.out.println("Can be valid: " + conf.canBeValid());
        System.out.println("Solutions: " + conf.number());
        List<List<String>> solutions = null;
		try {
			solutions = conf.getSolutions(Long.valueOf(conf.number()).intValue());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
        System.out.println("Possible solutions so far:");
        for (final List<String> solution : solutions) {
        	System.out.println(solution);
        }
	}

}
