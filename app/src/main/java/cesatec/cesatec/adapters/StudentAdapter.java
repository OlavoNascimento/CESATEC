package cesatec.cesatec.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.R;
import cesatec.cesatec.activities.detailActivity.StudentDetailActivity;
import cesatec.cesatec.models.Student;
import cesatec.cesatec.network.networkTasks.ApiSendTask;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private static final String TAG = "StudentAdapter";

    private Context context;
    private ArrayList<Student> studentList;

    public StudentAdapter(Context context, ArrayList<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    /**
     * Inflate the adapter view holder
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
     * @param viewHolder ViewHolder that represents each attributes
     * @param position   Position of the list to insert the user
     */
    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder viewHolder, int position) {
        final Student student = studentList.get(position);

        TextView textView = viewHolder.nameTextView;
        Log.d(TAG, "onBindViewHolder: " + student.getName());
        textView.setText(student.getName());
        ImageView avatarView = viewHolder.avatarImageView;
        Picasso.get()
                .load(student.getAvatarUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(avatarView);

        // Create detail activity on click
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Single click");
                Intent intent = new Intent(context, StudentDetailActivity.class);
                intent.putExtra("id", student.getId());
                intent.putExtra("name", student.getName());
                intent.putExtra("avatar_url", student.getAvatarUrl());
                context.startActivity(intent);
            }
        });
        // Send data to server on long click
        avatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: Long click confirmed");
                HashMap<String, String> properties = new HashMap<>();
                properties.put("name", student.getName());
                ApiSendTask apiSend = new ApiSendTask(context, properties);
                apiSend.execute(ApiConstants.API_USERS);
                return true;
            }
        });
    }

    /**
     * Returns the list size
     *
     * @return list size
     */
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    /**
     * Represents each user on the list and it's atributes
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView avatarImageView;

        private ViewHolder(View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.name);
            this.avatarImageView = itemView.findViewById(R.id.avatar);
        }
    }
}