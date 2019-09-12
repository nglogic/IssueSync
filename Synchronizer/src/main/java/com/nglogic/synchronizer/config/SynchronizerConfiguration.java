package com.nglogic.synchronizer.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "synchronizerConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynchronizerConfiguration {
	
	@XmlElement
	String lastModificationDateFile;
	
	@XmlElement
	BTSDriverConfig innerDriver;
	
	@XmlElement
	BTSDriverConfig outerDriver;

	@XmlElement
	MapperConfig mapper;
	
	@XmlElement
	InnerFilterConfig filter;

	public BTSDriverConfig getInnerDriver() {
		return innerDriver;
	}

	public BTSDriverConfig getOuterDriver() {
		return outerDriver;
	}

	public MapperConfig getMapper() {
		return mapper;
	}

	public InnerFilterConfig getFilter() {
		return filter;
	}

	public String getLastModificationDateFile() {
		return lastModificationDateFile;
	}

	
	
}
