/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters;

import java.util.List;
import java.util.Map;

/**
 * The interface of the recommendation converter
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IConverter<object> {
    
    Map<String,Integer> getFeatures(List<object> objects);

    
}
