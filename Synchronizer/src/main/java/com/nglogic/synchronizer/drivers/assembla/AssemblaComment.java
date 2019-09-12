package com.nglogic.synchronizer.drivers.assembla;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


@JsonRootName(value = "ticket_comment")
@JsonSerialize(include=Inclusion.NON_EMPTY)
public class AssemblaComment {
	
	Integer id;
	String comment;
	Integer ticket_id;
	String user_id;
	String created_on;
	String updated_at;
	String ticket_changes;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(Integer ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getTicket_changes() {
		return ticket_changes;
	}
	public void setTicket_changes(String ticket_changes) {
		this.ticket_changes = ticket_changes;
	}
	@Override
	public String toString() {
		return "AssemblaComment [id=" + id + ", comment=" + comment
				+ ", ticket_id=" + ticket_id + ", user_id=" + user_id
				+ ", created_on=" + created_on + ", updated_at=" + updated_at
				+ ", ticket_changes=" + ticket_changes + "]";
	}
	

}
