package cesatec.cesatec.activities.mainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.CourseListFragment;
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

        if (findViewById(R.id.course_list) != null) {
            // The list of courses will be present only on
            // large screen layouts (student_list.xml 900dp)
            Fragment coursesListFragment = fm.findFragmentById(R.id.course_list);
            if (coursesListFragment == null) {
                // Create the course list fragment if it doesn't already exist
                CourseListFragment newCourseListFragment = new CourseListFragment();
                fm.beginTransaction().add(R.id.course_list, newCourseListFragment).commit();
            }
        }

        // Try to reuse the student list fragment
        Fragment studentListFragment = fm.findFragmentById(R.id.student_list);
        if (studentListFragment == null) {
            // Create the student list fragment if it doesn't already exist
            StudentListFragment newStudentListFragment = new StudentListFragment();
            fm.beginTransaction().add(R.id.student_list, newStudentListFragment).commit();
        }
    }
}
