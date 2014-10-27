/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.old.client;

import java.io.*;
import java.net.*;

/**
 * Simple chat client
 */
class TCPClient {

    public static void main(String argv[]) throws Exception {
        System.out.println("Starting client");
        // start client socket
        try (Socket clientSocket = new Socket("localhost", 6780)){
            // initialize streams
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            InputReader inputReader = null;
            while (! clientSocket.isClosed()) {
                // start reading network input in a separate loop
                if (inputReader == null) {
                    inputReader = new InputReader(inFromServer);
                    inputReader.start();
                }
                // read user input
                message = inFromUser.readLine();
                // write user input
                outToServer.writeBytes(message + '\n');
                if (message.equals("/quit")) {
                    System.out.println("Closing connection");
                    inputReader.setKeepRunning(false);
                    clientSocket.close();
                    break;
                }
            }
        }
    }
}