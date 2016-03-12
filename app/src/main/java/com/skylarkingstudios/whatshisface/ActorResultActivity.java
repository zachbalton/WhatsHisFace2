package com.skylarkingstudios.whatshisface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;

import java.util.List;

public class ActorResultActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ActorList mFinalActorList;
    private List<Integer> mActorIds;
    private List<Actor> mActorList;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_loading);

        mFinalActorList = ActorList.get(this);
        mFinalActorList.clear();

        mDataManager = new DataManager(this);
        mActorIds = mDataManager.getCommonActorIds(this);

        mActorList = mDataManager.getActorBios(mActorIds);

        for (Actor actor : mActorList) {
            mFinalActorList.add(actor);
            Log.d("ActorResultActivity", "" + actor.getName() + " added to ActorList");
        }

        setContentView(R.layout.activity_result_actor);

        mViewPager = (ViewPager) findViewById(R.id.activity_result_actor_view_pager);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Actor actor = mFinalActorList.getActors().get(position);
                return ActorResultFragment.newInstance(actor.getId());
            }

            @Override
            public int getCount() {
                return mFinalActorList.getActors().size();
            }
        });

    }



}
