package com.nglogic.synchronizer.config;

import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class MapEntryString {
	@XmlAttribute
	String key;
	@XmlAttribute
	String value;
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	
}
