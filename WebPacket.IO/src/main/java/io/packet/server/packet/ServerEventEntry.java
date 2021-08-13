package io.packet.server.packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerEventEntry {
 
    private final Queue<ServerPacketListener> listeners = new ConcurrentLinkedQueue<ServerPacketListener>();;
 
    public ServerEventEntry() {
        super();
    }

    public void addListener(ServerPacketListener listener) {
        listeners.add(listener);
    }

    public Queue<ServerPacketListener> getListeners() {
        return listeners;
    }
    
}
