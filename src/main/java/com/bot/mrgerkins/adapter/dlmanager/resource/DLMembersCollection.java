package com.bot.mrgerkins.adapter.dlmanager.resource;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.bot.mrgerkins.adapter.dlmanager.DLManagerClientWrapper;

@Component
public class DLMembersCollection {

    @Autowired
    private DLManagerClientWrapper dLManagerClientWrapper;

    @Cacheable("dLMemberMap")
    public HashMap<String, ModelUser> getDLMembersMap() {
        return dLManagerClientWrapper.getMembersRecursive();
    }
}
