package cesatec.cesatec.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cesatec.cesatec.R;
import cesatec.cesatec.activities.detailActivity.StudentDetailActivity;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.network.ApiCreateRegistryTask;

/**
 * Adapter used to bind an ArrayList of Enrollments to a RecyclerView
 */
public class EnrollmentAdapter extends RecyclerView.Adapter<EnrollmentAdapter.ViewHolder> {
    private static final String TAG = "EnrollmentAdapter";

    private WeakReference<Context> contextReference;
    private ArrayList<Enrollment> enrollmentsList;

    public EnrollmentAdapter(Context context, ArrayList<Enrollment> enrollmentsList) {
        this.contextReference = new WeakReference<>(context);
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
     * Set the view holder and listeners for each user in the list
     *
     * @param viewHolder ViewHolder that specifies the views on each view holder
     * @param position   Enrollment to select from the list
     */
    @Override
    public void onBindViewHolder(@NonNull EnrollmentAdapter.ViewHolder viewHolder, int position) {
        final Context context = contextReference.get();
        if (context != null) {
            final Enrollment enrollment = enrollmentsList.get(position);

            TextView textView = viewHolder.nameTextView;
            textView.setText(enrollment.getStudent().getName());
            ImageView avatarView = viewHolder.avatarImageView;
            // TODO Decode image and load it using a placeholder as fallback
            // TODO Image color based on authorization status

            // Create detail activity on click
            avatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StudentDetailActivity.class);
                    intent.putExtra("enrollment", enrollment);
                    context.startActivity(intent);
                }
            });
            // TODO Send multiple students
            // Send data to server on long click
            avatarView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ArrayList<Enrollment> studentsNotPresent = new ArrayList<>();
                    // TODO Remove and add automatically on select
                    studentsNotPresent.add(enrollment);
                    for (Enrollment enrollment : studentsNotPresent) {
                        ApiCreateRegistryTask apiSend = new ApiCreateRegistryTask(context, enrollment);
                        apiSend.execute();
                    }
                    return true;
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