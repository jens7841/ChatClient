package client;

import java.util.HashMap;

public class ServiceRegistry {

	public static final String LOGIN_SUCCESS_MESSAGE_HANDLER = "loginsuccessmessagehandler";
	public static final String LOGIN_ERROR_MESSAGE_HANDLER = "loginerrormessagehandler";
	private final HashMap<String, Service> hashMap = new HashMap<>();

	public void register(Service service, String key) {
		hashMap.put(key, service);
	}

	public Service getService(String key) {
		return hashMap.get(key);
	}
}