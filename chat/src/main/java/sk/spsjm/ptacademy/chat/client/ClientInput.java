/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.client;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author michal.polkorab
 *
 */
public class ClientInput implements Runnable {

    private BufferedReader socketReader;

    public ClientInput(BufferedReader reader) {
        this.socketReader = reader;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(socketReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
