package cesatec.cesatec.network.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashMap;

import cesatec.cesatec.activities.mainActivity.StudentListActivity;
import cesatec.cesatec.constants.ApiConstants;
import cesatec.cesatec.models.RegistryTypes;
import cesatec.cesatec.network.utils.WebUtilities;

public class ApiGetRegistryTypesTask extends
        AsyncTask<Void, HashMap<String, Integer>, HashMap<String, Integer>> {
    private static final String TAG = "ApiGetRegistryTypesTask";

    private WeakReference<StudentListActivity> activityReference;
    private URL apiUrl;

    public ApiGetRegistryTypesTask(StudentListActivity activity) {
        this.activityReference = new WeakReference<>(activity);
        this.apiUrl = getApiUrl();
    }

    private URL getApiUrl() {
        String courseApiEndPoint = ApiConstants.RegistryTypesResource.API_ENDPOINT;
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
    protected HashMap<String, Integer> doInBackground(Void... voids) {
        // Get the JSON from the API
        String json = WebUtilities.getJSONFromUrl(apiUrl);
        if (json != null) {
            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
            HashMap<String, Integer> registryTypes = new HashMap<>();
            for (JsonElement typeElement : jsonArray) {
                JsonObject registerTypeObject = typeElement.getAsJsonObject();

                final int registerId = registerTypeObject.get(
                        ApiConstants.RegistryTypesResource.FIELD_ID).getAsInt();

                final String registerDescription = registerTypeObject.get(
                        ApiConstants.RegistryTypesResource.FIELD_DESCRIPTION).getAsString();

                registryTypes.put(normalizeString(registerDescription), registerId);
            }
            return registryTypes;
        }
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, Integer> registryTypes) {
        StudentListActivity activity = activityReference.get();
        if (activity != null && registryTypes != null) {
            String typeExitName = normalizeString(
                    ApiConstants.RegistryTypesResource.TYPE_EXIT_NAME);
            String typeReturnName = normalizeString(
                    ApiConstants.RegistryTypesResource.TYPE_RETURN_NAME);

            Integer exitId = registryTypes.get(typeExitName);
            Integer returnId = registryTypes.get(typeReturnName);
            if (exitId != null && returnId != null) {
                activity.setRegistryTypes(
                        new RegistryTypes(exitId, returnId));
            }
        }
    }

    private String normalizeString(String text) {
        return Normalizer.normalize(
                text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
}
