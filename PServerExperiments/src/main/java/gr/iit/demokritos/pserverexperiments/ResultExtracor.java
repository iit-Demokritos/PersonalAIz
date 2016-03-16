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
package gr.iit.demokritos.pserverexperiments;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class ResultExtracor {

    public static void main(String[] args) throws IOException, Exception {

        ArrayList<String> resultList = new ArrayList<>();
        resultList.add("1_scenario2_0_10_30-100.csv");
//        resultList.add("1_scenario2_0_10_50-100.csv");
//        resultList.add("2_scenario2_0_10_30-100.csv");
//        resultList.add("2_scenario2_0_10_50-100.csv");
//        resultList.add("4_scenario2_0_10_30-100.csv");
//        resultList.add("4_scenario2_0_10_50-100.csv");
//        resultList.add("8_scenario2_0_10_30-100.csv");
//        resultList.add("8_scenario2_0_10_50-100.csv");

        for (String cResultName : resultList) {

            String filename = cResultName;
            String filenamfeToExtrectResults = "/home/pgiotis/NetBeansProjects/DemokritosProjects/PersonalAIzResults/4C-4C/resultExport/export_" + filename;
            String resultsFilename = "/home/pgiotis/NetBeansProjects/DemokritosProjects/PersonalAIzResults/4C-4C/" + filename;
            String startAddUsers = "";
            String endAddUsers = "";
            String startModifyUsers = "";
            String endModifyUsers = "";
            String startGetuseronmodify = "";
            String endGetUserOnModify = "";
            String startGetUsersProfile = "";
            String endGetUsersProfile = "";
            int addUserCallCounter = 0;
            int modifyUserCallCounter = 0;
            int getUserOnModifyCallCounter = 0;
            int getUserProfileCallCounter = 0;
            long addUserCallsDuration = 0;
            long modifyUserCallsDuration = 0;
            long getUserOnModifyCallsDuration = 0;
            long getUserProfileCallsDuration = 0;

            List<String> storeList = new ArrayList<>();
            List<Long> addUserCallsList = new ArrayList<>();
            List<Long> modifyUserCallsList = new ArrayList<>();
            List<Long> getUserOnModifyCallsList = new ArrayList<>();
            List<Long> getUserProfileCallsList = new ArrayList<>();

            //Load the result set
            BufferedReader results = lodDataResultSet(resultsFilename);

            //export results
            String resultStrLine;
            while ((resultStrLine = results.readLine()) != null) {
                String[] splitedLine;

                if (resultStrLine.startsWith("scenario")) {

                    splitedLine = resultStrLine.split("::");

                    if (splitedLine[1].contains("adduser")) {
                        addUserCallCounter++;
                        long callDuration = Long.parseLong(splitedLine[3])
                                - Long.parseLong(splitedLine[2]);
                        addUserCallsDuration = addUserCallsDuration + callDuration;
                        addUserCallsList.add(callDuration);

                    } else if (splitedLine[1].contains("modifyuser")) {
                        modifyUserCallCounter++;
                        long callDuration = Long.parseLong(splitedLine[3])
                                - Long.parseLong(splitedLine[2]);
                        modifyUserCallsDuration = modifyUserCallsDuration + callDuration;
                        modifyUserCallsList.add(callDuration);

                    } else if (splitedLine[1].contains("getuseronmodify")) {
                        getUserOnModifyCallCounter++;
                        long callDuration = Long.parseLong(splitedLine[3])
                                - Long.parseLong(splitedLine[2]);
                        getUserOnModifyCallsDuration = getUserOnModifyCallsDuration + callDuration;
                        getUserOnModifyCallsList.add(callDuration);

                    } else if (splitedLine[1].contains("getuser")) {
                        getUserProfileCallCounter++;
                        long callDuration = Long.parseLong(splitedLine[3])
                                - Long.parseLong(splitedLine[2]);
                        getUserProfileCallsDuration = getUserProfileCallsDuration + callDuration;
                        getUserProfileCallsList.add(callDuration);
                    }

                } else if (resultStrLine.startsWith("#Start Add Users:")) {
                    splitedLine = resultStrLine.split(":");
                    startAddUsers = splitedLine[1].trim();
                } else if (resultStrLine.startsWith("#End Add Users:")) {
                    splitedLine = resultStrLine.split(":");
                    endAddUsers = splitedLine[1].trim();
                } else if (resultStrLine.startsWith("#Start Modify Users Profile:")) {
                    splitedLine = resultStrLine.split(":");
                    startModifyUsers = splitedLine[1].trim();
                } else if (resultStrLine.startsWith("#End Modify Users Profile:")) {
                    splitedLine = resultStrLine.split(":");
                    endModifyUsers = splitedLine[1].trim();
                } else if (resultStrLine.startsWith("#Start Get Users Profile:")) {
                    splitedLine = resultStrLine.split(":");
                    startGetUsersProfile = splitedLine[1].trim();
                } else if (resultStrLine.startsWith("#End Get Users Profile:")) {
                    splitedLine = resultStrLine.split(":");
                    endGetUsersProfile = splitedLine[1].trim();
                }

            }

            // Convert the action duration from milliseconds to seconds
            Long addUserDuration = TimeUnit.MILLISECONDS.toSeconds(
                    Long.parseLong(endAddUsers) - Long.parseLong(startAddUsers));
            Long modifyUserDuration = TimeUnit.MILLISECONDS.toSeconds(
                    Long.parseLong(endModifyUsers) - Long.parseLong(startModifyUsers));
            Long getUserOnModifyDuration = TimeUnit.MILLISECONDS.toSeconds(
                    Long.parseLong(endModifyUsers) - Long.parseLong(startModifyUsers));
            Long getUserProfileDuration = TimeUnit.MILLISECONDS.toSeconds(
                    Long.parseLong(endGetUsersProfile) - Long.parseLong(startGetUsersProfile));

            DecimalFormat df = new DecimalFormat("#.00");
            //calculate Standard Deviation 
            double addUserAverage = (double) addUserCallsDuration / (double) addUserCallCounter;
            double modifyUserAverage = (double) modifyUserCallsDuration / (double) modifyUserCallCounter;
            double getUserOnModifyAverage = (double) getUserOnModifyCallsDuration / (double) getUserOnModifyCallCounter;
            double getUserProfileAverage = (double) getUserProfileCallsDuration / (double) getUserProfileCallCounter;
            double addUserDevination = calculateDevination(addUserCallsList, addUserAverage);
            double modifyUserDevination = calculateDevination(modifyUserCallsList, modifyUserAverage);
            double getUserOnModifyDevination = calculateDevination(getUserOnModifyCallsList, getUserOnModifyAverage);
            double getUserProfileDevination = calculateDevination(getUserProfileCallsList, getUserProfileAverage);

            if (addUserDuration != 0) {
                storeList.add("AddUser calls per second: "
                        .concat(Long.toString(addUserCallCounter / addUserDuration)));
            }
            storeList.add("AddUser average call time in milliseconds: "
                    .concat(df.format(addUserAverage)));
            storeList.add("AddUser devination call time in milliseconds: "
                    .concat(df.format(addUserDevination)));
            storeList.add("AddUser Quartiles in milliseconds: "
                    .concat(Quartiles(addUserCallsList)));

            if (modifyUserDuration != 0) {
                storeList.add("ModifyUser calls per second: "
                        .concat(Long.toString(modifyUserCallCounter / modifyUserDuration)));
            }
            storeList.add("ModifyUser average call time in milliseconds: "
                    .concat(df.format(modifyUserAverage)));
            storeList.add("ModifyUser devination call time in milliseconds: "
                    .concat(df.format(modifyUserDevination)));
            storeList.add("ModifyUser Quartiles in milliseconds: "
                    .concat(Quartiles(modifyUserCallsList)));

            if (getUserOnModifyDuration != 0) {
                storeList.add("GetUserOnModify calls per second: "
                        .concat(Long.toString(getUserOnModifyCallCounter / getUserOnModifyDuration)));
            }
            storeList.add("GetUserOnModify average call time in milliseconds: "
                    .concat(df.format(getUserOnModifyAverage)));
            storeList.add("GetUserOnModify devination call time in milliseconds: "
                    .concat(df.format(getUserOnModifyDevination)));
            storeList.add("GetUserOnModify Quartiles in milliseconds: "
                    .concat(Quartiles(getUserOnModifyCallsList)));

            if (getUserProfileDuration != 0) {
                storeList.add("GetUserProfile calls per second: "
                        .concat(Long.toString(getUserProfileCallCounter / getUserProfileDuration)));
            }
            storeList.add("GetUserProfile average call time in milliseconds: "
                    .concat(df.format(getUserProfileAverage)));
            storeList.add("GetUserProfile devination call time in milliseconds: "
                    .concat(df.format(getUserProfileDevination)));
            storeList.add("GetUserProfile Quartiles in milliseconds: "
                    .concat(Quartiles(getUserProfileCallsList)));

            storeList.add("Total experiment duration in second: "
                    .concat(Long.toString(addUserDuration + modifyUserDuration + getUserProfileDuration)));

            //Store the export results
            storeData(storeList, filenamfeToExtrectResults);
        }
    }

    private static double calculateDevination(List<Long> callsList, Double average) {
        long sum = 0;

        for (long cCall : callsList) {

            long difference = (long) (cCall - average);
            long square = difference * difference;
            sum = sum + square;
        }

        return Math.sqrt(sum / (callsList.size() - 1));
    }

    /**
     * Load result set and return the bufferedReader
     *
     * @param filePath
     * @return
     */
    private static BufferedReader lodDataResultSet(String filePath) {
        BufferedReader br = null;
        try {
            FileInputStream fstream = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

//            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return br;
    }

    public static void storeData(List<String> storeList, String filename) {
        System.out.println("=== " + filename + " ===");
        PrintWriter pw = null;

        try {

            pw = new PrintWriter(new FileWriter(filename, true));
            for (String text : storeList) {
                // write to file
                pw.println(text);
                System.out.println(text);
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

    public static String Quartiles(List<Long> values) throws Exception {
        if (values.size() < 3) {
            throw new Exception("This method is not designed to handle lists with fewer than 3 elements.");
        }

        Long median = Median(values);

        ArrayList lowerHalf = GetValuesLessThan(values, median, true);
        ArrayList upperHalf = GetValuesGreaterThan(values, median, true);

        return " lowerHalf: ".concat(Long.toString(Median(lowerHalf)))
                .concat(" median: ")
                .concat(Long.toString(median))
                .concat(" upperHalf: ")
                .concat(Long.toString(Median(upperHalf)))
                .concat(" Min: ")
                .concat(Long.toString(Collections.min(values)))
                .concat(" Max: ")
                .concat(Long.toString(Collections.max(values)));
    }

    public static ArrayList GetValuesGreaterThan(List<Long> values, Long limit, boolean orEqualTo) {
        ArrayList modValues = new ArrayList();

        for (Long value : values) {
            if (value > limit || (value == limit && orEqualTo)) {
                modValues.add(value);
            }
        }

        return modValues;
    }

    public static ArrayList GetValuesLessThan(List<Long> values, Long limit, boolean orEqualTo) {
        ArrayList modValues = new ArrayList();

        for (Long value : values) {
            if (value < limit || (value == limit && orEqualTo)) {
                modValues.add(value);
            }
        }

        return modValues;
    }

    public static Long Median(List<Long> values) {
        Collections.sort(values);

        if (values.size() % 2 == 1) {
            return values.get((values.size() + 1) / 2 - 1);
        } else {
            Long lower = values.get(values.size() / 2 - 1);
            Long upper = values.get(values.size() / 2);

            return (lower + upper) / 2;
        }
    }

}
