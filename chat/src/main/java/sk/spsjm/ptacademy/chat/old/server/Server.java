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

class TCPServer {

    public static void main(String[] args) throws Exception {
        List<DataOutputStream> outputs = new ArrayList<>();
        System.out.println("Starting server");
        ServerSocket welcomeSocket = null;
        welcomeSocket = new ServerSocket(6780);
        System.out.println("Server started");
        while(true) {
            Socket connectionSocket = null;
            connectionSocket = welcomeSocket.accept();
            ServerConnection connection = new ServerConnection(connectionSocket, outputs);
            System.out.println("outputs.size(): " + outputs.size());
            connection.start();
        }
    }
}