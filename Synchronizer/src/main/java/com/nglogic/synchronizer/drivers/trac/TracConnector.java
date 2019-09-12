package com.nglogic.synchronizer.drivers.trac;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.nglogic.synchronizer.BTSSynchronizer;

public class TracConnector {

	private XmlRpcClient client;
	private String remoteIdFieldName;
	private String editCommentMarker;
	static private Logger log = Logger.getLogger(BTSSynchronizer.class);
	public TracConnector(String url, String userLogin, String userPassword, String remoteIdFieldName, String editCommentMarker)
			throws MalformedURLException {
		this.remoteIdFieldName = remoteIdFieldName;
		this.editCommentMarker = editCommentMarker;
		client = new XmlRpcClient();
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(url));
		config.setBasicUserName(userLogin);
		config.setBasicPassword(userPassword);
		client.setConfig(config);
	}

	public ArrayList<Integer> getRecentChanges(Date since)
			throws XmlRpcException {
		ArrayList<Integer> ticketIdList = new ArrayList<Integer>();
		Object[] response = (Object[]) client.execute(
				"ticket.getRecentChanges", new Object[] { since });
		for (int i = 0; i < response.length; i++)
			ticketIdList.add((Integer) response[i]);
		return ticketIdList;
	}

	public ArrayList<ChangeLog> getChangeLogList(Integer ticketId)
			throws XmlRpcException {
		ArrayList<ChangeLog> changeLogList = new ArrayList<ChangeLog>();
		Object[] response = (Object[]) client.execute("ticket.changeLog",
				new Object[] { new Integer(ticketId) });
		for (int i = 0; i < response.length; i++) {
			ChangeLog changeLog = new ChangeLog();
			Object[] responseItem = (Object[]) response[i];
			changeLog.setTime((Date) responseItem[0]);
			changeLog.setAuthor((String) responseItem[1]);
			changeLog.setField((String) responseItem[2]);
			changeLog.setOldvalue((String) responseItem[3]);
			changeLog.setNewvalue((String) responseItem[4]);
			changeLogList.add(changeLog);
		}
		return changeLogList;

	}

	public ArrayList<TracTicket> getTicketHistoryFromChangeLog(
			ArrayList<ChangeLog> changeLogList, int ticketId, TracTicket currentVersion)
			throws XmlRpcException {

		TracTicket startVersion = getTicketStartVersion(currentVersion,
				changeLogList);
		

		ArrayList<TracTicket> resultList = new ArrayList<TracTicket>();
		resultList.add(startVersion);
		int startIdx = 0;
		while (startIdx != changeLogList.size()) {

			TracTicket baseTicket = null;

			ArrayList<TracTicket> comments = new ArrayList<TracTicket>();
			Date changeDate = changeLogList.get(startIdx).getTime();
			
			boolean commentEdit = false;
			while (startIdx != changeLogList.size()
					&& (changeLogList.get(startIdx).getField()
							.startsWith("_comment") || changeLogList
							.get(startIdx).getField().startsWith("comment"))) {
				TracTicket tracTicket = new TracTicket();
				if (baseTicket == null)
					baseTicket = tracTicket;

				tracTicket.setTicketId(ticketId);
				if (changeLogList.get(startIdx).getField()
						.startsWith("_comment") ){
					tracTicket.setComment( (commentEdit? editCommentMarker: "") +changeLogList.get(startIdx)
							.getOldvalue());
					commentEdit = true;
				}
				else{
					String addEdit = "";
					if(commentEdit)
						addEdit=editCommentMarker;
					tracTicket.setComment(addEdit+changeLogList.get(startIdx)
							.getNewvalue());
				}
				tracTicket.setTimeChanged(changeDate);
				if (changeLogList.get(startIdx).getField()
						.startsWith("_comment")) {
					String dateString = changeLogList.get(startIdx)
							.getNewvalue();
					dateString = dateString.substring(0,
							dateString.length() - 3);
					changeDate = (new Date(Long.parseLong(dateString)));

				}
				tracTicket.setAuthor(changeLogList.get(startIdx).getAuthor());
				comments.add(tracTicket);
				startIdx++;
				if (changeLogList.get(startIdx - 1).getField()
						.startsWith("comment")) {
					break;
				}

			}

			HashMap<String, String> attributesMap = new HashMap<String, String>();
			while (startIdx != changeLogList.size()
					&& !changeLogList.get(startIdx).getField()
							.startsWith("_comment")
					&& !changeLogList.get(startIdx).getField()
							.startsWith("comment")) {
				attributesMap.put(changeLogList.get(startIdx).getField(),
						changeLogList.get(startIdx).getNewvalue());
				startIdx++;
			}

			if (attributesMap != null) {

				setTicketFieldsWithMapEntries(baseTicket, attributesMap);
			}

			resultList.addAll(comments);
		}
		//nie da sie odzyskac prawdziwej daty utworzenia ticketu,
		//najwyzej date ostatniego reopen
		for(TracTicket t: resultList)
		if(t.getTimeChanged().before(startVersion.getTimeCreated()))
			startVersion.setTimeCreated(t.getTimeChanged());
		startVersion.setTimeChanged(startVersion.getTimeCreated());
		return resultList;
	}

	private void setTicketFieldsWithMapEntries(TracTicket baseTicket,
			HashMap<String, String> attributesMap) {
		if(attributesMap.containsKey("component"))
			baseTicket.setComponent((String) attributesMap.get("component"));
		if(attributesMap.containsKey("description"))
			baseTicket.setDescription((String) attributesMap.get("description"));
		if(attributesMap.containsKey("keywords"))
			baseTicket.setKeywords((String) attributesMap.get("keywords"));
		if(attributesMap.containsKey("milestone"))
			baseTicket.setMilestone((String) attributesMap.get("milestone"));
		if(attributesMap.containsKey("owner"))
			baseTicket.setOwner((String) attributesMap.get("owner"));
		if(attributesMap.containsKey("priority"))
			baseTicket.setPriority((String) attributesMap.get("priority"));
		try {
			if(attributesMap.containsKey("remoteIdFieldName"))
				baseTicket.setRemoteId(Integer.parseInt((String) attributesMap
					.get(remoteIdFieldName)));
		} catch (NumberFormatException nfe) {
			// nic nie robimy
		}
		if(attributesMap.containsKey("reporter"))
			baseTicket.setReporter((String) attributesMap.get("reporter"));
		if(attributesMap.containsKey("resolution"))
			baseTicket.setResolution((String) attributesMap.get("resolution"));
		if(attributesMap.containsKey("status"))
			baseTicket.setStatus((String) attributesMap.get("status"));
		if(attributesMap.containsKey("summary"))
			baseTicket.setSummary((String) attributesMap.get("summary"));
		if(attributesMap.containsKey("type"))
			baseTicket.setType((String) attributesMap.get("type"));
		if(attributesMap.containsKey("version"))
			baseTicket.setVersion((String) attributesMap.get("version"));
	}

	// void fillTracTicketFieldValue

	// createChangeFromTracUpdate
	// createChangeFromTracUpdateOrCreateComment

	private TracTicket getTicketStartVersion(TracTicket currentVersion,
			ArrayList<ChangeLog> changeLogList) {
		TracTicket resultTicket = (TracTicket) currentVersion.clone();
		if(changeLogList.size()>0){
			Collections.reverse(changeLogList);
			HashMap<String, String> attributesMap = new HashMap<String, String>();
			for (ChangeLog cl : changeLogList) {
				if(!cl.getField().contains("comment")){
					attributesMap.put(cl.getField(), cl.getOldvalue().equals("")?null:cl.getOldvalue());
				}
			}
			Collections.reverse(changeLogList);
			setTicketFieldsWithMapEntries(resultTicket, attributesMap);
			
		}
		
		resultTicket.setAuthor(resultTicket.getReporter());
		
		return resultTicket;
	}

	public TracTicket getTicket(int ticketId) throws XmlRpcException {

		TracTicket tracTicket = new TracTicket();

		Object[] response = (Object[]) client.execute("ticket.get",
				new Object[] { new Integer(ticketId) });
		tracTicket.setTicketId((Integer) response[0]);
		tracTicket.setTimeCreated((Date) response[1]);
		tracTicket.setTimeChanged((Date) response[2]);
		HashMap<String, Object> attributesMap = (HashMap<String, Object>) response[3];
		if (((String) attributesMap.get("component")).equals(""))
			tracTicket.setComponent(null);
		else
			tracTicket.setComponent((String) attributesMap.get("component"));
		if (((String) attributesMap.get("description")).equals(""))
			tracTicket.setDescription(null);
		else
			tracTicket
					.setDescription((String) attributesMap.get("description"));
		if (((String) attributesMap.get("keywords")).equals(""))
			tracTicket.setKeywords(null);
		else
			tracTicket.setKeywords((String) attributesMap.get("keywords"));
		if (((String) attributesMap.get("milestone")).equals(""))
			tracTicket.setMilestone(null);
		else
			tracTicket.setMilestone((String) attributesMap.get("milestone"));
		if (((String) attributesMap.get("owner")).equals(""))
			tracTicket.setOwner(null);
		else
			tracTicket.setOwner((String) attributesMap.get("owner"));
		if (((String) attributesMap.get("priority")).equals(""))
			tracTicket.setPriority(null);
		else
			tracTicket.setPriority((String) attributesMap.get("priority"));

		if(attributesMap.get(remoteIdFieldName)!=null){
			if (((String) attributesMap.get(remoteIdFieldName)).equals(""))
				tracTicket.setRemoteId(null);
			else
				tracTicket.setRemoteId(Integer.parseInt((String) attributesMap
					.get(remoteIdFieldName)));
		}else{
			tracTicket.setRemoteId(null);
		}
		if (((String) attributesMap.get("reporter")).equals(""))
			tracTicket.setReporter(null);
		else
			tracTicket.setReporter((String) attributesMap.get("reporter"));

		if (((String) attributesMap.get("resolution")).equals(""))
			tracTicket.setResolution(null);
		else
			tracTicket.setResolution((String) attributesMap.get("resolution"));
		if (((String) attributesMap.get("status")).equals(""))
			tracTicket.setStatus(null);
		else
			tracTicket.setStatus((String) attributesMap.get("status"));
		if (((String) attributesMap.get("summary")).equals(""))
			tracTicket.setSummary(null);
		else
			tracTicket.setSummary((String) attributesMap.get("summary"));
		if (((String) attributesMap.get("type")).equals(""))
			tracTicket.setType(null);
		else
			tracTicket.setType((String) attributesMap.get("type"));
		if (((String) attributesMap.get("version")).equals(""))
			tracTicket.setVersion(null);
		else
			tracTicket.setVersion((String) attributesMap.get("version"));

		return tracTicket;
	}
	public TracTicket getTicketByRemoteId(int remoteId) throws XmlRpcException{
		TracTicket tracTicket = null;
		Object[] responseId = (Object[]) client.execute("ticket.query",
				new Object[] { new String("id_assembli="+Integer.toString(remoteId)) });
		if(responseId.length>1){
			//wysylamy maila o bledzie do admina
			log.error("remoteID: "+remoteId+" assigned to many tickets in Trac.");
		}
		else if(responseId.length==1){
			int ticketId = (Integer)responseId[0];
			tracTicket = getTicket(ticketId);
		}
		return tracTicket;
	}
	
	
	public void updateTicket(int id, String comment,HashMap<String, String> attributes,String author, Date when) throws XmlRpcException{
		Object[] params;
		if(comment == null)
			comment = "";
		params  = new Object[] {id, comment, attributes, new Boolean(false), author, when};
		client.execute("ticket.update", params);
	}
	
	public void createTicket(String summary, String description, HashMap<String, String> attributes,Date when) throws XmlRpcException{
		Object[] params;
		if(summary== null)
			summary="";
		if(description==null)
			description="";
		params  = new Object[] {summary, description, attributes, new Boolean(false), when};
		client.execute("ticket.create", params);
	}
	
	public void deleteTicket(int ticketId) throws XmlRpcException {

		int response = (Integer) client.execute("ticket.delete",
				new Object[] { new Integer(ticketId) });
	}
}
