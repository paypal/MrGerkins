package com.bot.mrgerkins.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;


@ConfigurationProperties(prefix = "email")
@Service
@Getter
@Setter
public class EmailConfig {

    private String smtpHost;
    private int smtpPort;
    private String smtpUser;
    private String smtpPass;
    private String sender;
    private List<String> releaseNotifyDl;
    private String domain;

    /**
     * Get the DL to notify after a release for the provided repository is drafted on Github
     * Record format ->  Repository-Full-Name:DL-To-Notify
     */
    public String getReleaseNotifyDlForRepo(String repository) {
        return releaseNotifyDl.stream().filter(record -> repository.equals(record.split(":")[0]))
                .map(record -> record.split(":")[1]).findAny()
                .orElseThrow(() -> new IllegalArgumentException("No DL found for given Repo"));
    }

}
