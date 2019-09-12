package com.nglogic.synchronizer.drivers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.nglogic.synchronizer.BTSSynchronizer;
import com.nglogic.synchronizer.Ticket;
import com.nglogic.synchronizer.TicketChanges;

public class BTSRDriver implements BTSDriverInterface{

	ArrayList<TicketChanges> inputTicketsChanges;
	ArrayList<Ticket> outputTickets = new ArrayList<Ticket>();
	int idSequece =111;
	static private Logger log = Logger.getLogger(BTSRDriver.class);
	HashMap<Integer,Ticket> allTicketsByRemoteID = new HashMap<Integer, Ticket>();
	String driverName;
	public BTSRDriver(String driverName){
		this.driverName = driverName;
	}
	public void setInputTicketsChanges(ArrayList<TicketChanges> inputTicketsChanges) {
		this.inputTicketsChanges = inputTicketsChanges;
	}

	public ArrayList<Ticket> getOutputTickets() {
		return outputTickets;
	}

/*	public ArrayList<Ticket> getTicketsByFromDate(Date since) {
		return inputTicketsChanges;
	}
*/
	public void setTicketByRemoteId(Integer remoteId, Ticket ticket){
		log.debug("setTicketByRemoteId: remoteID: "+remoteId+" ticket: "+ticket);
		allTicketsByRemoteID.put(remoteId, ticket);
	}
	
	public Ticket getTicketByRemoteId(int remoteId) {
		log.debug("getTicketByRemoteId: remoteID: "+remoteId+" return: "+allTicketsByRemoteID.get(remoteId));
		return allTicketsByRemoteID.get(remoteId);
	}

	public void updateTicket(Ticket ticket) {
		log.debug("updateTicket: driverName: "+driverName+" ticket: "+ticket);
		outputTickets.add(ticket);
		
	}

	public Ticket createTicket(Ticket ticket) {
		ticket.setTicketId(idSequece++);
		log.debug("createTicket: driverName: "+driverName+" ticket: "+ticket);
		outputTickets.add(ticket);
		return ticket;
	}

	public void clearTickets(){
		if(inputTicketsChanges != null) 
			inputTicketsChanges.clear();
		if(outputTickets != null) 
			outputTickets.clear();
		if(allTicketsByRemoteID!= null) 
			allTicketsByRemoteID.clear();
	
	}
	public ArrayList<TicketChanges> getTicketChanges(Date dateFrom) {
		// TODO Auto-generated method stub
		return inputTicketsChanges;
	}
	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return "BTSRDriver_Project_Name";
	}
}
