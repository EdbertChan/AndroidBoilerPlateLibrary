package com.edbert.library.sendRequest;

import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;

public interface SendRequestInterface<T extends JsonResponseInterface>{
	public T makeRequest(Bundle b);
	public abstract Class<T> getJSONclass();

	public abstract boolean writeToDatabase(Context c, Object o);
}