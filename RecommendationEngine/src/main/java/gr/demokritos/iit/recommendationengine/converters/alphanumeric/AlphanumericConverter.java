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
