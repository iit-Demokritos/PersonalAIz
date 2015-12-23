/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        String filename = "scenario1_0_10.csv";
        String filenamfeToExtrectResults = "results/export_" + filename;
        String resultsFilename = "results/" + filename;
        String startAddUsers = "";
        String endAddUsers = "";
        String startModifyUsers = "";
        String endModifyUsers = "";
        String startGetUsersProfile = "";
        String endGetUsersProfile = "";
        int addUserCallCounter = 0;
        int modifyUserCallCounter = 0;
        int getUserProfileCallCounter = 0;
        long addUserCallsDuration = 0;
        long modifyUserCallsDuration = 0;
        long getUserProfileCallsDuration = 0;

        List<String> storeList = new ArrayList<>();
        List<Long> addUserCallsList = new ArrayList<>();
        List<Long> modifyUserCallsList = new ArrayList<>();
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
        Long getUserProfileDuration = TimeUnit.MILLISECONDS.toSeconds(
                Long.parseLong(endGetUsersProfile) - Long.parseLong(startGetUsersProfile));

        //calculate Standard Deviation 
        long addUserAverage = addUserCallsDuration / addUserCallCounter;
        long modifyUserAverage = modifyUserCallsDuration / modifyUserCallCounter;
        long getUserProfileAverage = getUserProfileCallsDuration / getUserProfileCallCounter;
        double addUserDevination = calculateDevination(addUserCallsList, addUserAverage);
        double modifyUserDevination = calculateDevination(modifyUserCallsList, modifyUserAverage);
        double getUserProfileDevination = calculateDevination(getUserProfileCallsList, getUserProfileAverage);

        if (addUserDuration != 0) {
            storeList.add("AddUser calls per second: "
                    .concat(Long.toString(addUserCallCounter / addUserDuration)));
        }
        storeList.add("AddUser average call time in milliseconds: "
                .concat(Long.toString(addUserAverage)));
        storeList.add("AddUser devination call time in milliseconds: "
                .concat(Double.toString(addUserDevination)));
        storeList.add("AddUser Quartiles in milliseconds: "
                .concat(Quartiles(addUserCallsList)));

        if (modifyUserDuration != 0) {
            storeList.add("ModifyUser calls per second: "
                    .concat(Long.toString(modifyUserCallCounter / modifyUserDuration)));
        }
        storeList.add("ModifyUser average call time in milliseconds: "
                .concat(Long.toString(modifyUserAverage)));
        storeList.add("ModifyUser devination call time in milliseconds: "
                .concat(Double.toString(modifyUserDevination)));
        storeList.add("ModifyUser Quartiles in milliseconds: "
                .concat(Quartiles(modifyUserCallsList)));

        if (getUserProfileDuration != 0) {
            storeList.add("GetUserProfile calls per second: "
                    .concat(Long.toString(getUserProfileCallCounter / getUserProfileDuration)));
        }
        storeList.add("GetUserProfile average call time in milliseconds: "
                .concat(Long.toString(getUserProfileAverage)));
        storeList.add("GetUserProfile devination call time in milliseconds: "
                .concat(Double.toString(getUserProfileDevination)));
        storeList.add("GetUserProfile Quartiles in milliseconds: "
                .concat(Quartiles(getUserProfileCallsList)));

        storeList.add("Total experiment duration in second: "
                .concat(Long.toString(addUserDuration + modifyUserDuration + getUserProfileDuration)));

        //Store the export results
        storeData(storeList, filenamfeToExtrectResults);

    }

    private static double calculateDevination(List<Long> callsList, long average) {
        long sum = 0;

        for (long cCall : callsList) {

            long difference = cCall - average;
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
    
    
    
    
    
    
    
    public static String Quartiles(List<Long> values) throws Exception
{
    if (values.size() < 3)
    throw new Exception("This method is not designed to handle lists with fewer than 3 elements.");
 
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
 
public static ArrayList GetValuesGreaterThan(List<Long> values, Long limit, boolean orEqualTo)
{
    ArrayList modValues = new ArrayList();
 
    for (Long value : values)
        if (value > limit || (value == limit && orEqualTo))
            modValues.add(value);
 
    return modValues;
}
 
public static ArrayList GetValuesLessThan(List<Long> values, Long limit, boolean orEqualTo)
{
    ArrayList modValues = new ArrayList();
 
    for (Long value : values)
        if (value < limit || (value == limit && orEqualTo))
            modValues.add(value);
 
    return modValues;
}
    
 public static Long Median(List<Long> values)
{
    Collections.sort(values);
 
    if (values.size() % 2 == 1)
	return values.get((values.size()+1)/2-1);
    else
    {
	Long lower = values.get(values.size()/2-1);
	Long upper = values.get(values.size()/2);
 
	return (lower + upper) / 2;
    }	
}   
    
    
    
    
    
    

}
