package szenarienerstellungstool;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

public class Test4 {

	public static void main(String[] args) {
		final IFeatureModel fm = new FeatureModel("Birkemeyer Test Feature-Model");
		final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		final IFeatureModelStructure fmStructure = fm.getStructure();
		
		
		final Feature root = factory.createFeature(fm, "FM_AEB_Testing_Scenarios");
        // Template
		final Feature template = factory.createFeature(fm, "Template");
		final Feature simpleCrossing = factory.createFeature(fm, "Simple_Crossing");
		final Feature ncapAebCvn = factory.createFeature(fm, "NCAP_AEB_CVN");
        final IFeatureStructure rootStructure = root.getStructure();
		final IFeatureStructure templateStructure = template.getStructure();
		final IFeatureStructure simpleCrossingStructure = simpleCrossing.getStructure();
		final IFeatureStructure ncapAebCvnStructure = ncapAebCvn.getStructure();
		
		fmStructure.setRoot(rootStructure);
		fm.addFeature(root);
		fm.addFeature(template);
		fm.addFeature(simpleCrossing);
		fm.addFeature(ncapAebCvn);
		
		rootStructure.addChild(templateStructure);
		templateStructure.addChild(simpleCrossingStructure);
		templateStructure.addChild(ncapAebCvnStructure);

        // E4
        final Feature e4 = factory.createFeature(fm, "E4");
        final Feature ego = factory.createFeature(fm, "Ego");
        final Feature speed = factory.createFeature(fm, "Speed");
        final Feature speed50 = factory.createFeature(fm, "Speed_50");
        final Feature speed55 = factory.createFeature(fm, "Speed_55");
        final Feature speed60 = factory.createFeature(fm, "Speed_60");
        final Feature trailer5 = factory.createFeature(fm, "Trailer|5");
        
        final IFeatureStructure e4Structure = e4.getStructure();
        final IFeatureStructure egoStructure = ego.getStructure();
        final IFeatureStructure speedStructure = speed.getStructure();
        final IFeatureStructure speed50Structure = speed50.getStructure();
        final IFeatureStructure speed55Structure = speed55.getStructure();
        final IFeatureStructure speed60Structure = speed60.getStructure();
        final IFeatureStructure trailer5Structure = trailer5.getStructure();
        
        fm.addFeature(e4);
        fm.addFeature(ego);
        fm.addFeature(speed);
        fm.addFeature(speed50);
        fm.addFeature(speed55);
        fm.addFeature(speed60);
        fm.addFeature(trailer5);
        
        
        
        final Feature traffic = factory.createFeature(fm, "Traffic");
        final Feature crossingPath3 = factory.createFeature(fm, "Crossing_Path|3");
        final Feature crossingObj = factory.createFeature(fm, "Crossing_Obj");
        final IFeatureStructure trafficStructure = traffic.getStructure();
        final IFeatureStructure crossingPath3Structure = crossingPath3.getStructure();
        final IFeatureStructure crossingObjStructure = crossingObj.getStructure();
        
        fm.addFeature(traffic);
        fm.addFeature(crossingPath3);
        fm.addFeature(crossingObj);
        
        
        rootStructure.addChild(e4Structure);
        e4Structure.addChild(crossingObjStructure);
        rootStructure.addChild(e4Structure);
        e4Structure.addChild(egoStructure);
        egoStructure.addChild(speedStructure);
        speedStructure.addChild(speed50Structure);
        speedStructure.addChild(speed55Structure);
        speedStructure.addChild(speed60Structure);
        egoStructure.addChild(trailer5Structure);
        e4Structure.addChild(trafficStructure);
        trafficStructure.addChild(crossingPath3Structure);
        trafficStructure.addChild(crossingObjStructure);


        

        final Feature bicycle = factory.createFeature(fm, "Bicycle");
        final Feature cyclistMale01 = factory.createFeature(fm, "Cyclist_Male_01");
        final Feature cyclistMale01ChildCarrier01 = factory.createFeature(fm, "Cyclist_Male_01_ChildCarrier_01");
        final IFeatureStructure bicycleStructure = bicycle.getStructure();
        final IFeatureStructure cyclistMale01Structure = cyclistMale01.getStructure();
        final IFeatureStructure cyclistMale01ChildCarrier01Structure = cyclistMale01ChildCarrier01.getStructure();
        
        fm.addFeature(bicycle);
        fm.addFeature(cyclistMale01);
        fm.addFeature(cyclistMale01ChildCarrier01);
        
        bicycleStructure.addChild(cyclistMale01Structure);
        bicycleStructure.addChild(cyclistMale01ChildCarrier01Structure);

        final Feature animals = factory.createFeature(fm, "Animals");
        final Feature bear = factory.createFeature(fm, "Bear");
        final Feature cat = factory.createFeature(fm, "Cat");
        final Feature moose = factory.createFeature(fm, "Moose");
        final IFeatureStructure animalsStructure = animals.getStructure();
        final IFeatureStructure bearStructure = bear.getStructure();
        final IFeatureStructure catStructure = cat.getStructure();
        final IFeatureStructure mooseStructure = moose.getStructure();
        
        fm.addFeature(animals);
        fm.addFeature(bear);
        fm.addFeature(cat);
        fm.addFeature(moose);
        
        animalsStructure.addChild(bearStructure);
        animalsStructure.addChild(catStructure);
        animalsStructure.addChild(mooseStructure);

        final Feature people = factory.createFeature(fm, "People");
        final Feature pedestrianMaleCasual01Ipg = factory.createFeature(fm, "Pedestrian_Male_casual_01_IPG");
        final IFeatureStructure peopleStructure = people.getStructure();
        final IFeatureStructure pedestrianMaleCasual01IpgStructure = pedestrianMaleCasual01Ipg.getStructure();
        peopleStructure.addChild(pedestrianMaleCasual01IpgStructure);
        
        fm.addFeature(people);
        fm.addFeature(pedestrianMaleCasual01Ipg);

        crossingObjStructure.addChild(bicycleStructure);
        crossingObjStructure.addChild(animalsStructure);
        crossingObjStructure.addChild(peopleStructure);

        // E5
        final Feature e5 = factory.createFeature(fm, "E5");
        final Feature environment = factory.createFeature(fm, "Environment");
        final Feature fog4 = factory.createFeature(fm, "Fog|4");
        final Feature sunPosition9 = factory.createFeature(fm, "Sun_position|9");
        final Feature rain3 = factory.createFeature(fm, "Rain|3");
        final Feature airHumidity3 = factory.createFeature(fm, "Air_Humidity|3");
        final Feature temperature4 = factory.createFeature(fm, "Temperature|4");
        final Feature wind = factory.createFeature(fm, "Wind");
        final Feature windVelocity3 = factory.createFeature(fm, "Wind_velocity|3");
        final Feature windAngle4 = factory.createFeature(fm, "Wind_angle|4");
        final IFeatureStructure e5Structure = e5.getStructure();
        final IFeatureStructure environmentStructure = environment.getStructure();
        final IFeatureStructure fog4Structure = fog4.getStructure();
        final IFeatureStructure sunPosition9Structure = sunPosition9.getStructure();
        final IFeatureStructure rain3Structure = rain3.getStructure();
        final IFeatureStructure airHumidity3Structure = airHumidity3.getStructure();
        final IFeatureStructure temperature4Structure = temperature4.getStructure();
        final IFeatureStructure windStructure = wind.getStructure();
        final IFeatureStructure windVelocity3Structure = windVelocity3.getStructure();
        final IFeatureStructure windAngle4Structure = windAngle4.getStructure();
        
        fm.addFeature(e5);
        fm.addFeature(environment);
        fm.addFeature(fog4);
        fm.addFeature(sunPosition9);
        fm.addFeature(rain3);
        fm.addFeature(airHumidity3);
        fm.addFeature(temperature4);
        fm.addFeature(wind);
        fm.addFeature(windVelocity3);
        fm.addFeature(windAngle4);

        
        rootStructure.addChild(e5Structure);
        e5Structure.addChild(environmentStructure);
        environmentStructure.addChild(fog4Structure);
        environmentStructure.addChild(sunPosition9Structure);
        environmentStructure.addChild(rain3Structure);
        environmentStructure.addChild(airHumidity3Structure);
        environmentStructure.addChild(temperature4Structure);
        environmentStructure.addChild(windStructure);
        
        templateStructure.setMandatory(true);
        simpleCrossingStructure.setMandatory(true);
        ncapAebCvnStructure.setMandatory(true);
        windStructure.addChild(windVelocity3Structure);
        windStructure.addChild(windAngle4Structure);
        System.out.println("FM: ");
        System.out.println(fm.toString());

	}

}
