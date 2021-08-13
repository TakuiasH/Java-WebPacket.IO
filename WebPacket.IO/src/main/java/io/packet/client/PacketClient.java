package io.packet.client;

import java.net.URI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.packet.client.packet.ClientPacketListener;
import io.packet.client.web.WebPacketClient;

public class PacketClient { 
	
	private final WebPacketClient client;
	 
	public PacketClient(String serverURL, String identification) {
		this(URI.create(serverURL), identification);
	}
	
	public PacketClient(URI serverURI, String identification) {
		client = new WebPacketClient(serverURI, identification);
	}
	
	public void send(String event, JsonElement data) {
		JsonObject object = new JsonObject();
		
		object.addProperty("event", event);
		object.add("data", data);
		
		client.send(object.toString());
	}
	
	public void send(String event, String data) {
		JsonObject object = new JsonObject();
		
		object.addProperty("event", event);
		object.addProperty("data", data);
		
		client.send(object.toString());
	}
	
	public void on(String event, ClientPacketListener callback) {
		client.addEventListener(event, callback);
	}
	
	public void onConnected(ClientPacketListener callback) {
		client.addEventListener("connect", callback);
	}
	
	public void onDisconnect(ClientPacketListener callback) {
		client.addEventListener("disconnect", callback);
	}
	
	public void connect() {
		client.connect();
	}
	

}
