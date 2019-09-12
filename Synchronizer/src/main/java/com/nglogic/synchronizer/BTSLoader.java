package com.nglogic.synchronizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.nglogic.synchronizer.config.SynchronizerConfiguration;
import com.nglogic.synchronizer.drivers.BTSDriverInterface;
import com.nglogic.synchronizer.drivers.assembla.AssemblaDriver;
import com.nglogic.synchronizer.drivers.trac.TracDriver;
import com.nglogic.synchronizer.exceptions.AssemblaConfigurationException;
import com.nglogic.synchronizer.exceptions.CommunicationException;
import com.nglogic.synchronizer.exceptions.ConfigurationException;
import com.nglogic.synchronizer.exceptions.TracConfigurationException;

public class BTSLoader {
	static private Logger log = Logger.getLogger(BTSLoader.class);

	
	
	public static void main(String[] args) throws ConfigurationException , CommunicationException{
		try{
			if(args.length == 0 ){
				ConfigurationException ce = new ConfigurationException("Configuration file path required");
				log.error(ce);
				System.exit(1);
			}
			String filePath = args[0];
			SynchronizerConfiguration config = null;
			File configFile = new File(filePath);
			if (!configFile.exists() || !configFile.canRead()) {
				ConfigurationException ce = new ConfigurationException("File " + filePath + " does not exist or can not be read");
				log.error(ce);
				System.exit(1);
			}
	
			try {
	
				JAXBContext context = JAXBContext
						.newInstance(SynchronizerConfiguration.class);
				config = (SynchronizerConfiguration) context.createUnmarshaller().unmarshal(configFile);
				
				
			} catch (JAXBException e) {
				ConfigurationException ce = new ConfigurationException("error parsing configuration file", e); 
				log.error(ce);
				throw ce;
			}
			
			Date dateFrom;
			
			BTSSynchronizer synchronizer = new BTSSynchronizer(config);
			File dateFile = new File(config.getLastModificationDateFile());
			try{
			
				FileReader reader = new FileReader(dateFile);
				char cBuff[] = new char[100];
				int numOfBytes = reader.read(cBuff);
				String dateStr = new String(cBuff, 0, numOfBytes);
				dateFrom= Utils.stringWithMilisecondsToUniversalDate(dateStr);
				
				reader.close();
			}catch(FileNotFoundException fne){
				try{
					log.info("creating file " + config.getLastModificationDateFile());
					dateFile.createNewFile();
					
				}catch(IOException e){
					throw new ConfigurationException(e);
				}
				dateFrom = new Date(0);
			}
			catch(Exception e){
				log.error(e);
				throw new ConfigurationException(e);
			}
				
			Date lastModification = synchronizer.synchronizeBtsProjects(dateFrom);
			if(lastModification.before(dateFrom))
				lastModification = dateFrom;
			try{
				FileWriter writer = new FileWriter(dateFile);
				String newDateStr = Utils.universalDateToStringWithMiliseconds(TimeZone.getDefault(), lastModification);
				writer.write(newDateStr);
				writer.close();
			}catch(Exception e ){
				throw new ConfigurationException(e);
			}
			
			
			
		}catch(ConfigurationException e){
			log.error(e);
			throw e;
		}catch(CommunicationException e){
			log.error(e);
			throw e;
		}
	}

}
