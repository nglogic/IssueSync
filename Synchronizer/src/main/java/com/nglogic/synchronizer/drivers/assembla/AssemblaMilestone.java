package com.nglogic.synchronizer.drivers.assembla;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName(value = "milestone")
@JsonSerialize
public class AssemblaMilestone {
	
	String id;
	String due_date;
	String title; 
	String user_id;
	String created_at; 
	String created_by;
	String space_id;
	String description;
	Boolean is_completed;
	String completed_date;
	String updated_at; 	
	String updated_by; 	
	Integer release_level;
	String release_notes; 	
	Integer planner_type;
	String pretty_release_level; 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getSpace_id() {
		return space_id;
	}
	public void setSpace_id(String space_id) {
		this.space_id = space_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIs_completed() {
		return is_completed;
	}
	public void setIs_completed(Boolean is_completed) {
		this.is_completed = is_completed;
	}
	public String getCompleted_date() {
		return completed_date;
	}
	public void setCompleted_date(String completed_date) {
		this.completed_date = completed_date;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public Integer getRelease_level() {
		return release_level;
	}
	public void setRelease_level(Integer release_level) {
		this.release_level = release_level;
	}
	public String getRelease_notes() {
		return release_notes;
	}
	public void setRelease_notes(String release_notes) {
		this.release_notes = release_notes;
	}
	public Integer getPlanner_type() {
		return planner_type;
	}
	public void setPlanner_type(Integer planner_type) {
		this.planner_type = planner_type;
	}
	public String getPretty_release_level() {
		return pretty_release_level;
	}
	public void setPretty_release_level(String pretty_release_level) {
		this.pretty_release_level = pretty_release_level;
	}
		
}
