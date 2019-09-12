package com.nglogic.synchronizer;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.CommunicationException;

import org.junit.Before;
import org.junit.Test;

import com.nglogic.synchronizer.drivers.BTSDriverInterface;
import com.nglogic.synchronizer.drivers.BTSRDriver;
import com.nglogic.synchronizer.drivers.assembla.AssemblaDriver;
import com.nglogic.synchronizer.drivers.trac.TracDriver;
import com.nglogic.synchronizer.exceptions.AssemblaConfigurationException;
import com.nglogic.synchronizer.exceptions.TracConfigurationException;

public class AssemblaDriverTest {
	
	
	
	BTSDriverInterface outerBTS;
	BTSRDriver innerBTS;
	BTSSynchronizer synchronizer;
	Ticket ticketInner, ticketOuter;
	Date date;
	@Before
	public void init() throws TracConfigurationException, AssemblaConfigurationException{
		/*InnerTicketFilter innerTicketFilter = new InnerTicketFilter();
		SynchronizationMapper synchronizationInnerToOuterMapper = new SynchronizationMapper();
		
		innerTicketFilter.setUser("gosiek");
		innerTicketFilter.setUser("gosiek2");
		innerTicketFilter.setUser("marysia");
		innerTicketFilter.setPriority("major");
		innerTicketFilter.setPriority("minor");
		innerTicketFilter.setPriority("trival");
		innerTicketFilter.setMilestone("milestone1");
		innerTicketFilter.setMilestone("milestone2");
		
		synchronizationInnerToOuterMapper.statusMap.put("new", "New");
		synchronizationInnerToOuterMapper.statusMap.put("assigned", "accepted");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek2", "gosiek");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek", "gosiiek");
		synchronizationInnerToOuterMapper.priorityMap.put("major", "Normal");
		synchronizationInnerToOuterMapper.priorityMap.put("critical", "High");
		synchronizationInnerToOuterMapper.priorityMap.put("minor", "Low");
		synchronizationInnerToOuterMapper.priorityMap.put("trivial", "Lowest");
		synchronizationInnerToOuterMapper.componentMap.put("component1", "noga");
		synchronizationInnerToOuterMapper.componentMap.put("hand", "h");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone1", "Backlog");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone2", "Current");
		synchronizationInnerToOuterMapper.commentMarker="ASS";
		synchronizationInnerToOuterMapper.otherUser="other";
		
		/*ticketInner = new Ticket();
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
		*/
		
	/*	ticketOuter = new Ticket();
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
		
	
		outerBTS = new AssemblaDriver("projekt-synchronizacji");
		innerBTS = new BTSRDriver("innerBTS");
		synchronizer = new BTSSynchronizer(innerBTS, outerBTS, innerTicketFilter, synchronizationInnerToOuterMapper);
	*/
		
	}
	
	@Test
	public void testNewTicketInTracSuccessfullSynchronization() throws ParseException, CommunicationException{
		
	/*	innerBTS.clearTickets();
	
			
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = f.parse("2013-04-21 13:15:00");
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("1", "1", date);
			//weryfikujemy poprawnosc wyniku
			//ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			ArrayList<Ticket> innerTickets = innerBTS.getOutputTickets();
			
			//assertEquals(1,  innerTickets.size());
			assertEquals(1,  innerTickets.size());
			
			//Ticket innerTicket = innerTickets.get(0);
			Ticket innerTicket = innerTickets.get(0);
			
			
		
			assertEquals("new",innerTicket.getStatus());
			assertEquals("gosiek",innerTicket.getReporter());
			assertEquals("gosiek",innerTicket.getOwner());
			assertEquals("gosiek",innerTicket.getAuthor());
			assertEquals("major",innerTicket.getPriority());
			assertEquals(null,innerTicket.getType());
			assertEquals("component1",innerTicket.getComponent());
			assertEquals("milestone1",innerTicket.getMilestone());
			System.out.println(innerTicket.getUpdateTime().toString());
			//assertEquals(date,innerTicket.getUpdateTime());
			assertEquals("junit1",innerTicket.getSummary());
			assertEquals("opis",innerTicket.getDescription());
			assertEquals(null,innerTicket.getComment());
			assertEquals(new Integer(111), innerTicket.getTicketId());*/
	
	}
	/*AssemblaDriver assemblaDriver;
	Ticket ticketOuter;
	Date date;
	@Before
	public void init(){
		try {
			assemblaDriver = new AssemblaDriver("projekt-synchronizacji");
		
			ticketOuter = new Ticket();
			ticketOuter.setStatus("New");
			ticketOuter.setAuthor("gosiiek");
			ticketOuter.setReporter("gosiiek");
			ticketOuter.setOwner("gosiiek");
			ticketOuter.setPriority("Low");
			ticketOuter.setComponent("noga");
			ticketOuter.setMilestone("Backlog");
			date = new Date();
			ticketOuter.setUpdateTime(date);
			ticketOuter.setSummary("jakis ticket");
			ticketOuter.setDescription("jakis opis ticketu");
		//ticketOuter.setComment("jakis komentarz");
		} catch (AssemblaConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void createAssemblaTicketTest() throws CommunicationException{
		
		Ticket retTicket = assemblaDriver.createTicket(ticketOuter);
		assertEquals(ticketOuter.getStatus(),retTicket.getStatus());
		assertEquals(null,retTicket.getAuthor());
		assertEquals(ticketOuter.getReporter(),retTicket.getReporter());
		assertEquals(ticketOuter.getOwner(),retTicket.getOwner());
		assertEquals(ticketOuter.getPriority(),retTicket.getPriority());
		assertEquals(ticketOuter.getComponent(),retTicket.getComponent());
		assertEquals(ticketOuter.getMilestone(),retTicket.getMilestone());
		assertEquals(ticketOuter.getUpdateTime(),retTicket.getUpdateTime());
		assertEquals(ticketOuter.getSummary(),retTicket.getSummary());
		assertEquals(ticketOuter.getDescription(),retTicket.getDescription());
		assertEquals(ticketOuter.getComment(),retTicket.getComment());
		
	}
	@Test
	public void updateAssemblaTest(){
		Ticket ticket = ticketOuter.clone();
		ticket.setTicketId(50362233);
		ticket.setOwner("gosiek");
		assemblaDriver.updateTicket(ticket);
		
	}
*/
}
