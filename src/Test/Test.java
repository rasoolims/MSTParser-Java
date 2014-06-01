package Test;

import DepParser.DepParser;
import mstparser.DependencyInstance;
import mstparser.ParserOptions;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 6/1/14
 * Time: 12:44 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class Test {
    public static void main(String[] args) throws Exception {
        String modelPath="/Users/msr/Projects/mstparser/out/production/mstparser/dep.model";
        String[] opts=new String[6];
        opts[0]="test";
        opts[1]="model-name:" +modelPath;
        opts[2]="order:2";
        opts[3]="loss-type: punc";
        opts[4]="decode-type:proj";
        opts[5]="create-forest: true";

        ParserOptions options=new ParserOptions(opts);

        DepParser parser=new DepParser(options);

        for (int j=0;j<100;j++){
            String[] words=new String[]{"it","was","n't","black","monday","."};
            String[] lemmas=new String[]{"it","was","n't","black","monday","."};
            String[] pos=new String[]{"PRP","VBD","RB","JJ","NNP","."};
            String[] fpos=new String[]{"PRP","VBD","RB","JJ","NNP","."};

            DependencyInstance instance=parser.parse(words,lemmas,pos,fpos);

            for (int i=0;i<instance.length();i++){
                System.out.println((i+1)+"\t"+instance.forms[i]+"\t"+instance.postags[i]+"\t"+instance.heads[i]+"\t"+instance.deprels[i]);
            }
            System.out.println("");
        }
    }
}
