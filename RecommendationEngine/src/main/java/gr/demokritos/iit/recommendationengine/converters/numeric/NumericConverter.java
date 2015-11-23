/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.numeric;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a converter. The converter get a list list with objects
 * and generate numeric features.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class NumericConverter implements IConverter<String> {

    private final String language;

    /**
     * Numeric Converter Constructor
     *
     * @param language The objects language
     */
    public NumericConverter(String language) {
        this.language = language;
    }

    /**
     * Convert a list with number variables to numeric features
     *
     * @param objects
     * @return
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix numeric + the numeric name to Lower Case + value
            String[] tmpSplit = cCategory.toLowerCase().split(":");

            String feature
                    = language
                    + ".numeric."
                    + tmpSplit[0].trim()
                    + "."
                    + tmpSplit[1].trim();

            //Add feature on Map with value 1
            features.put(feature, 1);

        }

        return features;
    }

}
