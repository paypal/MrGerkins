package com.bot.mrgerkins.adapter.dlmanager.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelUser {
    private String isDisabled;

    private String site;

    private String dlPath;

    private String samAccountName;

    private String GUID;

    private String emailAddress;

    private String type;

    private String displayName;

    private String QID;

    public String getIsDisabled()
    {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled)
    {
        this.isDisabled = isDisabled;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getDlPath()
    {
        return dlPath;
    }

    public void setDlPath(String dlPath)
    {
        this.dlPath = dlPath;
    }

    public String getSamAccountName()
    {
        return samAccountName;
    }

    public void setSamAccountName(String samAccountName)
    {
        this.samAccountName = samAccountName;
    }

    public String getGUID()
    {
        return GUID;
    }

    public void setGUID(String GUID)
    {
        this.GUID = GUID;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getQID()
    {
        return QID;
    }

    public void setQID(String QID)
    {
        this.QID = QID;
    }
}
