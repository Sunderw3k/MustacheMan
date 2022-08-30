package net.sunderw.mustache;

import net.sunderw.mustache.utils.PacketUtils;
import net.sunderw.mustache.packets.handshake.C00PacketHandshake;
import net.sunderw.mustache.packets.login.C00PacketLogin;
import net.sunderw.mustache.packets.play.C01PacketChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String IP = "tcpshield.acmc.pl";
    public static final int PORT = 25565;

    private static final int CONNECT_TIMEOUT = 3000;
    private static final int CONNECT_DELAY = 2000;

    public static int compressionSize;

    public static void main(String[] args) {

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/targets.txt");
        assert stream != null;

        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        Arrays.stream(result.split("\n")).forEach(username -> {
            try {
                mustashify(username);
                Thread.sleep(CONNECT_DELAY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void mustashify(String username) throws IOException{
        InetSocketAddress host = new InetSocketAddress(IP, PORT);
        Socket socket = new Socket();

        // Try connect
        try {
            socket.connect(host, CONNECT_TIMEOUT);
        } catch (Exception e) {
            System.out.println("\u001B[31mFailed connecting\u001B[0m");
            return;
        }

        // Create Streams
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());

        // Handshake
        PacketUtils.sendPacketUncompressed(output, new C00PacketHandshake(47, IP, PORT, 2));

        // Send player username
        PacketUtils.sendPacketUncompressed(output, new C00PacketLogin(username));

        //noinspection StatementWithEmptyBody
        while (input.available() == 0) {}

        // Read response
        int size = PacketUtils.readVarInt(input);
        int id = PacketUtils.readVarInt(input);

        if (id == 1) {
            System.out.println("\u001B[31mEncryption request for " + username + "\u001B[0m");
            return;
        } else if (id == 0) {
            System.out.println("Kicked - Reason: " + PacketUtils.readString(input));
            return;
        }

        compressionSize = PacketUtils.readVarInt(input);

        // Set skin
        PacketUtils.sendPacketCompressed(output, new C01PacketChat("/skin Sunderw_19k"));
        System.out.println("\u001B[32mSkin set for " + username + "\u001B[0m");

        // Disconnect
        PacketUtils.sendPacketUncompressed(output, new C01PacketChat("invalid"));
        socket.close();
    }
}

