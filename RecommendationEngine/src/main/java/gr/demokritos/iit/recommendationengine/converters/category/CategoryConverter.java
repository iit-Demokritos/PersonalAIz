/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.category;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a converter. This converter get a list with categories
 * and create a feature list
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class CategoryConverter implements IConverter<String> {

    private final String language;

    /**
     * Category Converter Constructor
     *
     * @param language The objects language
     */
    public CategoryConverter(String language) {
        this.language = language;
    }

    /**
     * Convert a list with categories to a PServer feature List
     *
     * @param objects A list with category names
     * @return A Map with feature name and values
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix category + the category name to Lower Case
            String feature
                    = language
                    + ".category."
                    + cCategory.toLowerCase().trim();

            //Add feature ton Map with value 1
            features.put(feature, 1);
        }

        return features;
    }

}
