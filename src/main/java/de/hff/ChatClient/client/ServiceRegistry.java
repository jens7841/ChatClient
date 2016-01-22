package de.hff.ChatClient.client;

import java.util.HashMap;

public class ServiceRegistry {

	public static final String LOGIN_SUCCESS_MESSAGE_HANDLER = "loginsuccessmessagehandler";
	public static final String LOGIN_ERROR_MESSAGE_HANDLER = "loginerrormessagehandler";
	public static final String UPLOAD_CONFIRMATION_HANDLER = "uploadconfirmationhandler";
	public static final String TIMED_MESSAGE_OUTPUT_STREAM = "timedmessageoutputstream";
	private final HashMap<String, Service> hashMap = new HashMap<>();

	public void register(Service service, String key) {
		hashMap.put(key, service);
	}

	public Service getService(String key) {
		return hashMap.get(key);
	}
}