package features;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.dkpro.tc.api.exception.TextClassificationException;
import org.dkpro.tc.api.features.Feature;
import org.dkpro.tc.api.features.FeatureExtractor;
import org.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import org.dkpro.tc.api.type.TextClassificationTarget;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;

public class LastChar extends FeatureExtractorResource_ImplBase implements FeatureExtractor {

	@Override
	public Set<Feature> extract(JCas view, TextClassificationTarget target) throws TextClassificationException {
		String token = target.getCoveredText();
		Set<Feature> out = new HashSet<>();
		
		// I-PER -> "er"
		Feature erEnd = new Feature("EndWith_er", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("er"))
			erEnd.setValue(true);
		out.add(erEnd);
		
		// I-ORG -> "rs", "al"
		Feature rsEnd = new Feature("EndWith_rs", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("rs"))
			rsEnd.setValue(true);
		out.add(rsEnd);
		Feature alEnd = new Feature("EndWith_al", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("al"))
			alEnd.setValue(true);
		out.add(alEnd);
		
		// I-MISC -> "sh", "ic"
		Feature shEnd = new Feature("EndWith_sh", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("sh"))
			shEnd.setValue(true);
		out.add(shEnd);
		Feature icEnd = new Feature("EndWith_ic", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("ic"))
			icEnd.setValue(true);
		out.add(icEnd);
		
		// I-LOC -> "S.", "ia", "nd"
		Feature SEnd = new Feature("EndWith_S.", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("S."))
			SEnd.setValue(true);
		out.add(SEnd);
		Feature iaEnd = new Feature("EndWith_ia", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("ia"))
			iaEnd.setValue(true);
		out.add(iaEnd);
		Feature ndEnd = new Feature("EndWith_nd", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("nd"))
			ndEnd.setValue(true);
		out.add(ndEnd);
		
		// B-ORG -> "PD", "DP", "DS"
		Feature PDEnd = new Feature("EndWith_PD", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("PD"))
			PDEnd.setValue(true);
		out.add(PDEnd);
		Feature DPEnd = new Feature("EndWith_DP", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("DP"))
			DPEnd.setValue(true);
		out.add(DPEnd);
		Feature DSEnd = new Feature("EndWith_DS", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("DS"))
			DSEnd.setValue(true);
		out.add(DSEnd);
		
		// B-LOC -> "WA"
		Feature WAEnd = new Feature("EndWith_WA", false);
		if (token.length()>=2 && token.substring(token.length()-2).equals("WA"))
			WAEnd.setValue(true);
		out.add(WAEnd);
		
		return out;
	}

}
