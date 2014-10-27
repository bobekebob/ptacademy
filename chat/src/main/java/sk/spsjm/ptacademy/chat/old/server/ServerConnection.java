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
 * Class for handling new connections
 * - sends received messages to all connected clients
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
        // remember remote client socket
        this.socket = socket;
        try {
            // get remote client input stream (for receiving client messages)
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // get remote client output stream (for sending messages to client)
            myOutputStream = new DataOutputStream(socket.getOutputStream());
            // add client output to all client outputs so that we can send received
            // message to all other clients
            outputs.add(myOutputStream);
            outputConnections = outputs;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // read data until socket is closed
        while(! socket.isClosed()) {
            try {
                String message;
                // check if there is any message waiting to be read
                if (input.ready()) {
                    // read message
                    message = input.readLine();
                    System.out.println("<< " + message);
                    if ((message == null) || message.equals("/quit")) {
                        System.out.println("Closing connection");
                        socket.close();
                        outputConnections.remove(myOutputStream);
                        break;
                    }
                    // write the received message to all connected clients
                    for (DataOutputStream output : outputConnections) {
                        output.writeBytes(message + "\n");
                    }
                }
                // free thread for some additional processing
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}