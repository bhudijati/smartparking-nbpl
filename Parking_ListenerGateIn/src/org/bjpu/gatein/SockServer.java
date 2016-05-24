package org.bjpu.gatein;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Jati
 */
public class SockServer extends Thread {

    private static SockServer socketServer = null;
    private Socket socket;
    private ServerSocket server;
    private boolean isDisconnect = false;
    private boolean isStop = false;
    public static HashMap<String, String> clientList;
    private WriterOutput outputMonitor;

    public static synchronized HashMap<String, String> getConnectedClient() {
        return clientList;
    }

    public SockServer(ServerSocket server) {
        this.server = server;
        this.isStop = false;
        clientList = new HashMap<>();
        this.outputMonitor = new WriterOutput();
    }

    // Stop server
    public synchronized void setStop(boolean flag) {
        isStop = flag;
        if (server != null && flag) {
            try {
                server.close();
            } catch (IOException ex) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                    ex.printStackTrace(out);
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            }
        }
    }

    /*
     * Digunakan untuk inisiasi server secara aman
     */
    public static synchronized SockServer handle(ServerSocket server) {
        if (socketServer == null) {
            socketServer = new SockServer(server);
        } else {
            socketServer.setStop(true);
            if (socketServer.socket != null) {
                socketServer.socket = null;
            }
            if (socketServer.server != null) {
                socketServer.server = null;
            }
            socketServer.server = null;
            socketServer.socket = null;
            socketServer = new SockServer(server);
        }
        return socketServer;
    }

    protected static synchronized void updateList(String key, String value) {
        clientList.put(key, value);
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                socket = server.accept();
            } catch (IOException ex) {
                if (!isStop) {
                    System.out.println("Error accepting client");
                    isStop = true;
                }
                continue;
            }
            try {
                updateList(socket.getInetAddress().getHostAddress(), "client " + socket.getInetAddress().getHostAddress() + " is connected");
                outputMonitor.setOutputMonitoring("Client " + socket.getInetAddress().getHostAddress() + " is connected");
                SockClientThread st = new SockClientThread(socket);
                st.start();
            } catch (Exception ex) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                    ex.printStackTrace(out);
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            }
        }
    }
}
