package ner;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.util.HashMap;
import java.util.Map;

import static de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.INCLUDE_PREFIX;

import javafx.geometry.Pos;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.dkpro.lab.Lab;
import org.dkpro.lab.task.ParameterSpace;
import org.dkpro.lab.task.BatchTask.ExecutionPolicy;
import org.dkpro.lab.task.Dimension;
import org.dkpro.tc.api.features.TcFeatureFactory;
import org.dkpro.tc.api.features.TcFeatureSet;
import org.dkpro.tc.core.Constants;
import org.dkpro.tc.features.length.NrOfChars;
import org.dkpro.tc.features.style.InitialCharacterUpperCase;
import org.dkpro.tc.ml.ExperimentTrainTest;
import org.dkpro.tc.ml.crfsuite.CRFSuiteAdapter;
import org.dkpro.tc.ml.report.BatchTrainTestReport;

import features.DummyFeature;
import features.LastChar;
import features.PhraseFeature;
import features.PosFeature;
import reader.NERDataReader;
import utils.DemoUtils;

public class CRFSuiteNER implements Constants {

	public static final String LANGUAGE_CODE = "en";
	public static final int NUM_FOLDS = 10;
	public static final String corpusFilePathTrain;
	public static final String corpusFilePathTest;
	
	static {
		if (LANGUAGE_CODE.equals("en")) {
			corpusFilePathTrain = "src/main/resources/data/en";
			corpusFilePathTest = "src/main/resources/data/en";
		} else {
			corpusFilePathTrain = "src/main/resources/data/de";
			corpusFilePathTest = "src/main/resources/data/de";
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("java.util.logging.config.file",
                "src/main/resources/logging.properties");
		
		DemoUtils.setDkproHome(CRFSuiteNER.class.getSimpleName());
		
		CRFSuiteNER demo = new CRFSuiteNER();
		//ParameterSpace test = getParameterSpace();
		demo.runTrainTest(getParameterSpace());
	}
	
	public static ParameterSpace getParameterSpace() throws ResourceInitializationException {
		CollectionReaderDescription readerTrain = CollectionReaderFactory.createReaderDescription(
				NERDataReader.class, NERDataReader.PARAM_LANGUAGE, LANGUAGE_CODE,
				NERDataReader.PARAM_SOURCE_LOCATION, corpusFilePathTrain,
				NERDataReader.PARAM_PATTERNS, INCLUDE_PREFIX + "*.train"
				);
		CollectionReaderDescription readerTest = CollectionReaderFactory.createReaderDescription(
				NERDataReader.class, NERDataReader.PARAM_LANGUAGE, LANGUAGE_CODE,
				NERDataReader.PARAM_SOURCE_LOCATION, corpusFilePathTest,
				NERDataReader.PARAM_PATTERNS, INCLUDE_PREFIX + "*.dev"
				);
		
		Map<String, Object> dimReaders = new HashMap<String, Object>();
		dimReaders.put(DIM_READER_TRAIN, readerTrain);
		dimReaders.put(DIM_READER_TEST, readerTest);
		
		
		Dimension<TcFeatureSet> dimFeatureSets = Dimension.create(DIM_FEATURE_SET,
				new TcFeatureSet(TcFeatureFactory.create(NrOfChars.class),
						TcFeatureFactory.create(InitialCharacterUpperCase.class),
						TcFeatureFactory.create(PosFeature.class)));
						//TcFeatureFactory.create(LastChar.class)));
						//TcFeatureFactory.create(PosFeature.class),
						//TcFeatureFactory.create(PhraseFeature.class)));
		
		ParameterSpace pSpace = new ParameterSpace(Dimension.createBundle("readers", dimReaders),
				Dimension.create(DIM_LEARNING_MODE, Constants.LM_SINGLE_LABEL),
				Dimension.create(DIM_FEATURE_MODE, Constants.FM_SEQUENCE), dimFeatureSets
				);
		return pSpace;
	}
	
	protected void runTrainTest(ParameterSpace pSpace) throws Exception {
		ExperimentTrainTest batch = new ExperimentTrainTest("NamedEntitySequenceTrainTest", CRFSuiteAdapter.class);
		batch.setPreprocessing(getPreprocessing());
		batch.setParameterSpace(pSpace);
		batch.addReport(BatchTrainTestReport.class);
		//batch.addReport(ContextMemoryReport.class);
		batch.setExecutionPolicy(ExecutionPolicy.RUN_AGAIN);
		System.out.println("Lab.run");
		Lab.getInstance().run(batch);
	}
	
	protected AnalysisEngineDescription getPreprocessing() throws ResourceInitializationException {
	        return createEngineDescription(NoOpAnnotator.class);
    }
}
