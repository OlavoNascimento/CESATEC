package cesatec.cesatec.onClickListeners;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.ref.WeakReference;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.CourseListFragment;
import cesatec.cesatec.fragments.StudentLeftFragment;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.fragments.StudentReturnedFragment;
import cesatec.cesatec.models.Course;

public class CourseOnClickListener implements View.OnClickListener {
    private final static String TAG = "CourseOnClickListener";

    private WeakReference<Activity> activityReference;
    private Course course;

    public CourseOnClickListener(Activity activity, Course course) {
        this.activityReference = new WeakReference<>(activity);
        this.course = course;
    }

    @Override
    public void onClick(View view) {
        AppCompatActivity activity = (AppCompatActivity) activityReference.get();
        if (activity != null) {
            // Get the selected course from the course list
            if (course.getId() == CourseListFragment.studentsThatLeftFragmentId) {
                StudentLeftFragment newStudentLeftFragment = new StudentLeftFragment();
                replaceFragment(activity, newStudentLeftFragment);
            } else if (course.getId() == CourseListFragment.studentsThatReturnedFragmentId) {
                StudentReturnedFragment newStudentReturnedFragment = new StudentReturnedFragment();
                // Replace the current StudentListFragment with a new one
                replaceFragment(activity, newStudentReturnedFragment);
            } else {
                Bundle arguments = new Bundle();
                arguments.putInt("first_sub_course_id", course.getFirstSubCourseId());
                arguments.putInt("second_sub_course_id", course.getSecondSubCourseId());
                StudentListFragment newStudentListFragment = new StudentListFragment();
                newStudentListFragment.setArguments(arguments);
                // Replace the current StudentListFragment with a new one
                replaceFragment(activity, newStudentListFragment);
            }
        }
    }

    private void replaceFragment(AppCompatActivity activity, Fragment newFragment) {
        int oldFragmentId = R.id.student_list_container;
        // Replace the current StudentListFragment with a StudentLeftFragment
        activity.getSupportFragmentManager().beginTransaction()
                .replace(oldFragmentId, newFragment).commit();
    }

}
