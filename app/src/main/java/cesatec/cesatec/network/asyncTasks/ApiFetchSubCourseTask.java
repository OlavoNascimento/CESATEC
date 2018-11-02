package cesatec.cesatec.network.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.R;
import cesatec.cesatec.deserializers.SubCourseDeserializer;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.SubCourse;
import cesatec.cesatec.network.utils.WebUtilities;

/**
 * Fetch the list of SubCourse from the API
 */
public class ApiFetchSubCourseTask extends
        AsyncTask<Void, SubCourse, SubCourse> {
    private static final String TAG = "ApiFetchSubCourseTask";

    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private URL apiUrl;

    public ApiFetchSubCourseTask(Activity activity,
                                 StudentListFragment fragment,
                                 int subCourseId) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fragment);
        this.apiUrl = getSubCourseApiUrl(subCourseId);
    }

    private URL getSubCourseApiUrl(int subCourseId) {
        String subCourseApiEndPoint = ApiConstants.SubCoursesResource.API_ENDPOINT +
                "/" + subCourseId;
        try {
            return new URL(subCourseApiEndPoint);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "getSubCourseApiUrl: Malformed API url endpoint '" +
                            subCourseApiEndPoint
                            + "', check API constants!");
        }
        return null;
    }

    @Override
    public void onPreExecute() {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Set the waiting message text while retrieving the JSON data
            TextView apiStatusFetchStudents = activity.findViewById(
                    R.id.api_status_fetch_students);
            apiStatusFetchStudents.setText(R.string.api_fetching_sub_course);
        }
    }

    @Override
    public SubCourse doInBackground(Void... voids) {
        // Get the JSON from the API
        String json = WebUtilities.getJSONFromUrl(apiUrl);
        if (json != null) {
            // Set the deserializer used to transform JSON into the SubCourse class
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(SubCourse.class, new SubCourseDeserializer());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(json, SubCourse.class);
        }
        return null;
    }

    /**
     * Uses the received ArrayList to hide the loading message
     * and display the RecyclerView
     *
     * @param subCourse ArrayList used by a RecyclerView
     */
    @Override
    protected void onPostExecute(SubCourse subCourse) {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Hide the student list loading message
            TextView apiStatusFetchStudents = activity.findViewById(
                    R.id.api_status_fetch_students);
            apiStatusFetchStudents.clearComposingText();
            apiStatusFetchStudents.setVisibility(View.GONE);

            // Get the fragment reference so it's functions can be called
            StudentListFragment studentListFragment = fragmentReference.get();
            if (subCourse.getId() % 2 != 0) {
                studentListFragment.setFirstSubCourse(subCourse);
                // Set the array list to the fragment RecyclerView
                studentListFragment.setUpFirstSubCourseRecyclerView(activity);
            } else {
                studentListFragment.setSecondSubCourse(subCourse);
                // Set the array list to the fragment RecyclerView
                studentListFragment.setUpSecondSubCourseRecyclerView(activity);
            }
        }
    }
}
