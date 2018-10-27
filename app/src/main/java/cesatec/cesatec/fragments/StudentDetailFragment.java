package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cesatec.cesatec.R;
import cesatec.cesatec.models.Enrollment;

/**
 * Fragment that contains all details of a Student
 */
public class StudentDetailFragment extends Fragment {
    static final String TAG = "StudentDetailFragment";

    private Enrollment enrollment;

    /**
     * Set the enrollment using the arguments passed to the fragment
     *
     * @param savedInstanceState Saved fragmented data to be reused
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            this.enrollment = arguments.getParcelable("enrollment");
        }
    }

    /**
     * Set up the views when they are available
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setDetailViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * Set the information of the enrollment on the UI
     */
    private void setDetailViews() {
        Activity activity = getActivity();
        if (activity != null) {
            // Set the student name on the toolbar title
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.detail_toolbar_layout);
            appBarLayout.setTitle(enrollment.getStudent().getName());

            // Set the student id
            TextView idView = activity.findViewById(R.id.detail_student_ra);
            String idText = getString(R.string.student_detail_ra, enrollment.getStudent().getRa());
            idView.setText(idText);

            // TODO Add placeholder
            // Set the student image
            String avatarUrl = enrollment.getStudent().getAvatarUrl();
            if (avatarUrl != null) {
                ImageView avatarView = activity.findViewById(R.id.detail_student_avatar);
                Picasso.get().load(avatarUrl).into(avatarView);
            }

            // Set the student group
            TextView groupView = activity.findViewById(R.id.detail_student_group);
            String groupText = getString(R.string.student_detail_group, enrollment.getGroup());
            groupView.setText(groupText);

            // Set the student course
            TextView courseView = activity.findViewById(R.id.detail_student_course);
            String courseText = getString(R.string.student_detail_course, enrollment.getCourse());
            courseView.setText(courseText);
        }
    }
}
