package com.nglogic.synchronizer;

import java.util.Date;

import com.google.common.collect.HashBiMap;
import com.nglogic.synchronizer.config.MapEntryString;
import com.nglogic.synchronizer.config.MapperConfig;

public class SynchronizationMapper {
	
	String commentMarker;
	String otherUser;
	//HashBiMap<inner,outer>
	HashBiMap<String, String> userLoginMap;
	HashBiMap<String, String> statusMap;
	HashBiMap<String, String> milestoneMap;
	HashBiMap<String, String> priorityMap;
	HashBiMap<String, String> typMap;
	HashBiMap<String, String> componentMap;
	
	public SynchronizationMapper(MapperConfig config){
		userLoginMap = new HashBiMap<String, String>();
		if(config.getMilestoneMapping()!=null){
			for(MapEntryString entry: config.getUsersMapping()){
				userLoginMap.put(entry.getKey(), entry.getValue());
			}
		}
		statusMap = new HashBiMap<String, String>();
		if(config.getMilestoneMapping()!=null){
			for(MapEntryString entry: config.getStatusMapping()){
				statusMap.put(entry.getKey(), entry.getValue());
			}
		}
		milestoneMap = new HashBiMap<String, String>();
		if(config.getMilestoneMapping()!=null){
			for(MapEntryString entry: config.getMilestoneMapping()){
				milestoneMap.put(entry.getKey(), entry.getValue());
			}
		}
		priorityMap = new HashBiMap<String, String>();
		if(config.getPriorityMapping()!=null){
			for(MapEntryString entry: config.getPriorityMapping()){
				priorityMap.put(entry.getKey(), entry.getValue());
			}
		}
		typMap = new HashBiMap<String, String>();
		
		componentMap = new HashBiMap<String, String>();
		if(config.getComponentMapping()!=null)
			for(MapEntryString entry: config.getComponentMapping()){
				componentMap.put(entry.getKey(), entry.getValue());
			}
		
		commentMarker = config.getCommentMarker();
		otherUser = config.getOtherUserName();
		
	}
	public Ticket outerToInnerMapper(Ticket outerTicket){
		Ticket innerTicket = new Ticket();
		innerTicket.setTicketId(null);
		innerTicket.setRemoteID(outerTicket.getTicketId());
		innerTicket.setComment(outerTicket.getComment());
		innerTicket.setSummary(outerTicket.getSummary());
		innerTicket.setDescription(outerTicket.getDescription());
		innerTicket.setUpdateTime(outerTicket.getUpdateTime());
		if(outerTicket.getOwner()==null)
			innerTicket.setOwner(null);
		else{
			if(userLoginMap.inverse().get(outerTicket.getOwner())==null)
				innerTicket.setOwner(otherUser);
			else
				innerTicket.setOwner(userLoginMap.inverse().get(outerTicket.getOwner()));
		}
		if(outerTicket.getReporter()==null)
			innerTicket.setReporter(null);
		else{
			if(userLoginMap.inverse().get(outerTicket.getReporter())==null)
				innerTicket.setReporter(otherUser);
			else
				innerTicket.setReporter(userLoginMap.inverse().get(outerTicket.getReporter()));
		}
		if(userLoginMap.inverse().get(outerTicket.getAuthor())==null)
			innerTicket.setAuthor(otherUser);
		else
			innerTicket.setAuthor(userLoginMap.inverse().get(outerTicket.getAuthor()));
	
		if(outerTicket.getStatus()==null)
			innerTicket.setStatus(null);
		else
			innerTicket.setStatus(statusMap.inverse().get(outerTicket.getStatus()));
		if(outerTicket.getMilestone()==null)
			innerTicket.setMilestone(null);
		else
			innerTicket.setMilestone(milestoneMap.inverse().get(outerTicket.getMilestone()));
		if(outerTicket.getType()==null)
			innerTicket.setType(null);
		else
			innerTicket.setType(typMap.inverse().get(outerTicket.getType()));
		if(outerTicket.getPriority()==null)
			innerTicket.setPriority(null);
		else
			innerTicket.setPriority(priorityMap.inverse().get(outerTicket.getPriority()));
		if(outerTicket.getComponent()==null)
			innerTicket.setComponent(null);
		else
			innerTicket.setComponent(componentMap.inverse().get(outerTicket.getComponent()));
	
		
		
		return innerTicket;
	}
	
	public Ticket innerToOuterMapper(Ticket innerTicket){
		Ticket outerTicket = new Ticket();
		outerTicket.setTicketId(innerTicket.getRemoteID());
		if(innerTicket.getComment()!=null&&innerTicket.getComment().contains(commentMarker))
			outerTicket.setComment(innerTicket.getComment().replaceAll(commentMarker, ""));
		outerTicket.setSummary(innerTicket.getSummary());
		outerTicket.setDescription(innerTicket.getDescription());
		outerTicket.setUpdateTime(innerTicket.getUpdateTime());
		if(innerTicket.getOwner()==null)
			outerTicket.setOwner(null);
		else
			outerTicket.setOwner(userLoginMap.get(innerTicket.getOwner()));
		if(innerTicket.getReporter()==null)
			outerTicket.setReporter(null);
		else
			outerTicket.setReporter(userLoginMap.get(innerTicket.getReporter()));
		if(userLoginMap.get(innerTicket.getAuthor())==null)
			outerTicket.setAuthor(otherUser);
		else
			outerTicket.setAuthor(userLoginMap.get(innerTicket.getAuthor()));
		if(innerTicket.getStatus()==null)
			outerTicket.setStatus(null);
		else
			outerTicket.setStatus(statusMap.get(innerTicket.getStatus()));
		if(innerTicket.getMilestone()==null)
			outerTicket.setMilestone(null);
		else
			outerTicket.setMilestone(milestoneMap.get(innerTicket.getMilestone()));
		if(innerTicket.getType()==null)
			outerTicket.setType(null);
		else
			outerTicket.setType(typMap.get(innerTicket.getType()));
		if(innerTicket.getPriority()==null)
			outerTicket.setPriority(null);
		else
			outerTicket.setPriority(priorityMap.get(innerTicket.getPriority()));
		if(innerTicket.getComponent()==null)
			outerTicket.setComponent(null);
		else
			outerTicket.setComponent(componentMap.get(innerTicket.getComponent()));
	
		
		
		return outerTicket;
	}

}
