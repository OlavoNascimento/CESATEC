package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.mainActivity.StudentListActivity;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.models.SubCourse;
import cesatec.cesatec.network.asyncTasks.ApiFetchSubCourseTask;

/**
 * Fragment that displays all Enrollments in a RecyclerView
 */
public class StudentListFragment extends EnrollmentFragment {
    private static final String TAG = "StudentListFragment";

    private int firstSubCourseId = 1;
    private int secondSubCourseId = 2;

    private SubCourse firstSubCourse;
    private SubCourse secondSubCourse;

    /**
     * Store the enrollments ArrayList on a bundle before the fragment is destroyed
     *
     * @param outState Bundle containing variables to be reused when the fragment is recreated
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the enrollments list to a bundle,
        // so it can be used when the fragment is recreated
        if (firstSubCourse != null) {
            outState.putParcelable("first_sub_course", firstSubCourse);
        }
        if (secondSubCourse != null) {
            outState.putParcelable("second_sub_course", secondSubCourse);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.student_dual_list_fragment,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final StudentListActivity activity = activityReference.get();
        if (activity != null) {
            // Send the enrollments on the enrollmentsUpdateStatus list
            // to the API when a long click is registered
            createStudentRegistryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEnrollmentsStatus(view, activity, activity.getRegistryTypeIdExit());
                }
            });

            Bundle arguments = getArguments();
            if (arguments != null) {
                firstSubCourseId = arguments.getInt("first_sub_course_id");
                secondSubCourseId = arguments.getInt("second_sub_course_id");
            }

            if (savedInstanceState == null) {
                // When the fragment is created by the first time fetch the JSON from the server
                // and save it on studentList
                new ApiFetchSubCourseTask(activity, this, firstSubCourseId).execute();
                new ApiFetchSubCourseTask(activity, this, secondSubCourseId).execute();
            } else {
                // If the fragment is being recreated,
                // use the data already retrieved to set up the recycler view
                // Avoiding unnecessary requests to the API
                firstSubCourse = savedInstanceState.getParcelable("first_sub_course");
                secondSubCourse = savedInstanceState.getParcelable("second_sub_course");
                setUpFirstSubCourseRecyclerView(activity);
                setUpSecondSubCourseRecyclerView(activity);
            }
        }
    }

    /**
     * @param activity Activity containing the recycler view
     */
    public void setUpFirstSubCourseRecyclerView(Activity activity) {
        // Array list containing the enrollments of the selected course
        if (firstSubCourse != null && firstSubCourse.getEnrollments() != null) {
            RecyclerView rvFirstSubCourse = activity.findViewById(R.id.first_sub_course_student_list);
            super.setUpRecyclerView(activity, rvFirstSubCourse, firstSubCourse.getEnrollments());
        }
    }

    /**
     * @param activity Activity containing the recycler view
     */
    public void setUpSecondSubCourseRecyclerView(Activity activity) {
        // Array list containing the enrollments of the selected course
        if (secondSubCourse != null && secondSubCourse.getEnrollments() != null) {
            RecyclerView rvSecondSubCourse = activity.findViewById(R.id.second_sub_course_student_list);
            super.setUpRecyclerView(activity, rvSecondSubCourse, secondSubCourse.getEnrollments());
        }
    }

    /**
     * Send the current enrollments on the enrollmentsUpdateStatus list
     * to the API
     *
     * @param view View used to create the snack bar
     *             that display the status of the operation
     */
    protected ArrayList<Enrollment> sendEnrollmentsStatus(final View view,
                                                          Activity activity,
                                                          int registryTypeId) {
        if (activity != null) {
            ArrayList<Enrollment> createdRegistries = super.sendEnrollmentsStatus(
                    view, activity, registryTypeId);
            // Update the recycler view
            setUpFirstSubCourseRecyclerView(activity);
            setUpSecondSubCourseRecyclerView(activity);
            return createdRegistries;
        }
        return null;
    }

    public void setFirstSubCourse(SubCourse subCourse) {
        // Save the retrieved array list for the first time
        // so it can be reused when the fragment is recreated
        this.firstSubCourse = subCourse;
    }

    public void setSecondSubCourse(SubCourse subCourse) {
        // Save the retrieved array list for the first time
        // so it can be reused when the fragment is recreated
        this.secondSubCourse = subCourse;
    }
}