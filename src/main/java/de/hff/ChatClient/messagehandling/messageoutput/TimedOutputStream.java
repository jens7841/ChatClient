package de.hff.ChatClient.messagehandling.messageoutput;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hff.ChatClient.client.Service;

public class TimedOutputStream extends BufferedOutputStream implements Service {

	private static final String[] UNITS = new String[] { "B", "KB", "MB", "GB", "TB" };

	private long startTime = 0;
	private int bytePerSecond = 0;
	private int byteCounter = 0;

	public TimedOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public synchronized void write(int b) throws IOException {
		super.write(b);
		byteCounter++;
		updateTime();
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	// @Override
	// public synchronized void write(byte[] b, int off, int len) throws
	// IOException {
	// if (off >= 0 && off < b.length && off + len < b.length) {
	//
	// for (int i = off; i < len; i++) {
	// write(b[i]);
	// }
	//
	// }
	// }

	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException {
		updateTime();
		super.write(b, off, len);
		byteCounter += len;
		updateTime();
	}

	private void updateTime() {
		if (System.currentTimeMillis() - startTime >= 1000) {
			bytePerSecond = byteCounter;
			byteCounter = 0;
			startTime = System.currentTimeMillis();
			System.out.println(getSpeed());
		}
	}

	public String getSpeed() {

		updateTime();

		for (int i = UNITS.length - 1; i >= 0; i--) {
			double d = Math.round(100.0 * (bytePerSecond / Math.pow(1024, i))) / 100.0;
			if (d >= 1) {
				return d + " " + UNITS[i] + "/s";
			}
		}

		return bytePerSecond + "B/s";
	}

	public double getBytesPerSecond() {
		updateTime();
		return bytePerSecond;
	}

}