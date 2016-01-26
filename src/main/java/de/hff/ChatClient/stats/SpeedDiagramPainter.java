package de.hff.ChatClient.stats;

import de.hff.ChatClient.messagehandling.messageoutput.TimedOutputStream;

public class SpeedDiagramPainter extends Thread {

	private SpeedDiagram diagram;

	private boolean isRunning;

	private TimedOutputStream timedOutputStream;

	public SpeedDiagramPainter(SpeedDiagram diagram, TimedOutputStream timedOutputStream) {
		this.diagram = diagram;
		this.timedOutputStream = timedOutputStream;
	}

	@Override
	public synchronized void start() {
		this.isRunning = true;
		super.start();
	}

	@Override
	public void run() {

		int seconds = 0;

		while (isRunning) {
			try {
				Thread.sleep(1000);
				seconds++;
				diagram.addvalue(timedOutputStream.getBytesPerSecond() / 1024.0 / 1024.0, seconds);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void stopPainting() {
		isRunning = false;
	}

}