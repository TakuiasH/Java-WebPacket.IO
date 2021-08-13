package io.packet.server.web;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WebClient {
	 
	private final WebSocket socket;
	
	public WebClient(WebSocket socket) {
		this.socket = socket; 
	}
	
	public WebSocket getSocket() {
		return socket;
	}
	
	public void send(String event, JsonElement data) {
		JsonObject object = new JsonObject();
		
		object.addProperty("event", event);
		object.add("data", data);
		
		socket.send(object.toString());
	}
	
	public void send(String event, String data) {
		JsonObject object = new JsonObject();
		
		object.addProperty("event", event);
		object.addProperty("data", data);
		
		socket.send(object.toString());
	}
	
	public InetSocketAddress getAddress() {
		return socket.getRemoteSocketAddress();
	}
	
	public void close() {
		socket.close();
	}
	
	public void close(String reason) {
		socket.close(CloseFrame.NORMAL, reason);
	}
	
	public void close(CloseFrame code, String reason) {
		socket.close(code.getCloseCode(), reason);
	}
}
