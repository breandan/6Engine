package com.daexsys.sbc.net;

import com.daexsys.ijen3D.net.client.Client;

import java.io.IOException;
import java.net.Socket;

public class SBGClient {
    public static void connect() {
        try {
            Client client = new Client(new Socket("127.0.0.1", 3333));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
