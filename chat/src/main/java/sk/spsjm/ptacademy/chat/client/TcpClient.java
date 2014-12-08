/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import sk.spsjm.ptacademy.chat.util.Constants;

/**
 * @author michal.polkorab
 *
 */
public class TcpClient {

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost", Constants.PORT)) {
            System.out.println("Klient nastartovany");
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(isr);
            BufferedReader socketReader =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread thread = new Thread(new ClientInput(socketReader));
            thread.start();
            while (true) {
                outputStream.writeBytes(reader.readLine()+"\n");
            }
            
        } catch (IOException e) {
            System.out.println("Chyba pri inicializacii.");
            e.printStackTrace();
        }
    }
}
