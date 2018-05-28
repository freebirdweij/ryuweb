package com.freebirdweij.cloudroom.sdn.rest;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Component
@Transactional(readOnly = true)
public class RYURestClient extends RestClient {
	
	private String REST_SERVICE_URL = "http://192.168.122.128:8080/stats/flow";
	
	Client client =  ClientBuilder.newClient().register(JacksonFeature.class);

	public boolean addFlowTableRow(JSONObject fto,String urlbase){
		REST_SERVICE_URL = urlbase+"/stats/flowentry";
		WebTarget target= client.target(REST_SERVICE_URL).path("/add");
		
		target.request().post(Entity.entity(fto, MediaType.APPLICATION_JSON));
		return true;
	}

	public boolean deleteFlowTableRow(JSONObject fto,String urlbase){
		REST_SERVICE_URL = urlbase+"/stats/flowentry";
		WebTarget target= client.target(REST_SERVICE_URL).path("/delete_strict");
		target.request().post(Entity.entity(fto, MediaType.APPLICATION_JSON));
		return true;
	}

	public boolean changeFlowTableRow(JSONObject fto){
		client
		.target(REST_SERVICE_URL)
		.request().post(Entity.entity(fto, MediaType.APPLICATION_JSON),
				JSONObject.class);
		return true;
	}

	public JSONArray getFlowTableRow(JSONObject fto,String urlbase,String dpid){
		REST_SERVICE_URL = urlbase+"/stats/flow";
		WebTarget target= client.target(REST_SERVICE_URL).path("/"+dpid);
		JSONObject response =  target.request().post(Entity.entity(fto, MediaType.APPLICATION_JSON),
				JSONObject.class); 
		JSONArray arr = response.getJSONArray(dpid);
		return arr;
	}

	public JSONArray listFlowTableRows(String urlbase,String dpid){
		REST_SERVICE_URL = urlbase+"/stats/flow";
		WebTarget target= client.target(REST_SERVICE_URL).path("/"+dpid);
		 
		JSONObject response =  target.request().get(JSONObject.class); 
		JSONArray arr = response.getJSONArray(dpid);
		return arr;
	}

	public JSONArray listSwitches(String urlbase){
		REST_SERVICE_URL = urlbase+"/stats/switches";
		WebTarget target= client.target(REST_SERVICE_URL);
		 
		JSONArray response =  target.request().get(JSONArray.class); 
		//JSONArray arr = response.getJSONArray("1");
		return response;
	}

}
