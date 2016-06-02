package com.skylarkingstudios.whatshisface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class ActorResultFragment extends Fragment {

    private static final String ARG_ACTOR_ID="actor_id";
    private Actor actor;
    private ActorList mActorList;

    private TextView mResultDetailsTextView;
    private ImageView mActorImageView;
    private TextView mActorNameTextView;
    private TextView mActorDobTextView;
    private TextView mActorBioTextView;
    private TextView mActorKnownForTextView;
    private Button mNewSearchButton;
    private String baseURL;

    public static ActorResultFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTOR_ID, id);

        ActorResultFragment fragment = new ActorResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActorList = ActorList.get(getActivity());

        int actorId = getArguments().getInt(ARG_ACTOR_ID);
        actor = mActorList.getActor(actorId);

        baseURL = "http://image.tmdb.org/t/p/h632";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result_actor, container, false);

        mResultDetailsTextView = (TextView) v.findViewById(R.id.fragment_result_actor_result_details);
        // If more than one actor change to "Result #/Max#"
        if (mActorList.getActors().size() > 1) {
            String details = (mActorList.getActors().size() + " " + getResources().getString(R.string.fragment_result_actor_result));
            mResultDetailsTextView.setText(details);
        }

        mActorImageView = (ImageView) v.findViewById(R.id.fragment_result_actor_image);
        Picasso.with(getContext()).load(baseURL + actor.getImageURL()).placeholder(R.drawable.portrait_placeholder).into(mActorImageView);


        mActorNameTextView = (TextView) v.findViewById(R.id.fragment_result_actor_name);
        mActorNameTextView.setText(actor.getName());

        mActorDobTextView = (TextView) v.findViewById(R.id.fragment_result_actor_dob);
        try {
            String dob = actor.getDob();
            if (dob.equals("")) {
                mActorDobTextView.setText(R.string.fragment_result_actor_no_dob);
            } else {
                mActorDobTextView.setText(dob);
            }
        } catch (ParseException e) {
            mActorDobTextView.setText(getResources().getString(R.string.fragment_result_actor_no_dob));
            e.printStackTrace();
        }

        mActorBioTextView = (TextView) v.findViewById(R.id.fragment_result_actor_bio);
        if (actor.getBio() == null) {
            mActorBioTextView.setText(R.string.fragment_result_actor_no_bio);
        } else {
            mActorBioTextView.setText(actor.getBio());
        }

        mActorKnownForTextView = (TextView) v.findViewById(R.id.fragment_result_actor_known_for);

        mNewSearchButton = (Button) v.findViewById(R.id.fragment_result_actor_new_search_button);
        mNewSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActorList.clear();
                Intent i = new Intent(getContext(), MainMenuActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

}
