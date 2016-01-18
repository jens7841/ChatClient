package test;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test extends Thread {

	public static void main(String[] args) {
		new Test().start();
		new Test().start();
		new Test().start();
		new Test().start();
		new Test().start();
		new Test().start();
	}

	BufferedOutputStream fileOutputStream;

	public Test() {
		try {
			fileOutputStream = new BufferedOutputStream(new FileOutputStream("test.big", true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 190000000000000l; i++) {
				fileOutputStream.write(Byte.MAX_VALUE);
			}
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
