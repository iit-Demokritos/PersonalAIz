/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.demokritos.iit.recommendationengine.converters.tag;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This converter get a list with tags and create a feature list
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class TagConverter implements IConverter<String> {

    private final String language;

    /**
     * Tag Converter Constructor
     *
     * @param language The objects language
     */
    public TagConverter(String language) {
        this.language = language;
    }

    /**
     * Convert a list with tags to a PServer feature List
     *
     * @param objects A list with tag names
     * @return A Map with feature name and values
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix tag + the tag name to Lower Case
            String feature
                    = language
                    + ".tag."
                    + cCategory.toLowerCase().trim();

            //Add feature ton Map with value 1
            features.put(feature, 1);
        }

        return features;
    }

}
