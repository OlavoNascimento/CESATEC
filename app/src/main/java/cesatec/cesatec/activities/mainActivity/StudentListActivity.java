package cesatec.cesatec.activities.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jakewharton.threetenabp.AndroidThreeTen;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.CourseListFragment;
import cesatec.cesatec.models.RegistryTypes;
import cesatec.cesatec.network.asyncTasks.ApiGetRegistryTypesTask;

/**
 * Displays a list containing all students
 */
public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = "StudentListActivity";

    private RegistryTypes registryTypes;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the register types to a bundle,
        // so it can be used when the activity is recreated
        if (registryTypes != null) {
            outState.putParcelable("register_types", registryTypes);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        if (savedInstanceState == null) {
            new ApiGetRegistryTypesTask(this).execute();
        } else {
            this.registryTypes = savedInstanceState.getParcelable("register_types");
        }

        // Start timezone information
        AndroidThreeTen.init(this);

        // Set the title of the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FragmentManager fm = getSupportFragmentManager();

        // The list of courses will be present only on
        // large screen layouts (student_list.xml 900dp)
        Fragment courseListFragment = fm.findFragmentById(R.id.course_list_container);
        if (courseListFragment == null) {
            // Create the course list fragment if it doesn't already exist
            CourseListFragment newCourseListFragment = new CourseListFragment();
            fm.beginTransaction().add(
                    R.id.course_list_container, newCourseListFragment).commit();
        }
    }

    public void setRegistryTypes(RegistryTypes registryTypes) {
        this.registryTypes = registryTypes;
    }

    public int getRegistryTypeIdExit() {
        return registryTypes.getFieldTypeExitId();
    }

    public int getRegistryTypeIdReturn() {
        return registryTypes.getFieldTypeReturnId();
    }
}
