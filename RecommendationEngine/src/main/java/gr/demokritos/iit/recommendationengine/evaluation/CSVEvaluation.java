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
package gr.demokritos.iit.recommendationengine.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * NOT WORKING CLASS Implements the evaluation with csv file storage system
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class CSVEvaluation implements IEvaluation {

    /**
     * Store a evaluation entry on HBsase
     *
     * @param username The username
     * @param objectId The feed object id
     * @param recommended The flag if the object is recommended
     * @param timestamp The Feed timestamp
     * @param clientName The client name
     */
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
