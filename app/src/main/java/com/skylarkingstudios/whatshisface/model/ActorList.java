package com.skylarkingstudios.whatshisface.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ActorList {

    private static ActorList sActorList;

    private List<Actor> mActors;

    public static ActorList get(Context context) {
        if (sActorList == null) {
            sActorList = new ActorList(context);
        }
        return sActorList;
    }

    public void add(Actor actor) {
        mActors.add(actor);
    }

    private ActorList(Context context) {
        mActors = new ArrayList<>();
    }

    public List<Actor> getActors() {
        return mActors;
    }

    public void removeActor(Actor actor){
        mActors.remove(actor);
    }

    public Actor getActor(int id) {
        for (Actor actor : sActorList.getActors()) {
            if (actor.getId() == id) {
                return actor;
            }
        }
        return null;
    }

    public void clear() {
        mActors.clear();
    }

}
