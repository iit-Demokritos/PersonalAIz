/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.iit.demokritos.pserverexperiments;

import gr.iit.demokritos.pserverexperiments.datasets.MovieLens1M;
import gr.iit.demokritos.pserverexperiments.interfaces.ILoadDataset;
import gr.iit.demokritos.pserverexperiments.interfaces.IStroreResults;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class BenchmarkTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.class);

    public static void main(String[] args) {

        //get properties
        Properties properties = configuration(args);

        //initialize variables
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        IStroreResults warehouse;
        Scheduler scheduler;
        String scenario2GetPropability = properties.getProperty("scenario2GetPropability", "30/100");
        String scenario = properties.getProperty("scenario", "2");
        String host = properties.getProperty("host", "192.168.0.1");
        String clientName = properties.getProperty("clientName", "root");
        String clientPass = properties.getProperty("clientPass", "root");
        int requestPerMin = Integer.parseInt(properties.getProperty("rpm", "0"));
        int loadedUsers = Integer.parseInt(properties.getProperty("loadedUsers", "0"));
        int batch = Integer.parseInt(properties.getProperty("batch", "10"));
        ILoadDataset dataset = new MovieLens1M(LOGGER,loadedUsers);

        //print arguments
        System.out.println("#====== Experiment settings ======#");
        System.out.println("Host: " + host);
        System.out.println("Client credencials: " + clientName+"/"+clientPass);
        System.out.println("Scenario: " + scenario);
        System.out.println("Request/Min: " + requestPerMin);
        System.out.println("Batch: " + batch);
        System.out.println("scenario2GetPropability: " + scenario2GetPropability);
        System.out.println("number of users: " + scenario2GetPropability);
        System.out.println("#====== Experiment settings ======#");

        //----------------------------------------------------------------------
        //Create scheduler no limit on request / min
        LOGGER.info("#Create scheduler with " + requestPerMin + " request/min: "
                + dateFormat.format(date.getTime()));

        scheduler = new Scheduler(dataset, requestPerMin, scenario2GetPropability, 
                LOGGER, batch, host, clientName, clientPass);

        //check and run the scenario
        switch (scenario) {
            case "1":
                //execute senario 1
                LOGGER.info("#Execute Scenario 1: "
                        + dateFormat.format(date.getTime()));
                scheduler.executeScenario1();
                break;
            case "2":
                //execute senario 2
                LOGGER.info("#Execute Scenario 2: "
                        + dateFormat.format(date.getTime()));
                scheduler.executeScenario2();
                break;
        }

    }

    public static Properties configuration(String[] args) {
        LOGGER.info("#Start export configuration");
        Properties properties = new Properties();

        for (String cArgument : args) {
            String[] tmpArg = cArgument.split("=");
            if (tmpArg.length > 1) {
                properties.setProperty(tmpArg[0], tmpArg[1]);
            }
        }
        LOGGER.info("#End export configuration");
        return properties;
    }

}
