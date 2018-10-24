package cesatec.cesatec.activities.mainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.StudentListFragment;

/**
 * Displays a list containing all students
 */
public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = "StudentListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Set the title of the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FragmentManager fm = getSupportFragmentManager();
        // Try to reuse the student list fragment
        Fragment fetchFragment = fm.findFragmentByTag("student_list_fragment");
        if (fetchFragment == null) {
            // Recreate the student list fragment if it doesn't already exist
            StudentListFragment newFetchFragment = new StudentListFragment();
            fm.beginTransaction().add(newFetchFragment, "student_list_fragment").commit();
        }
    }
}
