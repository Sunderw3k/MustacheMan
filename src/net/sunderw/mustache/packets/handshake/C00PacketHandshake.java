package net.sunderw.mustache.packets.handshake;

import net.sunderw.mustache.utils.PacketUtils;
import net.sunderw.mustache.packets.Packet;

public class C00PacketHandshake extends Packet {

    private final int version, port, state;
    private final String hostname;

    public C00PacketHandshake(int version, String hostname, int port, int state) {

        super(0x00);

        this.version = version;
        this.hostname = hostname;
        this.port = port;
        this.state = state;

        write();
    }

    @Override
    protected void write() {
        try {
            stream.writeByte(id);

            PacketUtils.writeVarInt(stream, version);
            PacketUtils.writeString(stream, hostname);
            stream.writeShort(port);
            PacketUtils.writeVarInt(stream, state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
