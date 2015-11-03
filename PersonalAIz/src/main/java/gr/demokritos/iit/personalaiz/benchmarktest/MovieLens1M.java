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
    private BufferedReader moviesDat;
    private String moviesStrLine;

    public MovieLens1M() {

        //Get BufferedReaders
        this.usersDat = lodDataSet("");
        this.ratingsDat = lodDataSet("");
        this.moviesDat = lodDataSet("");
        //Fill usernames
        //Fill users first 1M
        //Fill userModifications first 1M
    }

    @Override
    public String getNextUser() {
        String user = "";
        user = users.poll();
        return user;
    }

    private boolean userHasNext() throws IOException {
        //if user is Empty refill users
        if (users.isEmpty()) {

            //if there is any users to load
            if (usersStrLine != null) {

                while ((usersStrLine = usersDat.readLine()) != null) {

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

    @Override
    public String getNextUserModification() {
        String userModification = "";

        return userModification;
    }

    private boolean userModificationHasNext() {

    }

    @Override
    public List<String> getUsernamesList() {
        return usernames;
    }

    @Override
    public String getRandomUsername() {
        String username = "";

        return username;
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
