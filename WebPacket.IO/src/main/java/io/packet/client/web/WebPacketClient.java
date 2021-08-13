package io.packet.client.web;

import java.net.URI;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.packet.client.packet.ClientEventEntry;
import io.packet.client.packet.ClientPacketListener;

public class WebPacketClient extends WebSocketClient {
	
	private final Map<String, ClientEventEntry> listeners = new ConcurrentHashMap<>();
	 
	public WebPacketClient(URI serverUri, String identification) {
		super(serverUri);
		
		this.addHeader("Identification", identification);
	}
	
	public void onMessage(String response) {
		try {
			JsonObject result = JsonParser.parseString(response).getAsJsonObject();
			
			callListeners(result.get("event").getAsString(), result.get("data"));
		} catch (JsonSyntaxException e) {}
	}
	
	public void callListeners(String event, JsonElement data) {
        ClientEventEntry entry = listeners.get(event);

        if (entry == null) 
            return;
     
        Queue<ClientPacketListener> listeners = entry.getListeners();

        for (ClientPacketListener dataListener : listeners) 
            dataListener.run(data);
	}
	public void addEventListener(String event, ClientPacketListener listener) {
		ClientEventEntry entry = listeners.get(event);

        if (entry == null) {
            entry = new ClientEventEntry();
            
            ClientEventEntry oldEntry = listeners.putIfAbsent(event, entry);

            if (oldEntry != null) 
                entry = oldEntry;            
        }
        
        entry.addListener(listener);
	}
    public void removeAllListeners(String event) {
    	listeners.remove(event);
    }
	
	public void onError(Exception ex) {
		System.out.println(ex.getMessage());
		ex.printStackTrace();
	}
	public void onOpen(ServerHandshake handshakedata) { callListeners("connect", null); }
	public void onClose(int code, String reason, boolean remote) {		
		JsonObject response = new JsonObject();
		
		response.addProperty("code", code);
		
		if(reason != null)
			response.addProperty("reason", reason);
		
		callListeners("disconnect", response);
	}





}
