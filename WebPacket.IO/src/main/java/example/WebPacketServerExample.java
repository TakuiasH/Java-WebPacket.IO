package example;

import com.google.gson.JsonObject;

import io.packet.server.PacketServer;

public class WebPacketServerExample {

	private static PacketServer server = new PacketServer(3000, "MyCustomServer");
	
	public static void main(String[] args) {
		 
		//On a client connect in the server; 'data' is always null in onConnected;
		server.onConnected((client, data) -> {
			System.out.println(client.getSocket().getRemoteSocketAddress() + " Connected!");
		});
		
		//On a client disconnect from the server
		server.onDisconnect((client, data) -> {
			System.out.println(client.getSocket().getRemoteSocketAddress() + " Disconnected!");
			
			JsonObject obj = data.getAsJsonObject();
			
			//CloseFrame is the code list
			System.out.println("Disconect code: " + obj.get("code").getAsInt());
			
			if(obj.has("reason"))
				System.out.println(obj.get("reason").getAsString());
		});
		
		//On receive the packet 'from.client' from the client
		server.on("from.client", (client, data) -> {
			System.out.println(data.getAsString());
			
			JsonObject response = new JsonObject();
			
			response.addProperty("jsonPong", "Pong!");
			
			client.send("from.server", response);
		});
		
		server.connect();
		
		System.out.println("started");
	}


}
