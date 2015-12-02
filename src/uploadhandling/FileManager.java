package uploadhandling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	private List<UploadFile> files;

	public FileManager() {
		files = new ArrayList<>();
	}

	public void addFile(File file) {
		UploadFile uploadFile = new UploadFile(file);
		files.add(uploadFile);
	}

	public UploadFile getFile(String fileName, long size) {
		for (UploadFile uploadFile : files) {
			if (uploadFile.getFile().getName().equals(fileName) && uploadFile.getFile().length() == size) {
				return uploadFile;
			}
		}
		return null;
	}

	public UploadFile getFileWaintingForConfirmation(String fileName, long size) {
		for (UploadFile uploadFile : files) {
			if (uploadFile.getFile().getName().equals(fileName) && uploadFile.getFile().length() == size
					&& !uploadFile.isInUpload() && !uploadFile.isUploadFinished()) {
				return uploadFile;
			}
		}
		return null;
	}

}