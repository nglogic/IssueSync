package com.nglogic.synchronizer.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


@XmlAccessorType(XmlAccessType.FIELD)
public class MapperConfig {
	
    @XmlElementWrapper(name="statusMapping")
	@XmlElement(name="entry")
	List<MapEntryString> statusMapping;
	
    @XmlElementWrapper(name="usersMapping")
	@XmlElement(name="entry")
	List<MapEntryString> usersMapping;
	
    @XmlElementWrapper(name="priorityMapping")
	@XmlElement(name="entry")
	List<MapEntryString> priorityMapping;
	
    @XmlElementWrapper(name="componentMapping")
	@XmlElement(name="entry")
	List<MapEntryString> componentMapping;
	
    @XmlElementWrapper(name="milestoneMapping")
	@XmlElement(name="entry")
	List<MapEntryString> milestoneMapping;
	
	@XmlElement
	String commentMarker;
	
	@XmlElement
	String otherUserName;

	public List<MapEntryString> getStatusMapping() {
		return statusMapping;
	}

	public List<MapEntryString> getUsersMapping() {
		return usersMapping;
	}

	public List<MapEntryString> getPriorityMapping() {
		return priorityMapping;
	}

	public List<MapEntryString> getComponentMapping() {
		return componentMapping;
	}

	public List<MapEntryString> getMilestoneMapping() {
		return milestoneMapping;
	}

	public String getCommentMarker() {
		return commentMarker;
	}

	public String getOtherUserName() {
		return otherUserName;
	}
	

	
	
	
}
