/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments.warehouse;

import gr.iit.demokritos.pserverexperiments.interfaces.IStroreResults;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class CSVStoreResults implements IStroreResults {

    private final String fileName;

    public CSVStoreResults(String fileName) {

        this.fileName = fileName;

    }

    @Override
    public void storeData(List<String> storeList) {

        PrintWriter pw = null;

        try {

            pw = new PrintWriter(new FileWriter("./results/" + fileName + ".csv",true));
            for (String text : storeList) {
                // write to file
                pw.println(text);
            }
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
