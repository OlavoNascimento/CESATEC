package cesatec.cesatec.network.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.R;
import cesatec.cesatec.deserializers.EnrollmentDeserializer;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.Utils.WebUtilities;

/**
 * Fetch the list of enrollments from the API
 */
public class ApiFetchEnrollmentsTask extends
        AsyncTask<Void, ArrayList<Enrollment>, ArrayList<Enrollment>> {
    private static final String TAG = "ApiFetchEnrollmentsTask";

    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private URL apiUrl;

    /**
     * Uses WeakReferences so the AsyncTask can be garbage collected, avoiding leaks
     *
     * @param activity Activity that called the task
     * @param fragment StudentListFragment that called task
     */
    public ApiFetchEnrollmentsTask(Activity activity, StudentListFragment fragment) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fragment);
        try {
            this.apiUrl = new URL(ApiConstants.EnrollmentResource.API_ENDPOINT);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "ApiFetchEnrollmentsTask: Malformed API url endpoint '" +
                            ApiConstants.EnrollmentResource.API_ENDPOINT
                            + "', check API constants!");
        }
    }

    @Override
    public void onPreExecute() {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Set the waiting message text while retrieving the JSON data
            TextView apiStatusFetchStudents = activity.findViewById(
                    R.id.api_status_fetch_students);
            apiStatusFetchStudents.setText(R.string.api_fetching_enrollments);
        }
    }

    /**
     * Access the api, retrieve the JSON and convert the data to an Enrollment ArrayList
     *
     * @return An ArrayList of Enrollments used by a RecyclerView
     */
    @Override
    public ArrayList<Enrollment> doInBackground(Void... voids) {
        // Get the JSON from the API
        String json = WebUtilities.getJSONFromUrl(apiUrl);
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
     * @param apiEnrollmentsList ArrayList used by a RecyclerView
     */
    @Override
    protected void onPostExecute(ArrayList<Enrollment> apiEnrollmentsList) {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Hide the student list loading message
            TextView apiStatusFetchStudents = activity.findViewById(
                    R.id.api_status_fetch_students);
            apiStatusFetchStudents.clearComposingText();
            apiStatusFetchStudents.setVisibility(View.GONE);

            // Get the fragment reference so it's functions can be called
            StudentListFragment studentListFragment = fragmentReference.get();
            // Save the array list retrieved from the API to the fragment
            studentListFragment.setEnrollmentsList(apiEnrollmentsList);
            // Set the array list to the fragment RecyclerView
            studentListFragment.setUpRecyclerView(activity);
        }
    }
}
