/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class CSVEvaluation implements IEvaluation {

    @Override
    public void storeEntry(String username, String objectId, 
            boolean recommended, long timestamp, String clientName) {

        //ClienName : username : objID : recommended : timestamp
        String recordToStore= clientName+":"+username+":"+objectId+":"
                +recommended+":"+timestamp;
        
        PrintWriter pw = null;

        try {

            pw = new PrintWriter(new FileWriter("evaluation_records.csv", true));
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
