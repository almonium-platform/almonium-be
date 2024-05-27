package linguarium.engine.analyzer.service.impl;

import static lombok.AccessLevel.PRIVATE;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import linguarium.engine.analyzer.model.enums.POS;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

// @Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CoreNLPServiceImpl {
    private StanfordCoreNLP pipeline;

    public CoreNLPServiceImpl() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<POS> posTagging(String documentText) {
        List<POS> pos = new ArrayList<>();
        for (CoreLabel tok : pipeline.processToCoreDocument(documentText).tokens()) {
            System.out.printf("%s\t%s%n", tok.word(), tok.tag());
            pos.add(POS.fromString(tok.tag()));
        }
        return pos;
    }

    public List<String> lemmatize(String documentText) {
        List<String> lemmas = new LinkedList<>();
        Annotation document = new Annotation(documentText);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        return lemmas;
    }
}
