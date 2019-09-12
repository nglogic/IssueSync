package com.nglogic.synchronizer.drivers.trac;

import java.util.Date;

public class ChangeLog {
	
	Date time;
	String author;
	String field;
	String oldvalue;
	String newvalue;
	
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOldvalue() {
		return oldvalue;
	}
	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}
	public String getNewvalue() {
		return newvalue;
	}
	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}
	
	@Override
	public String toString() {
		return "ChangeLog [time=" + time + ", author=" + author + ", field="
				+ field + ", oldvalue=" + oldvalue + ", newvalue=" + newvalue
				+ "]";
	}
}
