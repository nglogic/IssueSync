package com.nglogic.synchronizer.drivers.assembla;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class AssemblaAccessToken {
	String token_type;
	String access_token;
	Integer expires_in;
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	
}
