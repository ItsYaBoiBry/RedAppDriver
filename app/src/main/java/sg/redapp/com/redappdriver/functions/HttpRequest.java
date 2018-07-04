package sg.redapp.com.redappdriver.functions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by user on 22/3/2018.
 */

public class HttpRequest {

    public HttpRequest(){

    }
    public String GetRequest(String url){
        try {
            return sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String PostRequest(String url, String parameters){
        try {
            return sendPost(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public Class Login extends AsyncTask<String, Void, String>{
//        String details = "email="+email+"&password="+password;
//        try {
//            sendPost("http://bryanlowsk.com/UHoo/API/login.php", details);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // HTTP GET request
    private String sendGet(String url) throws Exception {

        String[] getLink = url.split("/");
        if (getLink[0].equals("http:")) {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        } else if (getLink[0].equals("https:")) {

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");


            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        } else {
            System.out.print("Please enter a http:// link");
        }

        return null;
    }

    // HTTP POST request
    private String sendPost(String url, String parameters) throws Exception {

        String[] getLink = url.split("/");
        if (getLink[0].equals("http:")) {

            URL obj = new URL(url);
            HttpURLConnection conHttp = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            conHttp.setRequestMethod("POST");
            conHttp.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = parameters;

            // Send post request
            conHttp.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conHttp.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conHttp.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conHttp.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();

        } else if (getLink[0].equals("https:")) {

            URL obj = new URL(url);
            HttpURLConnection conHttp = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            conHttp.setRequestMethod("POST");
            conHttp.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "email=haziq@gmail.com&password=password";

            // Send post request
            conHttp.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conHttp.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conHttp.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conHttp.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();

        } else {
            System.out.print("Please enter a http:// link");
        }

        return null;

    }

}