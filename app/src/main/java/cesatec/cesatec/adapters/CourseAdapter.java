package cesatec.cesatec.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private WeakReference<Activity> activityReference;
    private ArrayList<Course> coursesList;
    private View lastSelectedCourse;

    public CourseAdapter(Activity activity, ArrayList<Course> coursesList) {
        this.activityReference = new WeakReference<>(activity);
        this.coursesList = coursesList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate custom row layout
        View courseRow = inflater.inflate(R.layout.course_list_content, parent, false);
        // Return new holder instance
        return new CourseViewHolder(courseRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder viewHolder, int position) {
        final AppCompatActivity activity = (AppCompatActivity) activityReference.get();
        if (activity != null) {
            final Course course = coursesList.get(position);

            // Set the first course as selected
            if (position == 0) {
                View viewHolderBackground = viewHolder.courseNameView;
                markCourseAsSelected(activity, viewHolderBackground);
                lastSelectedCourse = viewHolderBackground;
            }

            // Set student name on list
            TextView courseNameView = viewHolder.courseNameView;
            courseNameView.setText(course.getName());

            // Recreate the student list when changing the select course
            courseNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Pass the course id to the StudentListFragment
                    // only displaying the appropriated enrollments
                    Bundle arguments = new Bundle();
                    arguments.putInt("course_id", course.getId());
                    arguments.putInt("first_sub_course_id", course.getFirstSubCourseId());
                    arguments.putInt("second_sub_course_id", course.getSecondSubCourseId());
                    StudentListFragment newStudentListFragment = new StudentListFragment();
                    newStudentListFragment.setArguments(arguments);
                    // Replace the current StudentListFragment with a new one
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.student_list, newStudentListFragment).commit();
                    updateLastSelectCourse(activity, view);
                }
            });
        }
    }

    private void updateLastSelectCourse(Activity activity, View view) {
        markCourseAsSelected(activity, view);
        // Reset the background color of the last selected course
        lastSelectedCourse.setBackgroundColor(Color.TRANSPARENT);
        // Update the last selected course variable
        lastSelectedCourse = view;
    }

    private void markCourseAsSelected(Activity activity, View view) {
        // Change the background color of the selected course
        view.setBackgroundColor(
                ContextCompat.getColor(activity, R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        // Returns zero if there's no courses on the list
        if (coursesList == null) {
            return 0;
        }
        return coursesList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView courseNameView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            this.courseNameView = itemView.findViewById(R.id.course_list_name);
        }
    }
}
