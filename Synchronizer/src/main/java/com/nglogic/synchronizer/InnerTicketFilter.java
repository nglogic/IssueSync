package com.nglogic.synchronizer;

import java.util.HashSet;
import java.util.Set;

import com.nglogic.synchronizer.config.InnerFilterConfig;
import com.nglogic.synchronizer.config.MapEntryString;

public class InnerTicketFilter {
	Set<String> userSet;
	Set<String> milestoneSet;
	//Set<String> projectSet; - jesli bedzie na poziomie sterownika sprawdzany projekt to niepotrzebne
	Set<String> prioritySet;
	
	public InnerTicketFilter(InnerFilterConfig config){
		userSet = new HashSet<String>();
		if(config.getUsers()!=null){
			for(String entry: config.getUsers()){
				userSet.add(entry);
			}
		}
		milestoneSet = new HashSet<String>();
		if(config.getMilestones()!=null){
			for(String entry: config.getMilestones()){
				milestoneSet.add(entry);
			}
		}
		prioritySet = new HashSet<String>();
		if(config.getPriorities()!=null){
			for(String entry: config.getPriorities()){
				prioritySet.add(entry);
			}
		}
	}
	public Ticket filteredTicket(Ticket ticket){
		Ticket retTicket = ticket.clone();
		if(!userSet.contains(ticket.getReporter()))
			retTicket.setReporter(null);	
		if(!userSet.contains(ticket.getOwner()))
			retTicket.setOwner(null);
		if(!milestoneSet.contains(ticket.getMilestone()))
			retTicket.setMilestone(null);
		if(!prioritySet.contains(ticket.getPriority()))
			retTicket.setPriority(null);
		boolean isEmpty=true;
		if(retTicket.getComment()!=null)
			isEmpty=false;
		if(retTicket.getComponent()!=null)
			isEmpty=false;
		if(retTicket.getDescription()!=null)
			isEmpty=false;
		if(retTicket.getMilestone()!=null)
			isEmpty=false;
		if(retTicket.getOwner()!=null)
			isEmpty=false;
		if(retTicket.getPriority()!=null)
			isEmpty=false;
		if(retTicket.getRemoteID()!=null)
			isEmpty=false;
		if(retTicket.getReporter()!=null)
			isEmpty=false;
		if(retTicket.getStatus()!=null)
			isEmpty=false;
		if(retTicket.getSummary()!=null)
			isEmpty=false;
		if(retTicket.getType()!=null)
			isEmpty=false;
		if(isEmpty)
			return null;
		else
			return retTicket;
	}
	public boolean shouldSynchronizeTicket(Ticket ticket){
		boolean shouldSynchronize = true;
		
		shouldSynchronize &= userSet.contains(ticket.getReporter()) ;
		shouldSynchronize &= userSet.contains(ticket.getOwner());
		shouldSynchronize &= milestoneSet.contains(ticket.getMilestone());
		shouldSynchronize &= prioritySet.contains(ticket.getPriority());
		
		return shouldSynchronize;
	}
	

	public void setUser(String user){
		userSet.add(user);
	}
	public void setMilestone(String milestone){
		milestoneSet.add(milestone);
	}
	public void setPriority(String priority){
		prioritySet.add(priority);
	}
	
	public Set<String> getUserSet() {
		return userSet;
	}
	public void setUserSet(Set<String> userSet) {
		this.userSet = userSet;
	}
	public Set<String> getMilestoneSet() {
		return milestoneSet;
	}
	public void setMilestoneSet(Set<String> milestoneSet) {
		this.milestoneSet = milestoneSet;
	}
	public Set<String> getPrioritySet() {
		return prioritySet;
	}
	public void setPrioritySet(Set<String> prioritySet) {
		this.prioritySet = prioritySet;
	}
}
