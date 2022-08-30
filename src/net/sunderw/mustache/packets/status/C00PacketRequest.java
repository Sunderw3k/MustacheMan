package net.sunderw.mustache.packets.status;

import net.sunderw.mustache.packets.Packet;

public class C00PacketRequest extends Packet {

    public C00PacketRequest() {
        super(0x00);

        write();
    }

    @Override
    protected void write() {
        try {
            stream.writeByte(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
