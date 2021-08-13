package io.packet.client.packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientEventEntry {
 
    private final Queue<ClientPacketListener> listeners = new ConcurrentLinkedQueue<ClientPacketListener>();;
 
    public ClientEventEntry() {
        super(); 
    }

    public void addListener(ClientPacketListener listener) {
        listeners.add(listener);
    }

    public Queue<ClientPacketListener> getListeners() {
        return listeners;
    }
    
}
