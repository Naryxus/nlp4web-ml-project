package features;

import java.text.NumberFormat;
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

public class PosFeature extends FeatureExtractorResource_ImplBase implements FeatureExtractor {

	@Override
	public Set<Feature> extract(JCas view, TextClassificationTarget target) throws TextClassificationException {

		NumberFormat perc = NumberFormat.getPercentInstance();
		perc.setMinimumFractionDigits(2);
		System.out.print("\r" + perc.format(target.getBegin() / 3281528.0));

		POS tag = JCasUtil.selectCovered(POS.class, target).get(0);//(POS) JCasUtil.selectCovering(view, POS.class, target.getBegin(), target.getEnd()).get(0);
		Set<Feature> out = new HashSet<>();
		
		POS tmp = target.get;
		
		// I-ORG, I-PER
		Feature posUH = new Feature("POS UH", false);
		if (tag.getPosValue().equals("UH"))
			posUH.setValue(true);
		out.add(posUH);
		
		Feature posNNSYM = new Feature("POS NNSYM", false);
		if (tag.getPosValue().equals("NN|SYM"))
			posNNSYM.setValue(true);
		out.add(posNNSYM);
		
		Feature posNNPS = new Feature("POS NNPS", false);
		if (tag.getPosValue().equals("NNPS"))
			posNNPS.setValue(true);
		out.add(posNNPS);
		
		Feature posNNP = new Feature("POS NNP", false);
		if (tag.getPosValue().equals("NNP"))
			posNNP.setValue(true);
		out.add(posNNP);
		return out;
	}

}
