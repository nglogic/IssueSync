package com.nglogic.synchronizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class Utils {
	
	static private Logger log = Logger.getLogger(BTSSynchronizer.class);
	
	public static HashMap<Integer, Integer> createConflictedInnerIdToRemoteId(ArrayList<TicketChanges> innerTicketChanges, ArrayList<TicketChanges> outerTicketChanges){
		
		HashMap<Integer, Integer> conflictedInnerIdToRemoteIdMap = new HashMap<Integer, Integer>();
		HashSet<Integer> outerIdSet = new HashSet<Integer>();
		for(TicketChanges ticketChanges:outerTicketChanges){
			outerIdSet.add(ticketChanges.getId());
		}
		for(TicketChanges ticketChanges:innerTicketChanges){
			if(outerIdSet.contains(ticketChanges.getRemoteId())){
				log.debug("createConflictedInnerIdToREmoteId: InnerId: "+ticketChanges.getId()+" RemoteId: "+ticketChanges.getRemoteId());
				conflictedInnerIdToRemoteIdMap.put(ticketChanges.getId(), ticketChanges.getRemoteId());
			}
		}
		return conflictedInnerIdToRemoteIdMap;
	}
	
	public static String universalDateToString(TimeZone timeZone, Date date){
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		f.setTimeZone(timeZone);
		return f.format(date).replace("+0000", "Z");
	}
	public static Date stringToUniversalDate(String dateString) throws ParseException{
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date parsedDate = f.parse(dateString.replace( "Z", "+0000"));
		return parsedDate;
	}
	
	public static String universalDateToStringWithMiliseconds(TimeZone timeZone, Date date){
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
		f.setTimeZone(timeZone);
		return f.format(date).replace("+0000", "Z");
	}
	public static Date stringWithMilisecondsToUniversalDate(String dateString) throws ParseException{
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
		Date parsedDate = f.parse(dateString.replace( "Z", "+0000"));
		return parsedDate;
	}
	
	public static Date stringPlusOneToUniversalDate(String dateString) throws ParseException{
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+01:00");
		f.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date parsedDate = f.parse(dateString.replace( "01:00", "+0100"));
		return parsedDate;
	}
}
