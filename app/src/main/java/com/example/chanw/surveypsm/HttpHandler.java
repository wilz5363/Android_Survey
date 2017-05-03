package com.example.chanw.surveypsm;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by chanw on 3/26/2017.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private static HttpURLConnection conn = null;
    public String makeServiceCall(String regUrl){
        String response = null;

        try {
            URL url = new URL(regUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            // read the response

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

    public String postServiceCall(String regUrl, JSONObject postDataParams){
        String response = null;
        HttpURLConnection conn = null;
        Log.e(TAG, "URL: "+regUrl);
        Log.e(TAG, "Params: "+postDataParams.toString());
        try{
            URL url = new URL(regUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                Log.e(TAG,"Post service call: "+ sb.toString());
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }

        } catch (MalformedURLException e) {
            return new String("URLException: " + e.getMessage());
        } catch (IOException e) {
            return new String("IOException: " + e.getMessage());
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    public String postServiceCallWithReturnJson(String regUrl, JSONObject postDataParams){
        String response = null;
        HttpURLConnection conn = null;
        Log.e(TAG, "URL: "+regUrl);
        Log.e(TAG, "Params: "+postDataParams.toString());
        try{
            URL url = new URL(regUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
                return response;

            }
            else {
                return new String("false : "+responseCode);
            }

        } catch (MalformedURLException e) {
            return new String("URLException: " + e.getMessage());
        } catch (IOException e) {
            return new String("IOException: " + e.getMessage());
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }




    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        Log.e(TAG, "Get Post Data String: "+ result.toString());
        return result.toString();
    }


}
