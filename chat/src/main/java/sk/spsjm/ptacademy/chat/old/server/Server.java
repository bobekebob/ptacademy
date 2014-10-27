/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.old.server;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple chat server - listens for new connections
 */
class TCPServer {

    public static void main(String[] args) throws Exception {
        // this list stores output stream for each connected remote client
        List<DataOutputStream> outputs = new ArrayList<>();
        System.out.println("Starting server");
        // start our server
        ServerSocket serverSocket = null;
        try (ServerSocket trySocket = new ServerSocket(6780)) {
            serverSocket = trySocket;
            System.out.println("Server started");
            while(true) {
                // listen for incoming connections in loop
                Socket connectionSocket = serverSocket.accept();
                // save information about remote connection
                ServerConnection connection = new ServerConnection(connectionSocket, outputs);
                // start handling incoming messages
                connection.start();
                System.out.println("Remote client connected");
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}