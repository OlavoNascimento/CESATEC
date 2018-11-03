package cesatec.cesatec.network.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.constants.ApiConstants;
import cesatec.cesatec.deserializers.EnrollmentDeserializer;
import cesatec.cesatec.fragments.EnrollmentFragment;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.utils.WebUtilities;

public class ApiCheckRegistriesForStudentsTask extends
        AsyncTask<Void, ArrayList<Enrollment>, ArrayList<Enrollment>> {
    private static final String TAG = "ApiCheckStudentsThatLef";

    private WeakReference<Activity> activityReference;
    private WeakReference<EnrollmentFragment> fragmentReference;
    private int searchRegistryTypeId;
    private URL apiUrl;

    public ApiCheckRegistriesForStudentsTask(Activity activity,
                                             EnrollmentFragment fragment,
                                             int searchRegistryTypeId) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fragment);
        this.searchRegistryTypeId = searchRegistryTypeId;
        this.apiUrl = getApiUrl();
    }

    private URL getApiUrl() {
        String courseApiEndPoint = ApiConstants.RegistriesResource.API_ENDPOINT;
        try {
            return new URL(courseApiEndPoint);
        } catch (MalformedURLException e) {
            Log.e(TAG,
                    "getApiUrl: Malformed API url endpoint '" +
                            courseApiEndPoint
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
            apiStatusFetchStudents.setText(R.string.api_fetching_enrollments);
            apiStatusFetchStudents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<Enrollment> doInBackground(Void... voids) {
        // Get the JSON from the API
        String json = WebUtilities.getJSONFromUrl(apiUrl);
        if (json != null) {
            ArrayList<Enrollment> studentsThatMatchSearch = new ArrayList<>();
            JsonArray registries = new JsonParser().parse(json).getAsJsonArray();

            LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());

            for (JsonElement registryElement : registries) {
                JsonObject registryObject = registryElement.getAsJsonObject();

                int registryType = registryObject.get(
                        ApiConstants.RegistriesResource.FIELD_TYPE_ID).getAsInt();

                if (checkRegistryDate(dateNow, registryObject)) {
                    if (registryType == searchRegistryTypeId) {
                        JsonObject enrollmentJson = registryObject.get(
                                ApiConstants.RegistriesResource.NESTED_ENROLLMENT).getAsJsonObject();
                        Enrollment enrollment = jsonToEnrollment(enrollmentJson);
                        if (!studentsThatMatchSearch.contains(enrollment)) {
                            studentsThatMatchSearch.add(enrollment);
                        }
                    }
                }

            }
            return studentsThatMatchSearch;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Enrollment> studentsThatMatchSearch) {
        EnrollmentFragment fragment = fragmentReference.get();
        Activity activity = activityReference.get();
        if (activity != null && fragment != null) {
            // Hide the student list loading message
            TextView apiStatusFetchStudents = activity.findViewById(
                    R.id.api_status_fetch_students);
            apiStatusFetchStudents.setVisibility(View.INVISIBLE);

            TextView singleListHeader = activity.findViewById(R.id.student_single_list_header);
            singleListHeader.setVisibility(View.VISIBLE);

            RecyclerView singleListRecyclerView = activity.findViewById
                    (R.id.student_single_list);
            fragment.setUpRecyclerView(activity, singleListRecyclerView, studentsThatMatchSearch);
        }
    }

    private boolean checkRegistryDate(LocalDate dateNow, JsonObject registryObject) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss");

        String registryCreatedString = registryObject.get(
                ApiConstants.RegistriesResource.FIELD_DATE_TIME).getAsString();
        LocalDate registryCreatedDate = LocalDate.parse(
                registryCreatedString, dateTimeFormatter);

        Log.d(TAG, "checkRegistryDate: " + dateNow.equals(registryCreatedDate));
        return dateNow.equals(registryCreatedDate);
    }

    private Enrollment jsonToEnrollment(JsonObject enrollmentJSON) {
        String subCourseName = enrollmentJSON
                .get(ApiConstants.EnrollmentResource.FIELD_STUDENT_SUB_COURSE).getAsJsonObject()
                .get(ApiConstants.SubCoursesResource.FIELD_NAME).getAsString();
        // Set the enrollment deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Enrollment.class, new EnrollmentDeserializer(subCourseName));
        Gson enrollmentsGSON = gsonBuilder.create();
        // Deserialize the enrollment JSON into an Enrollment object array
        return enrollmentsGSON.fromJson(enrollmentJSON, Enrollment.class);
    }
}
