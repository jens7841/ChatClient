package de.hff.ChatClient.filehandling;

import java.io.File;

public class UploadFile {

	private File file;
	private int id;

	public UploadFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}