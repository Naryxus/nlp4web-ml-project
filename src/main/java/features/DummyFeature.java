package features;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.jcas.JCas;
import org.dkpro.tc.api.exception.TextClassificationException;
import org.dkpro.tc.api.features.Feature;
import org.dkpro.tc.api.features.FeatureExtractor;
import org.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import org.dkpro.tc.api.type.TextClassificationTarget;

public class DummyFeature extends FeatureExtractorResource_ImplBase implements FeatureExtractor {
	
	/*
	 * This class creates feature, which is always true and can be used as a reference.
	 * The results can be found in the documentation.
	*/

	@Override
	public Set<Feature> extract(JCas view, TextClassificationTarget target) throws TextClassificationException {
		Set<Feature> out = new HashSet<>();
		out.add(new Feature("Dummy", true));
		//out.add(new Feature("Dummy2", true));
		return out;
	}

}
