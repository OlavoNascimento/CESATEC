package cesatec.cesatec.activities.detailActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.StudentDetailFragment;

/**
 * Show details of a student
 */
public class StudentDetailActivity extends AppCompatActivity {
    private static final String TAG = "StudentDetailActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Display return button if there is an action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Create a new fragment if this is there isn't one to be reused
        if (savedInstanceState == null) {
            StudentDetailFragment detailFragment = new StudentDetailFragment();
            detailFragment.setArguments(incomingIntentToBundle());
            getSupportFragmentManager().beginTransaction()
                    .add(detailFragment, "detail_fragment").commit();
        }

    }

    /**
     * Create a bundle containing the incoming intent data
     *
     * @return Bundle containing the arguments to be sent to the detail fragment
     */
    private Bundle incomingIntentToBundle() {
        if (getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("avatar_url")) {
            Bundle arguments = new Bundle();
            Log.d(TAG, "getIncomingIntent: found intent values");
            arguments.putShort("id", getIntent().getShortExtra("id", (short) 0));
            arguments.putString("name", getIntent().getStringExtra("name"));
            arguments.putString("avatar_url", getIntent().getStringExtra("avatar_url"));
            return arguments;
        }
        return null;
    }

    /**
     * Action to be made when pressing the return button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Navigate back to StudentListActivity activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
