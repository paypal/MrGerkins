package com.bot.mrgerkins.adapter.dlmanager;

import java.util.HashMap;

import com.bot.mrgerkins.adapter.dlmanager.resource.ModelUser;


public interface DLManagerClientWrapper {

    public HashMap<String, ModelUser> getMembersRecursive();

}
