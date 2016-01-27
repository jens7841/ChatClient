package de.hff.ChatClient.filehandling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hff.ChatShared.filehandling.Filesaver;

public class FileManager {

	private List<TransferFile> files;
	private Map<TransferFile, Filesaver> downloadingFiles;

	public FileManager() {
		this.files = new ArrayList<>();
		this.downloadingFiles = new HashMap<>();
	}

	public void addFile(TransferFile file) {
		this.files.add(file);
	}

	public void addDownloadingFile(TransferFile file) {
		downloadingFiles.put(file, new Filesaver(file.getFile()));
	}

	public void savePackage(int id, byte[] data) {

		for (Entry<TransferFile, Filesaver> entry : downloadingFiles.entrySet()) {
			TransferFile file = entry.getKey();
			if (file.getId() == id) {
				Filesaver saver = entry.getValue();
				try {
					saver.savePackage(data);
					if (saver.getReceivedBytes() >= file.getExpectedSize()) {
						saver.endSave();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}

	}

	public TransferFile getFile(long size, String fileName) {
		for (TransferFile file : files) {
			if (file.getFile().getName().equals(fileName) && file.getFile().length() == size) {
				return file;
			}
		}
		return null;
	}

	public void removeFile(long fileSize, String fileName) {
		int deleteIndex = -1;
		for (int i = 0; i < files.size(); i++) {
			TransferFile transferFile = files.get(i);
			if (transferFile.getFile().length() == fileSize && transferFile.getFile().getName().equals(fileName)) {
				deleteIndex = i;
				break;
			}
		}

		if (deleteIndex != -1)
			files.remove(deleteIndex);

	}

}