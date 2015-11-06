/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments.datasets;

import gr.iit.demokritos.pserverexperiments.interfaces.ILoadDataset;
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
import java.util.logging.Level;
import org.slf4j.Logger;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class MovieLens1M implements ILoadDataset {

    private final Logger LOGGER;
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
    private boolean initUsersFlag=true;
    private boolean initUsersModificationFlag=true;
    
    private final String datUsers="datasets/ml-1m/users.dat";
    private final String datRatings="datasets/ml-1m/ratings.dat";
    private final String datMovies="datasets/ml-1m/movies.dat";

    public MovieLens1M(Logger LOGGER) {

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
        this.LOGGER = LOGGER;

        try {
            //Get BufferedReaders
            this.usersDat = lodDataSet(datUsers);
            this.ratingsDat = lodDataSet(datRatings);
            //load movies
            loadMovies();
            //load usernames
            loadUsernames();
            //Fill users first frame
            userHasNext();
            //Fill userModifications first frame
            userModificationHasNext();
            
        } catch (IOException ex) {
            LOGGER.error("Error on Dataset: ", ex);
        }
    }

    private void loadUsernames() throws IOException {
        BufferedReader usernamesDat = lodDataSet(datUsers);
        String moviesStrLine;
        while ((moviesStrLine = usernamesDat.readLine()) != null) {
            String[] splitedRecord = moviesStrLine.split("::");
            usernames.add(splitedRecord[0]);
        }
    }
    private void loadMovies() throws IOException {

        BufferedReader moviesDat = lodDataSet(datMovies);
        String moviesStrLine;
        while ((moviesStrLine = moviesDat.readLine()) != null) {
            //1::Toy Story (1995)::Animation|Children's|Comedy
            String[] splitedRecord = moviesStrLine.split("::");
            ArrayList<String> features = new ArrayList<>();
            String[] splitedGenres = splitedRecord[2].split("\\|");
            for (String splitedGenre : splitedGenres) {
                features.add("genre." + splitedGenre);
            }
            movies.put(splitedRecord[0], features);
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
     */
    @Override
    public boolean userHasNext() {
        try {
            //if user is Empty refill users
            if (users.isEmpty()) {
                //if there is any users to load
                if (usersStrLine != null || initUsersFlag) {
                    initUsersFlag=false;
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
        } catch (IOException ex) {
            LOGGER.error("Error on user has next:", ex);
            return false;
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
     */
    @Override
    public boolean userModificationHasNext() {
        try {
            //if user is Empty refill user Modifications
            if (userModifications.isEmpty()) {

                //if there is any users to load
                if (ratingsStrLine != null || initUsersModificationFlag) {
                    initUsersModificationFlag=false;
                    while (((ratingsStrLine = ratingsDat.readLine()) != null) && userModifications.size() < frameSize) {
                        //1::1193::5::978300760
                        String[] splitedRecord = ratingsStrLine.split("::");
                        String userModificationRecord
                                = splitedRecord[0] + "|"
                                + "{";

                        for (String cFeature : movies.get(splitedRecord[2])) {
                            userModificationRecord = userModificationRecord
                                    + "\"" + cFeature + "\": \"" + splitedRecord[2] + "\",";
                        }
                        
                        userModificationRecord = userModificationRecord
                                + "\"movie." + splitedRecord[1] + "\": \"" + splitedRecord[2] + "\""
                                + "}";
                        
                        userModifications.add(userModificationRecord);
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
        } catch (IOException ex) {
            LOGGER.error("Error on user modification has next:", ex);
            return false;
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
