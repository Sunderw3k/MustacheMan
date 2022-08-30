package net.sunderw.mustache.packets.play;

import net.sunderw.mustache.utils.PacketUtils;
import net.sunderw.mustache.packets.Packet;

public class C01PacketChat extends Packet {

    private final String message;

    public C01PacketChat(String message) {
        super(0x01);

        this.message = message;

        write();
    }

    @Override
    protected void write() {
        try {
            stream.writeByte(id);

            PacketUtils.writeString(stream, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
