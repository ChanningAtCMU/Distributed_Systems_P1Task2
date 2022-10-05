package ds.project1task2;

/**
 * Author: Changzhou (Channing) Zheng (changzhz)
 * Last Modified: September 24, 2022
 *
 * This is an MVC model that defines all methods
 * getNicknames: scraping corresponding webpage and get the appropriate output nickname as a string for each state
 * getCapital: scraping corresponding webpage and get the appropriate output capital as a string for each state
 * getSong: scraping corresponding webpage and get the appropriate output state song as a string for each state
 * getFlower: scraping corresponding webpage and get the appropriate output flower as a string URL for each state
 * getFlag: scraping corresponding webpage and get the appropriate output flag for as a string URL each state
 * getPop: scraping corresponding webpage and get the appropriate output population as a string for each state
 */

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

public class stateInfoModel {

    /**
     * @param stateName: get user-input state name
     * @return nickname of the input state
     * @throws UnsupportedEncodingException
     * Finding states' nicknames.
     */
    public String getNicknames(String stateName) throws UnsupportedEncodingException {
        String nickname;

        // The source list excludes the District of Columbia and U.S. territories.
        if(stateName.equals("District of Columbia")){
            nickname = "No nickname for 'District of Columbia'";
        } else {
            // Retrieve URL
            String urlNickname = "https://www.britannica.com/topic/List-of-nicknames-of-U-S-States-2130544";
            String response = fetch(urlNickname, "TLSV1.3");

            // Find patterns for the state's nicknames
            int leftIndex = response.indexOf(stateName + "</a></td><td>");
            String leftBound = stateName+"</a></td><td>";
            int rightIndex = response.indexOf("</td>", leftIndex+leftBound.length());

            nickname = response.substring(leftIndex+leftBound.length(), rightIndex);
        }
        return nickname;
    }

    /**
     * @param stateName
     * @return capital names
     * @throws UnsupportedEncodingException
     * Finding capital city for each state.
     */
    public String getCapital(String stateName) throws UnsupportedEncodingException {
        String urlCapital = "https://gisgeography.com/united-states-map-with-capitals/";
        String response = fetch(urlCapital, "TLSV1.3");

        // Starting with the best place right before the scraping target
        String startingPoint = "<h3>United States Capital</h3>";

        //Get locate the left and right indicator, use .substring() to get content in between
        int leftIndex = response.indexOf(stateName, response.indexOf(startingPoint));
        leftIndex = leftIndex + stateName.length() + 2;
        int rightIndex = response.indexOf(")", leftIndex);
        String capital = response.substring(leftIndex, rightIndex);

        return capital;
    }

    /**
     * @param stateName
     * @return songs' names
     * @throws UnsupportedEncodingException
     * Finding state song for each state.
     */
    public String getSong(String stateName) throws UnsupportedEncodingException {
        if(stateName.equals("District of Columbia")){
            return "No song for 'District of Columbia'";
        } else {
            String urlSong = "https://www.50states.com/songs/";
            String response = fetch(urlSong, "TLSV1.3");

            // Find a place to get start
            String startingPoint = "</p></div><td width=50%><ul class=bulletedList>";

            String dt = "<dt>";
            String webCode = "</dt><dd><a href=https://www.50states.com";

            int leftIndex = response.indexOf(dt + stateName + webCode, response.indexOf(startingPoint));
            int headLen = dt.length() + stateName.length() + webCode.length();

            // slashLiInd helps to local the right boundary of each state's info
            int slashLiInd = response.indexOf("</li>", leftIndex);
            int rightIndex;;
            int curInd = 0;
            ArrayList<String> songArray = new ArrayList<>();

            // if the current indicator go across the right boundary, stop the iteration
            while (curInd <= slashLiInd){
                leftIndex = response.indexOf(">", leftIndex);
                rightIndex = response.indexOf("</a></dd></dl>", leftIndex+headLen);
                String song = response.substring(leftIndex+1, rightIndex);

                // In case some states will have multiple songs, add them in to a ArrayList and output as a combined String
                songArray.add(song);

                leftIndex = response.indexOf("title=\"" ,leftIndex);

                // "</a></dd></dl>" has 14 chars, +15 just in case
                curInd += Integer.valueOf(rightIndex) + 15;
            }

            // Output each song in the list
            String finalSongList = String.join("\n", songArray);

            return finalSongList;
        }
    }

    /**
     * @param stateName
     * @return
     * @throws UnsupportedEncodingException
     * Get flower information
     */
    public String getFlower (String stateName) throws UnsupportedEncodingException {

        if(stateName.equals("District of Columbia")){
            return "https://upload.wikimedia.org/wikipedia/commons/d/d1/Image_not_available.png";
        }
        String urlFlower = "https://statesymbolsusa.org/categories/flower";
        String response = fetch(urlFlower, "TLSV1.3");

        // Set up a starting point and try to locate state by name
        String startingPoint = "<li class=\"views-row views-row-1 views-row-odd views-row-first\">";
        int nameLeftIndex = response.indexOf(stateName, response.indexOf(startingPoint));
        int leftIndex = nameLeftIndex - 578;
        int rightIndex;
        String urlContainer;

        // Local position for each flower pics
        leftIndex = response.indexOf("src=", leftIndex);
        rightIndex = response.indexOf("width", leftIndex);
        urlContainer = response.substring(leftIndex + 5, rightIndex - 2);

        return urlContainer;
    }

    /**
     * @param stateName
     * @return URLs directing to flag pictures
     * @throws UnsupportedEncodingException
     * Get flag information for states
     */
    public String getFlag (String stateName) throws UnsupportedEncodingException {
        String urlFlag = "https://www.states101.com/flags";
        String response = fetch(urlFlag, "TLSV1.3");

        // Set a starting point
        String startingPoint = "<a href=\"/flags/alabama\"><b>";
        int leftIndex = response.indexOf(stateName, response.indexOf(startingPoint));
        leftIndex = response.indexOf("src=", leftIndex);
        int rightIndex = response.indexOf("alt", leftIndex);

        String flagURL = response.substring(leftIndex+5, rightIndex-2);

        //return must start with "https://www.states101.com"
        return "https://www.states101.com"+flagURL;
    }

    /**
     * @param stateName
     * @return Population number in String Type
     * @throws UnsupportedEncodingException
     * Get state's population by user's input
     */
    public String getPop (String stateName) throws UnsupportedEncodingException {
        if (stateName.equals("District of Columbia")){
            return "No population data found";
        }

        // Pair and store state names with corresponding FIPS code
        HashMap<String, String> fipsPairs = new HashMap<>();
        fipsPairs.put("Alabama", "01");
        fipsPairs.put("Alaska", "02");
        fipsPairs.put("Arizona", "04");
        fipsPairs.put("Arkansas", "05");
        fipsPairs.put("California", "06");
        fipsPairs.put("Colorado", "08");
        fipsPairs.put("Connecticut", "09");
        fipsPairs.put("Delaware", "10");
        fipsPairs.put("Florida", "12");
        fipsPairs.put("Georgia", "13");
        fipsPairs.put("Hawaii", "15");
        fipsPairs.put("Idaho", "16");
        fipsPairs.put("Illinois", "17");
        fipsPairs.put("Indiana", "18");
        fipsPairs.put("Iowa", "19");
        fipsPairs.put("Kansas", "20");
        fipsPairs.put("Kentucky", "21");
        fipsPairs.put("Louisiana", "22");
        fipsPairs.put("Maine", "23");
        fipsPairs.put("Maryland", "24");
        fipsPairs.put("Massachusetts", "25");
        fipsPairs.put("Michigan", "26");
        fipsPairs.put("Minnesota", "27");
        fipsPairs.put("Mississippi", "28");
        fipsPairs.put("Missouri", "29");
        fipsPairs.put("Montana", "30");
        fipsPairs.put("Nebraska", "31");
        fipsPairs.put("Nevada", "32");
        fipsPairs.put("New Hampshire", "33");
        fipsPairs.put("New Jersey", "34");
        fipsPairs.put("New Mexico", "35");
        fipsPairs.put("New York", "36");
        fipsPairs.put("North Carolina", "37");
        fipsPairs.put("North Dakota", "38");
        fipsPairs.put("Ohio", "39");
        fipsPairs.put("Oklahoma", "40");
        fipsPairs.put("Oregon", "41");
        fipsPairs.put("Pennsylvania", "42");
        fipsPairs.put("Rhode Island", "44");
        fipsPairs.put("South Carolina", "45");
        fipsPairs.put("South Dakota", "46");
        fipsPairs.put("Tennessee", "47");
        fipsPairs.put("Texas", "48");
        fipsPairs.put("Utah", "49");
        fipsPairs.put("Vermont", "50");
        fipsPairs.put("Virginia", "51");
        fipsPairs.put("Washington", "53");
        fipsPairs.put("West Virginia", "54");
        fipsPairs.put("Wisconsin", "55");
        fipsPairs.put("Wyoming", "56");

        // Get corresponding FIPS code to match user's input
        String fips = fipsPairs.get(stateName);
        String urlPop = "https://api.census.gov/data/2020/dec/pl?get=NAME,P1_001N&for=state:" + fips + "&key=" + "67d78e2a10e70e1b6d051aa6850a3fad359553e0";
        String response = fetch(urlPop, "TLSV1.3");

        // Find a starting point for scraping
        String startingPoint = "],";
        int leftIndex = response.indexOf(",\"", response.indexOf(startingPoint));
        leftIndex = leftIndex + 2;
        int rightIndex = response.indexOf("\",", leftIndex);

        String population = response.substring(leftIndex,rightIndex);

        return population;
    }

    /**
     * Fetch down below
     */
    private String fetch(String searchURL, String certType) {
        try {
            // Create trust manager, which lets you ignore SSLHandshakeExceptions
            createTrustManager(certType);
        } catch (KeyManagementException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Shouldn't come here: ");
            ex.printStackTrace();
        }

        String response = "";
        try {
            URL url = new URL(searchURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Something wrong with URL");
            return null;
        }
        return response;
    }

    private void createTrustManager(String certType) throws KeyManagementException, NoSuchAlgorithmException{
        /**
         * Taken from: http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
         */
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance(certType);
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
