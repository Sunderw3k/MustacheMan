package net.sunderw.mustache.packets.login;

import net.sunderw.mustache.utils.PacketUtils;
import net.sunderw.mustache.packets.Packet;

public class C00PacketLogin extends Packet {

    private final String username;

    public C00PacketLogin(String username) {
        super(0x00);

        this.username = username;

        write();
    }

    @Override
    protected void write() {
        try {
            stream.writeByte(id);

            PacketUtils.writeString(stream, username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
