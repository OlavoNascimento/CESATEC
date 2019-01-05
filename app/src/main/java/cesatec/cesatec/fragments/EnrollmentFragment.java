package cesatec.cesatec.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.mainActivity.StudentListActivity;
import cesatec.cesatec.adapters.EnrollmentAdapter;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.asyncTasks.ApiCreateRegistryTask;

public class EnrollmentFragment extends Fragment {
    private static final String TAG = "EnrollmentFragment";

    protected WeakReference<StudentListActivity> activityReference;

    protected FloatingActionButton createStudentRegistryButton;

    protected ArrayList<Enrollment> enrollmentsUpdateStatus = new ArrayList<>();
    private int enrollmentsUpdateStatusSize;
    private int successfulCreatedRegistries = 0;

    private BroadcastReceiver registriesCreatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Display the number of enrollments sent
            Log.d(TAG, "onReceive: received broadcast");
            if (isAdded()) {
                String message = getString(R.string.student_enrollments_sent,
                        successfulCreatedRegistries, enrollmentsUpdateStatusSize);
                Snackbar.make(createStudentRegistryButton, message, Snackbar.LENGTH_LONG).show();
                resetSuccessfulCreatedRegistries();
            }
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StudentListActivity activity = (StudentListActivity) getActivity();
        this.activityReference = new WeakReference<>(activity);
        if (activity != null) {
            this.createStudentRegistryButton = activity.findViewById(
                    R.id.create_student_registry_button);
            createStudentRegistryButton.show();

            LocalBroadcastManager.getInstance(activity).registerReceiver(registriesCreatedReceiver,
                    new IntentFilter("all_registries_tasks_finished"));
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
            ArrayList<Enrollment> createdStudentsRegistries = new ArrayList<>();
            // Message to be displayed  on the snack bar
            if (enrollmentsUpdateStatus != null && !enrollmentsUpdateStatus.isEmpty()) {
                // Creates a iterator so a item can be removed while iterating through the list
                ListIterator<Enrollment> iterator = enrollmentsUpdateStatus.listIterator();

                // Iterate through the enrollments setting them to false and removing them from
                // the enrollments to be updated list
                AtomicInteger remainingThreads = new AtomicInteger(enrollmentsUpdateStatus.size());
                while (iterator.hasNext()) {
                    Enrollment enrollment = iterator.next();
                    // Create a new registry of the students
                    new ApiCreateRegistryTask(
                            activity, this,
                            remainingThreads, registryTypeId, enrollment).execute();
                    // Set the enrollment as unselected
                    enrollment.setSelected(false);
                    createdStudentsRegistries.add(enrollment);
                }
            } else {
                // No enrollment was selected
                String message = getString(R.string.student_no_enrollments_sent);
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
            return createdStudentsRegistries;
        }
        return null;
    }

    /**
     * Set an ArrayList of enrollment to a recycler view
     * based on the current select course
     */
    public void setUpRecyclerView(Activity activity,
                                  RecyclerView recyclerView,
                                  ArrayList<Enrollment> enrollments) {
        // Array list containing the enrollments of the selected course
        if (recyclerView != null && enrollments != null) {
            // Set the recycler view adapter
            EnrollmentAdapter enrollmentAdapter = new EnrollmentAdapter(activity,
                    this, enrollments);
            recyclerView.setAdapter(enrollmentAdapter);

            if (activity.findViewById(R.id.course_list_recycler_view) == null) {
                recyclerView.setNestedScrollingEnabled(false);
            }

            // Set the visibility of the recycler view that will display the list
            recyclerView.setVisibility(View.VISIBLE);

            // Set the gridlayout to the maximum number of columns possible
            recyclerView.setLayoutManager(
                    new GridLayoutManager(activity, calculateNoOfColumns(activity)));
        }
    }

    /**
     * Calculate the maximum number of columns available for a recycler view
     *
     * @param context Context of the recycler view
     * @return Maximum number of columns
     */
    protected int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    /**
     * Add an enrollment to the enrollments to be updated list
     * Used by EnrollmentAdapter class to add an
     * enrollment to the enrollmentsUpdateStatus list
     *
     * @param enrollment Enrollment to be added to the list
     */
    public void addToUpdateStatus(Enrollment enrollment) {
        this.enrollmentsUpdateStatus.add(enrollment);
        this.enrollmentsUpdateStatusSize = enrollmentsUpdateStatus.size();
    }

    /**
     * Remove an enrollment from the enrollments to be updated list
     * Used by EnrollmentAdapter class to remove an
     * enrollment from the enrollmentsUpdateStatus list
     *
     * @param enrollment Enrollment to be removed from the list
     */
    public void removeFromUpdateStatus(Enrollment enrollment) {
        enrollmentsUpdateStatus.remove(enrollment);
    }

    public void addToSuccessfulCreatedRegistries() {
        this.successfulCreatedRegistries += 1;
    }

    public void resetSuccessfulCreatedRegistries() {
        this.successfulCreatedRegistries = 0;
    }
}
