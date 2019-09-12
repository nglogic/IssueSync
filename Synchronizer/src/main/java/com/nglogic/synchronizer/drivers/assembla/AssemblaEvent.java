package com.nglogic.synchronizer.drivers.assembla;

import java.util.HashMap;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName(value = "event")
@JsonSerialize
public class AssemblaEvent {
	
	String object;
	String url;
	String date;
	String operation;
	String object_id;
	String title;
	String space_id;
	String space_name;
	String author_id;
	String author_name;
	String whatchanged;
	String ticket_operation;
	String comment_or_description;
	
	public String getComment_or_description() {
		return comment_or_description;
	}
	public void setComment_or_description(String comment_or_description) {
		this.comment_or_description = comment_or_description;
	}
	public String getTicket_operation() {
		return ticket_operation;
	}
	public void setTicket_operation(String ticket_operation) {
		this.ticket_operation = ticket_operation;
	}
	HashMap<String, String> ticket;
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSpace_id() {
		return space_id;
	}
	public void setSpace_id(String space_id) {
		this.space_id = space_id;
	}
	public String getSpace_name() {
		return space_name;
	}
	public void setSpace_name(String space_name) {
		this.space_name = space_name;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getWhatchanged() {
		return whatchanged;
	}
	public void setWhatchanged(String whatchanged) {
		this.whatchanged = whatchanged;
	}
	public HashMap<String, String> getTicket() {
		return ticket;
	}
	public void setTicket(HashMap<String, String> ticket) {
		this.ticket = ticket;
	}
	@Override
	public String toString() {
		return "AssemblaEvent [object=" + object + ", url=" + url + ", date="
				+ date + ", operation=" + operation + ", object_id="
				+ object_id + ", title=" + title + ", space_id=" + space_id
				+ ", space_name=" + space_name + ", author_id=" + author_id
				+ ", author_name=" + author_name + ", whatchanged="
				+ whatchanged + ", ticket_operation=" + ticket_operation
				+ ", comment_or_description=" + comment_or_description
				+ ", ticket=" + ticket + "]";
	}
	

	
}
