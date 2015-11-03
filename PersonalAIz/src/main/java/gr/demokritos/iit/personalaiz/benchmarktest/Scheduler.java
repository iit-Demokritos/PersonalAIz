/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.benchmarktest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private final int threadsPerMinute;
    private final Random r = new Random();
    private final int fromPointer;
    private final int getPointer;
    private final Logger LOGGER;
    private final Date date = new Date();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    //Constructor
    public Scheduler(ILoadDataset dataset, IStroreResults warehouse,
            int threadsPerMinute, String scenario2GetPropability, Logger LOGGER) {

        //Set settings
        this.dataset = dataset;
        this.warehouse = warehouse;
        this.threadsPerMinute = threadsPerMinute;
        String[] tmp = scenario2GetPropability.split("/");
        this.getPointer = Integer.parseInt(tmp[0]);
        this.fromPointer = Integer.parseInt(tmp[1]);
        this.LOGGER = LOGGER;
    }

    /**
     * Scenario 1: Add users, Modify Users Profile, Get users profile.
     */
    public void executeScenario1() {

        //Add users 
         LOGGER.info("#Start Add Users: "
                + dateFormat.format(date.getTime()));

        //----------------------------------------------------------------------
        //Modify users profile
         LOGGER.info("#Start Modify Users Profile: "
                + dateFormat.format(date.getTime()));
        //----------------------------------------------------------------------
        //Get users profile
         LOGGER.info("#Start Get Users Profile: "
                + dateFormat.format(date.getTime()));
    }

    /**
     * Scenario 2: Add users, (Modify Users Profile, Get users profile) with
     * possibility, Get Users profile.
     */
    public void executeScenario2() {

        //Add users 
        //----------------------------------------------------------------------
        //Modify users profile with possibility
        //Get users profile with possibility
        //----------------------------------------------------------------------
        //Get users profile 
    }

    /**
     *
     * @param baseURL
     * @param getParams
     * @return
     * @throws Exception
     */
    public static String execGet(String baseURL,
            Map<String, String> getParams) throws Exception {

        String url = baseURL;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        // add header
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.7.12)");

        List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();

        for (Map.Entry<String, String> entry : getParams.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // append params
            urlParameters.add(new BasicNameValuePair(key, val));
        }

//        get.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        HttpResponse response = client.execute(get);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("GET parameters : " + get.toString());
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    /**
     *
     * @param baseURL
     * @param postParams
     * @return
     * @throws Exception
     */
    public static String execPost(String baseURL,
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
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.toString());
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    /**
     *
     * @param baseURL
     * @param putParams
     * @return
     * @throws Exception
     */
    public static String execPut(String baseURL,
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
        System.out.println("\nSending 'PUT' request to URL : " + url);
        System.out.println("PUT parameters : " + put.toString());
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return result.toString();
    }

}
