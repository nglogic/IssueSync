package com.nglogic.synchronizer;

import java.util.ArrayList;
import java.util.Date;

public class TicketChanges {
	Integer id;
	Integer remoteId;
	ArrayList<Ticket> tickets;
	
	
	public Date getLastModification(){
		return tickets.get(tickets.size()-1).getUpdateTime();//chyba get(0)
	}
	
	
	@Override
	public String toString() {
		return "TicketChanges [id=" + id + ", remoteId=" + remoteId
				+ ", tickets=" + tickets + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRemoteId() {
		return remoteId;
	}
	public void setRemoteId(Integer remoteId) {
		this.remoteId = remoteId;
	}
	public ArrayList<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(ArrayList<Ticket> tickets) {
		this.tickets = tickets;
	}

}
