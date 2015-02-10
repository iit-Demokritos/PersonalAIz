/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.test;

import static gr.demokritos.iit.personalaiz.storage.HBase.getRow;
import java.io.IOException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class CustomTest {

    final static private Logger logger = LoggerFactory.getLogger(CustomTest.class);

    public static void main(String[] args) {

     
            getRow();
            
            
            
//        System.out.println(logger.isDebugEnabled());
//        if (logger.isDebugEnabled()) {
//
//            logger.debug("debug");
//        }
//        logger.info("info");
//        logger.error("error");
       
    }

}
