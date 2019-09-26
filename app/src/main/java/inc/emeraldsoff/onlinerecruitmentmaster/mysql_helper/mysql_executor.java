package inc.emeraldsoff.onlinerecruitmentmaster.mysql_helper;


import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class mysql_executor {
    private static final String user_url = "https://103.127.157.109:5001/api/employee_recruit";
    private static final String employee_url = "https://103.127.157.109:5001/api/employee_recruit";

    public void runner(){

    }
    protected String doInBackground(String geturl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(geturl);
            connection.setRequestMethod("GET");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
