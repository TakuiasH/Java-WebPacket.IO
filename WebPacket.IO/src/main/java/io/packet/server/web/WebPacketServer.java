package io.packet.server.web;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.packet.server.packet.ServerEventEntry;
import io.packet.server.packet.ServerPacketListener;

public class WebPacketServer extends WebSocketServer {
 
	private final Map<String, ServerEventEntry> listeners = new ConcurrentHashMap<>();
	private final List<WebClient> clients = new ArrayList<>();

	private final String identification;

	public WebPacketServer(InetSocketAddress address, String identification) {
		super(address);

		this.identification = identification;
	}

	public void onMessage(WebSocket conn, String response) {
		try {
			JsonObject result = JsonParser.parseString(response).getAsJsonObject();

			callListeners(getClient(conn), result.get("event").getAsString(), result.get("data"));
		} catch (JsonSyntaxException e) {
		}
	}

	public void callListeners(WebClient client, String event, JsonElement data) {
		ServerEventEntry entry = listeners.get(event);

		if (entry == null)
			return;

		Queue<ServerPacketListener> listeners = entry.getListeners();

		for (ServerPacketListener dataListener : listeners)
			dataListener.run(client, data);
	}

	public void addEventListener(String event, ServerPacketListener listener) {
		ServerEventEntry entry = listeners.get(event);

		if (entry == null) {
			entry = new ServerEventEntry();

			ServerEventEntry oldEntry = listeners.putIfAbsent(event, entry);

			if (oldEntry != null)
				entry = oldEntry;
		}

		entry.addListener(listener);
	}

	public void removeAllListeners(String event) {
		listeners.remove(event);
	}

	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
		ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);

		if (!request.getResourceDescriptor().equals("/")) {
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not Accepted!");
		}

		if (!request.hasFieldValue("Identification")) {
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not Accepted!");
		}
		
		if (!request.getFieldValue("Identification").equals(identification)) {
			throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not Accepted!");
		}

		return builder;
	}

	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	public void onStart() {
	}

	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		WebClient client = new WebClient(conn);

		clients.add(client);
		callListeners(client, "connect", null);
	}

	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		WebClient client = getClient(conn);

		clients.remove(client);

		JsonObject response = new JsonObject();

		response.addProperty("code", code);

		if (reason != null)
			response.addProperty("reason", reason);

		callListeners(client, "disconnect", response);
	}

	public Collection<WebClient> getClients() {
		return Collections.unmodifiableCollection(clients);
	}

	public WebClient getClient(WebSocket connection) {
		return clients.stream().filter(client -> client.getSocket().equals(connection)).findFirst().orElse(null);
	}

}
