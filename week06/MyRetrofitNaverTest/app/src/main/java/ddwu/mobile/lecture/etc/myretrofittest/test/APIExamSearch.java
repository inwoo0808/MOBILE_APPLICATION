package ddwu.mobile.lecture.etc.myretrofittest.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APIExamSearch {

    public static void main(String[] args) {
        String clientId = "";   // put your cliendId
        String clientSecret = "";   // put your clientSecret
        try {
            String text = URLEncoder.encode("android", "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+ text; // json
            //String apiURL = "https://openapi.naver.com/v1/search/book.xml?query="+ text; // xml
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
