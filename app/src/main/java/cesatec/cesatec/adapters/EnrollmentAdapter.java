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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.detailActivity.StudentDetailActivity;
import cesatec.cesatec.fragments.StudentListFragment;
import cesatec.cesatec.models.Enrollment;

/**
 * Adapter used to bind an ArrayList of Enrollments to a RecyclerView
 */
public class EnrollmentAdapter extends
        RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder> {
    private static final String TAG = "EnrollmentAdapter";

    private WeakReference<Activity> activityReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private ArrayList<Enrollment> enrollmentsList;

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

            // Set student image on list
            // TODO Use placeholder as fallback
            // TODO Use placeholder when loading
            // TODO Image border color based on authorization status
            ImageView studentAvatarView = viewHolder.studentAvatarView;
            // Get the student avatar url
            String avatarUrl = enrollment.getStudent().getAvatarUrl();
            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).into(studentAvatarView);
            }

            // Check if the enrollment was already selected, avoiding
            // changing colors of different enrollments
            if (enrollment.isSelected()) {
                setEnrollmentSelected(studentAvatarView, activity);
            } else {
                setEnrollmentUnselected(studentAvatarView);
            }

            // Create detail activity on click
            studentAvatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, StudentDetailActivity.class);
                    intent.putExtra("enrollment", enrollment);
                    activity.startActivity(intent);
                }
            });

            // Select enrollment and add it to a list sent to the API
            studentAvatarView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    StudentListFragment fragment = fragmentReference.get();
                    if (fragment != null) {
                        if (enrollment.isSelected()) {
                            // Unselected the enrollment if it's already selected
                            fragment.removeFromUpdateStatus(enrollment);
                            enrollment.setSelected(false);
                            setEnrollmentUnselected(view);
                        } else {
                            // Mark the enrollment as selected
                            fragment.addToUpdateStatus(enrollment);
                            enrollment.setSelected(true);
                            setEnrollmentSelected(view, activity);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
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
     * Change the color of a Enrollment avatarView, symbolizing the enrollment has been selected
     *
     * @param avatarView Avatar view to change background
     * @param activity   Activity of the avatar view
     */
    private void setEnrollmentSelected(View avatarView, Activity activity) {
        avatarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
    }

    /**
     * Reset the color of a enrollment background
     *
     * @param avatarView Avatar view to reset background color
     */
    private void setEnrollmentUnselected(View avatarView) {
        avatarView.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Represents each enrollment on the list, displaying the student name and image
     */
    class EnrollmentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameView;
        private ImageView studentAvatarView;

        private EnrollmentViewHolder(View itemView) {
            super(itemView);
            this.studentNameView = itemView.findViewById(R.id.student_list_name);
            this.studentAvatarView = itemView.findViewById(R.id.student_list_avatar);
        }
    }
}