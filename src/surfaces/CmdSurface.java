package surfaces;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdSurface extends Surface {

	@Override
	public void outputChatMessaage(String message) {
		System.out.println(message);
	}

	@Override
	public void outputErrorMessage(String message) {
		System.out.println("Error: " + message);
	}

	@Override
	public String getDefaultChatInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
		String input = "";
		try {
			input = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	@Override
	public String getInputWithMessage(String message) {

		System.out.print(message + ": ");

		return getDefaultChatInput();
	}

	@Override
	public void outputSuccessMessage(String message) {
		System.out.println(message);
	}

}
