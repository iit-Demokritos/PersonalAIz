/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.benchmarktest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class BenchmarkTest {

    public static void main(String[] args) {

        //initialize variables
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ILoadDataset dataset = new MovieLens1M();
        IStroreResults warehouse = new CSVStoreResults();
        Scheduler scheduler;
        String scenario2GetPropability = "1/10";
        

        //----------------------------------------------------------------------
        //Create scheduler 2 request / min
        System.out.println("#Start Book Crossing experiment: "
                + dateFormat.format(date.getTime()));

        scheduler = new Scheduler(dataset, warehouse,
                2, scenario2GetPropability);
        //execute senario 1
        scheduler.executeScenario1();
        //execute senario 2
        scheduler.executeScenario2();

        //----------------------------------------------------------------------
        //Create scheduler 4 request / min
        scheduler = new Scheduler(dataset, warehouse,
                4, scenario2GetPropability);
        //execute senario 1
        scheduler.executeScenario1();
        //execute senario 2
        scheduler.executeScenario2();

        //----------------------------------------------------------------------
        //Create scheduler 8 request / min
        scheduler = new Scheduler(dataset, warehouse,
                8, scenario2GetPropability);
        //execute senario 1
        scheduler.executeScenario1();
        //execute senario 2
        scheduler.executeScenario2();

        //----------------------------------------------------------------------
        //Create scheduler 16 request / min
        scheduler = new Scheduler(dataset, warehouse,
                16, scenario2GetPropability);
        //execute senario 1
        scheduler.executeScenario1();
        //execute senario 2
        scheduler.executeScenario2();

        //----------------------------------------------------------------------
        //Create scheduler 32 request / min
        scheduler = new Scheduler(dataset, warehouse,
                32, scenario2GetPropability);
        //execute senario 1
        scheduler.executeScenario1();
        //execute senario 2
        scheduler.executeScenario2();

    }
}
