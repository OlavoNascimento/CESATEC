package cesatec.cesatec.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cesatec.cesatec.network.ApiCreateRegistryTask;

/**
 * Adapter used to bind an ArrayList of Enrollments to a RecyclerView
 */
public class EnrollmentAdapter extends RecyclerView.Adapter<EnrollmentAdapter.ViewHolder> {
    private static final String TAG = "EnrollmentAdapter";

    private WeakReference<Context> contextReference;
    private WeakReference<StudentListFragment> fragmentReference;
    private ArrayList<Enrollment> enrollmentsList;

    public EnrollmentAdapter(Context context,
                             StudentListFragment fragment,
                             ArrayList<Enrollment> enrollmentsList) {
        this.contextReference = new WeakReference<>(context);
        this.fragmentReference = new WeakReference<>(fragment);
        this.enrollmentsList = enrollmentsList;
    }

    /**
     * Create a new adapter view holder, containing an enrollment
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate custom row layout
        View detailRow = inflater.inflate(R.layout.student_list_content, parent, false);
        // Return new holder instance
        return new ViewHolder(detailRow);
    }

    /**
     * Set the view holder and listeners for each student in the list
     *
     * @param viewHolder ViewHolder that specifies the views on each view holder
     * @param position   Enrollment to select from the list
     */
    @Override
    public void onBindViewHolder(@NonNull EnrollmentAdapter.ViewHolder viewHolder, int position) {
        final Context context = contextReference.get();
        if (context != null) {
            final Enrollment enrollment = enrollmentsList.get(position);

            // Set student name on list
            TextView textView = viewHolder.nameTextView;
            textView.setText(enrollment.getStudent().getName());

            // Set student image on list
            // TODO Use placeholder as fallback
            // TODO Use placeholder when loading
            // TODO Image border color based on authorization status
            ImageView avatarView = viewHolder.avatarImageView;
            String avatarUrl = enrollment.getStudent().getAvatarUrl();
            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).into(avatarView);
            }

            // Check if the enrollment was already selected, avoiding
            // changing colors of different enrollments
            if (enrollment.isSelected()) {
                setEnrollmentSelected(avatarView, context);
            } else {
                setEnrollmentUnselected(avatarView);
            }

            // Create detail activity on click
            avatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StudentDetailActivity.class);
                    intent.putExtra("enrollment", enrollment);
                    context.startActivity(intent);
                }
            });

            // Select enrollment and add it to a list sent to the API
            avatarView.setOnLongClickListener(new View.OnLongClickListener() {
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
                            setEnrollmentSelected(view, context);
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
     * @param avatarView Avatar view to change background
     * @param context Context of the avatar view
     */
    private void setEnrollmentSelected(View avatarView, Context context) {
        avatarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    /**
     * Reset the color of a enrollment background
     * @param avatarView Avatar view to reset background color
     */
    private void setEnrollmentUnselected(View avatarView) {
        avatarView.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Represents each enrollment on the list, displaying the student name and image
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView avatarImageView;

        private ViewHolder(View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.name);
            this.avatarImageView = itemView.findViewById(R.id.avatar);
        }
    }
}