package io.packet.server.packet;

import com.google.gson.JsonElement;

import io.packet.server.web.WebClient;

public interface ServerPacketListener {
  
	public void run(WebClient client, JsonElement data);
	
}
