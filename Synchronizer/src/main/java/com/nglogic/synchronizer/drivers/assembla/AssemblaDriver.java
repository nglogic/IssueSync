package com.nglogic.synchronizer.drivers.assembla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.mail.internet.MimeMessage.RecipientType;
import javax.sound.sampled.ReverbType;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.springframework.cglib.core.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.nglogic.synchronizer.BTSSynchronizer;
import com.nglogic.synchronizer.Ticket;
import com.nglogic.synchronizer.TicketChanges;
import com.nglogic.synchronizer.Utils;
import com.nglogic.synchronizer.config.BTSDriverConfig;
import com.nglogic.synchronizer.drivers.BTSDriverInterface;
import com.nglogic.synchronizer.exceptions.AssemblaConfigurationException;
import com.nglogic.synchronizer.exceptions.CommunicationException;


public class AssemblaDriver implements BTSDriverInterface{
	
	private HashMap<String, AssemblaUserWithKeys> loginToAssemblaUserWithKeysMap;
	private HashBiMap<String, String> idUserToLoginMap;
	private HashBiMap<String, Integer> componentNameToIdMap;
	private HashBiMap<String, Integer> milestoneNameToIdMap;
	private HashBiMap<String, Integer> priorityNameToNumberMap;
	private String projectName;
	private String url;
	private RestTemplate restTemplate;
	private String editCommentMarker;
	static private Logger log = Logger.getLogger(AssemblaDriver.class);
	public AssemblaDriver(BTSDriverConfig config) throws AssemblaConfigurationException{
		projectName = config.getProjectName();
		url = config.getBTSUrl();
		editCommentMarker = config.getEditCommentMarker();
		restTemplate = createRestTemplate();
		try {
			loginToAssemblaUserWithKeysMap = new HashMap<String, AssemblaUserWithKeys>();
			idUserToLoginMap = new HashBiMap<String, String>();
			
			FileReader fr = new FileReader(new File(config.getAdditionalConfigFile()));
			BufferedReader br = new BufferedReader(fr);
			String line;	
			while((line = br.readLine()) != null){
				String[] lineItems = line.split(" ");
				
				RestTemplate restTemplateToRefreshToken = new RestTemplate();
				
				HttpHeaders headers = createHeaders(config.getUser(), config.getPassword());
				
				MultiValueMap<String, String> mapParameters = new LinkedMultiValueMap<String, String>();
				mapParameters.add("grant_type", "refresh_token");
				mapParameters.add("refresh_token", lineItems[2]);
				
				HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapParameters, headers);
						
				AssemblaAccessToken responseAccessToken = restTemplateToRefreshToken.postForObject(url + "token", entity, AssemblaAccessToken.class);

				AssemblaUserWithKeys aUserWithKeys= new AssemblaUserWithKeys(lineItems[0],responseAccessToken.getAccess_token(), lineItems[2]);
				loginToAssemblaUserWithKeysMap.put(lineItems[0], aUserWithKeys);
				idUserToLoginMap.put(aUserWithKeys.getUserId(), lineItems[0]);
				
			}		
			priorityNameToNumberMap = new HashBiMap<String, Integer>();
			priorityNameToNumberMap.put("Highest", 1);
			priorityNameToNumberMap.put("High", 2);
			priorityNameToNumberMap.put("Normal", 3);
			priorityNameToNumberMap.put("Low", 4);
			priorityNameToNumberMap.put("Lowest", 5);
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer "+loginToAssemblaUserWithKeysMap.values().iterator().next().getAccessToken());
		    
		    componentNameToIdMap = new HashBiMap<String, Integer>();
		    ResponseEntity<AssemblaComponent[]> responseComponentList = restTemplate.exchange(url + "v1/spaces/{projectName}/ticket_components.json",HttpMethod.GET,
		    		new HttpEntity<AssemblaComponent>(headers)
		    		, AssemblaComponent[].class,projectName);
		    if(responseComponentList.getBody()!=null){
				List<AssemblaComponent> componentList = Arrays.asList(responseComponentList.getBody());
				for(AssemblaComponent component: componentList)
					componentNameToIdMap.put(component.getName(), component.getId());
		    }
			 milestoneNameToIdMap = new HashBiMap<String, Integer>();
			 ResponseEntity<AssemblaMilestone[]> responseMilestoneList = restTemplate.exchange(url + "v1/spaces/{projectName}/milestones.json",HttpMethod.GET,
			    		new HttpEntity<AssemblaMilestone>(headers)
			    		, AssemblaMilestone[].class,projectName);
			if(responseMilestoneList.getBody()!=null){
				List<AssemblaMilestone> milestoneList = Arrays.asList(responseMilestoneList.getBody());
				for(AssemblaMilestone milestone: milestoneList)
					milestoneNameToIdMap.put(milestone.getTitle(), Integer.parseInt(milestone.getId()));
			}
			
		} catch (IOException e) {
			throw new AssemblaConfigurationException(e);
		}
	}
	
	HttpHeaders createHeaders( final String clientId, final String clientSecret ){
		   return new HttpHeaders(){
		      {
		         String auth = clientId + ":" + clientSecret;
		         byte[] encodedAuth = Base64.encodeBase64(
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }
		   };
		}


	public AssemblaTicket convertFromTicketToAssemblaTicket(Ticket ticket){
		
		AssemblaTicket assemblaTicket = new AssemblaTicket();
		assemblaTicket.setStatus(ticket.getStatus());
		assemblaTicket.setComponent_id(componentNameToIdMap.get(ticket.getComponent()));
		assemblaTicket.setDescription(ticket.getDescription());
		assemblaTicket.setMilestone_id(milestoneNameToIdMap.get(ticket.getMilestone()));
		if(ticket.getOwner()==null)
			ticket.setOwner(null);
		else{
			if(loginToAssemblaUserWithKeysMap.containsKey(ticket.getOwner()))
				assemblaTicket.setAssigned_to_id(loginToAssemblaUserWithKeysMap.get(ticket.getOwner()).getUserId());
			else
				ticket.setOwner(null);
		}
		assemblaTicket.setPriority(priorityNameToNumberMap.get(ticket.getPriority()));
		if(ticket.getReporter()==null)
			assemblaTicket.setReporter_id(null);
		else{
			if(loginToAssemblaUserWithKeysMap.containsKey(ticket.getReporter()))
				assemblaTicket.setReporter_id(loginToAssemblaUserWithKeysMap.get(ticket.getReporter()).getUserId());
			else
				ticket.setReporter(null);
		}
		assemblaTicket.setSummary(ticket.getSummary());
		if(ticket.getTicketId()!=null)
			assemblaTicket.setId(ticket.getTicketId());
		assemblaTicket.setUpdated_at(Utils.universalDateToString(TimeZone.getTimeZone("GMT"), ticket.getUpdateTime()));
		//assemblaTicket.setCreated_on(Utils.universalDateToString(TimeZone.getTimeZone("GMT"), ticket.getUpdateTime()));
		
		return assemblaTicket;
		
	}
	public void updateTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		boolean shouldNotUpdateTicket = true;
		
		shouldNotUpdateTicket &= ticket.getComponent()==null;
		shouldNotUpdateTicket &= ticket.getDescription()==null;
		shouldNotUpdateTicket &= ticket.getMilestone()==null;
		shouldNotUpdateTicket &= ticket.getOwner()==null;
		shouldNotUpdateTicket &= ticket.getPriority()==null;
		shouldNotUpdateTicket &= ticket.getReporter()==null;
		shouldNotUpdateTicket &= ticket.getStatus()==null;
		shouldNotUpdateTicket &= ticket.getSummary()==null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+loginToAssemblaUserWithKeysMap.get(ticket.getAuthor()).getAccessToken());
		int ticketId = ticket.getTicketId();
		
		try{
			ResponseEntity<AssemblaTicket> atBeforeChangeResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets/id/{ticketId}.json",HttpMethod.GET,
		    		new HttpEntity<AssemblaTicket>(headers)
		    		, AssemblaTicket.class, projectName, ticketId);
			int ticketNumber=atBeforeChangeResponse.getBody().getNumber();
			
			headers.setContentType(MediaType.APPLICATION_JSON);		
			if(!shouldNotUpdateTicket){
				
				AssemblaTicket assemblaTicket = convertFromTicketToAssemblaTicket(ticket);
				ResponseEntity<AssemblaTicket> aticketResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets/{ticketNumber}.json",HttpMethod.PUT,
			    		new HttpEntity<AssemblaTicket>(assemblaTicket,headers)
			    		, AssemblaTicket.class, projectName, ticketNumber);
			}
			if(ticket.getComment()!=null){
				AssemblaComment assemblaComment = createComment(ticket);
				ResponseEntity<AssemblaComment> acommentResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets/{ticketNumber}/ticket_comments.json",HttpMethod.POST,
			    		new HttpEntity<AssemblaComment>(assemblaComment,headers)
			    		, AssemblaComment.class, projectName, ticketNumber);
			}
		}catch (HttpClientErrorException e){
			log.error("Nie uda³o siê pobraæ ticketu z Assembli o id "+ticketId);
		}	
		
	}
	public AssemblaComment createComment(Ticket ticket){
		AssemblaComment assemblaComment = new AssemblaComment();
		//assemblaComment.setTicket_id(ticket.getTicketId());
		assemblaComment.setComment(ticket.getComment());
		assemblaComment.setCreated_on(Utils.universalDateToString(TimeZone.getTimeZone("GMT"), ticket.getUpdateTime()));
		assemblaComment.setUser_id(loginToAssemblaUserWithKeysMap.get(ticket.getAuthor()).getUserId());
		return assemblaComment;
	}
	
	public Ticket createTicket(Ticket ticket) throws CommunicationException {
		System.out.println("Assembla ticket: "+ticket.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+loginToAssemblaUserWithKeysMap.get(ticket.getAuthor()).getAccessToken());
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		AssemblaTicket assemblaTicket = new AssemblaTicket();
		assemblaTicket.setComponent_id(componentNameToIdMap.get(ticket.getComponent()));
		assemblaTicket.setDescription(ticket.getDescription());
		assemblaTicket.setMilestone_id(milestoneNameToIdMap.get(ticket.getMilestone()));
		if(ticket.getOwner() != null){		
			assemblaTicket.setAssigned_to_id(loginToAssemblaUserWithKeysMap.get(ticket.getOwner()).getUserId());
		}else{
			assemblaTicket.setAssigned_to_id(null);
		}
		assemblaTicket.setPriority(priorityNameToNumberMap.get(ticket.getPriority()));
		if(ticket.getReporter() != null){
			assemblaTicket.setReporter_id(loginToAssemblaUserWithKeysMap.get(ticket.getReporter()).getUserId());
		}else{
			assemblaTicket.setReporter_id(null);
		}
		assemblaTicket.setSummary(ticket.getSummary());
		assemblaTicket.setCreated_on(Utils.universalDateToString(TimeZone.getTimeZone("GMT"), ticket.getUpdateTime()));
	
		ResponseEntity<AssemblaTicket> aticketResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets",HttpMethod.POST,
	    		new HttpEntity<AssemblaTicket>(assemblaTicket,headers)
	    		, AssemblaTicket.class, projectName);
		AssemblaTicket newAssemblaTicket = aticketResponse.getBody();
		AssemblaComment retComment=null;
		if(ticket.getComment()!=null){
			AssemblaComment assemblaComment = createComment(ticket);
			Integer ticketNumber=newAssemblaTicket.getNumber();
			ResponseEntity<AssemblaComment> acommentResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets/{ticketNumber}/ticket_comments.json",HttpMethod.POST,
		    		new HttpEntity<AssemblaComment>(assemblaComment,headers)
		    		, AssemblaComment.class, projectName, ticketNumber);
			retComment = acommentResponse.getBody();
		}
		
		Ticket retTicket = new Ticket();
		retTicket.setTicketId(newAssemblaTicket.getId());
		//retTicket.setAuthor(ticket.getAuthor());
		if(retComment!=null)
			retTicket.setComment(retComment.getComment());
		retTicket.setComponent(componentNameToIdMap.inverse().get(newAssemblaTicket.getComponent_id()));
		retTicket.setDescription(newAssemblaTicket.getDescription());
		retTicket.setMilestone(milestoneNameToIdMap.inverse().get(newAssemblaTicket.getMilestone_id()));
		retTicket.setOwner(idUserToLoginMap.get(newAssemblaTicket.getAssigned_to_id()));
		retTicket.setPriority(priorityNameToNumberMap.inverse().get(newAssemblaTicket.getPriority()));
		retTicket.setReporter(idUserToLoginMap.get(newAssemblaTicket.getReporter_id()));
		retTicket.setStatus(newAssemblaTicket.getStatus());
		retTicket.setSummary(newAssemblaTicket.getSummary());
		try {
			retTicket.setUpdateTime(Utils.stringToUniversalDate(isoDateString(newAssemblaTicket.getCreated_on())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new CommunicationException(e);
		}
		return retTicket;
	}
	
	private Ticket convertFromAssemblaToTicket(AssemblaTicket newAssemblaTicket) throws CommunicationException{
		Ticket retTicket = new Ticket();
		retTicket.setTicketId(newAssemblaTicket.getId());
		//retTicket.setAuthor(idUserToLoginMap.inverse().get(newAssemblaTicket.getAuthor()));
		//retTicket.setComment(retComment.getComment());
		retTicket.setComponent(componentNameToIdMap.inverse().get(newAssemblaTicket.getComponent_id()));
		retTicket.setDescription(newAssemblaTicket.getDescription());
		retTicket.setMilestone(milestoneNameToIdMap.inverse().get(newAssemblaTicket.getMilestone_id()));
		retTicket.setOwner(idUserToLoginMap.get(newAssemblaTicket.getAssigned_to_id()));
		retTicket.setPriority(priorityNameToNumberMap.inverse().get(newAssemblaTicket.getPriority()));
		retTicket.setReporter(idUserToLoginMap.get(newAssemblaTicket.getReporter_id()));
		retTicket.setStatus(newAssemblaTicket.getStatus());
		retTicket.setSummary(newAssemblaTicket.getSummary());
		try {
			retTicket.setUpdateTime(Utils.stringToUniversalDate(isoDateString(newAssemblaTicket.getCreated_on())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new CommunicationException(e);
		}
		return retTicket;
	}

	private RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.getObjectMapper().configure(Feature.WRAP_ROOT_VALUE, true);
		//converter.getObjectMapper().setSerializationInclusion(Inclusion.NON_NULL);
		HttpMessageConverter<?> jackson = converter ;
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jackson);
	    restTemplate.setMessageConverters(converters);
		return restTemplate;
	}
	
	public Ticket getTicketById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Ticket> getTicketChangesById(int id){
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
		return ticketList;
	}
	
	private Ticket revertTicketChangesByComment(Ticket currentVersion, AssemblaComment comment){
		Ticket ticket = currentVersion;
		String changes = comment.getTicket_changes();
		if(changes!=null){
			String[] changesTab = changes.split("- -");
			HashMap<String, String[]> changesMap = null;
			if(changesTab.length>1){
				changesMap = new HashMap<String, String[]>();
				for(int i=1;i<changesTab.length;i++){
					String[] namesTab = changesTab[i].split("\n");
					String[] pair = {namesTab[1].replace("  - ", ""),namesTab[2].replace("  - ", "")};
					changesMap.put(namesTab[0].replace(" ", ""), pair);
				}
			}
			if(changesMap!=null){
			
				if(changesMap.get("component_id")!=null) ticket.setComponent(changesMap.get("component_id")[0]);
				if(changesMap.get("description")!=null) ticket.setDescription(changesMap.get("description")[0]);
				if(changesMap.get("milestone_id")!=null) ticket.setMilestone(changesMap.get("milestone_id")[0]);
				if(changesMap.get("assigned_to_id")!=null) ticket.setOwner(changesMap.get("assigned_to_id")[0]);
				if(changesMap.get("prioryty_id")!=null) ticket.setPriority(changesMap.get("priority_id")[0]);
				if(changesMap.get("status")!=null) ticket.setStatus(changesMap.get("status")[0]);
				if(changesMap.get("summary")!=null) ticket.setSummary(changesMap.get("summary")[0]);
				if(changesMap.get("reporter")!=null)
					{ 
						ticket.setReporter(changesMap.get("reporter")[0]);
						ticket.setAuthor(changesMap.get("reporter")[0]);
					}
			}
			
		}
		ticket.setTicketId(comment.getTicket_id());
		//ticket.setAuthor(comment.getUser_id());
		ticket.setComment(null);
		return ticket;
	}
	
	private Ticket getChangeTicket(AssemblaComment comment) throws CommunicationException{
		Ticket ticket = new Ticket();
		String changes = comment.getTicket_changes();
		if(changes!=null){
			String[] changesTab = changes.split("- -");
			HashMap<String, String> changesMap = null;
			if(changesTab.length>1){
				changesMap = new HashMap<String, String>();
				for(int i=1;i<changesTab.length;i++){
					String[] namesTab = changesTab[i].split("\n");
					String[] pair = {namesTab[1].replace("  - ", ""),namesTab[2].replace("  - ", "")};
					changesMap.put(namesTab[0].replace(" ", ""), namesTab[2].replace("  - ", ""));
				}
			}
			if(changesMap!=null){
			
				ticket.setComponent(changesMap.get("component_id"));
				ticket.setDescription(changesMap.get("description"));
				ticket.setMilestone(changesMap.get("milestone_id"));
				ticket.setOwner(changesMap.get("assigned_to_id"));
				ticket.setPriority(changesMap.get("priority_id"));
				ticket.setStatus(changesMap.get("status"));
				ticket.setSummary(changesMap.get("summary"));
			}
		}
		try {
			//TODO: zmieniam comment.getUpdated_at()
			ticket.setUpdateTime(Utils.stringToUniversalDate(isoDateString(comment.getCreated_on() ) ));
			
			if(comment.getComment()!=null){
				String addToComment="";
				ticket.setComment(comment.getComment());
				Date updated_at, created_on;
				updated_at = Utils.stringToUniversalDate(isoDateString(comment.getUpdated_at()));
				//ticket.setUpdateTime(updated_at);
				created_on = Utils.stringToUniversalDate(isoDateString(comment.getCreated_on()));
				if(updated_at.after(created_on)){
					ticket.setUpdateTime(updated_at);
					addToComment=editCommentMarker;
				}
		
				
				ticket.setComment(addToComment+comment.getComment());
			}else
				ticket.setComment(null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new CommunicationException(e);
		}
		ticket.setTicketId(comment.getTicket_id());
		ticket.setAuthor(idUserToLoginMap.get(comment.getUser_id()));
		return ticket;
	}

	
	
	public Ticket getTicketByRemoteId(int remoteId) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public String isoDateString(String isoDateString){
		if(!isoDateString.endsWith("Z")){
			int index = isoDateString.lastIndexOf(':');
			isoDateString = isoDateString.substring(0,index) + isoDateString.substring(index+1);
		}
		return isoDateString;
	}
	public HashMap<Integer,Integer> getTicketIdsFromEvents(Date dateFrom){
		HashMap<Integer,Integer> ticketIdToNumbersMap = null;
		for(AssemblaUserWithKeys user: loginToAssemblaUserWithKeysMap.values()){
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer "+user.getAccessToken());
			
		    String dateString = Utils.universalDateToString(TimeZone.getTimeZone("GMT"), dateFrom);
			ResponseEntity<AssemblaEvent[]> responseList;
			try{
				responseList = restTemplate.exchange(url + "v1/activity.json?from={dateString}&space_id={projectName}",HttpMethod.GET,
			    		new HttpEntity<AssemblaEvent>(headers)
			    		, AssemblaEvent[].class, dateString, projectName);
			}catch (HttpServerErrorException e){
				log.error("Nie uda³o siê pobraæ ostatnich zmian w projekcie "+projectName+" przez u¿ytkownika "+user.getUserLogin());
				/*
				final Email email = new Email();

				email.setFromAddress("synchronizer", "malgosiek@onet.eu");
				email.setSubject("Synchro conflict");
				email.addRecipient("admin", "gosiiek@gmail.com", RecipientType.TO);
				//email.addRecipient("C. Bo", "chocobo@candyshop.org", RecipientType.BCC);
				email.setText("Nie uda³o siê pobraæ ostatnich zmian w projekcie "+projectName+" przez u¿ytkownika "+user.getUserLogin());
				//email.setTextHTML("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>");

				//email.addAttachment("invitation", pdfByteArray, "application/pdf");

				new Mailer("smtp.poczta.onet.pl", 587, "malgosiek@onet.eu", "11111").sendMail(email);
				*/
				
				continue;
			}
			if(responseList.getBody()!=null){
				ticketIdToNumbersMap = new HashMap<Integer,Integer>();
				List<AssemblaEvent> eventList = Arrays.asList(responseList.getBody());
				for(AssemblaEvent event: eventList){
					HashMap<String,String> ticketEvent = event.getTicket();
					if(ticketEvent!=null)
						ticketIdToNumbersMap.put(Integer.parseInt(ticketEvent.get("id")),Integer.parseInt(ticketEvent.get("number")));
				}
			}
		}
		return ticketIdToNumbersMap;
	}
	public ArrayList<AssemblaComment> getCommentsByTicketNumber(Integer ticketNumber, Date dateFrom) throws CommunicationException{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+loginToAssemblaUserWithKeysMap.values().iterator().next().getAccessToken());
	    
	    ResponseEntity<AssemblaComment[]> responseList = restTemplate.exchange(url + "v1/spaces/{proectName}/tickets/{ticketNumber}/ticket_comments.json",HttpMethod.GET,
	    		new HttpEntity<AssemblaComment>(headers)
	    		, AssemblaComment[].class, projectName, ticketNumber);
		List<AssemblaComment> commentList = Arrays.asList(responseList.getBody());
		ArrayList<AssemblaComment> returnCommentList = new ArrayList<AssemblaComment>();
	    for(AssemblaComment comment: commentList){
	    	try {
	    		
	    		Date updated_at, created_on;
				updated_at = Utils.stringToUniversalDate(isoDateString(comment.getUpdated_at()));
				
				created_on = Utils.stringToUniversalDate(isoDateString(comment.getCreated_on()));
	    		if(comment.getComment()!=null&&updated_at.compareTo(created_on)!=0&&updated_at.after(dateFrom)){
	    			returnCommentList.add(comment);
	    		}
	    		//TODO:zmieniam comment.getUpdated_at()
	    		else if((Utils.stringToUniversalDate(isoDateString(comment.getCreated_on()))).after(dateFrom)){
					returnCommentList.add(comment);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new CommunicationException(e);
			}	
	    }
	    if(returnCommentList!=null)
	    	System.out.println(returnCommentList.toString());
	    return returnCommentList;
	    
	}
	public ArrayList<Ticket> getTicketsByComments(ArrayList<AssemblaComment> commentList, int ticketNumber, Date dateFrom) throws CommunicationException{
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+loginToAssemblaUserWithKeysMap.values().iterator().next().getAccessToken());
		ResponseEntity<AssemblaTicket> aticketResponse = restTemplate.exchange(url + "v1/spaces/{projectName}/tickets/{ticketNumber}.json",HttpMethod.GET,
	    		new HttpEntity<AssemblaTicket>(headers)
	    		, AssemblaTicket.class, projectName, ticketNumber);
		AssemblaTicket currentAssembla = aticketResponse.getBody();
		
		Ticket startTicketVersion = convertFromAssemblaToTicket(currentAssembla);
		
		Collections.reverse(commentList);
		
		for(AssemblaComment comment:commentList){
			tickets.add(getChangeTicket(comment));
		}
		
		try {
			if(Utils.stringToUniversalDate(isoDateString(currentAssembla.getCreated_on())).after(dateFrom)){
				for(AssemblaComment comment:commentList)
					revertTicketChangesByComment(startTicketVersion, comment);
				if(startTicketVersion.getAuthor()==null)
				startTicketVersion.setAuthor(startTicketVersion.getReporter());
			
				tickets.set(0, startTicketVersion);
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new CommunicationException(e);
		}
		
		
		return tickets;
	}
	
	public ArrayList<TicketChanges> getTicketChanges(Date dateFrom) throws CommunicationException {
		ArrayList<TicketChanges> ticketChangesList = null;
				
		HashMap<Integer, Integer> ticketIdToNumbersMap =  getTicketIdsFromEvents(dateFrom);
		if(ticketIdToNumbersMap!=null){
			ticketChangesList = new ArrayList<TicketChanges>();
			for(int id: ticketIdToNumbersMap.keySet()){
				ArrayList<AssemblaComment> assemblaComments = getCommentsByTicketNumber(ticketIdToNumbersMap.get(id), dateFrom);
				ArrayList<Ticket> tickets = getTicketsByComments(assemblaComments, ticketIdToNumbersMap.get(id), dateFrom);
				if(tickets!= null && tickets.size() != 0){
					TicketChanges ticketChanges = new TicketChanges();
					ticketChanges.setTickets(tickets);
					ticketChanges.setId(id);
					ticketChangesList.add(ticketChanges);
				}
			}
		}
		String retStr= "nic";
		if(ticketChangesList!=null)
			retStr = ticketChangesList.toString();
		log.debug("Metoda getTicketChanges w AssemblaDriver zwrocila: "+retStr);
		return ticketChangesList;
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return projectName;
	}
}
