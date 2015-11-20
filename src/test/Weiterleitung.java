package test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Weiterleitung extends Thread {

	public static void main(String[] args) throws Throwable {
		ServerSocket serverSocket = new ServerSocket(81);

		new Weiterleitung(serverSocket.accept());
	}

	public Weiterleitung(Socket toClient) throws Exception {

		this.toClient = toClient;

		this.toServer = new Socket("youtube.com", 80);

		this.start();

		int read;
		while ((read = toClient.getInputStream().read()) != -1) {
			toServer.getOutputStream().write(read);
		}

	}

	private Socket toClient;
	private Socket toServer;

	@Override
	public void run() {

		try {
			int read;

			InputStream in = new BufferedInputStream(toServer.getInputStream());
			while ((read = in.read()) != -1) {
				toClient.getOutputStream().write(read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
