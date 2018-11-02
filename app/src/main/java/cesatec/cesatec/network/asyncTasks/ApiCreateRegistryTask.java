package cesatec.cesatec.network.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Enrollment;

/**
 * Add an registration of a student
 */
public class ApiCreateRegistryTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "ApiCreateRegistryTask";

    private WeakReference<Context> contextReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private AtomicInteger workingThreads;
    private Enrollment enrollment;
    private URL api_url;

    public ApiCreateRegistryTask(Context context,
                                 StudentListFragment fragment,
                                 AtomicInteger workingThreads,
                                 Enrollment enrollment) {
        this.contextReference = new WeakReference<>(context);
        this.fragmentReference = new WeakReference<>(fragment);
        this.workingThreads = workingThreads;
        this.enrollment = enrollment;
        this.api_url = getApiUrl();
    }

    private URL getApiUrl() {
        String registriesApiEndPoint = ApiConstants.RegistriesResource.API_ENDPOINT;
        try {
            return new URL(registriesApiEndPoint);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "getApiUrl: Malformed API url endpoint '" +
                            registriesApiEndPoint
                            + "', check API constants!");
        }
        return null;
    }

    /**
     * Creates a object and return the creation status
     *
     * @return Object creation status
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        return createStudentRegistry(enrollment);
    }

    /**
     * Display a toast notifying about the status of the registry creation
     *
     * @param creationStatus Status of the object's creation
     */
    @Override
    protected void onPostExecute(Boolean creationStatus) {
        super.onPostExecute(creationStatus);
        int remainingThreads = workingThreads.decrementAndGet();
        Log.d(TAG, "onPostExecute: " + remainingThreads);
        if (creationStatus) {
            StudentListFragment fragment = fragmentReference.get();
            fragment.addToSuccessfulCreatedRegistries();
        }
        Context context = contextReference.get();
        if (remainingThreads == 0) {
            Log.d(TAG, "onPostExecute: sending broadcast");
            // Warn the StudentListFragment that all registries tasks finished
            LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
            mgr.sendBroadcast(new Intent("all_registries_tasks_finished"));
        }
    }

    /**
     * Create a new registry of a student, marking that they have left or entered
     *
     * @param enrollment Student enrollment object used to extract the information used to create a
     *                   new registry
     * @return Whether the registry creation succeeded
     */
    private boolean createStudentRegistry(Enrollment enrollment) {
        try {
            Log.d(TAG, "createStudentRegistry: " + enrollment.toString());
            // Transform a Enrollment object to a string used to create a new registry
            String dataString = enrollmentToParameters(enrollment);

            // Size of the new registry data
            int postDataLength = dataString.getBytes("UTF-8").length;

            HttpsURLConnection conn = (HttpsURLConnection) api_url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));

            // Write the enrollment information to the API
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8");
            try {
                out.write(dataString);
            } finally {
                out.flush();
                out.close();
            }

            // Checks if the registry creation was successful
            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                return true;
            } else {
                Log.e(TAG, "Failed creating new registry using '" + dataString + "' on "
                        + api_url + ", response code:" + conn.getResponseCode());
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * Use an Enrollment object to form url parameters used to send a POST request.
     * Ex: id=1&name=example
     *
     * @return Parameters representation of Enrollment object
     */
    private String enrollmentToParameters(Enrollment enrollment)
            throws UnsupportedEncodingException {
        // TODO Add return registry
        // Encode the string values and concatenate them
        // into a url containing the registry data
        return URLEncoder.encode(
                ApiConstants.RegistriesResource.FIELD_ENROLLMENT_ID, "UTF-8") +
                "=" + enrollment.getId() +

                "&" + URLEncoder.encode(
                ApiConstants.RegistriesResource.FIELD_TYPE_ID, "UTF-8") +
                "=" + ApiConstants.RegistryTypesResource.FIELD_TYPE_EXIT_ID +

                "&" + URLEncoder.encode(
                ApiConstants.RegistriesResource.FIELD_DATE_TIME, "UTF-8") +
                "=" + URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                .format(new java.util.Date()), "UTF-8");
    }
}
