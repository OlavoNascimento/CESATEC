package cesatec.cesatec.network.networkTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cesatec.cesatec.network.networkUtilities.WebConnection;

/**
 * Created an AsyncTask that send the specified parameters to an url
 */
public class ApiSendTask extends AsyncTask<String, Void, Boolean> {

    private HashMap<String, String> params;
    private WeakReference<Context> contextReference;

    public ApiSendTask(Context context, HashMap<String, String> properties) {
        this.contextReference = new WeakReference<>(context);
        this.params = properties;
    }

    /**
     * Creates a object and return the creation status
     *
     * @param urls Url used to create the object
     * @return Creation status
     */
    @Override
    protected Boolean doInBackground(String... urls) {
        return WebConnection.sendDataToUrl(urls[0], params);
    }

    /**
     * Display a toast if the object was successfully created
     *
     * @param creationStatus Status of the object's creation
     */
    @Override
    protected void onPostExecute(Boolean creationStatus) {
        super.onPostExecute(creationStatus);
        if (creationStatus) {
            // TODO Change text
            Toast.makeText(contextReference.get(),
                    "Student object created!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contextReference.get(),
                    "Failed creating object!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
