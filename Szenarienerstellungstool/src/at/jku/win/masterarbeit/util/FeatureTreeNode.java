package at.jku.win.masterarbeit.util;

import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;

public class FeatureTreeNode<T> {
	final IFeatureModel fm;
	final DefaultFeatureModelFactory factory;
	final IFeatureModelStructure fmStructure;
    private Feature f;
    private IFeatureStructure fs;
    private List<FeatureTreeNode<T>> children;
    private String addit="";
    private int level;

    public FeatureTreeNode(IFeatureModel fm,DefaultFeatureModelFactory factory,String feature,String addit) {
        this.fm=fm;
        this.factory=factory;
        this.fmStructure=this.fm.getStructure();
    	this.f = this.factory.createFeature(this.fm, feature);
        this.fs = this.f.getStructure();
        this.children = new ArrayList<>();
        if (addit.equals("root")){
        	this.fmStructure.setRoot(fs);
        }
        else if (addit.equals("mandatory")) {
        	this.fs.setMandatory(true);
        }
        fm.addFeature(f);
    }

    public Feature getData() {
        return f;
    }

    public void setData(Feature data) {
        this.f = data;
    }

    public List<FeatureTreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<FeatureTreeNode<T>> children) {
        this.children = children;
    }

    public void addChild(FeatureTreeNode<T> child) {
        children.add(child);
    }

	public IFeatureStructure getFs() {
		return fs;
	}

	public void setFs(IFeatureStructure fs) {
		this.fs = fs;
	}
	public void fmToString() {
		//System.out.println("FM: ");
		System.out.println(fm.toString());
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}