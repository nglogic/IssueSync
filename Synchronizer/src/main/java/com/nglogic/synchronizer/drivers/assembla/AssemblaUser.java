package com.nglogic.synchronizer.drivers.assembla;

import java.util.HashMap;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName(value = "user")
@JsonSerialize
public class AssemblaUser {

	String id;
	String login;
	String name; 
	String email;
	String organization;
	String phone;
	HashMap<String, String> im;
	HashMap<String,String> im2;
	byte[] picture;
	
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public HashMap<String, String> getIm() {
		return im;
	}
	public void setIm(HashMap<String, String> im) {
		this.im = im;
	}
	public HashMap<String, String> getIm2() {
		return im2;
	}
	public void setIm2(HashMap<String, String> im2) {
		this.im2 = im2;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
