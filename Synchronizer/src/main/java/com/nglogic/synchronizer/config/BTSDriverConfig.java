package com.nglogic.synchronizer.config;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BTSDriverConfig {

	@XmlElement
	String driverName;
	@XmlElement
	String BTSUrl;

	@XmlElement
	String user;
	@XmlElement
	String password;
	@XmlElement
	String projectName;
	@XmlElement
	String additionalConfigFile;
	@XmlElement
	String remoteIdFieldName;
	
	@XmlElement
	int tracBugTimeHourOffset;
	
	@XmlElement
	String editCommentMarker;

	@XmlElement
	HashMap<String, String> customProperties;

	public String getDriverName() {
		return driverName;
	}

	public String getBTSUrl() {
		return BTSUrl;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getAdditionalConfigFile() {
		return additionalConfigFile;
	}

	public HashMap<String, String> getCustomProperties() {
		return customProperties;
	}

	public int getTracBugTimeOffset() {
		return tracBugTimeHourOffset;
	}

	public String getRemoteIdFieldName() {
		return remoteIdFieldName;
	}

	public void setRemoteIdFieldName(String remoteIdFieldName) {
		this.remoteIdFieldName = remoteIdFieldName;
	}

	public String getEditCommentMarker() {
		return editCommentMarker;
	}
	
	
	
}
