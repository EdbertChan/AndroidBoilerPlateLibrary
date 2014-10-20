package com.edbert.library.sendRequest;
/**
 * Anything that implements this interface is responsible for reading from a source and 
 * writing itself to the database. Ideally, we'd keep the source and writing separate
 * in case the need ever arose for people to want to read from multiple sources
 * that gave the same response but this is highly unlikely.
 * 
 */
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class SendRequestStrategyManager {
	private static HashMap<String, SendRequestInterface> listOfSendRequestInterfaces = new HashMap<String, SendRequestInterface>();

	public static Object executeRequest(SendRequestInterface requestInterface,
			Bundle b) {
		return requestInterface.makeRequest(b);
	}

	public static void executeWriteToDatabase(SendRequestInterface requestInterface,
			Context c, Object o) {
		if(requestInterface == null){
			Log.e("SendRequestStrategyManager", "No requestInterface specified");
			return;
		}
		requestInterface.writeToDatabase(c, o);
	}

	public static SendRequestInterface getHelper(String tag) {
		return listOfSendRequestInterfaces.get(tag);
	}
	public static void register(SendRequestInterface helper) {
		String tag = generateTag(helper);
		listOfSendRequestInterfaces.put(tag, helper);
	}
	public static String generateTag(SendRequestInterface helper){
		return helper.getClass().getCanonicalName().toString();
	}

	
}