/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.old.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * @author michal.polkorab
 *
 */
public class ServerConnection extends Thread {

    private final Socket socket;
    private BufferedReader input;
    private List<DataOutputStream> outputConnections;
    private DataOutputStream myOutputStream;

    /**
     * Creates ServerConnection which stores remote client
     * socket and output to all connected clients
     * @param socket connection to remote client
     * @param outputs output to all connected clients
     */
    public ServerConnection(Socket socket, List<DataOutputStream> outputs) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            myOutputStream = new DataOutputStream(socket.getOutputStream());
            outputs.add(myOutputStream);
            outputConnections = outputs;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(! socket.isClosed()) {
            try {
                String message;
                if (input.ready()) {
                    message = input.readLine();
                    System.out.println("<< " + message);
                    if ((message == null) || message.equals("/quit")) {
                        System.out.println("Closing connection");
                        socket.close();
                        outputConnections.remove(myOutputStream);
                        break;
                    }
                    for (DataOutputStream output : outputConnections) {
                        output.writeBytes(message + "\n");
                    }
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}