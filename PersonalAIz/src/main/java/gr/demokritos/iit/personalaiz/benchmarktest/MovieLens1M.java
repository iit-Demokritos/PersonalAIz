/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.benchmarktest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class MovieLens1M implements ILoadDataset {

    private List<String> usernames = new ArrayList<>();
    private LinkedList<String> users = new LinkedList<>();
    private final LinkedList<String> userModifications = new LinkedList<>();
    private final Random r = new Random();
    private BufferedReader usersDat;
    private String usersStrLine;
    private BufferedReader ratingsDat;
    private String ratingsStrLine;
    private HashMap<String, String> gender = new HashMap<>();
    private HashMap<String, String> occupation = new HashMap<>();
    private int frameSize = 500000;
    private HashMap<String, List<String>> movies = new HashMap<>();

    public MovieLens1M() {

        //init vars
        gender.put("M", "male");
        gender.put("F", "female");
        occupation.put("0", "other");
        occupation.put("1", "academic");
        occupation.put("2", "artist");
        occupation.put("3", "admin");
        occupation.put("4", "college");
        occupation.put("5", "customerService");
        occupation.put("6", "doctor");
        occupation.put("7", "managerial");
        occupation.put("8", "farmer");
        occupation.put("9", "homemaker");
        occupation.put("10", "student");
        occupation.put("11", "lawyer");
        occupation.put("12", "programmer");
        occupation.put("13", "retired");
        occupation.put("14", "sales");
        occupation.put("15", "scientist");
        occupation.put("16", "selfEmployed");
        occupation.put("17", "technician");
        occupation.put("18", "tradesman");
        occupation.put("19", "unemployed");
        occupation.put("20", "writer");

        //Get BufferedReaders
        this.usersDat = lodDataSet("");
        this.ratingsDat = lodDataSet("");
        //load movies

        //Fill usernames
        //Fill users first 1M
        //Fill userModifications first 1M
    }

    private void loadMovies() throws IOException {

        BufferedReader moviesDat = lodDataSet("");
        String moviesStrLine;
        while ((moviesStrLine = moviesDat.readLine()) != null) {
            //1::Toy Story (1995)::Animation|Children's|Comedy
            String[] splitedRecord = moviesStrLine.split("::");
            ArrayList<String> features = new ArrayList<>();
            String[] splitedGenres = moviesStrLine.split("\\|");
            for (String splitedGenre : splitedGenres) {
            }

        }
    }

    /**
     * Get the next user element from the list
     *
     * @return
     */
    @Override
    public String getNextUser() {
        return users.poll();
    }

    /**
     * Check if there is next element on users list
     *
     * @return True or False if there is next element
     * @throws IOException
     */
    private boolean userHasNext() throws IOException {
        //if user is Empty refill users
        if (users.isEmpty()) {
            //if there is any users to load
            if (usersStrLine != null) {
                while (((usersStrLine = usersDat.readLine()) != null) && users.size() < frameSize) {
//                    1::F::1::10::48067
                    String[] splitedRecord = usersStrLine.split("::");
                    String userRecord
                            = "\"" + splitedRecord[0] + "\":{"
                            + "\"attributes\":{"
                            + "\"gender\": \"" + gender.get(splitedRecord[1]) + "\","
                            + "\"age\": \"" + splitedRecord[2] + "\","
                            + "\"occupation\": \"" + occupation.get(splitedRecord[3]) + "\""
                            + "}"
                            + "}";
                    users.add(userRecord);
                }
                return true;
            } else {
                //if no any users return false
                return false;
            }
        } else {
            //if not empty return true
            return true;
        }
    }

    /**
     * Get the next modification element from the list
     *
     * @return
     */
    @Override
    public String getNextUserModification() {
        return userModifications.poll();
    }

    /**
     *
     * Check if there is next element on user modification list
     *
     * @return True or False if there is next element
     * @throws IOException
     */
    private boolean userModificationHasNext() throws IOException {
        //if user is Empty refill user Modifications
        if (userModifications.isEmpty()) {

            //if there is any users to load
            if (ratingsStrLine != null) {

                while ((ratingsStrLine = ratingsDat.readLine()) != null) {
                    //1::1193::5::978300760
                    String[] splitedRecord = usersStrLine.split("::");
                    String userRecord
                            = "\"" + splitedRecord[0] + "\":{"
                            + "\"features\":{"
                            + "\"ftrname\": \"" + gender.get(splitedRecord[1]) + "\","
                            + "\"age\": \"" + splitedRecord[2] + "\","
                            + "\"occupation\": \"" + occupation.get(splitedRecord[3]) + "\""
                            + "}"
                            + "}";
                    userModifications.add(userRecord);
                }
                return true;
            } else {
                //if no any user modification return false
                return false;
            }

        } else {
            //if not empty return true
            return true;
        }
    }

    /**
     * Get a list with all usernames
     *
     * @return
     */
    @Override
    public List<String> getUsernamesList() {
        return usernames;
    }

    /**
     * Get random a Username
     *
     * @return A string with a random username
     */
    @Override
    public String getRandomUsername() {
        return usernames.get(r.nextInt(usernames.size()));
    }

    /**
     * Load dataset and return the bufferedReader
     *
     * @param filePath
     * @return
     */
    private BufferedReader lodDataSet(String filePath) {
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

}
