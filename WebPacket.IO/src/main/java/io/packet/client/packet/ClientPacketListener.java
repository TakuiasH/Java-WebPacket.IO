package io.packet.client.packet;

import com.google.gson.JsonElement;

public interface ClientPacketListener {

	public void run(JsonElement data);
	 
}