package szenarienerstellungstool;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureModelStructure;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;
public class test1 {

	public static void main(String[] args) {
		DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
		IFeatureModel featureModel = new FeatureModel("Test");
		IFeatureModelStructure fms= new FeatureModelStructure(featureModel);
		
		Feature root = factory.createFeature(featureModel, "root");
		Feature one = factory.createFeature(featureModel, "one");
		Feature two = factory.createFeature(featureModel, "two");
		
		
		
		IFeature rootFeature = new Feature(featureModel, "RootFeature");
		IFeature feature1 = new Feature(featureModel, "Feature1");
		IFeature feature2 = new Feature(featureModel, "Feature2");
		
		
		IFeatureStructure rootStructure = rootFeature.getStructure();
		IFeatureStructure feature1Structure = feature1.getStructure();
		IFeatureStructure feature2Structure = feature2.getStructure();
		
		
		IFeatureStructure rootS = root.getStructure();
		IFeatureStructure oneS = one.getStructure();
		IFeatureStructure twoS = two.getStructure();
		
		
		fms.setRoot(rootS);
		
		featureModel.addFeature(root);
		featureModel.addFeature(one);
		featureModel.addFeature(two);
		twoS.setAlternative();
		rootS.addChild(feature1Structure);
		rootS.addChild(feature2Structure);
		
		
		//String print=fms.toString();
		String print2=featureModel.toString();
		String print3=fms.getFeatureModel().toString();
		System.out.println(print3);
		

	}

}
