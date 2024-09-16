package szenarienerstellungstool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import org.sat4j.specs.TimeoutException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;


public class test3 {

	public static void main(String[] args) {
		
        
        ArrayList<TwoStrings> ar = filltwostrings();
        IFeatureModel fm = fillArrayList(ar);
        System.out.println("fm2:");
        System.out.println(fm.toString());
        final Configuration conf = new Configuration(fm);
        //conf.setManual("Template", Selection.SELECTED);
        System.out.println("Can be valid: " + conf.canBeValid());
        System.out.println("Solutions: " + conf.number());
        List<List<String>> solutions = null;
		try {
			solutions = conf.getSolutions(Long.valueOf(conf.number()).intValue());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println("Free feature (SELECTED/UNSELECT choice left open): ");
        /*for (IFeature a : conf.getUndefinedSelectedFeatures()) {
        System.out.println(a);
        }

        System.out.println("Status of the features");
        for (SelectableFeature feature : conf.getFeatures()) {
        System.out.println(feature + " " + feature.getSelection());
        }
*/
        System.out.println("Possible solutions so far:");
        for (final List<String> solution : solutions) {
        	System.out.println(solution);
        }
        
        //
        /*
         * 
        final IFeatureModel fm = new FeatureModel("Birkemeyer Test Feature-Model");
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		TreeNode<String> demoStringTree=filldemodata();
		FeatureTreeNode<FeatureTreeNode> demoFeatureTree=fillfeaturedemodata();
        printTree(demoStringTree, 0);
        printFeatureTree(demoFeatureTree,0);
        demoFeatureTree.fmToString();
        System.out.println("iterative");
        printFeatureTreeIterative(demoFeatureTree);
        
        
        //
        
        Deque<FeatureTreeNode<FeatureTreeNode>> stack = new ArrayDeque<>();
	    stack.push(demoFeatureTree);
	    while (!stack.isEmpty()) {
	        FeatureTreeNode<FeatureTreeNode> node = stack.pop();
	        int level = node.getLevel();
	        node.getData();
	        
	        // Die Kinder werden in umgekehrter Reihenfolge hinzugefügt, um die Ausgabe in der korrekten Reihenfolge zu erhalten
	        for (int i = node.getChildren().size() - 1; i >= 0; i--) {
	            FeatureTreeNode<FeatureTreeNode> child = node.getChildren().get(i);
	            child.setLevel(level + 1); // Kindknoten erhält die Ebene des Elternknotens plus eins
	            stack.push(child);
	        }
	    }
	    */
	}
	public static void printTree(TreeNode<String> node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  "); // Zwei Leerzeichen pro Ebene
        }
        System.out.println(indent.toString() + node.getData());
        for (TreeNode<String> child : node.getChildren()) {
            printTree(child, level + 1);
        }
    }
	

	public static void printFeatureTree(FeatureTreeNode<FeatureTreeNode> demoFeatureTree, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  "); // Zwei Leerzeichen pro Ebene
        }
        System.out.println(indent.toString() + demoFeatureTree.getData());
        for (FeatureTreeNode<FeatureTreeNode> child : demoFeatureTree.getChildren()) {
        	printFeatureTree(child, level + 1);
        }
    }
	public static void printFeatureTreeIterative(FeatureTreeNode<FeatureTreeNode> demoFeatureTree) {
	    Deque<FeatureTreeNode<FeatureTreeNode>> stack = new ArrayDeque<>();
	    stack.push(demoFeatureTree);
	    while (!stack.isEmpty()) {
	        FeatureTreeNode<FeatureTreeNode> node = stack.pop();
	        int level = node.getLevel();
	        
	        StringBuilder indent = new StringBuilder();
	        if (node.getData().getName().equals("E4")) {
	        	System.out.println("!!!");
	        }
	        for (int i = 0; i < level; i++) {
	            indent.append("  "); // Zwei Leerzeichen pro Ebene
	        }
	        System.out.println(indent.toString() + node.getData());

	        // Die Kinder werden in umgekehrter Reihenfolge hinzugefügt, um die Ausgabe in der korrekten Reihenfolge zu erhalten
	        for (int i = node.getChildren().size() - 1; i >= 0; i--) {
	            FeatureTreeNode<FeatureTreeNode> child = node.getChildren().get(i);
	            child.setLevel(level + 1); // Kindknoten erhält die Ebene des Elternknotens plus eins
	            stack.push(child);
	        }
	    }
	}
	public static TreeNode<String> filldemodata() {
		// Baumstruktur erstellen und mit String-Werten befüllen
				TreeNode<String> root = new TreeNode<>("FM_AEB_Testing_Scenarios");

		        // Template
		        TreeNode<String> template = new TreeNode<>("Template");
		        TreeNode<String> simpleCrossing = new TreeNode<>("Simple_Crossing");
		        TreeNode<String> ncapAebCvn = new TreeNode<>("NCAP_AEB_CVN");
		        template.addChild(simpleCrossing);
		        template.addChild(ncapAebCvn);
		        root.addChild(template);

		        // E4
		        TreeNode<String> e4 = new TreeNode<>("E4");
		        TreeNode<String> ego = new TreeNode<>("Ego");
		        TreeNode<String> speed = new TreeNode<>("Speed");
		        TreeNode<String> speed50 = new TreeNode<>("Speed_50");
		        TreeNode<String> speed55 = new TreeNode<>("Speed_55");
		        TreeNode<String> speed60 = new TreeNode<>("Speed_60");
		        TreeNode<String> trailer5 = new TreeNode<>("Trailer|5");
		        
		        TreeNode<String> traffic = new TreeNode<>("Traffic");
		        TreeNode<String> crossingPath3 = new TreeNode<>("Crossing_Path|3");
		        TreeNode<String> crossingObj = new TreeNode<>("Crossing_Obj");
		        
		        root.addChild(e4);
		        e4.addChild(ego);
		        ego.addChild(speed);
		        speed.addChild(speed50);
		        speed.addChild(speed55);
		        speed.addChild(speed60);
		        ego.addChild(trailer5);
		        e4.addChild(traffic);
		        traffic.addChild(crossingPath3);
		        traffic.addChild(crossingObj);


		        

		        TreeNode<String> bicycle = new TreeNode<>("Bicycle");
		        TreeNode<String> cyclistMale01 = new TreeNode<>("Cyclist_Male_01");
		        TreeNode<String> cyclistMale01ChildCarrier01 = new TreeNode<>("Cyclist_Male_01_ChildCarrier_01");
		        bicycle.addChild(cyclistMale01);
		        bicycle.addChild(cyclistMale01ChildCarrier01);

		        TreeNode<String> animals = new TreeNode<>("Animals");
		        TreeNode<String> bear = new TreeNode<>("Bear");
		        TreeNode<String> cat = new TreeNode<>("Cat");
		        TreeNode<String> moose = new TreeNode<>("Moose");
		        animals.addChild(bear);
		        animals.addChild(cat);
		        animals.addChild(moose);

		        TreeNode<String> people = new TreeNode<>("People");
		        TreeNode<String> pedestrianMaleCasual01Ipg = new TreeNode<>("Pedestrian_Male_casual_01_IPG");
		        people.addChild(pedestrianMaleCasual01Ipg);

		        crossingObj.addChild(bicycle);
		        crossingObj.addChild(animals);
		        crossingObj.addChild(people);

		        // E5
		        TreeNode<String> e5 = new TreeNode<>("E5");
		        TreeNode<String> environment = new TreeNode<>("Environment");
		        TreeNode<String> fog4 = new TreeNode<>("Fog|4");
		        TreeNode<String> sunPosition9 = new TreeNode<>("Sun_position|9");
		        TreeNode<String> rain3 = new TreeNode<>("Rain|3");
		        TreeNode<String> airHumidity3 = new TreeNode<>("Air_Humidity|3");
		        TreeNode<String> temperature4 = new TreeNode<>("Temperature|4");
		        TreeNode<String> wind = new TreeNode<>("Wind");
		        TreeNode<String> windVelocity3 = new TreeNode<>("Wind_velocity|3");
		        TreeNode<String> windAngle4 = new TreeNode<>("Wind_angle|4");

		        root.addChild(e5);
		        e5.addChild(environment);
		        environment.addChild(fog4);
		        environment.addChild(sunPosition9);
		        environment.addChild(rain3);
		        environment.addChild(airHumidity3);
		        environment.addChild(temperature4);
		        environment.addChild(wind);
		        wind.addChild(windVelocity3);
		        wind.addChild(windAngle4);
		        return root;
	}
	public static FeatureTreeNode<FeatureTreeNode> fillfeaturedemodata() {
		// Baumstruktur erstellen und mit String-Werten befüllen
		final IFeatureModel fm = new FeatureModel("Birkemeyer Test Feature-Model");
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		FeatureTreeNode<FeatureTreeNode> root = new FeatureTreeNode<>(fm, factory, "FM_AEB_Testing_Scenarios","root");
		
        // Template
		FeatureTreeNode<FeatureTreeNode> template = new FeatureTreeNode<>(fm, factory, "Template","mandatory");
		FeatureTreeNode<FeatureTreeNode> simpleCrossing = new FeatureTreeNode<>(fm, factory, "Simple_Crossing","mandatory");
		FeatureTreeNode<FeatureTreeNode> ncapAebCvn = new FeatureTreeNode<>(fm, factory, "NCAP_AEB_CVN","mandatory");
        template.addChild(simpleCrossing);
        template.addChild(ncapAebCvn);
        root.addChild(template);

        // E4
        FeatureTreeNode<FeatureTreeNode> e4 = new FeatureTreeNode<>(fm, factory, "E4","");
        FeatureTreeNode<FeatureTreeNode> ego = new FeatureTreeNode<>(fm, factory, "Ego","");
        FeatureTreeNode<FeatureTreeNode> speed = new FeatureTreeNode<>(fm, factory, "Speed","");
        FeatureTreeNode<FeatureTreeNode> speed50 = new FeatureTreeNode<>(fm, factory, "Speed_50","");
        FeatureTreeNode<FeatureTreeNode> speed55 = new FeatureTreeNode<>(fm, factory, "Speed_55","");
        FeatureTreeNode<FeatureTreeNode> speed60 = new FeatureTreeNode<>(fm, factory, "Speed_60","");
        FeatureTreeNode<FeatureTreeNode> trailer5 = new FeatureTreeNode<>(fm, factory, "Trailer|5","");
        
        FeatureTreeNode<FeatureTreeNode> traffic = new FeatureTreeNode<>(fm, factory, "Traffic","");
        FeatureTreeNode<FeatureTreeNode> crossingPath3 = new FeatureTreeNode<>(fm, factory, "Crossing_Path|3","");
        FeatureTreeNode<FeatureTreeNode> crossingObj = new FeatureTreeNode<>(fm, factory, "Crossing_Obj","");
        
        root.addChild(e4);
        e4.addChild(ego);
        ego.addChild(speed);
        speed.addChild(speed50);
        speed.addChild(speed55);
        speed.addChild(speed60);
        ego.addChild(trailer5);
        e4.addChild(traffic);
        traffic.addChild(crossingPath3);
        traffic.addChild(crossingObj);


        

        FeatureTreeNode<FeatureTreeNode> bicycle = new FeatureTreeNode<>(fm, factory, "Bicycle","");
        FeatureTreeNode<FeatureTreeNode> cyclistMale01 = new FeatureTreeNode<>(fm, factory, "Cyclist_Male_01","");
        FeatureTreeNode<FeatureTreeNode> cyclistMale01ChildCarrier01 = new FeatureTreeNode<>(fm, factory, "Cyclist_Male_01_ChildCarrier_01","");
        bicycle.addChild(cyclistMale01);
        bicycle.addChild(cyclistMale01ChildCarrier01);

        FeatureTreeNode<FeatureTreeNode> animals = new FeatureTreeNode<>(fm, factory, "Animals","");
        FeatureTreeNode<FeatureTreeNode> bear = new FeatureTreeNode<>(fm, factory, "Bear","");
        FeatureTreeNode<FeatureTreeNode> cat = new FeatureTreeNode<>(fm, factory, "Cat","");
        FeatureTreeNode<FeatureTreeNode> moose = new FeatureTreeNode<>(fm, factory, "Moose","");
        animals.addChild(bear);
        animals.addChild(cat);
        animals.addChild(moose);

        FeatureTreeNode<FeatureTreeNode> people = new FeatureTreeNode<>(fm, factory, "People","");
        FeatureTreeNode<FeatureTreeNode> pedestrianMaleCasual01Ipg = new FeatureTreeNode<>(fm, factory, "Pedestrian_Male_casual_01_IPG","");
        people.addChild(pedestrianMaleCasual01Ipg);

        crossingObj.addChild(bicycle);
        crossingObj.addChild(animals);
        crossingObj.addChild(people);

        // E5
        FeatureTreeNode<FeatureTreeNode> e5 = new FeatureTreeNode<>(fm, factory, "E5","");
        FeatureTreeNode<FeatureTreeNode> environment = new FeatureTreeNode<>(fm, factory, "Environment","");
        FeatureTreeNode<FeatureTreeNode> fog4 = new FeatureTreeNode<>(fm, factory, "Fog|4","");
        FeatureTreeNode<FeatureTreeNode> sunPosition9 = new FeatureTreeNode<>(fm, factory, "Sun_position|9","");
        FeatureTreeNode<FeatureTreeNode> rain3 = new FeatureTreeNode<>(fm, factory, "Rain|3","");
        FeatureTreeNode<FeatureTreeNode> airHumidity3 = new FeatureTreeNode<>(fm, factory, "Air_Humidity|3","");
        FeatureTreeNode<FeatureTreeNode> temperature4 = new FeatureTreeNode<>(fm, factory, "Temperature|4","");
        FeatureTreeNode<FeatureTreeNode> wind = new FeatureTreeNode<>(fm, factory, "Wind","");
        FeatureTreeNode<FeatureTreeNode> windVelocity3 = new FeatureTreeNode<>(fm, factory, "Wind_velocity|3","");
        FeatureTreeNode<FeatureTreeNode> windAngle4 = new FeatureTreeNode<>(fm, factory, "Wind_angle|4","");

        root.addChild(e5);
        e5.addChild(environment);
        environment.addChild(fog4);
        environment.addChild(sunPosition9);
        environment.addChild(rain3);
        environment.addChild(airHumidity3);
        environment.addChild(temperature4);
        environment.addChild(wind);
        wind.addChild(windVelocity3);
        wind.addChild(windAngle4);
        root.fmToString();
        
        return root;
	}
	
	public static TreeNode<String> filldemodata2() {
		// Baumstruktur erstellen und mit String-Werten befüllen
				TreeNode<String> root = new TreeNode<>("FM_AEB_Testing_Scenarios");

		        // Template
		        TreeNode<String> template = new TreeNode<>("Template");
		        TreeNode<String> simpleCrossing = new TreeNode<>("Simple_Crossing");
		        TreeNode<String> ncapAebCvn = new TreeNode<>("NCAP_AEB_CVN");
		        TreeNode<String> e4 = new TreeNode<>("E4");
		        TreeNode<String> ego = new TreeNode<>("Ego");
		        TreeNode<String> speed = new TreeNode<>("Speed");
		        TreeNode<String> speed50 = new TreeNode<>("Speed_50");
		        TreeNode<String> speed55 = new TreeNode<>("Speed_55");
		        TreeNode<String> speed60 = new TreeNode<>("Speed_60");
		        TreeNode<String> trailer5 = new TreeNode<>("Trailer|5");
		        TreeNode<String> traffic = new TreeNode<>("Traffic");
		        TreeNode<String> crossingPath3 = new TreeNode<>("Crossing_Path|3");
		        TreeNode<String> crossingObj = new TreeNode<>("Crossing_Obj");
		        TreeNode<String> animals = new TreeNode<>("Animals");
		        TreeNode<String> bear = new TreeNode<>("Bear");
		        TreeNode<String> cat = new TreeNode<>("Cat");
		        TreeNode<String> moose = new TreeNode<>("Moose");
		        TreeNode<String> bicycle = new TreeNode<>("Bicycle");
		        TreeNode<String> cyclistMale01 = new TreeNode<>("Cyclist_Male_01");
		        TreeNode<String> cyclistMale01ChildCarrier01 = new TreeNode<>("Cyclist_Male_01_ChildCarrier_01");
		        TreeNode<String> people = new TreeNode<>("People");
		        TreeNode<String> pedestrianMaleCasual01Ipg = new TreeNode<>("Pedestrian_Male_casual_01_IPG");
		        TreeNode<String> e5 = new TreeNode<>("E5");
		        TreeNode<String> environment = new TreeNode<>("Environment");
		        TreeNode<String> fog4 = new TreeNode<>("Fog|4");
		        TreeNode<String> sunPosition9 = new TreeNode<>("Sun_position|9");
		        TreeNode<String> rain3 = new TreeNode<>("Rain|3");
		        TreeNode<String> airHumidity3 = new TreeNode<>("Air_Humidity|3");
		        TreeNode<String> temperature4 = new TreeNode<>("Temperature|4");
		        TreeNode<String> wind = new TreeNode<>("Wind");
		        TreeNode<String> windVelocity3 = new TreeNode<>("Wind_velocity|3");
		        TreeNode<String> windAngle4 = new TreeNode<>("Wind_angle|4");

		        template.addChild(simpleCrossing);
		        template.addChild(ncapAebCvn);
		        root.addChild(template);
		        root.addChild(e4);
		        e4.addChild(ego);
		        ego.addChild(speed);
		        speed.addChild(speed50);
		        speed.addChild(speed55);
		        speed.addChild(speed60);
		        ego.addChild(trailer5);
		        e4.addChild(traffic);
		        traffic.addChild(crossingPath3);
		        traffic.addChild(crossingObj);
		        bicycle.addChild(cyclistMale01);
		        bicycle.addChild(cyclistMale01ChildCarrier01);
		        animals.addChild(bear);
		        animals.addChild(cat);
		        animals.addChild(moose);
		        people.addChild(pedestrianMaleCasual01Ipg);
		        crossingObj.addChild(bicycle);
		        crossingObj.addChild(animals);
		        crossingObj.addChild(people);
		        root.addChild(e5);
		        e5.addChild(environment);
		        environment.addChild(fog4);
		        environment.addChild(sunPosition9);
		        environment.addChild(rain3);
		        environment.addChild(airHumidity3);
		        environment.addChild(temperature4);
		        environment.addChild(wind);
		        wind.addChild(windVelocity3);
		        wind.addChild(windAngle4);
		        return root;
	}
	public static TreeNode<String> filldemodata3() {
		// Baumstruktur erstellen und mit String-Werten befüllen
				TreeNode<String> root = new TreeNode<>("FM_AEB_Testing_Scenarios");
		        TreeNode<String> template = new TreeNode<>("Template");
		        root.addChild(template);
		        TreeNode<String> simpleCrossing = new TreeNode<>("Simple_Crossing");
		        template.addChild(simpleCrossing);
		        TreeNode<String> ncapAebCvn = new TreeNode<>("NCAP_AEB_CVN");
		        template.addChild(ncapAebCvn);
		        TreeNode<String> e4 = new TreeNode<>("E4");
		        root.addChild(e4);
		        TreeNode<String> ego = new TreeNode<>("Ego");
		        e4.addChild(ego);
		        TreeNode<String> speed = new TreeNode<>("Speed");
		        ego.addChild(speed);
		        TreeNode<String> speed50 = new TreeNode<>("Speed_50");
		        speed.addChild(speed50);
		        TreeNode<String> speed55 = new TreeNode<>("Speed_55");
		        speed.addChild(speed55);
		        TreeNode<String> speed60 = new TreeNode<>("Speed_60");
		        speed.addChild(speed60);
		        TreeNode<String> trailer5 = new TreeNode<>("Trailer|5");
		        ego.addChild(trailer5);
		        TreeNode<String> traffic = new TreeNode<>("Traffic");
		        e4.addChild(traffic);
		        TreeNode<String> crossingPath3 = new TreeNode<>("Crossing_Path|3");
		        traffic.addChild(crossingPath3);
		        TreeNode<String> crossingObj = new TreeNode<>("Crossing_Obj");
		        traffic.addChild(crossingObj);
		        TreeNode<String> animals = new TreeNode<>("Animals");
		        crossingObj.addChild(animals);
		        TreeNode<String> bear = new TreeNode<>("Bear");
		        animals.addChild(bear);
		        TreeNode<String> cat = new TreeNode<>("Cat");
		        animals.addChild(cat);
		        TreeNode<String> moose = new TreeNode<>("Moose");
		        animals.addChild(moose);
		        TreeNode<String> bicycle = new TreeNode<>("Bicycle");
		        crossingObj.addChild(bicycle);
		        TreeNode<String> cyclistMale01 = new TreeNode<>("Cyclist_Male_01");
		        bicycle.addChild(cyclistMale01);
		        TreeNode<String> cyclistMale01ChildCarrier01 = new TreeNode<>("Cyclist_Male_01_ChildCarrier_01");
		        bicycle.addChild(cyclistMale01ChildCarrier01);
		        TreeNode<String> people = new TreeNode<>("People");
		        crossingObj.addChild(people);
		        TreeNode<String> pedestrianMaleCasual01Ipg = new TreeNode<>("Pedestrian_Male_casual_01_IPG");
		        people.addChild(pedestrianMaleCasual01Ipg);
		        TreeNode<String> e5 = new TreeNode<>("E5");
		        root.addChild(e5);
		        TreeNode<String> environment = new TreeNode<>("Environment");
		        e5.addChild(environment);
		        TreeNode<String> fog4 = new TreeNode<>("Fog|4");
		        environment.addChild(fog4);
		        TreeNode<String> sunPosition9 = new TreeNode<>("Sun_position|9");
		        environment.addChild(sunPosition9);
		        TreeNode<String> rain3 = new TreeNode<>("Rain|3");
		        environment.addChild(rain3);
		        TreeNode<String> airHumidity3 = new TreeNode<>("Air_Humidity|3");
		        environment.addChild(airHumidity3);
		        TreeNode<String> temperature4 = new TreeNode<>("Temperature|4");
		        environment.addChild(temperature4);
		        TreeNode<String> wind = new TreeNode<>("Wind");
		        environment.addChild(wind);
		        TreeNode<String> windVelocity3 = new TreeNode<>("Wind_velocity|3");
		        wind.addChild(windVelocity3);
		        TreeNode<String> windAngle4 = new TreeNode<>("Wind_angle|4");
		        wind.addChild(windAngle4);

		        return root;
	}
	public static ArrayList<TwoStrings> filltwostrings() {
		// Baumstruktur erstellen und mit String-Werten befüllen
		ArrayList<TwoStrings> ar = new ArrayList<>();
		ar.add(new TwoStrings(null,"FM_AEB_Testing_Scenarios",true));
		ar.add(new TwoStrings("FM_AEB_Testing_Scenarios","Template",true));
		ar.add(new TwoStrings("Template","Simple_Crossing",true));
		ar.add(new TwoStrings("Template","NCAP_AEB_CVN",true));
		ar.add(new TwoStrings("FM_AEB_Testing_Scenarios","E4",true));
		ar.add(new TwoStrings("E4","Ego",true));
		ar.add(new TwoStrings("Ego","Speed",true));
		ar.add(new TwoStrings("Speed","Speed_50",true));
		ar.add(new TwoStrings("Speed","Speed_55",true));
		ar.add(new TwoStrings("Speed","Speed_60",true));
		ar.add(new TwoStrings("E4","Traffic",true));
		ar.add(new TwoStrings("Ego","Trailer|5",true));
		ar.add(new TwoStrings("Traffic","Crossing_Obj",true));
		ar.add(new TwoStrings("Traffic","Crossing_Path",true));
		ar.add(new TwoStrings("Crossing_Obj","Animals",false));
		ar.add(new TwoStrings("Animals","Bear",true));
		ar.add(new TwoStrings("Animals","Cat",true));
		ar.add(new TwoStrings("Animals","Moose",true));
		ar.add(new TwoStrings("Crossing_Obj","Bicycle",false));
		ar.add(new TwoStrings("Bicycle","Cyclist_Male_01",true));
		ar.add(new TwoStrings("Bicycle","Cyclist_Male_01_ChildCarrier_01",true));
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

}
