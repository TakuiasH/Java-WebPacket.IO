package example;

import com.google.gson.JsonObject;

import io.packet.client.PacketClient;

public class WebPacketClientExample {

	private static PacketClient client = new PacketClient("ws://localhost:3000/", "MyCustomServer");
	
	public static void main(String[] args) {
		 
		//send a packet on connect in the server
		client.onConnected((data) -> {
			System.out.println("Connected!");
			
			client.send("from.client", "Ping!");
		});
		
		//On disconnect from the server
		client.onDisconnect((data) -> {
			System.out.println("Disconnected!");
			
			JsonObject obj = data.getAsJsonObject();
			
			//CloseFrame is the code list
			System.out.println("Disconect code: " + obj.get("code").getAsInt());
			
			if(obj.has("reason"))
				System.out.println(obj.get("reason").getAsString());
		});
		
		//On receive the packet 'from.server' from the server
		client.on("from.server", (data) -> {
			System.out.println(data.getAsJsonObject().get("jsonPong").getAsString());
		});
				
		client.connect();
		
		System.out.println("started");
	}

}
