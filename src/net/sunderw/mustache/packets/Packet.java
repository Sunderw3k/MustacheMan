package net.sunderw.mustache.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public abstract class Packet {

    protected final int id;
    protected final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    protected final DataOutputStream stream = new DataOutputStream(buffer);

    public Packet(int id) {
        this.id = id;
    }

    public DataOutputStream getStream() {
        return stream;
    }
    public ByteArrayOutputStream getBuffer() {
        return buffer;
    }

    @SuppressWarnings("unused")
    protected abstract void write();
}
