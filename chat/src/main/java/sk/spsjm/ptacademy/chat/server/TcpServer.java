/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import sk.spsjm.ptacademy.chat.server.ServerConnection;

import sk.spsjm.ptacademy.chat.util.Constants;

/**
 * @author michal.polkorab
 *
 */
public class TcpServer {

    public static void main(String[] args) {
        List<ClientConnectionData> vystupy = new ArrayList<>();
        ServerSocket serverSocket = null;
        try (ServerSocket trySocket = new ServerSocket(Constants.PORT)) {
            serverSocket = trySocket;
            System.out.println("Server nastartovany");
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                ServerConnection connection = new ServerConnection(connectionSocket, vystupy);
                connection.start();
                System.out.println("Klient pripojeny");
            }
        } catch (IOException e) {
            System.out.println("Chyba pri inicializacii socketu.");
            e.printStackTrace();
        }
    }
}
