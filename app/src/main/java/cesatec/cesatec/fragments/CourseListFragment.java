package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.adapters.CourseAdapter;
import cesatec.cesatec.models.Course;
import cesatec.cesatec.network.AsyncTasks.ApiFetchCoursesTask;

public class CourseListFragment extends Fragment {
    private static final String TAG = "CourseListFragment";

    private ArrayList<Course> courseList = new ArrayList<>();

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
            if (savedInstanceState == null) {
                new ApiFetchCoursesTask(activity, this).execute();
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
        addGlobalCourse();
    }

    /**
     * Add a course that contains all enrollments to the courses list
     */
    private void addGlobalCourse() {
        // Add a course that contains all enrollments
        Course globalCourse = new Course(-1, getString(R.string.global_course));
        courseList.add(0, globalCourse);
    }

    /**
     * Set an ArrayList of Students to a recycler view
     *
     * @param activity Activity containing the recycler view
     */
    public void setUpRecyclerView(Activity activity) {
        if (courseList != null) {
            RecyclerView rvCourses = activity.findViewById(R.id.course_list);
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
}
