package com.nglogic.synchronizer.drivers.trac;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.xmlrpc.XmlRpcException;

import com.nglogic.synchronizer.Ticket;
import com.nglogic.synchronizer.TicketChanges;
import com.nglogic.synchronizer.config.BTSDriverConfig;
import com.nglogic.synchronizer.drivers.BTSDriverInterface;
import com.nglogic.synchronizer.exceptions.TracConfigurationException;

public class TracDriver implements BTSDriverInterface{
	
	private TracConnector tracConnector;
	private HashSet<String> resolutionSet;
	private String projectName;
	private int tracTimeOffset;
	private String editCommentMarker;
	public TracDriver(BTSDriverConfig config) throws TracConfigurationException {
		try {
			projectName = config.getProjectName();
			editCommentMarker = config.getEditCommentMarker();
			tracTimeOffset = config.getTracBugTimeOffset();
			tracConnector = new TracConnector(config.getBTSUrl()+projectName+"/login/xmlrpc",config.getUser(), config.getPassword(), config.getRemoteIdFieldName(), editCommentMarker);
			resolutionSet = new HashSet<String>();
			resolutionSet.add("fixed");
			resolutionSet.add("invalid");
			resolutionSet.add("wontfix");
			resolutionSet.add("duplicate");
			resolutionSet.add("worksforme");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new TracConfigurationException(e);
		}
		
	}

	public Ticket getTicketByRemoteId(int remoteId) {
		// TODO Auto-generated method stub
	
		try {
			Ticket ticket = createTicketFromTracTicket(tracConnector.getTicketByRemoteId(remoteId));
			return ticket;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.toString());
		}
		
	}
	
	public Date convertDateToTrac(Date date, int change){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//odejmujemy 2 godz bo cos jest nie tak z trac, moze na innym tracu nie trzeba tego robic
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + change);
		return cal.getTime();
	}
	public TracTicket convertFromTicketToTracTicket(Ticket ticket){
		
		TracTicket tracTicket = new TracTicket();
		tracTicket.setAuthor(ticket.getAuthor());
		tracTicket.setComment(ticket.getComment());
		tracTicket.setComponent(ticket.getComponent());
		tracTicket.setDescription(ticket.getDescription());
		tracTicket.setMilestone(ticket.getMilestone());
		tracTicket.setOwner(ticket.getOwner());
		tracTicket.setPriority(ticket.getPriority());
		tracTicket.setRemoteId(ticket.getRemoteID());
		tracTicket.setReporter(ticket.getReporter());
		tracTicket.setSummary(ticket.getSummary());
		if(ticket.getTicketId()!=null)
			tracTicket.setTicketId(ticket.getTicketId());

		tracTicket.setTimeChanged(convertDateToTrac(ticket.getUpdateTime(), - tracTimeOffset));
		tracTicket.setType(ticket.getType());
		if(resolutionSet.contains(ticket.getStatus())){
			tracTicket.setResolution(ticket.getStatus());
			tracTicket.setStatus("closed");
		}
		else
			tracTicket.setStatus(ticket.getStatus());
		return tracTicket;
	}
	public HashMap<String, String> createAttributesMap(TracTicket tracTicket){
		HashMap<String, String> attributesMap = new HashMap<String, String>();
		if(tracTicket.getComponent()!=null)
			 attributesMap.put("component",tracTicket.getComponent());
		if(tracTicket.getDescription()!=null)
			 attributesMap.put("description",tracTicket.getDescription());
		if(tracTicket.getKeywords()!=null)
			 attributesMap.put("keywords",tracTicket.getKeywords());
		if(tracTicket.getMilestone()!=null)
			 attributesMap.put("milestone",tracTicket.getMilestone());
		if(tracTicket.getOwner()!=null)
			 attributesMap.put("owner",tracTicket.getOwner());
		if(tracTicket.getPriority()!=null)
			 attributesMap.put("priority",tracTicket.getPriority());
		if(tracTicket.getRemoteId()!=null)
			 attributesMap.put("id_assembli",Integer.toString(tracTicket.getRemoteId()));
		if(tracTicket.getReporter()!=null)
			 attributesMap.put("reporter",tracTicket.getReporter());
		if(tracTicket.getResolution()!=null)
			 attributesMap.put("resolution",tracTicket.getResolution());
		if(tracTicket.getStatus()!=null)
			 attributesMap.put("status",tracTicket.getStatus());
		if(tracTicket.getSummary()!=null)
			 attributesMap.put("summary",tracTicket.getSummary());
		if(tracTicket.getType()!=null)
			 attributesMap.put("type",tracTicket.getType());
		if(tracTicket.getVersion()!=null)
			 attributesMap.put("version",tracTicket.getVersion());
		return attributesMap;
	}
	public void updateTicket(Ticket ticket) {
		// TODO Auto-generated method stub
	/*	array ticket.update(int id, string comment, struct attributes={},
				boolean notify=False, string author="", DateTime when=None)
				
		*/
		TracTicket tracTicket = convertFromTicketToTracTicket(ticket);
		HashMap<String, String> attributesMap = createAttributesMap(tracTicket);

		try {
			tracConnector.updateTicket(tracTicket.getTicketId(), tracTicket.getComment(), attributesMap, tracTicket.getAuthor(), tracTicket.getTimeChanged());
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Ticket createTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		
		TracTicket tracTicket = convertFromTicketToTracTicket(ticket);
		HashMap<String, String> attributesMap = createAttributesMap(tracTicket);
		if(ticket.getComment()!=null)
			attributesMap.put("comment", ticket.getComment());
		try {
			tracConnector.createTicket(tracTicket.getSummary(), tracTicket.getDescription(), attributesMap, tracTicket.getTimeChanged());
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Ticket createTicketFromTracTicket(TracTicket tracTicket){
		Ticket ticket = null;
		if(tracTicket!=null){
			ticket = new Ticket();
			ticket.setTicketId(tracTicket.getTicketId());
			ticket.setComment(tracTicket.getComment());
			ticket.setComponent(tracTicket.getComponent());
			ticket.setDescription(tracTicket.getDescription());
			ticket.setMilestone(tracTicket.getMilestone());
			ticket.setOwner(tracTicket.getOwner());
			ticket.setPriority(tracTicket.getPriority());
			ticket.setRemoteID(tracTicket.getRemoteId());
			ticket.setReporter(tracTicket.getReporter());
			if(tracTicket.getStatus()!=null&&tracTicket.getStatus().equals("closed"))
				ticket.setStatus(tracTicket.getResolution());
			else ticket.setStatus(tracTicket.getStatus());
			ticket.setSummary(tracTicket.getSummary());
			ticket.setType(tracTicket.getType());
			ticket.setAuthor(tracTicket.getAuthor());
			//bo akurat w przypadku edycji daje dobra date
			if(ticket.getComment()!=null&&ticket.getComment().contains(editCommentMarker))
				ticket.setUpdateTime(tracTicket.getTimeChanged());
			else
				ticket.setUpdateTime(convertDateToTrac(tracTicket.getTimeChanged(), tracTimeOffset));//+2godz
		}
		return ticket;
	}
	public ArrayList<TicketChanges> getTicketChanges(Date dateFrom) {
		Date dateWithoutOffset = dateFrom;
		dateFrom = convertDateToTrac(dateFrom, -tracTimeOffset);
		try {
			ArrayList<TicketChanges> ticketChangesList = new ArrayList<TicketChanges>();
			ArrayList<Integer> ticketIds = tracConnector.getRecentChanges(dateFrom);
			for(int id: ticketIds){
				TracTicket currentVersion = tracConnector.getTicket(id);
				ArrayList<ChangeLog> changeLogList = tracConnector.getChangeLogList(id);
				System.out.println(changeLogList.toString());
				ArrayList<TracTicket> tracTicketList = tracConnector.getTicketHistoryFromChangeLog(changeLogList,id,currentVersion);
				TicketChanges ticketChanges = new TicketChanges();
				ticketChanges.setId(id);
				ticketChanges.setRemoteId(currentVersion.getRemoteId());
				ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
				for(TracTicket tracTicket: tracTicketList){
					if(tracTicket.getComment()!=null&&tracTicket.getComment().contains(editCommentMarker)){
						if(tracTicket.getTimeChanged().after(dateWithoutOffset))
								ticketList.add(createTicketFromTracTicket(tracTicket));
					}else{
						if(tracTicket.getTimeChanged().after(dateFrom))
							ticketList.add(createTicketFromTracTicket(tracTicket));
					}
				}
				if(ticketList.size() != 0){
					ticketChanges.setTickets(ticketList);
					ticketChangesList.add(ticketChanges);
				}
			}
			System.out.println(ticketChangesList.toString());
			return ticketChangesList;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
	
			throw new RuntimeException(e.toString());
		}
		
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return projectName;
	}

	
	
}
