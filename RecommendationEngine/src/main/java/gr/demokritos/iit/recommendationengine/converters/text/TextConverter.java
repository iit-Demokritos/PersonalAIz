/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.text;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a converter. The converter get a list with row texts
 * and create a feature list
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class TextConverter implements IConverter<String> {

    private final String language;
    private final HashSet<String> STOPWORDS;
    private IStemmer stemmer;
    private boolean stemming;
    public static final Logger LOGGER = LoggerFactory.getLogger(TextConverter.class);

    /**
     * Text Converter Constructor
     *
     * @param language The objects language
     * @param stemming Option if we want stemming or not
     */
    public TextConverter(String language, boolean stemming) {
        this.language = language;
        this.stemming = stemming;

        //Load StopWord list
        this.STOPWORDS = new HashSet<>(loadStopWordList(language));

        if (stemming) {
            //load stemmer
            if (language.equalsIgnoreCase("el")) {
                stemmer = new GreekStemmer(STOPWORDS);
            } else {
                stemmer = new EnglishStemmer();
            }
        }
    }

    /**
     * Convert a list with texts to a PServer feature List
     *
     * @param objects A list with row texts
     * @return A Map with feature name and values
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        //For each Text
        for (String cText : objects) {

            ArrayList<String> tokens = new ArrayList<>();
            //Normalize and tokenizing content
            tokens.addAll(textTokenizer(normalizeText(cText)));

            //For each token 
            for (String cToken : tokens) {
                //Create feature name without stemming
                String feature = language + ".text." + cToken;

                //If stemming is enabled
                if (stemming) {
                    //Create feature name with stem
                    feature = language + ".text." + stemmer.stem(cToken);
                }

                if (features.containsKey(feature)) {
                    int tmpFrequency = features.get(feature);
                    features.put(feature, tmpFrequency + 1);
                } else {
                    //Add features on Feature Map
                    features.put(feature, 1);
                }

            }
        }
        return features;
    }

    /**
     * Load the stopwordlist for the given language
     *
     * @param lang The language prefix
     * @return A set of Stop words
     */
    private HashSet<String> loadStopWordList(String lang) {

        HashSet<String> stopWordList = new HashSet<>();

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedReader br = null;
        try {
            //Create file inpute stream with resource path
            FileInputStream fstream = new FileInputStream(
                    classLoader.getResource("stopwords_" + lang + ".txt").getPath());
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //for each word in the file add it on stopWordList
            while ((strLine = br.readLine()) != null) {
                stopWordList.add(strLine);
            }

        } catch (IOException e) {
            LOGGER.error("Fail to load StopwordList", e);
            return null;
        }

        return stopWordList;
    }

    /**
     * Generate token list from text
     *
     * @param text
     * @return
     */
    private ArrayList textTokenizer(String text) {

        ArrayList<String> tokeList = new ArrayList<>();
        //Removes all non-letter characters and split on space
        String[] tokens = text.replaceAll("\\p{P}", "").split("\\s+");

        //For each token check if is stopword
        for (String cToken : tokens) {
            if (!STOPWORDS.contains(cToken) && cToken.length() > 1) {
                //if not then add it to list
                tokeList.add(cToken);
            }
        }

        return tokeList;
    }

    /**
     * Normalize given text
     *
     * @param str row text
     * @return Normalized text
     */
    private String normalizeText(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).
                replaceAll("\\p{InCombiningDiacriticalMarks}+", "").
                toLowerCase();
    }

}
