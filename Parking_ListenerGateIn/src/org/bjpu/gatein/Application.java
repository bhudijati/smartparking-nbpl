package org.bjpu.gatein;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Jati
 */
public class Application {

    protected static Thread serverThread;
    private static SockServer sockServer;
    private int portNumber;

    public void createConnection() throws IOException {
        portNumber = 61234;
        ServerSocket server = new ServerSocket(portNumber);
        sockServer = SockServer.handle(server);
        startServer(sockServer);
    }

    public SockServer getConnection() {
        return sockServer;
    }

    public static void startServer(final SockServer sockServer) {
        if (serverThread == null) {
            serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sockServer.start();
                }
            });
            serverThread.start();
        }
    }
}
