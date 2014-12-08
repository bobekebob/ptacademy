/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * @author michal.polkorab
 *
 */
public class ServerConnection extends Thread {

    private Socket socket;
    private List<ClientConnectionData> clientConnectionDataList = new ArrayList<>();
    private BufferedReader input;
    private DataOutputStream myOutput;

    public ServerConnection(Socket socket, List<ClientConnectionData> clientData) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            myOutput = new DataOutputStream(socket.getOutputStream());

            ClientConnectionData clientConnData = new ClientConnectionData(myOutput);
            clientConnectionDataList.add(clientConnData);
            clientData.add(clientConnData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // citaj data kym je socket otvoreny
        while (! socket.isClosed()) {
            String message;
            try {
                // skontroluj ci nieco caka na vstupe
                if (input.ready()) {
                    // precitaj spravu
                    message = input.readLine();
                    if (message.equals("/quit")) {
                        System.out.println("Ukoncujem spojenie");
                        socket.close();
                        for (ClientConnectionData clientConnectionData:clientConnectionDataList){
                            if (clientConnectionData.getDataOutputStream().equals(myOutput)){
                                clientConnectionDataList.remove(clientConnectionData);
                                break;
                            }
                        }
                        break;
                    }
                    // zapis spravu vsetkym pripojenym klientom
                    for (ClientConnectionData clientConnectionData : clientConnectionDataList) {
                        clientConnectionData.getDataOutputStream().writeBytes(message + "\n");
                    }
                }
                // uvolni thread pre dalsie aplikacie
                sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
