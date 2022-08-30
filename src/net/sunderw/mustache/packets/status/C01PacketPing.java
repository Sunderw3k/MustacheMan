package net.sunderw.mustache.packets.status;

import net.sunderw.mustache.packets.Packet;

public class C01PacketPing extends Packet {
    private final long time;

    public C01PacketPing(long time) {
        super(0x01);

        this.time = time;

        write();
    }

    @Override
    protected void write() {
        try {
            stream.writeByte(id);

            stream.writeLong(time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
