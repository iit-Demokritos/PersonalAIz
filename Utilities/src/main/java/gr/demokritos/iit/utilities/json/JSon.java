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
package gr.demokritos.iit.utilities.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used as wrapper for Gson with data types that aren't in this package
 * JSonizable interface is implemented for the other classes
 */
public class JSon {

    /**
     * Static attribute, available to all callers. Disables Html Escaping by
     * default.
     */
    public static Gson json = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Returns a String of the JSON format of the object
     *
     * @param object any object to be converted.
     * @param classOfT the class that the object belongs to
     * @return JSON format of object.
     */
    public static String jsonize(Object object, Class<? extends Object> classOfT) {

        return json.toJson(object, classOfT);

    }

    /**
     * Returns an instance of the Object relevant to the JSON string
     *
     * @param jsonstring the String in json format to be converted.
     * @param classOfT the template the object corresponds to.
     * @return instance of object corresponding to the JSON String.
     */
    public static <T> T unjsonize(String jsonstring, Class<T> classOfT) {

        return json.fromJson(jsonstring, classOfT);

    }

    /**
     * Use to jsonize an object '@Exposed' attributes only.
     *
     * @param object any object to be converted.
     * @param classOfT the class that the object belongs to
     * @return JSON format of object.
     */
    public static String jsoniseExposedOnly(Object object, Class<? extends Object> classOfT) {

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return gson.toJson(object, classOfT);

    }
}
