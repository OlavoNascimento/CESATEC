package cesatec.cesatec.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import cesatec.cesatec.R;
import cesatec.cesatec.adapters.EnrollmentAdapter;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.asyncTasks.ApiCreateRegistryTask;
import cesatec.cesatec.network.asyncTasks.ApiFetchEnrollmentsTask;

/**
 * Fragment that displays all Enrollments in a RecyclerView
 */
public class StudentListFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StudentListFragment";

    private FloatingActionButton fab;
    private int selectedCourseId = -1;
    private ArrayList<Enrollment> enrollmentsList;
    private ArrayList<Enrollment> enrollmentsUpdateStatus = new ArrayList<>();
    private int enrollmentsUpdateStatusSize;
    private int successfulCreatedRegistries = 0;

    private BroadcastReceiver registriesCreatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Display the number of enrollments sent
            Log.d(TAG, "onReceive: received broadcast");
            String message = getString(R.string.student_enrollments_sent,
                    successfulCreatedRegistries, enrollmentsUpdateStatusSize);
            Snackbar.make(fab, message, Snackbar.LENGTH_LONG).show();
            resetSuccessfulCreatedRegistries();
        }
    };

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
        if (enrollmentsList != null) {
            outState.putParcelableArrayList("enrollments_list", enrollmentsList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            LocalBroadcastManager.getInstance(activity).registerReceiver(registriesCreatedReceiver,
                    new IntentFilter("all_registries_tasks_finished"));

            FloatingActionButton fabView = activity.findViewById(R.id.fab);
            this.fab = fabView;
            // Send the enrollments on the enrollmentsUpdateStatus list
            // to the API when a long click is registered
            fabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEnrollmentsStatus(view);
                }
            });

            Bundle arguments = getArguments();
            if (arguments != null) {
                selectedCourseId = arguments.getInt("course_id");
            }

            if (savedInstanceState == null) {
                // When the fragment is created by the first time fetch the JSON from the server
                // and save it on studentList
                new ApiFetchEnrollmentsTask(activity, this).execute();
            } else {
                // If the fragment is being recreated,
                // use the data already retrieved to set up the recycler view
                // Avoiding unnecessary requests to the API
                enrollmentsList = savedInstanceState.getParcelableArrayList("enrollments_list");
                setUpRecyclerView(activity);
            }
        }
    }

    /**
     * Set an ArrayList of enrollment to a recycler view
     * based on the current select course
     * @param activity Activity containing the recycler view
     */
    public void setUpRecyclerView(Activity activity) {
        if (enrollmentsList != null) {
            Log.d(TAG, "setUpRecyclerView: " + selectedCourseId);
            // Array list containing the enrollments of the selected course
            ArrayList<Enrollment> updatedEnrollmentList = new ArrayList<>();
            if (selectedCourseId != -1) {
                // Add all enrollments of the selected course to the ArrayList
                for (Enrollment enrollment : enrollmentsList) {
                    if (enrollment.getSubCourse().getParentCourseId() == selectedCourseId) {
                        updatedEnrollmentList.add(enrollment);
                    }
                }
            } else {
                // Add all enrollments in case the global
                // course is selected
                updatedEnrollmentList = enrollmentsList;
            }

            RecyclerView rvStudents = activity.findViewById(R.id.student_list);
            // Set the recycler view adapter
            EnrollmentAdapter enrollmentAdapter = new EnrollmentAdapter(activity,
                    this, updatedEnrollmentList);
            rvStudents.setAdapter(enrollmentAdapter);

            // Set the visibility of the recycler view that will display the list
            rvStudents.setVisibility(View.VISIBLE);

            // Set the gridlayout to the maximum number of columns possible
            rvStudents.setLayoutManager(
                    new GridLayoutManager(activity,
                            calculateNoOfColumns(activity)));
        }
    }

    /**
     * Calculate the maximum number of columns available for a recycler view
     *
     * @param context Context of the recycler view
     * @return Maximum number of columns
     */
    private int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    /**
     * Send the current enrollments on the enrollmentsUpdateStatus list
     * to the API
     *
     * @param view View used to create the snack bar
     *             that display the status of the operation
     */
    private void sendEnrollmentsStatus(final View view) {
        Activity activity = getActivity();
        if (activity != null) {
            // Message to be displayed  on the snack bar
            if (! enrollmentsUpdateStatus.isEmpty()) {
                // Creates a iterator so a item can be removed while iterating through the list
                ListIterator<Enrollment> iterator = enrollmentsUpdateStatus.listIterator();

                // Iterate through the enrollments setting them to false and removing them from
                // the enrollments to be updated list
                // TODO Add return registry
                AtomicInteger remainingThreads = new AtomicInteger(enrollmentsUpdateStatus.size());
                while(iterator.hasNext()) {
                    Enrollment enrollment = iterator.next();
                    // Create a new registry of the students
                    new ApiCreateRegistryTask(
                            activity, this, remainingThreads, enrollment).execute();
                    // Set the enrollment as unselected
                    enrollment.setSelected(false);
                    // Remove the enrollment from the list
                    iterator.remove();
                }

                // Update the recycler view
                setUpRecyclerView(activity);
            } else {
                // No enrollment was selected
                String message = getString(R.string.student_no_enrollments_sent);
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Used by ApiFetchEnrollmentsTask to save the retrieved enrollments
     * to the fragment list avoiding calling the API again
     *
     * @param enrollmentsList Enrollment list retrieved from the API
     *                        by the ApiFetchEnrollmentsTask
     */
    public void setEnrollmentsList(ArrayList<Enrollment> enrollmentsList) {
        // Save the retrieved array list for the first time
        // so it can be reused when the fragment is recreated
        this.enrollmentsList = enrollmentsList;
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