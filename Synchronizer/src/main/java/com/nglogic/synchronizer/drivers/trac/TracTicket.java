package com.nglogic.synchronizer.drivers.trac;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.google.common.collect.HashBiMap;
import com.nglogic.synchronizer.Ticket;


@JsonSerialize(include=Inclusion.NON_EMPTY)
public class TracTicket {
	
	Integer ticketId;
	String summary;
	String keywords;
	String status;
	String resolution;
	String type;
	String version;
	String milestone;
	String reporter;
	Date timeCreated;
	String component;
	String description;
	String priority;
	String owner;
	Date timeChanged;
	Integer remoteId;
	String comment;
	String author;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getTicketId() {
		return ticketId;
	}
	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMilestone() {
		return milestone;
	}
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Date getTimeChanged() {
		return timeChanged;
	}
	public void setTimeChanged(Date timeChanged) {
		this.timeChanged = timeChanged;
	}
	public Integer getRemoteId() {
		return remoteId;
	}
	public void setRemoteId(Integer remoteId) {
		this.remoteId = remoteId;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	

	
	@Override
	public String toString() {
		return "TracTicket [ticketId=" + ticketId + ", summary=" + summary
				+ ", keywords=" + keywords + ", status=" + status
				+ ", resolution=" + resolution + ", type=" + type
				+ ", version=" + version + ", milestone=" + milestone
				+ ", reporter=" + reporter + ", timeCreated=" + timeCreated
				+ ", component=" + component + ", description=" + description
				+ ", priority=" + priority + ", owner=" + owner
				+ ", timeChanged=" + timeChanged + ", remoteId=" + remoteId
				+ ", comment=" + comment + ", author=" + author + "]";
	}
	public TracTicket clone() {
		
		TracTicket cloneTicket = new TracTicket();
		cloneTicket.setAuthor(this.getAuthor());
		cloneTicket.setTicketId(this.getTicketId());
		cloneTicket.setSummary(this.getSummary());
		cloneTicket.setKeywords(this.getKeywords());
		cloneTicket.setStatus(this.getStatus());
		cloneTicket.setResolution(this.getResolution());
		cloneTicket.setType(this.getType());
		cloneTicket.setVersion(this.getVersion());
		cloneTicket.setMilestone(this.getMilestone());
		cloneTicket.setReporter(this.getReporter());
		cloneTicket.setTimeCreated(this.getTimeCreated());
		cloneTicket.setComponent(this.getComponent());
		cloneTicket.setDescription(this.getDescription());
		cloneTicket.setPriority(this.getPriority());
		cloneTicket.setOwner(this.getOwner());
		cloneTicket.setTimeChanged(this.getTimeChanged());
		cloneTicket.setRemoteId(this.getRemoteId());
		cloneTicket.setComment(this.getComment());
		
		return cloneTicket;
	}
}
