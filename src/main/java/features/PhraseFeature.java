package features;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.dkpro.tc.api.exception.TextClassificationException;
import org.dkpro.tc.api.features.Feature;
import org.dkpro.tc.api.features.FeatureExtractor;
import org.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import org.dkpro.tc.api.type.TextClassificationTarget;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import types.PhraseAnnotation;

public class PhraseFeature extends FeatureExtractorResource_ImplBase implements FeatureExtractor {
	
	/*
	 * This class is the feature extractor for the phrase feature of a word,
	 * which is given via the training data. We found some correlation between
	 * the phrase-tag and the NER-IOB-Tag. The documentation yields more information
	 * on how and why we choose these Tags to check for.
	*/

	@Override
	public Set<Feature> extract(JCas view, TextClassificationTarget target) throws TextClassificationException {
		
		PhraseAnnotation tag = JCasUtil.selectCovered(PhraseAnnotation.class, target).get(0);
		Set<Feature> out = new HashSet<>();
		Feature np = new Feature("I-NP", false);
		if (tag.getPhraseTag().equals("I-NP"))
			np.setValue(true);
		out.add(np);
		Feature intj = new Feature("I-INTJ", false);
		if (tag.getPhraseTag().equals("I-INTJ"))
			intj.setValue(true);
		out.add(intj);
		return out;
	}

}
