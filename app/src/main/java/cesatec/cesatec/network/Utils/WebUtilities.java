package cesatec.cesatec.network.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebUtilities {
    private static final String TAG = "WebUtilities";

    /**
     * Get a input stream from an url and transform it in a string
     *
     * @return String representation of the url data
     */
    public static String getJSONFromUrl(@NonNull URL url) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Return api response input stream as a string
                return inputStreamToString(conn.getInputStream());
            } else {
                Log.d(TAG, "Connection to " + url + "failed, " +
                        "response code:" + conn.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Transform an input stream to string
     *
     * @param stream Input stream to be transformed to string
     * @return String representation of the InputStream
     * @throws IOException If the BufferedReader fails to read the InputStream
     */
    private static String inputStreamToString(@NonNull InputStream stream)
            throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(stream, "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line);
        }
        in.close();
        return stringBuilder.toString();
    }
}
