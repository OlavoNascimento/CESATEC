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

public class StudentReturnedFragment extends EnrollmentFragment {
    private static final String TAG = "StudentLeftFragment";

    private ArrayList<Enrollment> studentsThatReturned = new ArrayList<>();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store the enrollments list to a bundle,
        // so it can be used when the fragment is recreated
        if (studentsThatReturned != null) {
            outState.putParcelableArrayList("students_that_left", studentsThatReturned);
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

        // Hide the floating action button
        createStudentRegistryButton.hide();

        StudentListActivity activity = activityReference.get();
        if (activity != null) {
            TextView singleListHeader = activity.findViewById(R.id.student_single_list_header);
            singleListHeader.setText(
                    getString(R.string.single_list_header, "retornaram"));

            RecyclerView studentsRecyclerView = activity.findViewById(R.id.student_single_list);
            if (savedInstanceState == null) {
                new ApiCheckRegistriesForStudentsTask(
                        activity, this, activity.getRegistryTypeIdReturn()).execute();
            } else {
                studentsThatReturned = savedInstanceState.getParcelableArrayList(
                        "students_that_left");
                setUpRecyclerView(activity, studentsRecyclerView, studentsThatReturned);
            }
        }

    }

    @Override
    public void setUpRecyclerView(Activity activity,
                                  RecyclerView recyclerView,
                                  ArrayList<Enrollment> enrollments) {
        this.studentsThatReturned = enrollments;
        super.setUpRecyclerView(activity, recyclerView, enrollments);
    }
}
