package cesatec.cesatec.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.ListIterator;

import cesatec.cesatec.R;
import cesatec.cesatec.adapters.EnrollmentAdapter;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.ApiFetchEnrollmentsTask;

/**
 * Fragment that displays all Enrollments in a RecyclerView
 */
public class StudentListFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StudentListFragment";

    private boolean twoPane;
    private ArrayList<Enrollment> enrollmentsList;
    private ArrayList<Enrollment> enrollmentsUpdateStatus = new ArrayList<>();

    /**
     * Save the Enrollment ArrayList on a bundle before the fragment is destroyed
     *
     * @param outState Bundle containing variables to be reused when the fragment is recreated
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save studentList to bundle, so it can be used when the fragment is recreated
        if (enrollmentsList != null) {
            outState.putParcelableArrayList("enrollments_list", enrollmentsList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            // TODO Implement two panels
            if (activity.findViewById(R.id.course_list) != null) {
                // The list of courses will be present only on
                // large screen layouts (student_list.xml 900dp)
                twoPane = true;
            }

            FloatingActionButton fab = activity.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEnrollmentsStatus(view);
                }
            });

            if (savedInstanceState == null) {
                // When the fragment is created by the first time fetch the JSON from the server
                // and save it on studentList
                new ApiFetchEnrollmentsTask(activity, this).execute();
            } else {
                // If the fragment is being recreated,
                // use the data already retrieved to set up the recycler view
                // Avoiding unnecessary requests to the API
                enrollmentsList = savedInstanceState.getParcelableArrayList("enrollments_list");
                setUpRecyclerView(activity, enrollmentsList);
            }
        }

    }

    /**
     * Set an ArrayList of Students to a recycler view
     *
     * @param activity            Activity containing the recycler view
     * @param enrollmentArrayList ArrayList of Students to bind to the recycler view
     */
    public void setUpRecyclerView(Activity activity, ArrayList<Enrollment> enrollmentArrayList) {
        if (enrollmentsList == null) {
            // Save the retrieved array list for the first time
            // so it can be reused when the fragment is recreated
            this.enrollmentsList = enrollmentArrayList;
        }
        RecyclerView rvStudents = activity.findViewById(R.id.student_list);
        // Set the recycler view student adapter
        EnrollmentAdapter adapter = new EnrollmentAdapter(activity,
                this, enrollmentArrayList);
        rvStudents.setAdapter(adapter);
        // Show the recycler view that will display the list
        rvStudents.setVisibility(View.VISIBLE);
        // Set the gridlayout to maximum number of columns possible
        rvStudents.setLayoutManager(
                new GridLayoutManager(activity,
                        calculateNoOfColumns(activity)));
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

    private void sendEnrollmentsStatus(View view) {
        Activity activity = getActivity();
        if (activity != null) {
            // Message to be displayed  on the snack bar
            String message;
            if (! enrollmentsUpdateStatus.isEmpty()) {
                // Display the number of enrollments sent
                message = getString(R.string.student_enrollments_sent,
                        enrollmentsUpdateStatus.size());

                // Creates a iterator so a item can be removed while iterating through the list
                ListIterator<Enrollment> iterator = enrollmentsUpdateStatus.listIterator();

                // Iterate through the enrollments setting them to false and removing them from
                // the enrollments to be updated list
                while(iterator.hasNext()) {
                    Enrollment enrollment = iterator.next();
                    // Set the enrollment as unselected
                    enrollment.setSelected(false);
                    // Remove the enrollment from the list
                    iterator.remove();
                }

                // Update the recycler view
                setUpRecyclerView(activity, enrollmentsList);
            } else {
                // No enrollment was selected
                message = getString(R.string.student_no_enrollments_sent);
            }
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void addToUpdateStatus(Enrollment enrollment) {
        enrollmentsUpdateStatus.add(enrollment);
    }

    public void removeFromUpdateStatus(Enrollment enrollment) {
        enrollmentsUpdateStatus.remove(enrollment);
    }
}
