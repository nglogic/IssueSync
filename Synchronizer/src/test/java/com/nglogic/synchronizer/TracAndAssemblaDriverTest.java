package com.nglogic.synchronizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.CommunicationException;

import org.junit.Before;
import org.junit.Test;

import com.nglogic.synchronizer.drivers.BTSRDriver;
import com.nglogic.synchronizer.drivers.assembla.AssemblaDriver;
import com.nglogic.synchronizer.drivers.trac.TracDriver;
import com.nglogic.synchronizer.exceptions.AssemblaConfigurationException;
import com.nglogic.synchronizer.exceptions.TracConfigurationException;

public class TracAndAssemblaDriverTest {
	/*AssemblaDriver outerBTS;
	TracDriver innerBTS;
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
		
		synchronizationInnerToOuterMapper.statusMap.put("new", "New");
		synchronizationInnerToOuterMapper.statusMap.put("assigned", "Accepted");
		synchronizationInnerToOuterMapper.statusMap.put("fixed", "Fixed");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek2", "gosiek");
		synchronizationInnerToOuterMapper.userLoginMap.put("gosiek", "gosiiek");
		synchronizationInnerToOuterMapper.priorityMap.put("major", "Normal");
		synchronizationInnerToOuterMapper.priorityMap.put("critical", "High");
		synchronizationInnerToOuterMapper.priorityMap.put("minor", "Low");
		synchronizationInnerToOuterMapper.priorityMap.put("trivial", "Lowest");
		synchronizationInnerToOuterMapper.componentMap.put("component1", "noga");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone1", "Backlog");
		synchronizationInnerToOuterMapper.milestoneMap.put("milestone2", "Current");
		synchronizationInnerToOuterMapper.commentMarker="ASS";
		synchronizationInnerToOuterMapper.otherUser="other";
		

		
	
		
		try {
			 outerBTS = new AssemblaDriver("projekt-synchronizacji");
			innerBTS = new TracDriver();
			synchronizer = new BTSSynchronizer(innerBTS, outerBTS, innerTicketFilter, synchronizationInnerToOuterMapper);
		} catch (TracConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AssemblaConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@Test
	public void testNewTicketInTracSuccessfullSynchronization() throws ParseException, CommunicationException{

			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = f.parse("2013-04-22 22:03:00");
			//odpalamy synchronizacje
			synchronizer.synchronizeBtsProjects("1", "1", date);
	}*/
}
