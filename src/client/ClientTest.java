package client;

import surfaces.CmdSurface;

public class ClientTest {

	public static void main(String[] args) throws Throwable {

		Client c = new Client(new CmdSurface());
		c.startClient();
		c.createSocket();
		c.login("jens", "jens");

	}

}