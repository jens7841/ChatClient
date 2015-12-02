package client;

import java.net.Socket;

import surfaces.CmdSurface;

public class ClientTest {

	public static void main(String[] args) throws Throwable {

		Client c = new Client(new CmdSurface());
		c.startClient();
		c.setSocket(new Socket("", 12345));
		c.login("jens", "jens");

	}

}