package cesatec.cesatec.network.networkTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.deserializers.StudentDeserializer;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Student;
import cesatec.cesatec.network.networkUtilities.WebConnection;

/**
 * Fetch the list of users from the API
 */
public class ApiFetchTask extends AsyncTask<String, ArrayList<Student>, ArrayList<Student>> {
    private static final String TAG = "ApiAsyncTask";
    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;

    /**
     * Uses WeakReferences so the AsyncTask can be garbage collected, avoiding leaks
     *
     * @param activity      Activity that called the ApiFetchTask
     * @param fetchFragment StudentListFragment that called the ApiFetchTask
     */
    public ApiFetchTask(Activity activity, StudentListFragment fetchFragment) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fetchFragment);
    }

    /**
     * Set up the loading message while retrieving the data
     */
    @Override
    public void onPreExecute() {
        // Set the waiting message text while retrieving the json data
        Activity activity = activityReference.get();
        if (activity != null) {
            TextView tvApiStatus = activity.findViewById(R.id.api_status);
            tvApiStatus.setText(R.string.api_connecting);
        }
    }

    /**
     * Access the api, retrieve the json and convert the response to an Student ArrayList
     *
     * @param url Url to be accessed
     * @return An ArrayList of Student, to be used on a RecyclerView
     */
    @Override
    public ArrayList<Student> doInBackground(String... url) {
        // Get the json from the web server
        String json = WebConnection.getStringFromUrl(url[0]);
        if (json != null) {
            String jsonstudents = new JsonParser().parse(json).getAsJsonObject().get("data").toString();
            Log.d(TAG, "doInBackground: data json=" + jsonstudents);
            // Type of an Student array list to be used by Gson
            Type typeToken = new TypeToken<ArrayList<Student>>() {
            }.getType();
            // Set the deserializer used to transform JSON into the Student class
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Student.class, new StudentDeserializer());
            Gson gson = gsonBuilder.create();
            // Return an Array class array list of Student created using the retrieved json
            return gson.fromJson(jsonstudents, typeToken);
        }
        return null;
    }

    /**
     * Uses the received ArrayList to hide the loading message
     * and display the RecyclerView
     *
     * @param studentArrayList ArrayList to be used on the RecyclerView
     */
    @Override
    protected void onPostExecute(ArrayList<Student> studentArrayList) {
        Activity activity = activityReference.get();
        activityReference = null;
        if (activity != null) {
            // Hide the loading message
            TextView tvApiStatus = activity.findViewById(R.id.api_status);
            tvApiStatus.clearComposingText();
            tvApiStatus.setVisibility(View.GONE);

            // Set up the recycler view
            StudentListFragment fetchFragment = fragmentReference.get();
            fetchFragment.setUpRecyclerView(activity, studentArrayList);
        }
    }
}
