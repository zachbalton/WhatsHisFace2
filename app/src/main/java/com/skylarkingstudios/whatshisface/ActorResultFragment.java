package com.skylarkingstudios.whatshisface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result_actor, container, false);

        mResultDetailsTextView = (TextView) v.findViewById(R.id.fragment_result_actor_result_details);
        // TODO: Set mResultDetailsTextView text to reflect the amount of results

        mActorImageView = (ImageView) v.findViewById(R.id.fragment_result_actor_image);
        // TODO: implement an image helper and return images for actor portrait

        mActorNameTextView = (TextView) v.findViewById(R.id.fragment_result_actor_name);
        mActorNameTextView.setText(actor.getName());

        mActorDobTextView = (TextView) v.findViewById(R.id.fragment_result_actor_dob);
        mActorBioTextView = (TextView) v.findViewById(R.id.fragment_result_actor_bio);
        mActorKnownForTextView = (TextView) v.findViewById(R.id.fragment_result_actor_known_for);

        return v;
    }

}
