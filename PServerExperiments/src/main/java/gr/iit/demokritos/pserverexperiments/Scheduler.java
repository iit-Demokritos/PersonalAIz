/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments;

import gr.iit.demokritos.pserverexperiments.interfaces.ILoadDataset;
import gr.iit.demokritos.pserverexperiments.interfaces.IStroreResults;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Scheduler {

    //variables
    private final ILoadDataset dataset;
    private final IStroreResults warehouse;
    private final int requestPerMinute;
    private final Random r = new Random();
    private final int fromPointer;
    private final int getPointer;
    private final Logger LOGGER;
    private final Date date = new Date();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final String host = "http://localhost:8080";
    private final String clientAuth = "root2%7Croot2";
//    private final String host = "http://gsoft.gr:8080";
//    private final String clientAuth = "CrashTestClient%7CqbwGoUOB14p";
    private final String mode = "personal";
    private final String URL = host + "/PersonalAIz/api/pserver/" + clientAuth + "/" + mode + "/";
    private HashMap<String, String> postParams;
    private HashMap<String, String> putParams;
    private HashMap<String, String> getParams;
    private final int batch;
    private final long timeSleep = 10000;

    //Constructor
    public Scheduler(ILoadDataset dataset, IStroreResults warehouse,
            int requestPerMinute, String scenario2GetPropability, Logger LOGGER, int batch) {

        //Set settings
        this.batch = batch;
        this.dataset = dataset;
        this.warehouse = warehouse;
        this.requestPerMinute = requestPerMinute;
        String[] tmp = scenario2GetPropability.split("/");
        this.getPointer = Integer.parseInt(tmp[0]);
        this.fromPointer = Integer.parseInt(tmp[1]);
        this.LOGGER = LOGGER;
    }

    /**
     * Scenario 1: Add users, Modify Users Profile, Get users profile.
     */
    public void executeScenario1() {

        //Create executor service with a thread pool with all available processors
        ExecutorService addUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        //Create executor service with a thread pool with all available processors
        ExecutorService modifyUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        //Create executor service with a thread pool with all available processors
        ExecutorService getUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        final List<String> addUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());
        final List<String> modifyUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());
        final List<String> getUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());

        //Add users 
        LOGGER.info("#Start Add Users: "
                + dateFormat.format(date.getTime()));

        int requestCounter = 1;
        int batchCounter = 0;
        String JSONUsers = "{";
        while (dataset.userHasNext()) {

            if (batchCounter == batch) {

                JSONUsers = JSONUsers + dataset.getNextUser();
                JSONUsers = JSONUsers + "}";
                final String finalUser = JSONUsers;
                addUsersEx.submit(new Runnable() {

                    String JSONUsers;

                    @Override
                    public void run() {
                        Date date = new Date();
                        this.JSONUsers = finalUser;
                        long startTime = 0, endTime = 0;

                        try {
                            String addURL = URL + "users";
                            postParams = new HashMap<>();
                            postParams.put("JSONUsers", JSONUsers);
                            startTime = date.getTime();
                            execPost(addURL, postParams);
                            endTime = date.getTime();
                        } catch (Exception ex) {
                            LOGGER.error("Add Users Failed", ex);
                        }
                        System.out.println("scenario1::"
                                + "adduser::"
                                + requestPerMinute + "::"
                                + startTime + "::"
                                + endTime);

                    }
                });
                batchCounter = 0;
                JSONUsers = "{";
            } else {
                JSONUsers = JSONUsers + dataset.getNextUser();
                if (dataset.userHasNext()) {
                    JSONUsers = JSONUsers + ",";
                }
            }

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            batchCounter++;
            requestCounter++;

//            //DEBUG LINES
//            if (addUsersCount == 25) {
//                break;
//            }
        }

//        //DEBUG LINES
//        if (JSONUsers.endsWith(",")) {
//            JSONUsers = JSONUsers.substring(0, JSONUsers.length() - 1);
//        }
        //If there is users to add call adduser
        if (JSONUsers.length() > 2) {
            JSONUsers = JSONUsers + "}";
            LOGGER.debug(JSONUsers);
            addUsers(JSONUsers);
        }

        //Shutdown threads
        addUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            addUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        //----------------------------------------------------------------------
        //Modify users profile
        LOGGER.info("#Start Modify Users Profile: "
                + dateFormat.format(date.getTime()));

        requestCounter = 1;
        while (dataset.userModificationHasNext()) {

            String modificationRedord = dataset.getNextUserModification();
            final String finalRecord = modificationRedord;
            modifyUsersEx.submit(new Runnable() {

                String modificationRedord;

                @Override
                public void run() {
                    Date date = new Date();
                    this.modificationRedord = finalRecord;
                    long startTime = 0, endTime = 0;
                    String[] splitedRecord = modificationRedord.split("\\|");
                    try {
                        String modifyURL = URL + "users/" + splitedRecord[0] + "/features/modify";
                        putParams = new HashMap<>();
                        putParams.put("JSONUserFeatures", splitedRecord[1]);
                        startTime = date.getTime();
                        execPut(modifyURL, putParams);
                        endTime = date.getTime();
                    } catch (Exception ex) {
                        LOGGER.error("Modify Users Failed", ex);
                    }

                    System.out.println("scenario1::"
                            + "modifyuser::"
                            + requestPerMinute + "::"
                            + startTime + "::"
                            + endTime);
                }
            });

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (userMofificationsCounter > 50) {
//                break;
//            }
        }

        //Shutdown threads
        modifyUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            modifyUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        //----------------------------------------------------------------------
        //Get users profile
        LOGGER.info("#Start Get Users Profile: "
                + dateFormat.format(date.getTime()));

        ArrayList<String> users = new ArrayList<>(dataset.getUsernamesList());

        requestCounter = 1;

        for (String cUsername : users) {

            final String finalRecord = cUsername;
            getUsersEx.submit(new Runnable() {

                String username;

                @Override
                public void run() {
                    Date date = new Date();
                    this.username = finalRecord;
                    long startTime = 0, endTime = 0;
                    try {
                        String getURL = URL + "users/" + username + "/features";
                        startTime = date.getTime();
                        execGet(getURL, getParams);
                        endTime = date.getTime();
                    } catch (Exception ex) {
                        LOGGER.error("Get Users Failed", ex);
                    }

                    System.out.println("scenario1::"
                            + "getuser::"
                            + requestPerMinute + "::"
                            + startTime + "::"
                            + endTime);
                }
            });

            try {
                String getURL = URL + "users/" + cUsername + "/features";
                execGet(getURL, getParams);
            } catch (Exception ex) {
                LOGGER.error("Get Users Failed", ex);
            }

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (getUsersCounter > 25) {
//                break;
//            }
        }

        //Shutdown threads
        getUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            getUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }
    }

    /**
     * Scenario 2: Add users, (Modify Users Profile, Get users profile) with
     * possibility, Get Users profile.
     */
    public void executeScenario2() {

        //Create executor service with a thread pool with all available processors
        ExecutorService addUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        //Create executor service with a thread pool with all available processors
        ExecutorService modifyUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        //Create executor service with a thread pool with all available processors
        ExecutorService getUsersEx = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        final List<String> addUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());
        final List<String> modifyUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());
        final List<String> getUserResultTimes
                = Collections.synchronizedList(
                        new ArrayList<String>());

        //Add users 
        LOGGER.info("#Start Add Users: "
                + dateFormat.format(date.getTime()));

        int requestCounter = 1;
        int batchCounter = 0;
        String JSONUsers = "{";

        while (dataset.userHasNext()) {

            if (batchCounter == batch) {

                JSONUsers = JSONUsers + dataset.getNextUser();
                JSONUsers = JSONUsers + "}";
                final String finalUser = JSONUsers;
                addUsersEx.submit(new Runnable() {

                    String JSONUsers;

                    @Override
                    public void run() {
                        Date date = new Date();
                        this.JSONUsers = finalUser;
                        long startTime = 0, endTime = 0;

                        try {
                            String addURL = URL + "users";
                            postParams = new HashMap<>();
                            postParams.put("JSONUsers", JSONUsers);
                            execPost(addURL, postParams);
                        } catch (Exception ex) {
                            LOGGER.error("Add Users Failed", ex);
                        }
                        //                    synchronized (addUserResultTimes) {
                        //                        addUserResultTimes.add(Long.toString(startTime-endTime));
                        //                    }
                    }
                });
                batchCounter = 0;
                JSONUsers = "{";
            } else {
                JSONUsers = JSONUsers + dataset.getNextUser();
                if (dataset.userHasNext()) {
                    JSONUsers = JSONUsers + ",";
                }
            }

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            batchCounter++;
            requestCounter++;

//            //DEBUG LINES
//            if (addUsersCount == 25) {
//                break;
//            }
        }

//        //DEBUG LINES
//        if (JSONUsers.endsWith(",")) {
//            JSONUsers = JSONUsers.substring(0, JSONUsers.length() - 1);
//        }
        //If there is users to add call adduser
        if (JSONUsers.length() > 2) {
            JSONUsers = JSONUsers + "}";
            LOGGER.debug(JSONUsers);
            addUsers(JSONUsers);
        }

        //Shutdown threads
        addUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            addUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        //----------------------------------------------------------------------
        //Modify users profile
        LOGGER.info("#Start Modify Users Profile with probability to get user: "
                + dateFormat.format(date.getTime()));

        requestCounter = 1;

        while (dataset.userModificationHasNext()) {

            String modificationRedord = dataset.getNextUserModification();
            final String finalRecord = modificationRedord;

            //if Propability execute get user
            if (r.nextInt(fromPointer) <= getPointer) {
                requestCounter++;

                final String finalGetUser = dataset.getRandomUsername();
                modifyUsersEx.submit(new Runnable() {

                    String username;

                    @Override
                    public void run() {
                        Date date = new Date();
                        this.username = finalGetUser;
                        long startTime = 0, endTime = 0;
                        try {
                            String getURL = URL + "users/" + username + "/features";
                            execGet(getURL, getParams);
                        } catch (Exception ex) {
                            LOGGER.error("Get Users Failed", ex);
                        }
                        //                    synchronized (addUserResultTimes) {
                        //                        addUserResultTimes.add(Long.toString(startTime-endTime));
                        //                    }
                    }
                });
            }

            modifyUsersEx.submit(new Runnable() {

                String modificationRedord;

                @Override
                public void run() {
                    Date date = new Date();
                    this.modificationRedord = finalRecord;
                    long startTime = 0, endTime = 0;
                    String[] splitedRecord = modificationRedord.split("\\|");
                    try {
                        String modifyURL = URL + "users/" + splitedRecord[0] + "/features/modify";
                        putParams = new HashMap<>();
                        putParams.put("JSONUserFeatures", splitedRecord[1]);
                        execPut(modifyURL, putParams);
                    } catch (Exception ex) {
                        LOGGER.error("Add Users Failed", ex);
                    }
                    //                    synchronized (addUserResultTimes) {
                    //                        addUserResultTimes.add(Long.toString(startTime-endTime));
                    //                    }
                }
            });

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (userMofificationsCounter > 50) {
//                break;
//            }
        }

        //Shutdown threads
        modifyUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            modifyUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        //----------------------------------------------------------------------
        //Get users profile
        LOGGER.info("#Start Get Users Profile: "
                + dateFormat.format(date.getTime()));

        ArrayList<String> users = new ArrayList<>(dataset.getUsernamesList());

        requestCounter = 1;
        for (String cUsername : users) {

            final String finalRecord = cUsername;
            getUsersEx.submit(new Runnable() {

                String username;

                @Override
                public void run() {
                    Date date = new Date();
                    this.username = finalRecord;
                    long startTime = 0, endTime = 0;
                    try {
                        String getURL = URL + "users/" + username + "/features";
                        execGet(getURL, getParams);
                    } catch (Exception ex) {
                        LOGGER.error("Get Users Failed", ex);
                    }
                    //                    synchronized (addUserResultTimes) {
                    //                        addUserResultTimes.add(Long.toString(startTime-endTime));
                    //                    }
                }
            });

            try {
                String getURL = URL + "users/" + cUsername + "/features";
                execGet(getURL, getParams);
            } catch (Exception ex) {
                LOGGER.error("Get Users Failed", ex);
            }

            if ((requestCounter % requestPerMinute) == 0 && requestPerMinute != 0) {
                try {
                    //sleep for 10 sec
                    Thread.sleep(timeSleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (getUsersCounter > 25) {
//                break;
//            }
        }

        //Shutdown threads
        getUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            getUsersEx.awaitTermination((long) 1.0, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }
    }

    private void addUsers(String JSONUsers) {
        try {
            String addURL = URL + "users";
            postParams = new HashMap<>();
            postParams.put("JSONUsers", JSONUsers);
            execPost(addURL, postParams);
        } catch (Exception ex) {
            LOGGER.error("Add Users Failed", ex);
        }

    }

    //--------------------------------------------------------------------------
    /**
     *
     * @param baseURL
     * @param getParams
     * @return
     * @throws Exception
     */
    private void execGet(String baseURL,
            Map<String, String> getParams) throws Exception {

        String url = baseURL;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        // add header
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.7.12)");

        HttpResponse response = client.execute(get);
    }

    /**
     *
     * @param baseURL
     * @param postParams
     * @return
     * @throws Exception
     */
    private void execPost(String baseURL,
            Map<String, String> postParams) throws Exception {

        String url = baseURL;

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.7.12)");

        List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();

        for (Map.Entry<String, String> entry : postParams.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // append params
            urlParameters.add(new BasicNameValuePair(key, val));
        }

        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

        HttpResponse response = client.execute(post);
    }

    /**
     *
     * @param baseURL
     * @param putParams
     * @return
     * @throws Exception
     */
    private void execPut(String baseURL,
            Map<String, String> putParams) throws Exception {

        String url = baseURL;

        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(url);
        // add header
        put.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.7.12)");

        List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();

        for (Map.Entry<String, String> entry : putParams.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // append params
            urlParameters.add(new BasicNameValuePair(key, val));
        }

        put.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

        HttpResponse response = client.execute(put);
    }

}
