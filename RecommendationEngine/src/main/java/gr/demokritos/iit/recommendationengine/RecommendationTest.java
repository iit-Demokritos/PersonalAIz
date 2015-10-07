/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine;

import gr.demokritos.iit.recommendationengine.converters.text.TextConverter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationTest {
    
    public static void main(String[] args) {
        
        String lang = "en";
        ArrayList textList = new ArrayList();
        textList.add("A tokenizer divides text into a sequence of tokens, which roughly correspond to \"words\". We provide a class suitable for tokenization of English, called PTBTokenizer. It was initially designed to largely mimic Penn Treebank 3 (PTB) tokenization, hence its name, though over time the tokenizer has added quite a few options and a fair amount of Unicode compatibility, so in general it will work well over text encoded in the Unicode Basic Multilingual Plane that does not require word segmentation (such as writing systems that do not put spaces between words) or more exotic language-particular rules (such as writing systems that use : or ? as a character inside words, etc.). An ancillary tool uses this tokenization to provide the ability to split text into sentences. PTBTokenizer mainly targets formal English writing rather than SMS-speak. ");
        textList.add("PTBTokenizer is a an efficient, fast, deterministic tokenizer. (For the more technically inclined, it is implemented as a finite automaton, produced by JFlex.) On a 2015 laptop computer, it will tokenize text at a rate of about 1,000,000 tokens per second. While deterministic, it uses some quite good heuristics, so it can usually decide when single quotes are parts of words, when periods do an don't imply sentence boundaries, etc. Sentence splitting is a deterministic consequence of tokenization: a sentence ends when a sentence-ending character (., !, or ?) is found which is not grouped with other characters into a token (such as for an abbreviation or number), though it may still include a few tokens that can follow a sentence ending character as part of the same sentence (such as quotes and brackets). ");
        
        
        TextConverter tc = new TextConverter(lang, false);
        HashMap<String,Integer> features= new HashMap<>(tc.getFeatures(textList));
        
        for(String cFeature: features.keySet()){
            System.out.println(cFeature +" ---> "+ features.get(cFeature));
        }
        
        
    }
}
