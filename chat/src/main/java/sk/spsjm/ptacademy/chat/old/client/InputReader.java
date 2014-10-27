/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package sk.spsjm.ptacademy.chat.old.client;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author michal.polkorab
 *
 */
public class InputReader extends Thread {

    private boolean keepRunning = true;
    private BufferedReader inFromServer;

    /**
     * @param inFromServer
     * @param outToServer
     */
    public InputReader(BufferedReader inFromServer) {
        this.inFromServer = inFromServer;
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                if (inFromServer.ready()) {
                    String message = inFromServer.readLine();
                    System.out.println(">> " + message);
                }
                sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets keepRunning variable, which causes this thread to stop
     * @param keepRunning false to stop this thread
     */
    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}