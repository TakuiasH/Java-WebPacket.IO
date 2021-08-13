package io.packet.server;

import java.net.InetSocketAddress;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.packet.server.packet.ServerPacketListener;
import io.packet.server.web.WebPacketServer;

public class PacketServer {
	 
	private final WebPacketServer server;
	
	public PacketServer(int port, String identification) {
		this(new InetSocketAddress(port), identification);
	} 
	
	public PacketServer(InetSocketAddress address, String identification) {
		server = new WebPacketServer(address, identification);
	}
	
	public void broadcast(String event, JsonElement data) {
		JsonObject object = new JsonObject();

		object.addProperty("event", event);
		object.add("data", data);
		
		server.broadcast(object.toString());
	}
	
	public void broadcast(String event, String data) {
		JsonObject object = new JsonObject();

		object.addProperty("event", event);
		object.addProperty("data", data);
		
		server.broadcast(object.toString());
	}
	
	public void on(String event, ServerPacketListener callback) {
		server.addEventListener(event, callback);
	}
	
	public void onConnected(ServerPacketListener callback) {
		server.addEventListener("connect", callback);
	}
	public void onDisconnect(ServerPacketListener callback) {
		server.addEventListener("disconnect", callback);
	}
	
	public void connect() {
		server.start();
	}

}
