package cesatec.cesatec.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import cesatec.cesatec.models.Course;
import cesatec.cesatec.onClickListeners.CourseOnClickListener;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private static final String TAG = "CourseAdapter";

    private View lastSelectedCourse;
    private WeakReference<Activity> activityReference;
    private ArrayList<Course> courseList;

    public CourseAdapter(Activity activity, ArrayList<Course> courseList) {
        this.activityReference = new WeakReference<>(activity);
        this.courseList = courseList;
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
            final Course course = courseList.get(position);

            // Set the third course as selected by default
            if (position == 2) {
                View viewHolderBackground = viewHolder.courseNameView;
                markCourseAsSelected(activity, viewHolderBackground);
                lastSelectedCourse = viewHolderBackground;
            }

            // Set student name on list
            TextView courseNameView = viewHolder.courseNameView;
            courseNameView.setText(course.getName());
        }
    }

    private void updateLastSelectCourse(Activity activity, View view) {
        if (view != lastSelectedCourse) {
            markCourseAsSelected(activity, view);
            // Reset the background color of the last selected course
            lastSelectedCourse.setBackgroundColor(Color.TRANSPARENT);
            // Update the last selected course variable
            lastSelectedCourse = view;
        }
    }

    private void markCourseAsSelected(Activity activity, View view) {
        // Change the background color of the selected course
        view.setBackgroundColor(
                ContextCompat.getColor(activity, R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        // Returns zero if there's no courses on the list
        if (courseList == null) {
            return 0;
        }
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView courseNameView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            this.courseNameView = itemView.findViewById(R.id.course_list_name);
            courseNameView.setOnClickListener(getCourseRecyclerViewClickListener());
        }

        private View.OnClickListener getCourseRecyclerViewClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Activity activity = activityReference.get();
                    Course course = courseList.get(getAdapterPosition());
                    final CourseOnClickListener courseOnClickListener = new CourseOnClickListener(
                            activity, course);
                    courseOnClickListener.onClick(view);
                    updateLastSelectCourse(activity, view);
                }
            };
        }
    }
}
