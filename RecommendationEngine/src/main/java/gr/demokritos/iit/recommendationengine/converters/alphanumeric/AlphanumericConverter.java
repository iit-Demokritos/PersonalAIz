/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.alphanumeric;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a converter. The converter get a list of alphanumeric
 * objects and generate the alphanumeric features.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class AlphanumericConverter implements IConverter<String> {

    private final String language;

    /**
     * Alphanumeric Converter Constructor
     *
     * @param language The objects language
     */
    public AlphanumericConverter(String language) {
        this.language = language;
    }

    /**
     * Get a list with alphanumeric features and converted on alphanumeric
     * features.
     *
     * @param objects
     * @return
     */
    @Override
    public Map getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix alphanumeric + the alphanumeric name to Lower Case + value
            String[] tmpSplit = cCategory.toLowerCase().split(":");

            String feature
                    = language
                    + ".alphanumeric."
                    + tmpSplit[0].trim()
                    + "."
                    + tmpSplit[1].trim();

            //Add feature on Map with value 1
            features.put(feature, 1);
        }

        return features;
    }

}
