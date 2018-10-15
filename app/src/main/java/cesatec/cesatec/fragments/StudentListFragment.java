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
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.R;
import cesatec.cesatec.adapters.StudentAdapter;
import cesatec.cesatec.models.Student;
import cesatec.cesatec.network.networkTasks.ApiFetchTask;

public class StudentListFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "StudentListFragment";
    boolean mTwoPane;
    private ArrayList<Student> studentList;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save studentList to bundle, so it can be used when the fragment is recreated
        if (studentList != null) {
            outState.putParcelableArrayList("student_list", studentList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            if (activity.findViewById(R.id.student_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;
            }

            // TODO Add FAB batch action
            FloatingActionButton fab = getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        if (savedInstanceState == null) {
            // When the fragment is created by the first time fetch the JSON from the server
            // save it on studentList
            Log.d(TAG, "onActivityCreated: Fetching data");
            new ApiFetchTask(getActivity(), this).execute(ApiConstants.API_USERS);
        } else {
            // if it's being recreated, use the data already retrieved to set up the recycler view
            Log.d(TAG, "onActivityCreated: Reusing data");
            studentList = savedInstanceState.getParcelableArrayList("student_list");
            setUpRecyclerView(getActivity(), studentList);
        }
    }

    public void setUpRecyclerView(Activity activity, ArrayList<Student> studentArrayList) {
        if (studentList == null) {
            // Save the retrieved array list for the first time
            // so it can be used when the fragment is recreated
            this.studentList = studentArrayList;
        }
        RecyclerView rvStudents = activity.findViewById(R.id.student_list);
        StudentAdapter adapter = new StudentAdapter(activity, studentArrayList);
        rvStudents.setAdapter(adapter);
        // Show the recycler view that will display the list
        rvStudents.setVisibility(View.VISIBLE);
        // Set the gridlayout to maximum number of columns possible
        rvStudents.setLayoutManager(
                new GridLayoutManager(activity,
                        calculateNoOfColumns(activity)));
    }

    private int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

}
