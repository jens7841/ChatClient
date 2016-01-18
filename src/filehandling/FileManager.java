package filehandling;

import java.util.ArrayList;
import java.util.List;

public class FileManager {

	private List<UploadFile> files;

	public FileManager() {
		this.files = new ArrayList<>();
	}

	public void addFile(UploadFile file) {
		this.files.add(file);
	}

	public UploadFile getFile(long size, String fileName) {
		for (UploadFile file : files) {
			if (file.getFile().getName().equals(fileName) && file.getFile().length() == size) {
				return file;
			}
		}
		return null;
	}

}