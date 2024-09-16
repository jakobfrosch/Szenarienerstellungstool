package szenarienerstellungstool;
import java.util.List;

import org.sat4j.specs.TimeoutException;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;

public class Test2 {
  public static void main(final String[] args){
  //IFeatureModel fm = FMFactoryManager.getEmptyFeatureModel();
  final IFeatureModel fm = new FeatureModel("CarScenario");
  final DefaultFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
  final Feature fexp = factory.createFeature(fm, "Map");
  final Feature fas = factory.createFeature(fm, "Car");
  final Feature fadd = factory.createFeature(fm, "Pedestrian");

  final Feature fecl = factory.createFeature(fm, "Weather");
  final Feature fa1 = factory.createFeature(fm, "A1");
  final Feature fa2 = factory.createFeature(fm, "Test");
  
  final IFeatureStructure fexpStructure = fexp.getStructure();
  final IFeatureModelStructure fmStructure = fm.getStructure();
  final IFeatureStructure fasStructure = fas.getStructure();
  final IFeatureStructure feclStructure = fecl.getStructure();
  final IFeatureStructure faddStructure = fadd.getStructure();
  final IFeatureStructure fa1Structure = fa1.getStructure();
  final IFeatureStructure fa2Structure = fa2.getStructure();

  fmStructure.setRoot(fexpStructure);
  fm.addFeature(fexp);
  fm.addFeature(fas);
  fm.addFeature(fadd);
  fm.addFeature(fecl);
  fm.addFeature(fa1);
  fm.addFeature(fa2);

  fexpStructure.addChild(fasStructure);

  fexpStructure.addChild(faddStructure);

  fexpStructure.addChild(feclStructure);
  feclStructure.setAlternative();

  feclStructure.addChild(fa1Structure);
  feclStructure.addChild(fa2Structure);
  
  fasStructure.setMandatory(true);
  System.out.println("FM: ");
  System.out.println(fm.toString());
  final Configuration conf = new Configuration(fm);
  conf.setManual("A1", Selection.SELECTED);
  System.out.println("Can be valid: " + conf.canBeValid());
  System.out.println("Solutions: " + conf.number());
  List<List<String>> solutions = null;
try {
	solutions = conf.getSolutions(Long.valueOf(conf.number()).intValue());
} catch (TimeoutException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  System.out.println("Free feature (SELECTED/UNSELECT choice left open): ");
  for (IFeature a : conf.getUndefinedSelectedFeatures()) {
  System.out.println(a);
  }

  System.out.println("Status of the features");
  for (SelectableFeature feature : conf.getFeatures()) {
  System.out.println(feature + " " + feature.getSelection());
  }

  System.out.println("Possible solutions so far:");
  for (final List<String> solution : solutions) {
  System.out.println(solution);
  }
  }
  
  

  /*
   * Can be valid: true
   * Solutions: 2
   * Still free variables:
   * Add
   * Status of the features
   * Expression SELECTED
   * AS SELECTED
   * Add UNDEFINED
   * Ecl SELECTED
   * A1 SELECTED
   * A2 UNSELECTED
   * Possible solutions
   * [Expression, AS, Ecl, A1]
   * [Expression, AS, Ecl, A1, Add]
   */

  
}