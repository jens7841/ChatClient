package uploadhandling;

import java.io.File;

import messagehandling.MessageSender;

public class UploadFile {

	private File file;
	private int id;
	private boolean uploadFinished;
	private boolean isInUpload;

	public UploadFile(File file) {
		this.file = file;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isUploadFinished() {
		return uploadFinished;
	}

	public void setUploadFinished() {
		this.uploadFinished = true;
	}

	public File getFile() {
		return file;
	}

	public void startUpload(MessageSender sender, int id) {
		if (isInUpload)
			return;
		this.id = id;
		isInUpload = true;
		new Uploader(this, sender).start();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UploadFile) {
			UploadFile file = (UploadFile) obj;
			if (file.getFile().getName().equals(getFile().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isInUpload() {
		return isInUpload;
	}

}
