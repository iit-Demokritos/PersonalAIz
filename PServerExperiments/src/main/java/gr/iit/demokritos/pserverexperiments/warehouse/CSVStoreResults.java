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
