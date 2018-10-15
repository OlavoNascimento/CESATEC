package cesatec.cesatec.activities.mainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.StudentListFragment;

public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = "StudentListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fetchFragment = fm.findFragmentByTag("fetch_fragment");
        if (fetchFragment == null) {
            StudentListFragment newFetchFragment = new StudentListFragment();
            Log.d(TAG, "onCreate: creating fragment");
            fm.beginTransaction().add(newFetchFragment, "fetch_fragment").commit();
        } else {
            Log.d(TAG, "onCreate: reusing fragment");
        }
    }

}
