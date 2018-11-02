package cesatec.cesatec.network.asyncTasks;

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
import cesatec.cesatec.deserializers.CourseDeserializer;
import cesatec.cesatec.fragments.CourseListFragment;
import cesatec.cesatec.models.Course;
import cesatec.cesatec.network.utils.WebUtilities;

public class ApiFetchCoursesTask extends
        AsyncTask<Void, ArrayList<Course>, ArrayList<Course>> {
    private static final String TAG = "ApiFetchCoursesTask";

    private WeakReference<Activity> activityReference;
    private WeakReference<CourseListFragment> fragmentReference;
    private URL apiUrl;

    public ApiFetchCoursesTask(Activity activity, CourseListFragment fragment) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fragment);
        try {
            this.apiUrl = new URL(ApiConstants.CoursesResource.API_ENDPOINT);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "ApiFetchCoursesTask: Malformed API url endpoint '" +
                            ApiConstants.CoursesResource.API_ENDPOINT +
                            "', check API constants!");
        }
    }

    @Override
    protected void onPreExecute() {
        Activity activity = activityReference.get();
        if (activity != null) {
            TextView apiStatusFetchCourses = activity.findViewById(
                    R.id.api_status_fetch_courses);
            apiStatusFetchCourses.setText(R.string.api_fetching_courses);
        }
    }

    @Override
    protected ArrayList<Course> doInBackground(Void... voids) {
        // Get the JSON from the API
        String json = WebUtilities.getJSONFromUrl(apiUrl);
        if (json != null) {
            // Return an array of courses retrieved from the API
            Type typeToken = new TypeToken<ArrayList<Course>>() {
            }.getType();
            // Set the deserializer used to transform JSON into the Enrollment class
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Course.class, new CourseDeserializer());
            Gson gson = gsonBuilder.create();
            // Return an array list of courses created using the retrieved json
            return gson.fromJson(json, typeToken);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Course> apiCoursesList) {
        Activity activity = activityReference.get();
        if (activity != null) {
            // Hide the courses list loading message
            TextView apiStatusFetchCourses = activity.findViewById(R.id.api_status_fetch_courses);
            apiStatusFetchCourses.clearComposingText();
            apiStatusFetchCourses.setVisibility(View.GONE);

            // Get the fragment reference so it's methods can be called
            CourseListFragment courseListFragment = fragmentReference.get();
            // Save the array list retrieved from the API to the fragment
            courseListFragment.setCourseList(apiCoursesList);
            // Set the array list to the fragment RecyclerView
            courseListFragment.setUpRecyclerView(activity);
        }
    }
}
