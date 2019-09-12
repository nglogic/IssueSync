package com.nglogic.synchronizer;

import java.util.Date;

public class Ticket {
	Integer ticketId;
	String comment;
	Integer remoteID;
	//?HashMap<String, String> attributes;
	String status;
	String milestone;
	String reporter;
	String owner;
	String summary;
	String type;
	String priority;
	String description;
	Date updateTime;
	String component;
	String author;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTicketId() {
		return ticketId;
	}
	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getRemoteID() {
		return remoteID;
	}
	public void setRemoteID(Integer remoteID) {
		this.remoteID = remoteID;
	}
	/*public HashMap<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}*/
	
	public Ticket clone() {
		
		Ticket cloneTicket = new Ticket();
		cloneTicket.setAuthor(this.getAuthor());
		cloneTicket.setTicketId(this.getTicketId());
		cloneTicket.setRemoteID(this.getRemoteID());
		cloneTicket.setComment(this.getComment());
		cloneTicket.setComponent(this.getComponent());
		cloneTicket.setDescription(this.getDescription());
		cloneTicket.setMilestone(this.getMilestone());
		cloneTicket.setOwner(this.getOwner());
		cloneTicket.setPriority(this.getPriority());
		cloneTicket.setReporter(this.getReporter());
		cloneTicket.setStatus(this.getStatus());
		cloneTicket.setSummary(this.getSummary());
		cloneTicket.setType(this.getType());
		cloneTicket.setUpdateTime(this.getUpdateTime());

		return cloneTicket;
	}
	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", comment=" + comment
				+ ", remoteID=" + remoteID + ", status=" + status
				+ ", milestone=" + milestone + ", reporter=" + reporter
				+ ", owner=" + owner + ", summary=" + summary + ", type="
				+ type + ", priority=" + priority + ", description="
				+ description + ", updateTime=" + updateTime + ", component="
				+ component + ", author=" + author + "]";
	}

	

}
