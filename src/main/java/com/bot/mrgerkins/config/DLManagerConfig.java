package com.bot.mrgerkins.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="dl-manager")
@Component
public class DLManagerConfig {

	private String token;
	private String executor;
	private String serviceUrl;
	private String targetDl;
	private Boolean enabled;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getTargetDl() {
		return targetDl;
	}

	public void setTargetDl(String targetDl) {
		this.targetDl = targetDl;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getToken(){
		return token;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getExecutor(){
		return executor;
	}

	public void setExecutor(String executor){
		this.executor = executor;
	}
}
