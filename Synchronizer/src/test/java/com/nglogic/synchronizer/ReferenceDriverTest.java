package com.nglogic.synchronizer;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.naming.CommunicationException;

import org.junit.Before;
import org.junit.Test;

import com.nglogic.synchronizer.drivers.BTSRDriver;
import com.nglogic.synchronizer.drivers.assembla.AssemblaDriver;
import com.nglogic.synchronizer.drivers.trac.TracDriver;
import com.nglogic.synchronizer.exceptions.AssemblaConfigurationException;
import com.nglogic.synchronizer.exceptions.TracConfigurationException;

import static  org.junit.Assert.*;

public class ReferenceDriverTest {
/*	BTSRDriver innerBTS;
	BTSRDriver outerBTS;
	//TracDriver innerBTS;
	//AssemblaDriver outerBTS;
	BTSSynchronizer synchronizer;
	Ticket ticketInner, ticketOuter;
	Date date;
	@Before
	public void init(){
		InnerTicketFilter innerTicketFilter = new InnerTicketFilter();
		SynchronizationMapper synchronizationInnerToOuterMapper = new SynchronizationMapper();
		
		innerTicketFilter.setUser("gosiek");
		innerTicketFilter.setUser("gosiek2");
		innerTicketFilter.setUser("marysia");
		innerTicketFilter.setPriority("major");
		innerTicketFilter.setPriority("minor");
		innerTicketFilter.setPriority("trival");
		innerTicketFilter.setMilestone("milestone1");
		innerTicketFilter.setMilestone("milestone2");
		
		synchronizationInnerToOuterMapper.statusMap.put("new", "new");
		synchronizationInnerToOuterMapper.statusMap.put("assigned", "accepted");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek2", "gosiek");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek", "gosiiek");
		synchronizationInnerToOuterMapper.priorityMap.put("major", "normal");
		synchronizationInnerToOuterMapper.priorityMap.put("critical", "high");
		synchronizationInnerToOuterMapper.priorityMap.put("minor", "low");
		synchronizationInnerToOuterMapper.priorityMap.put("trivial", "lowest");
		synchronizationInnerToOuterMapper.componentMap.put("leg", "l");
		synchronizationInnerToOuterMapper.componentMap.put("hand", "h");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone1", "m1");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone2", "m2");
		synchronizationInnerToOuterMapper.commentMarker="ASS";
		synchronizationInnerToOuterMapper.otherUser="other";
		
		ticketInner = new Ticket();
		ticketInner.setStatus("new");
		ticketInner.setReporter("gosia");
		ticketInner.setOwner("asia");
		ticketInner.setPriority("major");
		ticketInner.setType("task");
		ticketInner.setComponent("leg");
		ticketInner.setMilestone("milestone1");
		date = new Date();
		ticketInner.setUpdateTime(date);
		ticketInner.setSummary("jakis ticket");
		ticketInner.setDescription("jakis opis ticketu");
		ticketInner.setComment("jakis komentarz ASS");
		
		ticketOuter = new Ticket();
		ticketOuter.setStatus("new");
		ticketOuter.setReporter("goska");
		ticketOuter.setOwner("aska");
		ticketOuter.setPriority("low");
		ticketOuter.setComponent("h");
		ticketOuter.setMilestone("m2");
		date = new Date();
		ticketOuter.setUpdateTime(date);
		ticketOuter.setSummary("jakis ticket");
		ticketOuter.setDescription("jakis opis ticketu");
		ticketOuter.setComment("jakis komentarz");
		
		innerBTS = new BTSRDriver("innerBTS");
		outerBTS = new BTSRDriver("outerBTS");
		
		synchronizer = new BTSSynchronizer(innerBTS, outerBTS, innerTicketFilter, synchronizationInnerToOuterMapper);
	
		
		
			
	}
	
	@Test
	public void testNewTicketInTracSuccessfullSynchronization() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketInner.clone();
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("1", "1", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			Ticket outerTicket = outerTickets.get(0);
			
			
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals("asia",innerTicket.getOwner());
			assertEquals("major",innerTicket.getPriority());
			assertEquals("task",innerTicket.getType());
			assertEquals("leg",innerTicket.getComponent());
			assertEquals("milestone1",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz ASS",innerTicket.getComment());
		
			assertEquals("new",outerTicket.getStatus());
			assertEquals("goska",outerTicket.getReporter());
			assertEquals("aska",outerTicket.getOwner());
			assertEquals("normal",outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals("m1",outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals("jakis komentarz ",outerTicket.getComment());
			assertEquals(innerTicket.getRemoteID(), outerTicket.getTicketId());
	
	}
	
	@Test
	public void testUpdateTicketInTracSuccessfullSynchronization() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1 = ticketInner.clone();
			ticket1.setTicketId(1);
			ticket1.setRemoteID(10);
			ticket1.setStatus("assigned");
			ticket1.setComment("jakis komentarz bez asa");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			ticketChanges.setRemoteId(10);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("2", "2", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			assertEquals(0,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			Ticket outerTicket = outerTickets.get(0);
			
			assertEquals("accepted",outerTicket.getStatus());
			assertEquals("goska",outerTicket.getReporter());
			assertEquals("aska",outerTicket.getOwner());
			assertEquals("normal",outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals("m1",outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals(null,outerTicket.getComment());
			assertEquals(new Integer(10), outerTicket.getTicketId());

	}
	
	@Test
	public void testNewTicketInAssemblaSuccessfullSynchronization() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketOuter.clone();
			
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			outerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("3", "3", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			
			
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals("asia",innerTicket.getOwner());
			assertEquals("minor",innerTicket.getPriority());
			assertEquals(null,innerTicket.getType());
			assertEquals("hand",innerTicket.getComponent());
			assertEquals("milestone2",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz",innerTicket.getComment());
			assertEquals(ticket1.getTicketId(),innerTicket.getRemoteID());
		
			
	
	}
	
	@Test
	public void testUpdateTicketInAssemblaSuccessfullSynchronization() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketOuter.clone();
			ticket1.setTicketId(11);
			ticket1.setStatus("accepted");
			ticket1.setOwner("zosia");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(11);
			inputTicketChangesList.add(ticketChanges);
			outerBTS.setInputTicketsChanges(inputTicketChangesList);
			Ticket ticket2 = new Ticket();
			ticket2.setTicketId(100);
			ticket2.setRemoteID(11);
			innerBTS.setTicketByRemoteId(ticket2.getRemoteID(), ticket2);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("4", "4", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			
			
			
			assertEquals("assigned",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals("other",innerTicket.getOwner());
			assertEquals("minor",innerTicket.getPriority());
			assertEquals(null,innerTicket.getType());
			assertEquals("hand",innerTicket.getComponent());
			assertEquals("milestone2",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz",innerTicket.getComment());
			assertEquals(ticket1.getTicketId(),innerTicket.getRemoteID());
		
			
	
	}
	
	@Test
	public void testConflitShouldNotSynchronized() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1= ticketOuter.clone();
		ticket1.setTicketId(11);
			
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
		ticketList.add(ticket1);
		ArrayList<TicketChanges> outerTicketChangesList = new ArrayList<TicketChanges>();
		TicketChanges ticketChanges = new TicketChanges();
		ticketChanges.setTickets(ticketList);
		ticketChanges.setId(11);
		outerTicketChangesList.add(ticketChanges);
		outerBTS.setInputTicketsChanges(outerTicketChangesList);
		
		Ticket ticket2 = new Ticket();
		ticket2.setTicketId(100);
		ticket2.setRemoteID(11);
		innerBTS.setTicketByRemoteId(ticket2.getRemoteID(), ticket2);
		ticketList = new ArrayList<Ticket>();
		ticketList.add(ticket2);
		ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
		TicketChanges ticketChanges2 = new TicketChanges();
		ticketChanges2.setTickets(ticketList);
		ticketChanges2.setId(100);
		ticketChanges2.setRemoteId(11);
		inputTicketChangesList.add(ticketChanges2);
		innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("5", "5", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(0,  innerTickets.size());
			assertEquals(0,  outerTickets.size());
	}
	
	@Test
	public void testReporterNotInFilterShouldNotSynchronized() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketInner.clone();
			ticket1.setReporter("zosia");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("6", "6", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			Ticket outerTicket = outerTickets.get(0);
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals(null,innerTicket.getReporter());
			assertEquals("asia",innerTicket.getOwner());
			assertEquals("major",innerTicket.getPriority());
			assertEquals("task",innerTicket.getType());
			assertEquals("leg",innerTicket.getComponent());
			assertEquals("milestone1",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz ASS",innerTicket.getComment());
			
			assertEquals("new",outerTicket.getStatus());
			assertEquals(null,outerTicket.getReporter());
			assertEquals("aska",outerTicket.getOwner());
			assertEquals("normal",outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals("m1",outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals("jakis komentarz ",outerTicket.getComment());
			assertEquals(innerTicket.getRemoteID(), outerTicket.getTicketId());
			
	}
	@Test
	public void testOwnerNotInFilterShouldNotSynchronized() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketInner.clone();
			ticket1.setOwner("zosia");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("7", "7", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			Ticket innerTicket = innerTickets.get(0);
			Ticket outerTicket = outerTickets.get(0);
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals(null,innerTicket.getOwner());
			assertEquals("major",innerTicket.getPriority());
			assertEquals("task",innerTicket.getType());
			assertEquals("leg",innerTicket.getComponent());
			assertEquals("milestone1",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz ASS",innerTicket.getComment());
			
			assertEquals("new",outerTicket.getStatus());
			assertEquals("goska",outerTicket.getReporter());
			assertEquals(null,outerTicket.getOwner());
			assertEquals("normal",outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals("m1",outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals("jakis komentarz ",outerTicket.getComment());
			assertEquals(innerTicket.getRemoteID(), outerTicket.getTicketId());
	}
	
	@Test
	public void testPriorityNotInFilterShouldNotSynchronized() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketInner.clone();
			ticket1.setPriority("blocker");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("8", "8", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			Ticket outerTicket = outerTickets.get(0);
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals("asia",innerTicket.getOwner());
			assertEquals(null,innerTicket.getPriority());
			assertEquals("task",innerTicket.getType());
			assertEquals("leg",innerTicket.getComponent());
			assertEquals("milestone1",innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz ASS",innerTicket.getComment());
			
			assertEquals("new",outerTicket.getStatus());
			assertEquals("goska",outerTicket.getReporter());
			assertEquals("aska",outerTicket.getOwner());
			assertEquals(null,outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals("m1",outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals("jakis komentarz ",outerTicket.getComment());
			assertEquals(innerTicket.getRemoteID(), outerTicket.getTicketId());
	}
	@Test
	public void testMilestoneNotInFilterShouldNotSynchronized() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketInner.clone();
			ticket1.setMilestone("jakas mila");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(1);
			inputTicketChangesList.add(ticketChanges);
			innerBTS.setInputTicketsChanges(inputTicketChangesList);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("9", "9", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			assertEquals(1,  outerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			Ticket outerTicket = outerTickets.get(0);
			
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosia",innerTicket.getReporter());
			assertEquals("asia",innerTicket.getOwner());
			assertEquals("major",innerTicket.getPriority());
			assertEquals("task",innerTicket.getType());
			assertEquals("leg",innerTicket.getComponent());
			assertEquals(null,innerTicket.getMilestone());
			assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("jakis ticket",innerTicket.getSummary());
			assertEquals("jakis opis ticketu",innerTicket.getDescription());
			assertEquals("jakis komentarz ASS",innerTicket.getComment());
			
			assertEquals("new",outerTicket.getStatus());
			assertEquals("goska",outerTicket.getReporter());
			assertEquals("aska",outerTicket.getOwner());
			assertEquals("normal",outerTicket.getPriority());
			assertEquals(null,outerTicket.getType());
			assertEquals("l",outerTicket.getComponent());
			assertEquals(null,outerTicket.getMilestone());
			assertEquals(date,outerTicket.getUpdateTime());
			assertEquals("jakis ticket",outerTicket.getSummary());
			assertEquals("jakis opis ticketu",outerTicket.getDescription());
			assertEquals("jakis komentarz ",outerTicket.getComment());
			assertEquals(innerTicket.getRemoteID(), outerTicket.getTicketId());
			
	}
	
	@Test
	public void testStatusAssemblaTicketNotInMapperSuccessfullSynchronization() throws CommunicationException{
		innerBTS.clearTickets();
		outerBTS.clearTickets();
		//ustawiamy tickety
		Ticket ticket1=null;
			ticket1 = ticketOuter.clone();
			ticket1.setTicketId(11);
			ticket1.setStatus("jakis");
			
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			ticketList.add(ticket1);
			ArrayList<TicketChanges> inputTicketChangesList = new ArrayList<TicketChanges>();
			TicketChanges ticketChanges = new TicketChanges();
			ticketChanges.setTickets(ticketList);
			ticketChanges.setId(11);
			inputTicketChangesList.add(ticketChanges);
			outerBTS.setInputTicketsChanges(inputTicketChangesList);
			Ticket ticket2 = new Ticket();
			ticket2.setTicketId(100);
			ticket2.setRemoteID(11);
			innerBTS.setTicketByRemoteId(ticket2.getRemoteID(), ticket2);
			
			
			
			
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("10", "10", date);
			//weryfikujemy poprawnosc wyniku
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> outerTickets = outerBTS.getOutputTickets();
			
			assertEquals(1,  innerTickets.size());
			
			Ticket innerTicket = innerTickets.get(0);
			
			
			
			assertEquals(null,innerTicket.getStatus());
	}
	/*@Test
	public void testPierwszy(){
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedDate;
		try {
			parsedDate = f.parse("2013-04-16 13:30:00");
			synchronizer.synchronizeBtsProjects("ProjectTest", "projekt-synchronizacji", parsedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}