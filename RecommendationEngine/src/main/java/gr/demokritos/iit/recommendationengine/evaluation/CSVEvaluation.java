/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * NOT WORKING CLASS
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class CSVEvaluation implements IEvaluation {

    @Override
    public void storeEntry(String username, String objectId,
            boolean recommended, long timestamp, String clientName) {

        //ClienName : username : objID : recommended : timestamp
        String recordToStore = clientName + ":" + username + ":" + objectId + ":"
                + recommended + ":" + timestamp;
        PrintWriter pw = null;

        
        File eFile = new File("evaluation_records.csv");
        try {

            pw = new PrintWriter(new FileWriter(eFile, true));
            // write to file
            pw.println(recordToStore);
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close the PrintWriter
            if (pw != null) {
                pw.close();
            }

        }

    }

}
