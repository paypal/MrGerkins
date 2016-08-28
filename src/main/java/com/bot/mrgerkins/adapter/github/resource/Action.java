package com.bot.mrgerkins.adapter.github.resource;

/**
 * Created by biphilip on 6/2/16.
 */
public enum Action {
    OPENED("opened"),
    PUBLISHED("published");

    private final String action;

    private Action(String action){
        this.action = action;
    }

    public String toString() {
        return action;
    }

    public boolean equalsString(String actionStr){
        return (actionStr == null) ? false : action.equals(actionStr);
    }
}
