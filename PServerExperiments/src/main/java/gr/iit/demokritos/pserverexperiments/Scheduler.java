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

import gr.iit.demokritos.pserverexperiments.interfaces.ILoadDataset;
import gr.iit.demokritos.pserverexperiments.interfaces.IStroreResults;
import gr.iit.demokritos.pserverexperiments.warehouse.CSVStoreResults;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private final IStroreResults warehouseScenario1;
    private final IStroreResults warehouseScenario2;
    private final int requestPerMinute;
    private final Random r = new Random();
    private final int fromPointer;
    private final int getPointer;
    private double getProb;
    private final Logger LOGGER;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final String host;
    private final String clientAuth;
//    private final String host = "http://gsoft.gr:8080";
//    private final String clientAuth = "CrashTestClient%7CqbwGoUOB14p";
    private final String mode = "personal";
    private final String URL;
    private HashMap<String, String> postParams;
    private HashMap<String, String> putParams;
    private HashMap<String, String> getParams;
    private final int batch;
    private final long timeSleep = 60000;
    private final long timeToThreadWaitInDays=5;

    //Constructor
    public Scheduler(ILoadDataset dataset, int requestPerMinute,
            String scenario2GetPropability, Logger LOGGER, int batch,
            String host, String clientName, String clientPass) {

        //Set settings
        this.host = "http://".concat(host).concat(":8080");
        this.clientAuth = clientName.concat("%7C").concat(clientPass);
        this.URL = this.host + "/PersonalAIz/api/pserver/" + this.clientAuth + "/" + this.mode + "/";
        this.batch = batch;
        this.dataset = dataset;
        this.requestPerMinute = requestPerMinute;
        String[] tmp = scenario2GetPropability.split("/");
        this.getPointer = Integer.parseInt(tmp[0]);
        this.fromPointer = Integer.parseInt(tmp[1]);
        this.getProb = (double) this.getPointer / (double) this.fromPointer;
        this.LOGGER = LOGGER;

        this.warehouseScenario1 = new CSVStoreResults(clientName + "_scenario1_"
                + requestPerMinute + "_" + batch);
        this.warehouseScenario2 = new CSVStoreResults(clientName + "_scenario2_"
                + requestPerMinute + "_" + batch + "_" + getPointer + "-" + fromPointer);
    }

    /**
     * Scenario 1: Add users, Modify Users Profile, Get users profile.
     */
    public void executeScenario1() {

        long startAddUsers, endAddUsers,
                startModifyUsers, endModifyUsers,
                startGetUserProfiles, endGetUserProfiles;

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
        startAddUsers = System.currentTimeMillis();
        LOGGER.info("#Start Add Users: "
                + dateFormat.format(startAddUsers));
        addUserResultTimes.add("#Start Add Users:" + startAddUsers);

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
                        this.JSONUsers = finalUser;
                        long startTime = 0, endTime = 0;

                        try {
                            String addURL = URL + "users";
                            postParams = new HashMap<>();
                            postParams.put("JSONUsers", JSONUsers);
                            startTime = System.currentTimeMillis();
                            execPost(addURL, postParams);
                            endTime = System.currentTimeMillis();
                        } catch (Exception ex) {
                            LOGGER.error("Add Users Failed", ex);
                        }
//                        System.out.println("scenario1::"
//                                + "adduser::"
//                                + startTime + "::"
//                                + endTime + "::"
//                                + ((endTime - startTime) / 1000));
                        synchronized (addUserResultTimes) {
                            addUserResultTimes.add("scenario1::"
                                    + "adduser::"
                                    + startTime + "::"
                                    + endTime + "::"
                                    + ((endTime - startTime) / 1000));
                        }

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

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 1 min
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            batchCounter++;
            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 100) {
//                break;
//            }
        }

//        //DEBUG LINES
//        if (JSONUsers.endsWith(",")) {
//            JSONUsers = JSONUsers.substring(0, JSONUsers.length() - 1);
//        }
        //If there is users to add call adduser
        if (JSONUsers.length() > 2) {
            long startTime = 0, endTime = 0;
            JSONUsers = JSONUsers + "}";
            LOGGER.debug(JSONUsers);
            startTime = System.currentTimeMillis();
            addUsers(JSONUsers);
            endTime = System.currentTimeMillis();
            addUserResultTimes.add("scenario1::"
                    + "adduser::"
                    + startTime + "::"
                    + endTime + "::"
                    + ((endTime - startTime) / 1000));
        }

        //Shutdown threads
        addUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            addUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endAddUsers = System.currentTimeMillis();
        LOGGER.info("#End Add Users: "
                + dateFormat.format(endAddUsers));
        //Add End time of this phase
        addUserResultTimes.add("#End Add Users:" + endAddUsers);
        addUserResultTimes.add("#Duration Add Users:"
                + ((endAddUsers - startAddUsers) / 1000) + " sec");
        System.out.println("#Duration Add Users:"
                + ((endAddUsers - startAddUsers) / 1000) + " sec");
        //Store results for add user
        warehouseScenario1.storeData(addUserResultTimes);

        //----------------------------------------------------------------------
        //Modify users profile
        startModifyUsers = System.currentTimeMillis();
        LOGGER.info("#Start Modify Users Profile: "
                + dateFormat.format(startModifyUsers));
        modifyUserResultTimes.add("#Start Modify Users Profile:" + startModifyUsers);

        requestCounter = 1;
        while (dataset.userModificationHasNext()) {

            String modificationRedord = dataset.getNextUserModification();
            final String finalRecord = modificationRedord;
            modifyUsersEx.submit(new Runnable() {

                String modificationRedord;

                @Override
                public void run() {
                    this.modificationRedord = finalRecord;
                    long startTime = 0, endTime = 0;
                    String[] splitedRecord = modificationRedord.split("\\|");
                    try {
                        String modifyURL = URL + "users/" + splitedRecord[0] + "/features/modify";
                        putParams = new HashMap<>();
                        putParams.put("JSONUserFeatures", splitedRecord[1]);
                        startTime = System.currentTimeMillis();
                        execPut(modifyURL, putParams);
                        endTime = System.currentTimeMillis();
                    } catch (Exception ex) {
                        LOGGER.error("Modify Users Failed", ex);
                    }

//                    System.out.println("scenario1::"
//                            + "modifyuser::"
//                            + startTime + "::"
//                            + endTime + "::"
//                            + ((endTime - startTime) / 1000));
                    synchronized (modifyUserResultTimes) {
                        modifyUserResultTimes.add("scenario1::"
                                + "modifyuser::"
                                + startTime + "::"
                                + endTime + "::"
                                + ((endTime - startTime) / 1000));
                    }
                }
            });

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 10 sec
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 300) {
//                break;
//            }
        }

        //Shutdown threads
        modifyUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            modifyUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endModifyUsers = System.currentTimeMillis();
        LOGGER.info("#End Modify Users Profile: "
                + dateFormat.format(endModifyUsers));
        //Add end time of Modify users and store times
        modifyUserResultTimes.add("#End Modify Users Profile:" + endModifyUsers);
        modifyUserResultTimes.add("#Duration Modify Users Profile:"
                + ((endModifyUsers - startModifyUsers) / 1000) + " sec");
        System.out.println("#Duration Modify Users Profile:"
                + ((endModifyUsers - startModifyUsers) / 1000) + " sec");
        warehouseScenario1.storeData(modifyUserResultTimes);

        //----------------------------------------------------------------------
        //Get users profile
        startGetUserProfiles = System.currentTimeMillis();
        LOGGER.info("#Start Get Users Profile: "
                + dateFormat.format(startGetUserProfiles));
        getUserResultTimes.add("#Start Get Users Profile: " + startGetUserProfiles);

        ArrayList<String> users = new ArrayList<>(dataset.getUsernamesList());

        requestCounter = 1;

        for (String cUsername : users) {

            final String finalRecord = cUsername;
            getUsersEx.submit(new Runnable() {

                String username;

                @Override
                public void run() {
                    this.username = finalRecord;
                    long startTime = 0, endTime = 0;
                    try {
                        String getURL = URL + "users/" + username + "/features";
                        startTime = System.currentTimeMillis();
                        execGet(getURL, getParams);
                        endTime = System.currentTimeMillis();
                    } catch (Exception ex) {
                        LOGGER.error("Get Users Failed", ex);
                    }

//                    System.out.println("scenario1::"
//                            + "getuser::"
//                            + startTime + "::"
//                            + endTime + "::"
//                            + ((endTime - startTime) / 1000));
                    synchronized (getUserResultTimes) {
                        getUserResultTimes.add("scenario1::"
                                + "getuser::"
                                + startTime + "::"
                                + endTime + "::"
                                + ((endTime - startTime) / 1000));
                    }
                }
            });

            try {
                String getURL = URL + "users/" + cUsername + "/features";
                execGet(getURL, getParams);
            } catch (Exception ex) {
                LOGGER.error("Get Users Failed", ex);
            }

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 10 sec
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 300) {
//                break;
//            }
        }

        //Shutdown threads
        getUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            getUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endGetUserProfiles = System.currentTimeMillis();
        LOGGER.info("#End Get Users Profile: "
                + dateFormat.format(endGetUserProfiles));
        //Add end time of get users profile and store times
        getUserResultTimes.add("#End Get Users Profile:" + endGetUserProfiles);
        getUserResultTimes.add("#Duration Get Users Profile:"
                + ((endGetUserProfiles - startGetUserProfiles) / 1000) + " sec");
        System.out.println("#Duration Get Users Profile:"
                + ((endGetUserProfiles - startGetUserProfiles) / 1000) + " sec");
        warehouseScenario1.storeData(getUserResultTimes);

    }

    /**
     * Scenario 2: Add users, (Modify Users Profile, Get users profile) with
     * possibility, Get Users profile.
     */
    public void executeScenario2() {

        long startAddUsers, endAddUsers,
                startModifyUsers, endModifyUsers,
                startGetUserProfiles, endGetUserProfiles;

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
        startAddUsers = System.currentTimeMillis();
        LOGGER.info("#Start Add Users: "
                + dateFormat.format(startAddUsers));
        addUserResultTimes.add("#Start Add Users:" + startAddUsers);

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
                        this.JSONUsers = finalUser;
                        long startTime = 0, endTime = 0;

                        try {
                            String addURL = URL + "users";
                            postParams = new HashMap<>();
                            postParams.put("JSONUsers", JSONUsers);
                            startTime = System.currentTimeMillis();
                            execPost(addURL, postParams);
                            endTime = System.currentTimeMillis();
                        } catch (Exception ex) {
                            LOGGER.error("Add Users Failed", ex);
                        }
//                        System.out.println("scenario2::"
//                                + "adduser::"
//                                + startTime + "::"
//                                + endTime + "::"
//                                + ((endTime - startTime) / 1000));
                        synchronized (addUserResultTimes) {
                            addUserResultTimes.add("scenario2::"
                                    + "adduser::"
                                    + startTime + "::"
                                    + endTime + "::"
                                    + ((endTime - startTime) / 1000));
                        }

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

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 10 sec
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            batchCounter++;
            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 50) {
//                break;
//            }
        }

//        //DEBUG LINES
//        if (JSONUsers.endsWith(",")) {
//            JSONUsers = JSONUsers.substring(0, JSONUsers.length() - 1);
//        }
        //If there is users to add call adduser
        if (JSONUsers.length() > 2) {
            long startTime = 0, endTime = 0;
            JSONUsers = JSONUsers + "}";
            LOGGER.debug(JSONUsers);
            startTime = System.currentTimeMillis();
            addUsers(JSONUsers);
            endTime = System.currentTimeMillis();
            addUserResultTimes.add("scenario2::"
                    + "adduser::"
                    + startTime + "::"
                    + endTime + "::"
                    + ((endTime - startTime) / 1000));
        }

        //Shutdown threads
        addUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            addUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endAddUsers = System.currentTimeMillis();
        LOGGER.info("#End Add Users: "
                + dateFormat.format(endAddUsers));
        //Add End time of this phase
        addUserResultTimes.add("#End Add Users:" + endAddUsers);
        addUserResultTimes.add("#Duration Add Users:"
                + ((endAddUsers - startAddUsers) / 1000) + " sec");
        System.out.println("#Duration Add Users:"
                + ((endAddUsers - startAddUsers) / 1000) + " sec");
        //Store results for add user
        warehouseScenario2.storeData(addUserResultTimes);

        //----------------------------------------------------------------------
        //Modify users profile
        startModifyUsers = System.currentTimeMillis();
        LOGGER.info("#Start Modify Users Profile with probability to get user: "
                + dateFormat.format(startModifyUsers));
        modifyUserResultTimes.add("#Start Modify Users Profile:" + startModifyUsers);

        requestCounter = 1;

        while (dataset.userModificationHasNext()) {

            String modificationRedord = dataset.getNextUserModification();
            final String finalRecord = modificationRedord;

            //if Propability execute get user
//            if (r.nextInt(fromPointer) <= getPointer) {
            if (r.nextDouble() <= getProb) {

                final String finalGetUser = dataset.getRandomUsername();
                modifyUsersEx.submit(new Runnable() {

                    String username;

                    @Override
                    public void run() {
                        this.username = finalGetUser;
                        long startTime = 0, endTime = 0;
                        try {
                            String getURL = URL + "users/" + username + "/features";
                            startTime = System.currentTimeMillis();
                            execGet(getURL, getParams);
                            endTime = System.currentTimeMillis();
                        } catch (Exception ex) {
                            LOGGER.error("Get Users Failed", ex);
                        }
//                        System.out.println("scenario2::"
//                                + "getuseronmodify::"
//                                + startTime + "::"
//                                + endTime + "::"
//                                + ((endTime - startTime) / 1000));
                        synchronized (modifyUserResultTimes) {
                            modifyUserResultTimes.add("scenario2::"
                                    + "getuseronmodify::"
                                    + startTime + "::"
                                    + endTime + "::"
                                    + ((endTime - startTime) / 1000));
                        }
                    }
                });
            } else {

                modifyUsersEx.submit(new Runnable() {

                    String modificationRedord;

                    @Override
                    public void run() {
                        this.modificationRedord = finalRecord;
                        long startTime = 0, endTime = 0;
                        String[] splitedRecord = modificationRedord.split("\\|");
                        try {
                            String modifyURL = URL + "users/" + splitedRecord[0] + "/features/modify";
                            putParams = new HashMap<>();
                            putParams.put("JSONUserFeatures", splitedRecord[1]);
                            startTime = System.currentTimeMillis();
                            execPut(modifyURL, putParams);
                            endTime = System.currentTimeMillis();
                        } catch (Exception ex) {
                            LOGGER.error("Add Users Failed", ex);
                        }
//                    System.out.println("scenario2::"
//                            + "modifyuser::"
//                            + startTime + "::"
//                            + endTime + "::"
//                            + ((endTime - startTime) / 1000));
                        synchronized (modifyUserResultTimes) {
                            modifyUserResultTimes.add("scenario2::"
                                    + "modifyuser::"
                                    + startTime + "::"
                                    + endTime + "::"
                                    + ((endTime - startTime) / 1000));
                        }
                    }
                });

            }

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 10 sec
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 50) {
//                break;
//            }
        }

        //Shutdown threads
        modifyUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            modifyUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endModifyUsers = System.currentTimeMillis();
        LOGGER.info("#End Modify Users Profile: "
                + dateFormat.format(endModifyUsers));
        //Add end time of Modify users and store times
        modifyUserResultTimes.add("#End Modify Users Profile:" + endModifyUsers);
        modifyUserResultTimes.add("#Duration Modify Users Profile:"
                + ((endModifyUsers - startModifyUsers) / 1000) + " sec");
        System.out.println("#Duration Modify Users Profile:"
                + ((endModifyUsers - startModifyUsers) / 1000) + " sec");
        warehouseScenario2.storeData(modifyUserResultTimes);
        //----------------------------------------------------------------------
        //Get users profile
        startGetUserProfiles = System.currentTimeMillis();
        LOGGER.info("#Start Get Users Profile: "
                + dateFormat.format(startGetUserProfiles));
        getUserResultTimes.add("#Start Get Users Profile: " + startGetUserProfiles);

        ArrayList<String> users = new ArrayList<>(dataset.getUsernamesList());

        requestCounter = 1;
        for (String cUsername : users) {

            final String finalRecord = cUsername;
            getUsersEx.submit(new Runnable() {

                String username;

                @Override
                public void run() {
                    this.username = finalRecord;
                    long startTime = 0, endTime = 0;
                    try {
                        String getURL = URL + "users/" + username + "/features";
                        startTime = System.currentTimeMillis();
                        execGet(getURL, getParams);
                        endTime = System.currentTimeMillis();
                    } catch (Exception ex) {
                        LOGGER.error("Get Users Failed", ex);
                    }
//                    System.out.println("scenario2::"
//                            + "getuser::"
//                            + startTime + "::"
//                            + endTime + "::"
//                            + ((endTime - startTime) / 1000));
                    synchronized (getUserResultTimes) {
                        getUserResultTimes.add("scenario2::"
                                + "getuser::"
                                + startTime + "::"
                                + endTime + "::"
                                + ((endTime - startTime) / 1000));
                    }
                }
            });

            try {
                String getURL = URL + "users/" + cUsername + "/features";
                execGet(getURL, getParams);
            } catch (Exception ex) {
                LOGGER.error("Get Users Failed", ex);
            }

            if (requestPerMinute != 0) {
                if ((requestCounter % requestPerMinute) == 0) {
                    try {
                        //sleep for 10 sec
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            requestCounter++;

//            //DEBUG LINES
//            if (requestCounter > 50) {
//                break;
//            }
        }

        //Shutdown threads
        getUsersEx.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            getUsersEx.awaitTermination(timeToThreadWaitInDays, TimeUnit.DAYS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
        }

        endGetUserProfiles = System.currentTimeMillis();
        LOGGER.info("#End Get Users Profile: "
                + dateFormat.format(endGetUserProfiles));
        //Add end time of get users profile and store times
        getUserResultTimes.add("#End Get Users Profile:" + endGetUserProfiles);
        getUserResultTimes.add("#Duration Get Users Profile:"
                + ((endGetUserProfiles - startGetUserProfiles) / 1000) + " sec");
        System.out.println("#Duration Get Users Profile:"
                + ((endGetUserProfiles - startGetUserProfiles) / 1000) + " sec");
        warehouseScenario2.storeData(getUserResultTimes);
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
