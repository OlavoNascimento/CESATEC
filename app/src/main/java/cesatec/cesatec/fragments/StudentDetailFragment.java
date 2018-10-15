package cesatec.cesatec.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cesatec.cesatec.R;

public class StudentDetailFragment extends Fragment {
    static final String TAG = "StudentDetailFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            short id = getArguments().getShort("id");
            String name = getArguments().getString("name");
            String avatarUrl = getArguments().getString("avatar_url");
            setWidgets(id, name, avatarUrl);
        }
    }

    /**
     * Set the information of the user on the UI
     *
     * @param id        Id of the student
     * @param name      Name of the student
     * @param avatarUrl Url of the avatar of the student
     */
    private void setWidgets(Short id, String name, String avatarUrl) {
        Log.d(TAG, "setWidgets: setting Student information on detail activity");

        Activity activity = getActivity();

        if (activity != null) {
            TextView idView = activity.findViewById(R.id.detail_id);
            TextView nameView = activity.findViewById(R.id.detail_name);
            ImageView avatarView = activity.findViewById(R.id.detail_avatar);

            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            appBarLayout.setTitle(name);

            String idText = getString(R.string.detail_id, id);
            String nameText = getString(R.string.detail_name, name);
            idView.setText(idText);
            nameView.setText(nameText);
            Picasso.get()
                    .load(avatarUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(avatarView);
        }
    }


}
