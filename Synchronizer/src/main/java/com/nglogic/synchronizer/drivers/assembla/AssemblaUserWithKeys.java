package com.nglogic.synchronizer.drivers.assembla;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class AssemblaUserWithKeys {
	String userLogin;
	String accessToken;
	String refreshToken;
	String userId;
	public AssemblaUserWithKeys(String userLogin,String accessToken,String refreshToken){
		this.userLogin = userLogin;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+accessToken);
		RestTemplate restTemplate = new RestTemplate();
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.getObjectMapper().configure(Feature.WRAP_ROOT_VALUE, true);
		HttpMessageConverter<?> jackson = converter ;
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jackson);
	    restTemplate.setMessageConverters(converters);
	    
	    ResponseEntity<AssemblaUser> aUserResponse = restTemplate.exchange("https://api.assembla.com/v1/user.json",HttpMethod.GET,
	    		new HttpEntity<AssemblaUser>(headers)
	    		, AssemblaUser.class);
		AssemblaUser assemblaUser = aUserResponse.getBody();
		userId = assemblaUser.getId();
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
