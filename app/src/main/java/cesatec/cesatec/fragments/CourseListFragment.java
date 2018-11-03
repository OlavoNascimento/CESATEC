package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.adapters.CourseAdapter;
import cesatec.cesatec.models.Course;
import cesatec.cesatec.network.asyncTasks.ApiFetchCoursesTask;
import cesatec.cesatec.onClickListeners.CourseOnClickListener;

public class CourseListFragment extends Fragment {
    private static final String TAG = "CourseListFragment";

    public static final int studentsThatLeftFragmentId = -1;
    public static final int studentsThatReturnedFragmentId = -2;
    protected boolean twoPane;
    private ArrayList<Course> courseList = new ArrayList<>();
    // Recreate the student list when changing the select course

    /**
     * Store the courses ArrayList on a bundle before the fragment is destroyed
     *
     * @param outState Bundle containing variables to be reused when the fragment is recreated
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the courses list to a bundle,
        // so it can be used when the fragment is recreated
        if (courseList != null) {
            outState.putParcelableArrayList("course_list", courseList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            this.twoPane = activity.findViewById(R.id.course_list_recycler_view) != null;

            if (savedInstanceState == null) {
                new ApiFetchCoursesTask(activity, this, twoPane).execute();
            } else {
                courseList = savedInstanceState.getParcelableArrayList("course_list");
                setUpRecyclerView(activity);
            }
        }
    }

    /**
     * Set the courseList variable
     *
     * @param courseList ArrayList to be set as the courseList
     */
    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
        addEnrollmentsThatLeftToList();
        addEnrollmentsThatReturnedToList();
    }

    private void addEnrollmentsThatLeftToList() {
        Course enrollmentsThatLeft = new Course(studentsThatLeftFragmentId,
                "Alunos que saíram", studentsThatLeftFragmentId, studentsThatLeftFragmentId);
        this.courseList.add(0, enrollmentsThatLeft);
    }

    private void addEnrollmentsThatReturnedToList() {
        Course enrollmentsThatReturned = new Course(studentsThatReturnedFragmentId,
                "Alunos que retornaram", studentsThatReturnedFragmentId,
                studentsThatReturnedFragmentId);
        this.courseList.add(1, enrollmentsThatReturned);
    }

    public void setUpCourseListToView(Activity activity) {
        if (courseList != null) {
            if (!twoPane) {
                setUpSpinner(activity);
            } else {
                setUpRecyclerView(activity);
                createInitialStudentFragment((AppCompatActivity) activity);
            }
        }
    }

    private void setUpSpinner(final Activity activity) {
        Spinner courseListSpinner = activity.findViewById(R.id.course_list_spinner);
        ArrayAdapter<Course> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseListSpinner.setAdapter(adapter);
        courseListSpinner.setSelection(2);
        courseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new CourseOnClickListener(activity, courseList.get(position)).onClick(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createInitialStudentFragment(AppCompatActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment studentListFragment = fm.findFragmentById(R.id.student_list_container);
        if (studentListFragment == null) {
            // Create the student list fragment if it doesn't already exist
            StudentListFragment newStudentListFragment = new StudentListFragment();
            fm.beginTransaction().add(
                    R.id.student_list_container, newStudentListFragment).commit();
        }
    }

    /**
     * Set an ArrayList of Students to a recycler view
     *
     * @param activity Activity containing the recycler view
     */
    private void setUpRecyclerView(Activity activity) {
        RecyclerView rvCourses = activity.findViewById(R.id.course_list_recycler_view);
        // Set the recycler view adapter
        CourseAdapter courseAdapter = new CourseAdapter(activity, courseList);
        rvCourses.setAdapter(courseAdapter);

        // Set the visibility of the recycler view that will display the list
        rvCourses.setVisibility(View.VISIBLE);

        // Set the gridlayout to the maximum number of columns possible
        rvCourses.setLayoutManager(
                new LinearLayoutManager(getContext()));
    }
}
