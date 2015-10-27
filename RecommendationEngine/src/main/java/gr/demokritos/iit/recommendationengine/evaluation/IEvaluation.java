/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.evaluation;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public interface IEvaluation {

    void storeEntry(String username, String objectId, boolean recommended, long timestamp);

}
