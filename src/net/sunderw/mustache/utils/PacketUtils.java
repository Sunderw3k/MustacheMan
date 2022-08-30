package net.sunderw.mustache.utils;

import net.sunderw.mustache.Main;
import net.sunderw.mustache.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketUtils {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static void writeString(DataOutputStream out, String value) throws IOException {
        writeVarInt(out, value.length());
        out.writeBytes(value);
    }

    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                out.writeByte(value);
                return;
            }

            out.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    public static String readString(DataInputStream in) throws IOException {
        int length = PacketUtils.readVarInt(in);
        return new String(in.readNBytes(length), StandardCharsets.UTF_8);
    }

    public static int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static <T extends Packet> void sendPacketUncompressed(DataOutputStream output, T packet) throws IOException {
        writeVarInt(output, packet.getBuffer().toByteArray().length);
        output.write(packet.getBuffer().toByteArray());
        output.flush();
    }

    public static <T extends Packet> void sendPacketCompressed(DataOutputStream output, T packet) throws IOException {
        if (packet.getBuffer().toByteArray().length <= Main.compressionSize) {
            writeVarInt(output, packet.getBuffer().toByteArray().length + 1);
            writeVarInt(output, 0);
            output.write(packet.getBuffer().toByteArray());
            output.flush();
        } else {
            throw new RuntimeException("Packet too long (WIP)");
        }
    }
}
