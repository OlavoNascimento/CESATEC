package cesatec.cesatec.network.networkUtilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebConnection {
    private final static String TAG = "WebConnection";

    /**
     * Get a input stream from a url and transform it in a string
     *
     * @param url Url to receive InputStream
     * @return String representation of the url data
     */
    public static String getStringFromUrl(String url) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                Log.d(TAG, "Getting data from: " + url);
                return inputStreamToString(conn.getInputStream());
            } else {
                Log.d(TAG, "Connection to " + url + "failed, " +
                        "response code:" + conn.getResponseCode());
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Transform a input stream to string
     *
     * @param stream Input stream to be transformed to string
     * @return String representation of the InputStream
     * @throws IOException If the BufferedReader fails reading the InputStream
     */
    private static String inputStreamToString(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line);
        }
        in.close();
        return stringBuilder.toString();
    }

    /**
     * Send a POST request to an url
     *
     * @param url    Url to send the data
     * @param params Hashmap of parameters to be sent. Ex: name=example
     */
    public static boolean sendDataToUrl(String url, HashMap<String, String> params) {
        try {
            // Encode the data to be sent
            String dataString = encodeUrlParameters(params);
            // Size of the data
            int postDataLength = dataString.getBytes("UTF-8").length;

            Log.d(TAG, "sendDataToUrl: " + dataString);
            // URL fullUrl = new URL(url + dataString);
            URL finalUrl = new URL(url);

            HttpsURLConnection conn = (HttpsURLConnection) finalUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));

            // Write the data to the connection output stream
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),
                    "UTF-8");
            try {
                out.write(dataString);
            } finally {
                out.flush();
                out.close();
            }

            Log.d(TAG, "Sending data to: " + url);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                // TODO Remove response message
                Log.d(TAG, "sendDataToUrl: Confirmation data=" +
                        inputStreamToString(conn.getInputStream()));
                return true;
            } else {
                Log.d(TAG, "Connection to " + url + " failed, " +
                        "response code:" + conn.getResponseCode());
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * Use a hashmap of parameters to form an url used to send a POST request.
     * Name: example becomes &name=example
     *
     * @param params Hash of parameters that will be used on the url
     * @return Url representation of the hashmap parameters
     * @throws UnsupportedEncodingException When key or value can't be encoded to UTF-8
     */
    private static String encodeUrlParameters(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder dataString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (dataString.length() > 0) {
                dataString.append("&");
            }
            // Format key and value to x-www-form-urlencoded format
            dataString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            dataString.append("=");
            dataString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return dataString.toString();
    }

}
