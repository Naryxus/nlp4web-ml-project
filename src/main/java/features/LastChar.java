package features;

import java.util.Set;

import org.apache.uima.jcas.JCas;
import org.dkpro.tc.api.exception.TextClassificationException;
import org.dkpro.tc.api.features.Feature;
import org.dkpro.tc.api.features.FeatureExtractor;
import org.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import org.dkpro.tc.api.type.TextClassificationTarget;

public class LastChar extends FeatureExtractorResource_ImplBase implements FeatureExtractor {

	@Override
	public Set<Feature> extract(JCas view, TextClassificationTarget target) throws TextClassificationException {
		String token = target.getCoveredText();
		// last two chars to differentiate between NERs
		boolean[] r = new boolean[12];
		// I-PER -> "er"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("er"))
			r[0] = true;
		// I-ORG -> "rs", "al"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("rs"))
			r[1] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("al"))
			r[2] = true;
		// I-MISC -> "sh", "ic"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("sh"))
			r[3] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("ic"))
			r[4] = true;
		// I-LOC -> "S.", "ia", "nd"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("S."))
			r[5] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("ia"))
			r[6] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("nd"))
			r[7] = true;
		// B-ORG -> "PD", "DP", "DS"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("PD"))
			r[8] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("DP"))
			r[9] = true;
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("DS"))
			r[10] = true;
		// B-LOC -> "WA"
		if (token.length()>=2 && token.substring(token.length()-2, token.length()-1).equals("WA"))
			r[11] = true;
		return new Feature("LastChar", r).asSet();
	}

}
