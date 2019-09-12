package com.nglogic.synchronizer.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class InnerFilterConfig {
    @XmlElementWrapper(name="users")
	@XmlElement(name="user")
	List<String> users;
    
    @XmlElementWrapper(name="priorities")
	@XmlElement(name="priority")
	List<String> priorities;
    
    @XmlElementWrapper(name="milestones")
	@XmlElement(name="milestone")
	List<String> milestones;

	public List<String> getUsers() {
		return users;
	}

	public List<String> getPriorities() {
		return priorities;
	}

	public List<String> getMilestones() {
		return milestones;
	}
	
	
	
}

