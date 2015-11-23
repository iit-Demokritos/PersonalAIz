/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.booleans;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a converter. The converter get a list of objects and
 * generate the boolean features.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class BooleanConverter implements IConverter<String> {

    private final String language;

    /**
     * Boolean Converter Constructor
     *
     * @param language The objects language
     */
    public BooleanConverter(String language) {
        this.language = language;
    }

    /**
     * Convert a list with boolean variables to boolean features.
     *
     * @param objects
     * @return
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix boolean + the feature name to Lower Case  + true/false
            String[] tmpSplit = cCategory.toLowerCase().split(":");
            String featureName = tmpSplit[0].trim();

            String featureTrue
                    = language
                    + ".boolean."
                    + featureName
                    + ".true";

            String featureFalse
                    = language
                    + ".boolean."
                    + featureName
                    + ".false";

            if (Boolean.parseBoolean(tmpSplit[1])) {
                //Add featureTrue ton Map with value 1
                features.put(featureTrue, 1);
                //Add featureFalse ton Map with value 1
                features.put(featureFalse, 0);
            } else {
                //Add featureTrue ton Map with value 1
                features.put(featureTrue, 0);
                //Add featureFalse ton Map with value 1
                features.put(featureFalse, 1);
            }
        }

        return features;
    }
}
