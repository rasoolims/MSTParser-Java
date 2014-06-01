package DepParser;

import mstparser.*;

import java.util.ArrayList;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 6/1/14
 * Time: 12:12 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class DepParser {
    DependencyParser parser;

    ParserOptions options;

    ConfidenceEstimator confEstimator;

    public DepParser(ParserOptions options) throws Exception {
        this.options=options;
        DependencyPipe pipe = options.secondOrder ? new DependencyPipe2O(options)
                : new DependencyPipe(options);

        parser= new DependencyParser(pipe, options);

        parser.loadModel(options.modelName);

        pipe.closeAlphabets();

        confEstimator = null;
        if (options.confidenceEstimator != null) {
            confEstimator = ConfidenceEstimator.resolveByName(options.confidenceEstimator, parser);
        }
         pipe.labeled=true;
    }

    public DependencyInstance parse(String[] words, String[] lemma ,String[] poslist, String[] fPos){
        int length=words.length;

        String[] forms = new String[length + 1];
        String[] lemmas = new String[length + 1];
        String[] cpos = new String[length + 1];
        String[] pos = new String[length + 1];
        String[][] feats = new String[length + 1][];
        String[] deprels = new String[length + 1];
        int[] heads = new int[length + 1];
        double[] confscores =  null;

        forms[0] = "<root>";
        lemmas[0] = "<root-LEMMA>";
        cpos[0] = "<root-CPOS>";
        pos[0] = "<root-POS>";
        deprels[0] = "<no-type>";
        heads[0] = -1;


        for (int i = 0; i < length; i++) {
            forms[i + 1] = words[i];
            lemmas[i + 1] = lemma[i];
            cpos[i + 1] = poslist[i];
            pos[i + 1] = fPos[i];
            feats[i + 1] = "".split("\\|");   //todo  for the meanwhile I ignored the features
            deprels[i + 1] = "ROOT";
            heads[i + 1] = i;
        }

        feats[0] = new String[feats[1].length];
        for (int i = 0; i < feats[1].length; i++)
            feats[0][i] = "<root-feat>" + i;

        ArrayList<RelationalFeature> rfeats = new ArrayList<RelationalFeature>();

        RelationalFeature[] rfeatsList = new RelationalFeature[rfeats.size()];
        rfeats.toArray(rfeatsList);

        // End of discourse stuff.

       DependencyInstance instance= new DependencyInstance(forms, lemmas, cpos, pos, feats, deprels, heads, rfeatsList,
                confscores);
       DependencyInstance parsedInstance=parse(instance);

        return parsedInstance;
    }

    private DependencyInstance parse(DependencyInstance instance){
        String[] forms = instance.forms;
        String[] formsNoRoot = new String[forms.length - 1];
        String[] posNoRoot = new String[formsNoRoot.length];
        String[] labels = new String[formsNoRoot.length];
        int[] heads = new int[formsNoRoot.length];

        parser.decode(instance, options.testK, parser.params, formsNoRoot, posNoRoot, labels, heads);


        DependencyInstance parsedInstance;
        if (confEstimator != null) {
            double[] confidenceScores = confEstimator.estimateConfidence(instance);
            parsedInstance = new DependencyInstance(formsNoRoot, posNoRoot, labels, heads,
                    confidenceScores);
        } else {
            parsedInstance = new DependencyInstance(formsNoRoot, posNoRoot, labels, heads);
        }
        return parsedInstance;
    }


}
