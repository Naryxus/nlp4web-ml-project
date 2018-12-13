package reader;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.commons.lang.StringUtils;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.jcas.JCas;
import org.dkpro.tc.api.io.TCReaderSequence;
import org.dkpro.tc.api.type.TextClassificationOutcome;
import org.dkpro.tc.api.type.TextClassificationSequence;
import org.dkpro.tc.api.type.TextClassificationTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.IOUtils.closeQuietly;

public class NERDataReader extends JCasResourceCollectionReader_ImplBase implements TCReaderSequence {

    private static final int TOKEN = 0;
    private static final int IOBDE = 4;
    private static final int IOBEN = 3;
    private static final int CHUNKTAG = 1;

    /**
     * Character encoding of the input data.
     */
    public static final String PARAM_ENCODING = ComponentParameters.PARAM_SOURCE_ENCODING;
    @ConfigurationParameter(name = PARAM_ENCODING, mandatory = true, defaultValue = "UTF-8")
    private String encoding;

    /**
     * The language.
     */
    public static final String PARAM_LANGUAGE = ComponentParameters.PARAM_LANGUAGE;
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true)
    private String language;

    /**
     * Load the chunk tag to UIMA type mapping from this location instead of locating
     * the mapping automatically.
     */
    public static final String PARAM_CHUNK_MAPPING_LOCATION = ComponentParameters.PARAM_CHUNK_MAPPING_LOCATION;
    @ConfigurationParameter(name = PARAM_CHUNK_MAPPING_LOCATION, mandatory = false)
    protected String chunkMappingLocation;

    @Override
    public void getNext(JCas aJCas) throws IOException, CollectionException {
        Resource res = nextFile();
        initCas(aJCas, res);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(res.getInputStream(), encoding));
            convert(aJCas, reader);
        }
        finally {
            closeQuietly(reader);
        }
    }

    private void convert(JCas aJCas, BufferedReader aReader) throws IOException {
        JCasBuilder doc = new JCasBuilder(aJCas);

        List<String[]> words;
        while ((words = readSentence(aReader)) != null) {
            if (words.isEmpty()) {
                continue;
            }

            int sentenceBegin = doc.getPosition();
            int sentenceEnd = sentenceBegin;

            List<Token> tokens = new ArrayList<>();

            // Tokens, NER-IOB
            for (String[] word : words) {
                // Read token
                Token token = doc.add(word[TOKEN], Token.class);
                sentenceEnd = token.getEnd();
                doc.add(" ");

                TextClassificationTarget unit = new TextClassificationTarget(aJCas, token.getBegin(), token.getEnd());
                unit.addToIndexes();
                TextClassificationOutcome outcome = new TextClassificationOutcome(aJCas, token.getBegin(), token.getEnd());
                switch(language) {
                    case "en":
                        outcome.setOutcome(word[IOBEN]);
                        break;
                    case "de":
                        outcome.setOutcome(word[IOBDE]);
                }
                POS tmp = new POS(aJCas);
                tmp.setPosValue(word[CHUNKTAG]);
                tmp.setBegin(token.getBegin());
                tmp.setEnd(token.getEnd());
                tmp.addToIndexes();
                outcome.addToIndexes();
                tokens.add(token);
            }

            // Sentence
            Sentence sentence = new Sentence(aJCas, sentenceBegin, sentenceEnd);
            sentence.addToIndexes();

            TextClassificationSequence sequence = new TextClassificationSequence(aJCas, sentenceBegin, sentenceEnd);
            sequence.addToIndexes();

            // Once sentence per line.
            doc.add("\n");
        }

        doc.close();
    }

    /**
     * Read a single sentence.
     */
    private List<String[]> readSentence(BufferedReader aReader) throws IOException {
        List<String[]> words = new ArrayList<>();
        String line;
        while ((line = aReader.readLine()) != null) {
            if (StringUtils.isBlank(line))
                break; // End of sentence

            if (line.startsWith("-DOCSTART-"))
                continue;

            String[] fields = line.split(" ");
            int numFeatures = 0;
            switch(language) {
                case "de":
                    numFeatures = 5;
                    break;
                case "en":
                    numFeatures = 4;
                    break;
            }
            if (fields.length != numFeatures) {
                throw new IOException(
                        "Invalid file format. Line needs to have " + numFeatures + " tab-separated fields.");
            }

            words.add(fields);
        }

        if (line == null && words.isEmpty())
            return null;

        return words;
    }

    @Override
    public String getTextClassificationOutcome(JCas jcas, TextClassificationTarget unit) throws CollectionException {
        // without function here, as we do not represent this in the CAS
        return null;
    }
}
