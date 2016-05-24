/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjpu.gatein;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Jati
 */
public class SockClientThread extends Thread {

    private Socket clientSocket;
    private InputStream is;
    private OutputStream os;
    private BufferedInputStream clientInput;
    private DataOutputStream clientOutput;
    private WriterOutput outputMonitor;

    public SockClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.outputMonitor = new WriterOutput();
    }

    @Override
    public void run() {
        try {
            if (clientSocket != null) {
                is = clientSocket.getInputStream();
                os = clientSocket.getOutputStream();
                clientInput = new BufferedInputStream(is);
                clientOutput = new DataOutputStream(os);
//                shHand = new ServerHandler(mCardDAO, abonDAO, accDAO, accLogDAO, credDAO, transDAO, settings);
                String ipClient = clientSocket.getInetAddress().getHostAddress();
            }
        } catch (Exception ex) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                ex.printStackTrace(out);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
        try {
            while (true) {
                String rec = readInputStream(clientInput);//in.readLine();
                try {

                    if (rec != null) {
                        String trim = rec.trim();
                        rec = trim;
                        //cek kartu 
                        outputMonitor.setOutputMonitoring("[RAW]receive message from " + clientSocket.getInetAddress().getHostAddress() + " = " + rec);
                    }
                } catch (Exception ex) {
                    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                        ex.printStackTrace(out);
                    } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }
                }

            }
            //update client connection status
        } catch (Exception ex) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                ex.printStackTrace(out);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        } finally {
            try {
                clientSocket.close();
                outputMonitor.setOutputMonitoring("Client " + clientSocket.getInetAddress().getHostAddress() + " is disconnected");
                SockServer.updateList(clientSocket.getInetAddress().getHostAddress(), "Client " + clientSocket.getInetAddress().getHostAddress() + " is disconnected");
            } catch (IOException ex) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("error-log.txt", true)))) {
                    ex.printStackTrace(out);
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            }
        }
    }

    private String readInputStream(BufferedInputStream _in)
            throws IOException, InterruptedException {
        String data = "";
        int s = _in.read();
        if (s == -1) {
            return null;
        }
        sleep(50);
        data += "" + (char) s;
        int len = _in.available();
        if (len > 0) {
            byte[] byteData = new byte[len];
            _in.read(byteData);
            data += new String(byteData);
        }
        return data;
    }

}
