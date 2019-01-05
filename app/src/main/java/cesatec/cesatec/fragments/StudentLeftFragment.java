package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.mainActivity.StudentListActivity;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.asyncTasks.ApiCheckRegistriesForStudentsTask;

public class StudentLeftFragment extends EnrollmentFragment {
    private static final String TAG = "StudentLeftFragment";

    private ArrayList<Enrollment> studentsThatLeft = new ArrayList<>();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the enrollments list to a bundle,
        // so it can be used when the fragment is recreated
        if (studentsThatLeft != null) {
            outState.putParcelableArrayList("students_that_left", studentsThatLeft);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_single_list_fragment,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final StudentListActivity activity = activityReference.get();

        // Send the enrollments on the enrollmentsUpdateStatus list
        // to the API when a long click is registered
        createStudentRegistryButton.show();
        createStudentRegistryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEnrollmentsStatus(view, activity, activity.getRegistryTypeIdReturn());
            }
        });

        if (activity != null) {
            TextView singleListHeader = activity.findViewById(R.id.student_single_list_header);
            singleListHeader.setText(
                    getString(R.string.single_list_header, "saíram"));

            RecyclerView studentsRecyclerView = activity.findViewById(R.id.student_single_list);
            if (savedInstanceState == null) {
                new ApiCheckRegistriesForStudentsTask(
                        activity, this, activity.getRegistryTypeIdExit()).execute();
            } else {
                studentsThatLeft = savedInstanceState.getParcelableArrayList(
                        "students_that_left");
                setUpRecyclerView(activity, studentsRecyclerView, studentsThatLeft);
            }
        }
    }

    /**
     * Send the current enrollments on the enrollmentsUpdateStatus list
     * to the API
     *
     * @param view View used to create the snack bar
     *             that display the status of the operation
     */
    @Override
    protected ArrayList<Enrollment> sendEnrollmentsStatus(final View view,
                                                          Activity activity,
                                                          int registryTypeId) {
        if (activity != null) {
            ArrayList<Enrollment> createdRegistries = super.sendEnrollmentsStatus(
                    view, activity, registryTypeId);
            // Update the recycler view
            RecyclerView studentsSingleList = activity.findViewById(R.id.student_single_list);
            for (Enrollment enrollment : createdRegistries) {
                studentsThatLeft.remove(enrollment);
            }
            setUpRecyclerView(activity, studentsSingleList, studentsThatLeft);
            return createdRegistries;
        }
        return null;
    }

    @Override
    public void setUpRecyclerView(Activity activity,
                                  RecyclerView recyclerView,
                                  ArrayList<Enrollment> enrollments) {
        this.studentsThatLeft = enrollments;
        super.setUpRecyclerView(activity, recyclerView, enrollments);
    }
}
