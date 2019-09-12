package com.nglogic.synchronizer.drivers.assembla;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.google.common.collect.HashBiMap;
import com.nglogic.synchronizer.Ticket;

@JsonRootName(value = "ticket")
@JsonSerialize(include=Inclusion.NON_EMPTY)
public class AssemblaTicket {
	
	Integer id;
	Integer number;
	String summary;
	String description;
	Integer priority;
	String completed_date;
	Integer component_id;
	String created_on;
	Integer permission_type;
	Float importance;
	Boolean is_story;
	Integer milestone_id;
	String notification_list;
	ArrayList<String> folowers;
	String space_id;
	Integer state;
	String status;
	Integer story_importance;
	String updated_at;
	Float working_hours;
	Float estimate;
	Float total_estimate;
	Float total_invested_hours;
	Float total_working_hours;
	String assigned_to_id;
	String reporter_id;
	HashMap<String, String> custom_fields;
	
	//?
	private HashBiMap<Integer, String> priorityMap= new HashBiMap<Integer, String>();
	SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss"); 
	
	public AssemblaTicket(){
	}
	
	public Ticket convertToTicket(){
		Ticket ticket = new Ticket();
		//ticket.setComment(comment);
		//ticket.setComponent(component);
		ticket.setDescription(description);
		//ticket.setMilestone(milestone);
		//ticket.setOwner(assigned_to);
		//ticket.setReporter(reporter);
		ticket.setPriority(priorityMap.get(priority));
		ticket.setStatus(status);
		ticket.setSummary(summary);
		//ticket.setTicketId((int)id);
		Date updateTime=null;
		
		
		
		return ticket;
	}
	
	public void updateGivenTicket(Ticket ticket){
		//ticket.setComment(comment);
		//ticket.setComponent(component);
		description = ticket.getDescription();
		//ticket.setMilestone(milestone);
		//ticket.setOwner(assigned_to);
		//ticket.setReporter(reporter);
		priority = priorityMap.inverse().get(ticket.getPriority());
		status = ticket.getStatus();
		summary = ticket.getSummary();
		//id = ticket.getTicketId();
		updated_at = dt.format(ticket.getUpdateTime());
		updated_at.replace(" ", "T");
		updated_at.concat("Z");
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getCompleted_date() {
		return completed_date;
	}
	public void setCompleted_date(String completed_date) {
		this.completed_date = completed_date;
	}
	public Integer getComponent_id() {
		return component_id;
	}
	public void setComponent_id(Integer component_id) {
		this.component_id = component_id;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public Integer getPermission_type() {
		return permission_type;
	}
	public void setPermission_type(Integer permission_type) {
		this.permission_type = permission_type;
	}
	public Float getImportance() {
		return importance;
	}
	public void setImportance(Float importance) {
		this.importance = importance;
	}
	public Boolean getIs_story() {
		return is_story;
	}
	public void setIs_story(Boolean is_story) {
		this.is_story = is_story;
	}
	public Integer getMilestone_id() {
		return milestone_id;
	}
	public void setMilestone_id(Integer milestone_id) {
		this.milestone_id = milestone_id;
	}
	public String getNotification_list() {
		return notification_list;
	}
	public void setNotification_list(String notification_list) {
		this.notification_list = notification_list;
	}
	public ArrayList<String> getFolowers() {
		return folowers;
	}
	public void setFolowers(ArrayList<String> folowers) {
		this.folowers = folowers;
	}
	public String getSpace_id() {
		return space_id;
	}
	public void setSpace_id(String space_id) {
		this.space_id = space_id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getStory_importance() {
		return story_importance;
	}
	public void setStory_importance(Integer story_importance) {
		this.story_importance = story_importance;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public Float getWorking_hours() {
		return working_hours;
	}
	public void setWorking_hours(Float working_hours) {
		this.working_hours = working_hours;
	}
	public Float getEstimate() {
		return estimate;
	}
	public void setEstimate(Float estimate) {
		this.estimate = estimate;
	}
	public Float getTotal_estimate() {
		return total_estimate;
	}
	public void setTotal_estimate(Float total_estimate) {
		this.total_estimate = total_estimate;
	}
	public Float getTotal_invested_hours() {
		return total_invested_hours;
	}
	public void setTotal_invested_hours(Float total_invested_hours) {
		this.total_invested_hours = total_invested_hours;
	}
	public Float getTotal_working_hours() {
		return total_working_hours;
	}
	public void setTotal_working_hours(Float total_working_hours) {
		this.total_working_hours = total_working_hours;
	}
	public String getAssigned_to_id() {
		return assigned_to_id;
	}
	public void setAssigned_to_id(String assigned_to_id) {
		this.assigned_to_id = assigned_to_id;
	}
	public String getReporter_id() {
		return reporter_id;
	}
	public void setReporter_id(String reporter_id) {
		this.reporter_id = reporter_id;
	}
	public HashMap<String, String> getCustom_fields() {
		return custom_fields;
	}
	public void setCustom_fields(HashMap<String, String> custom_fields) {
		this.custom_fields = custom_fields;
	}
	@Override
	public String toString() {
		return "AssemblaTicket [id=" + id + ", number=" + number + ", summary="
				+ summary + ", description=" + description + ", priority="
				+ priority + ", completed_date=" + completed_date
				+ ", component_id=" + component_id + ", created_on="
				+ created_on + ", permission_type=" + permission_type
				+ ", importance=" + importance + ", is_story=" + is_story
				+ ", milestone_id=" + milestone_id + ", notification_list="
				+ notification_list + ", folowers=" + folowers + ", space_id="
				+ space_id + ", state=" + state + ", status=" + status
				+ ", story_importance=" + story_importance + ", updated_at="
				+ updated_at + ", working_hours=" + working_hours
				+ ", estimate=" + estimate + ", total_estimate="
				+ total_estimate + ", total_invested_hours="
				+ total_invested_hours + ", total_working_hours="
				+ total_working_hours + ", assigned_to_id=" + assigned_to_id
				+ ", reporter_id=" + reporter_id + ", custom_fields="
				+ custom_fields + "]";
	}

	

}
