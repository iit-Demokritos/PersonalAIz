/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.storage;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class HBase {

    public static void getRow() throws IOException {

        Configuration config = HBaseConfiguration.create();
        config.set("hbase.master", "83.212.124.145:60010");
        HTable table = new HTable(config, "Users");
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
        // ...
        }
        
        
        
    }

}
