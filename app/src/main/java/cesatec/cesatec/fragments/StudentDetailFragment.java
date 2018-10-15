package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cesatec.cesatec.R;

public class StudentDetailFragment extends Fragment {
    static final String TAG = "StudentDetailFragment";

    private int id;
    private String name;
    private String avatarUrl;

    /**
     * Set the fragment variables used the arguments passed to the fragment
     *
     * @param savedInstanceState Saved fragmented data to be reused
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Log.d(TAG, "onCreate: Student detail " + arguments.toString());
            this.id = getArguments().getShort("id");
            this.name = getArguments().getString("name");
            this.avatarUrl = getArguments().getString("avatar_url");
        }
    }

    /**
     * Set up widgets after the fragment views are created and available
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setWidgets();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * Set the information of the user on the UI
     */
    private void setWidgets() {
        Log.d(TAG, "setWidgets: setting Student information on detail activity");

        Activity activity = getActivity();
        if (activity != null) {
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            appBarLayout.setTitle(name);

            TextView idView = activity.findViewById(R.id.student_id);
            String idText = getString(R.string.student_detail_id, id);
            idView.setText(idText);

            TextView nameView = activity.findViewById(R.id.student_name);
            String nameText = getString(R.string.student_detail_name, name);
            nameView.setText(nameText);

            ImageView avatarView = activity.findViewById(R.id.student_avatar);
            Picasso.get()
                    .load(avatarUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(avatarView);
        }
    }
}
