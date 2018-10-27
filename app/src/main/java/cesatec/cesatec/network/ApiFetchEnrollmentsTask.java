package cesatec.cesatec.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.R;
import cesatec.cesatec.deserializers.EnrollmentDeserializer;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Enrollment;

/**
 * Fetch the list of enrollments from the API
 */
public class ApiFetchEnrollmentsTask extends
        AsyncTask<Void, ArrayList<Enrollment>, ArrayList<Enrollment>> {
    private static final String TAG = "ApiFetchEnrollmentsTask";

    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private URL api_url;

    /**
     * Uses WeakReferences so the AsyncTask can be garbage collected, avoiding leaks
     *
     * @param activity      Activity that called the task
     * @param fetchFragment StudentListFragment that called task
     */
    public ApiFetchEnrollmentsTask(Activity activity, StudentListFragment fetchFragment) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fetchFragment);
        try {
            this.api_url = new URL(ApiConstants.API_ENDPOINT_ENROLLMENTS);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "ApiFetchEnrollmentsTask: Malformed API url endpoint '" +
                            ApiConstants.API_ENDPOINT_ENROLLMENTS + "', check API constants!");
        }

    }

    @Override
    public void onPreExecute() {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Set the waiting message text while retrieving the JSON data
            TextView tvApiStatus = activity.findViewById(R.id.api_status);
            tvApiStatus.setText(R.string.api_connecting);
        }
    }

    /**
     * Access the api, retrieve the JSON and convert the data to an Enrollment ArrayList
     *
     * @return An ArrayList of Enrollments used by a RecyclerView
     */
    @Override
    public ArrayList<Enrollment> doInBackground(Void... voids) {
        // Get the JSON from the web server
        String json = getJSONFromUrl();
        if (json != null) {
            // Type token of an ArrayList of Enrollments
            Type typeToken = new TypeToken<ArrayList<Enrollment>>() {
            }.getType();
            // Set the deserializer used to transform JSON into the Enrollment class
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Enrollment.class, new EnrollmentDeserializer());
            Gson gson = gsonBuilder.create();
            // Return an array list of enrollments created using the retrieved json
            return gson.fromJson(json, typeToken);
        }
        return null;
    }

    /**
     * Uses the received ArrayList to hide the loading message
     * and display the RecyclerView
     *
     * @param enrollmentArrayList ArrayList used by a RecyclerView
     */
    @Override
    protected void onPostExecute(ArrayList<Enrollment> enrollmentArrayList) {
        Activity activity = activityReference.get();
        activityReference = null;
        if (activity != null) {
            // Hide the loading message
            TextView tvApiStatus = activity.findViewById(R.id.api_status);
            tvApiStatus.clearComposingText();
            tvApiStatus.setVisibility(View.GONE);

            // Set up the recycler view
            StudentListFragment studentListFragment = fragmentReference.get();
            studentListFragment.setUpRecyclerView(activity, enrollmentArrayList);
        }
    }

    /**
     * Get a input stream from an url and transform it in a string
     *
     * @return String representation of the url data
     */
    private String getJSONFromUrl() {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) api_url.openConnection();
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Return api response input stream as a string
                return inputStreamToString(conn.getInputStream());
            } else {
                Log.d(TAG, "Connection to " + api_url + "failed, " +
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
    private String inputStreamToString(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line);
        }
        in.close();
        return stringBuilder.toString();
    }
}
