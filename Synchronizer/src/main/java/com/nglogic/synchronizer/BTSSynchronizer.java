package com.nglogic.synchronizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;

import com.google.common.collect.HashBiMap;
import com.nglogic.synchronizer.config.BTSDriverConfig;
import com.nglogic.synchronizer.config.SynchronizerConfiguration;
import com.nglogic.synchronizer.drivers.BTSDriverInterface;
import com.nglogic.synchronizer.drivers.assembla.AssemblaDriver;
import com.nglogic.synchronizer.drivers.trac.TracDriver;
import com.nglogic.synchronizer.exceptions.CommunicationException;
import com.nglogic.synchronizer.exceptions.ConfigurationException;

public class BTSSynchronizer {

	private BTSDriverInterface innerBTS;
	private BTSDriverInterface outerBTS;
	private InnerTicketFilter innerTicketFilter;
	private SynchronizationMapper synchronizationMapper;
	
	static private Logger log = Logger.getLogger(BTSSynchronizer.class);
	
	public static BTSDriverInterface createDriver(BTSDriverConfig config) throws ConfigurationException{
		if(config.getDriverName().equals("AssemblaDriver")){
			return new AssemblaDriver(config);
		}else if(config.getDriverName().equals("TracDriver")){
			return new TracDriver(config);
		}else{
			throw new ConfigurationException("Unknown driver name "+config.getDriverName());
		}
		
	}
	
	public BTSSynchronizer(SynchronizerConfiguration config) throws ConfigurationException{
		innerBTS = createDriver(config.getInnerDriver());
		outerBTS = createDriver(config.getOuterDriver());
		synchronizationMapper = new SynchronizationMapper(config.getMapper());
		innerTicketFilter = new InnerTicketFilter(config.getFilter());
		
	}
	
	public Date synchronizeBtsProjects(Date dateFrom) throws CommunicationException{
		log.debug("synchronizeBtsProjects: "+innerBTS.getProjectName()+" "+outerBTS.getProjectName());
		Date maxModDate = new Date(0);
		ArrayList<TicketChanges> innerTicketChanges = innerBTS.getTicketChanges(dateFrom);
		ArrayList<TicketChanges> outerTicketChanges = outerBTS.getTicketChanges(dateFrom);
		
		HashMap<Integer, Integer> conflictedInnerIdToRemoteId=null;
		if(innerTicketChanges!=null&&outerTicketChanges!=null)
			conflictedInnerIdToRemoteId = Utils.createConflictedInnerIdToRemoteId(innerTicketChanges, outerTicketChanges);
		
		if(outerTicketChanges!=null){
			for(TicketChanges ticketChanges : outerTicketChanges){
				if(maxModDate.before( ticketChanges.getLastModification())){
					maxModDate = ticketChanges.getLastModification();
				}
				Integer outerTicketId=ticketChanges.getId();
				if(conflictedInnerIdToRemoteId ==null || !conflictedInnerIdToRemoteId.containsValue(outerTicketId)){
					try{
						transferOuterToInner(ticketChanges);
					}catch(Exception e){
						log.error("nie udalo sie przeslac zmian ticketa "+ticketChanges.getId()+" do systemu wewnetrznego",e);
						//wyslij maila
					}
				}
			}
		}
		if(innerTicketChanges!=null){
			for(TicketChanges ticketChanges : innerTicketChanges){
				if(maxModDate.before( ticketChanges.getLastModification())){
					maxModDate = ticketChanges.getLastModification();
				}
				if(conflictedInnerIdToRemoteId == null || !conflictedInnerIdToRemoteId.containsKey(ticketChanges.getId())){
					try{
						transferInnerToOuter(ticketChanges);
					}catch(Exception e){
						log.error("nie udalo sie przeslac zmian ticketa "+ticketChanges.getId()+" do systemu zewnetrznego",e);
						//wyslij maila
					}
				}else{
					processConflict(conflictedInnerIdToRemoteId.get(ticketChanges.getId()), ticketChanges.getId());
				}
			}
		}
		return maxModDate;
	}
	
	private void transferInnerToOuter(TicketChanges innerTicketChanges) throws CommunicationException {
		for(Ticket ticket: innerTicketChanges.getTickets()){
			Ticket innerTicket = innerTicketFilter.filteredTicket(ticket);
			if(innerTicket!=null){
				log.debug("transferInnerToOuter: filtr zaakceptowal "+innerTicket);
				Ticket outerTicket = synchronizationMapper.innerToOuterMapper(innerTicket);
					
				if(innerTicketChanges.getRemoteId()!=null){
					outerTicket.setTicketId(innerTicketChanges.getRemoteId());
					outerBTS.updateTicket(outerTicket);
				}
				else{
					Ticket newOuterTicket = outerBTS.createTicket(outerTicket);
					innerTicketChanges.setRemoteId(newOuterTicket.getTicketId());
					
					innerTicket.setRemoteID(newOuterTicket.getTicketId());
					innerBTS.updateTicket(innerTicket);
				}
			}else{
				log.info("transferInnerToOuter: filtr odrzucil " + innerTicket);
			
			}
			
		}
	}

	private void transferOuterToInner(TicketChanges outerTicketChanges) throws CommunicationException {
		for(Ticket outerTicket: outerTicketChanges.getTickets()){
			log.debug("transferOuterToInner: "+outerTicket);
			Ticket innerTicket = synchronizationMapper.outerToInnerMapper(outerTicket);
			Ticket ticket = null;
			if(outerTicket.getTicketId()!=null)		
				ticket = innerBTS.getTicketByRemoteId(outerTicket.getTicketId());
			if(ticket!=null){
				innerTicket.setTicketId(ticket.getTicketId());
				innerBTS.updateTicket(innerTicket);
			}
			else{
				innerTicket.setRemoteID(outerTicket.getTicketId());
				innerBTS.createTicket(innerTicket);
			}
		}
	}

	public void processConflict(int outerTicketId, int innerTicketId){
		log.warn("processConflict : outerTicketId: "+outerTicketId+" innerTicketId: "+innerTicketId);
	/*	final Email email = new Email();

		email.setFromAddress("synchronizer", "mwysocka@nglogic.com");
		email.setSubject("Synchro conflict");
		email.addRecipient("admin", "mwysocka@nglogic.com", RecipientType.TO);
		//email.addRecipient("C. Bo", "chocobo@candyshop.org", RecipientType.BCC);
		email.setText("processConflict : outerTicketId: "+outerTicketId+" innerTicketId: "+innerTicketId);
		//email.setTextHTML("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>");

		//email.addAttachment("invitation", pdfByteArray, "application/pdf");

		new Mailer("mail.nglogic.com", 465, "mwysocka@nglogic.com", "11111").sendMail(email);
	*/}

	

}
