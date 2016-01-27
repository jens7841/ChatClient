package de.hff.ChatClient.filehandling;

import java.io.File;

public class TransferFile {

	private File file;
	private int id;
	private long expectedSize;

	public TransferFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public long getExpectedSize() {
		return expectedSize;
	}

	public void setExpectedSize(long expectedSize) {
		this.expectedSize = expectedSize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}