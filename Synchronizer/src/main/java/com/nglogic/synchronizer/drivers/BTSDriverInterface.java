package com.nglogic.synchronizer.drivers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.nglogic.synchronizer.Ticket;
import com.nglogic.synchronizer.TicketChanges;
import com.nglogic.synchronizer.exceptions.CommunicationException;

public interface BTSDriverInterface {
	

	public Ticket getTicketByRemoteId(int remoteId);
	/**
	 * Tickety, w ktorych nic sie nie zmienia nie zostana przeslane
	 * @param tickets
	 */
	public void updateTicket(Ticket ticket);
	public Ticket createTicket(Ticket ticket) throws CommunicationException;
	public ArrayList<TicketChanges> getTicketChanges(Date dateFrom) throws CommunicationException;
	public String getProjectName();
}
