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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class BenchmarkTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.class);

    public static void main(String[] args) {

        //initialize variables
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ILoadDataset dataset = new MovieLens1M(LOGGER);
        IStroreResults warehouse;
        Scheduler scheduler;
        String scenario2GetPropability = "8/10";

        //----------------------------------------------------------------------
        //Create scheduler no limit on request / min
        LOGGER.info("#Create scheduler with 2 request/min: "
                + dateFormat.format(date.getTime()));

        scheduler = new Scheduler(dataset, 0, scenario2GetPropability, LOGGER, 10);
        //execute senario 1
        LOGGER.info("#Execute Scenario 1: "
                + dateFormat.format(date.getTime()));
        scheduler.executeScenario1();
        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();

//        //----------------------------------------------------------------------
//        //Create scheduler 2 request / min
//        LOGGER.info("#Create scheduler with 2 request/min: "
//                + dateFormat.format(date.getTime()));
//
//        scheduler = new Scheduler(dataset, warehouse,
//                2, scenario2GetPropability, LOGGER, 20);
//        //execute senario 1
//        LOGGER.info("#Execute Scenario 1: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario1();
//        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();
//        //----------------------------------------------------------------------
//        //Create scheduler 4 request / min
//        LOGGER.info("#Create scheduler with 4 request/min: "
//                + dateFormat.format(date.getTime()));
//        scheduler = new Scheduler(dataset, warehouse,
//                4, scenario2GetPropability, LOGGER);
//        //execute senario 1
//        LOGGER.info("#Execute Scenario 1: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario1();
//        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();
//
//        //----------------------------------------------------------------------
//        //Create scheduler 8 request / min
//        LOGGER.info("#Create scheduler with 8 request/min: "
//                + dateFormat.format(date.getTime()));
//        scheduler = new Scheduler(dataset, warehouse,
//                8, scenario2GetPropability, LOGGER);
//        //execute senario 1
//        LOGGER.info("#Execute Scenario 1: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario1();
//        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();
//
//        //----------------------------------------------------------------------
//        //Create scheduler 16 request / min
//        LOGGER.info("#Create scheduler with 16 request/min: "
//                + dateFormat.format(date.getTime()));
//        scheduler = new Scheduler(dataset, warehouse,
//                16, scenario2GetPropability, LOGGER);
//        //execute senario 1
//        LOGGER.info("#Execute Scenario 1: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario1();
//        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();
//
//        //----------------------------------------------------------------------
//        //Create scheduler 32 request / min
//        LOGGER.info("#Create scheduler with 32 request/min: "
//                + dateFormat.format(date.getTime()));
//        scheduler = new Scheduler(dataset, warehouse,
//                32, scenario2GetPropability, LOGGER);
//        //execute senario 1
//        LOGGER.info("#Execute Scenario 1: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario1();
//        //execute senario 2
//        LOGGER.info("#Execute Scenario 2: "
//                + dateFormat.format(date.getTime()));
//        scheduler.executeScenario2();
    }
}
