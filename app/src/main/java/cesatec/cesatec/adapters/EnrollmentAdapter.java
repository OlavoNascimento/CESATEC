package cesatec.cesatec.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.detailActivity.StudentDetailActivity;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Enrollment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter used to bind an ArrayList of Enrollments to a RecyclerView
 */

public class EnrollmentAdapter extends
        RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder> {
    private static final String TAG = "EnrollmentAdapter";

    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private ArrayList<Enrollment> enrollmentsList;

    // TODO Replace activity with context
    public EnrollmentAdapter(Activity activity,
                             StudentListFragment fragment,
                             ArrayList<Enrollment> enrollmentsList) {
        this.activityReference = new WeakReference<>(activity);
        this.fragmentReference = new WeakReference<>(fragment);
        this.enrollmentsList = enrollmentsList;
    }

    /**
     * Create a new view holder that contains an enrollment
     */
    @NonNull
    @Override
    public EnrollmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Activity activity = activityReference.get();
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate custom row layout
        View studentRow = inflater.inflate(R.layout.student_list_content,
                parent, false);
        // Return new holder instance
        return new EnrollmentViewHolder(studentRow);
    }

    /**
     * Set the view holder and listeners for each student in the list
     *
     * @param viewHolder ViewHolder that specifies the views on each view holder
     * @param position   Enrollment to select from the list
     */
    @Override
    public void onBindViewHolder(@NonNull EnrollmentViewHolder viewHolder, int position) {
        final Activity activity = activityReference.get();
        if (activity != null) {
            final Enrollment enrollment = enrollmentsList.get(position);

            // Set student name on list
            TextView studentNameView = viewHolder.studentNameView;
            // Replace each space in the student name with a line break
            String studentNameSurname = enrollment.getStudent().getName().replace(
                    " ", "\n");
            studentNameView.setText(studentNameSurname);

            // Set the student image if there is one
            viewHolder.setStudentImage(enrollment.getStudent().getAvatarUrl());

            CircleImageView studentAvatarView = viewHolder.studentAvatarView;
            // Check if the enrollment was already selected, avoiding
            // changing colors of different enrollments
            if (enrollment.isSelected()) {
                setViewBorderSelected(studentAvatarView);
            } else {
                setViewBorderUnselected(studentAvatarView);
            }
        }
    }

    private void setEnrollmentSelected(CircleImageView circleImageView,
                                       StudentListFragment fragment,
                                       Enrollment enrollment) {
        enrollment.setSelected(true);
        fragment.addToUpdateStatus(enrollment);

        setViewBorderSelected(circleImageView);
    }

    private void setViewBorderSelected(CircleImageView circleImageView) {
        circleImageView.setBorderColor(
                ContextCompat.getColor(circleImageView.getContext(), R.color.pendingImageBorder));
    }

    private void setEnrollmentUnselected(CircleImageView circleImageView,
                                         StudentListFragment fragment,
                                         Enrollment enrollment) {
        enrollment.setSelected(false);
        fragment.removeFromUpdateStatus(enrollment);

        setViewBorderUnselected(circleImageView);
    }

    private void setViewBorderUnselected(CircleImageView circleImageView) {
        circleImageView.setBorderColor(Color.BLACK);
    }

    /**
     * Returns the enrollments list size
     *
     * @return list size
     */
    @Override
    public int getItemCount() {
        // Returns zero if there's no enrollments on the list
        if (enrollmentsList == null) {
            return 0;
        }
        return enrollmentsList.size();
    }

    /**
     * Represents each enrollment on the list, displaying the student name and image
     */
    class EnrollmentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameView;
        private CircleImageView studentAvatarView;

        /**
         * Create a new StudentDetailFragment when a student
         * is selected
         */
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = activityReference.get();
                if (activity != null) {
                    // Get the selected enrolment from the enrollment list
                    Enrollment enrollment = enrollmentsList.get(getAdapterPosition());
                    Intent intent = new Intent(activity, StudentDetailActivity.class);
                    // Pass the enrollment to the detail activity
                    intent.putExtra("enrollment", enrollment);
                    activity.startActivity(intent);
                }
            }
        };

        /**
         * Select a student to create a new registry
         */
        private CircleImageView.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Parent fragment that contains the RecyclerView
                StudentListFragment fragment = fragmentReference.get();
                if (fragment != null) {
                    // Get the selected enrollment from the enrollment list
                    Enrollment enrollment = enrollmentsList.get(getAdapterPosition());
                    if (enrollment.isSelected()) {
                        // Unselected the enrollment if it's already selected
                        setEnrollmentUnselected(studentAvatarView, fragment, enrollment);
                    } else {
                        // Mark the enrollment as selected
                        setEnrollmentSelected(studentAvatarView, fragment, enrollment);
                    }
                    return true;
                }
                return false;
            }
        };

        private EnrollmentViewHolder(View itemView) {
            super(itemView);
            this.studentNameView = itemView.findViewById(R.id.student_list_name);
            this.studentAvatarView = itemView.findViewById(R.id.student_list_avatar);
            // Create detail activity on click
            studentAvatarView.setOnClickListener(onClickListener);
            // Select enrollment and add it to a list sent to the API
            studentAvatarView.setOnLongClickListener(onLongClickListener);
        }

        private void setStudentImage(String avatarUrl) {
            // Set student image on list
            // TODO Image border color based on authorization status
            // Get the student avatar url
            if (avatarUrl != null) {
                Picasso.get()
                        .load(avatarUrl)
                        .placeholder(R.drawable.student_loading)
                        .into(studentAvatarView);
            } else {
                studentAvatarView.setImageDrawable(
                        ContextCompat.getDrawable(
                                studentAvatarView.getContext(), R.drawable.student_fallback));
            }
        }
    }
}