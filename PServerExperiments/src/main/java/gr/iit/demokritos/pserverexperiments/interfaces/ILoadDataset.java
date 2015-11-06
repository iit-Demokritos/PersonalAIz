/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments.interfaces;

import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface ILoadDataset {

    String getNextUser();
    boolean userHasNext();
    String getNextUserModification();
    boolean userModificationHasNext();
    List<String> getUsernamesList();
    String getRandomUsername();

}
