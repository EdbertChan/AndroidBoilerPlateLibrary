package com.edbert.library.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

public class SocketOperator<T> {

	private static SocketOperator instance = null;
	Class<T> classType;


	private static final int TIMEOUT = 7000;
	public SocketOperator(Class<T> type) {
		classType = type;
		// Exists only to defeat instantiation.
	}

	public static SocketOperator getInstance(Class<?> type) {
		instance = new SocketOperator(type);
		return instance;
	}


	public static String httpPostRequest(String url,
			Map<String, String> params, String jsonBody, File file)
			throws Exception {
		// params = imageParams();

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		String result = "";
		try {

			post.addHeader("enctype", "multipart/form-data");

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			if (file.exists()) {

				FileBody bin = new FileBody(file, "image.jpg", "image/jpg",
						"utf-8");
				// bin.getMediaType();
				// mpEntity.addPart("picture", bin);
				builder.addPart("image", bin);

			} else {
				Log.w("socket operator", "File doesn't exist!");
			}

			post.setEntity(builder.build());
			// System.out.println(post.getHeaders("Content-Type")[0].getValue());

			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), TIMEOUT);
			HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);

			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					post.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// post.setHeader("Content-Length",Long.toString(cbFile.length()) );
			// Log.i("headers dump", getHeadersAsString(post.getAllHeaders()));
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getHeadersAsString(Header[] headers) {

		StringBuffer s = new StringBuffer("Headers:");
		s.append("------------");
		for (Header h : headers)
			s.append(h.toString());
		s.append("------------");
		return s.toString();
	}

	public static String httpPostRequest(String url,
			Map<String, String> params, String body) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String xml = body;

		String result = "";

		HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);
		post.setEntity(entity);

		if (params != null) {

			for (Map.Entry<String, String> entry : params.entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}

		HttpResponse response = client.execute(post);
		result = EntityUtils.toString(response.getEntity());

		return result;
	}


	public static String httpPutRequest(String url,
			Map<String, String> params, String body) throws Exception {
	      // example url : http://localhost:9898/data/1d3n71f13r.json
        DefaultHttpClient httpClient = new DefaultHttpClient();
        StringBuilder result = new StringBuilder();
        try {
            HttpPut putRequest = new HttpPut(url);
            putRequest.addHeader("Content-Type", "application/json");
            putRequest.addHeader("Accept", "application/json");
            
            StringEntity input;
            try {
                input = new StringEntity(body);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
          
            putRequest.setEntity(input);
            HttpResponse response = httpClient.execute(putRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));
            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
	
	public static String httpGetRequest(String url, Map<String, String> params)
			throws Exception {
		String response = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		if (params != null) {

			for (Map.Entry<String, String> entry : params.entrySet()) {
				get.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse responseGet = client.execute(get);

		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);

		HttpEntity entity = responseGet.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		response = responseString;

		return response;
	}
	
	public static String httpDeleteRequest(String url,
			Map<String, String> params) throws Exception {
		String response = "";
		HttpClient client = new DefaultHttpClient();
		HttpDelete get = new HttpDelete(url);

		if (params != null) {

			for (Map.Entry<String, String> entry : params.entrySet()) {
				get.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse responseGet = client.execute(get);

		HttpEntity entity = responseGet.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		response = responseString;

		return response;
	}

	public static Map<String, String> defaultParams() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		map.put("Cache-Control", "no-cache");
		return map;
	}

	public static Map<String, String> imageParams() {
		Map<String, String> map = new LinkedHashMap<String, String>();

		map.put("Accept", "application/json");
		map.put("Cache-Control", "no-cache");
		return map;
	}

	// this is used for querying resposnes
	public T getResponse(Context c, String url, Map<String, String> map) {
		String s;
		try {
			s = this.httpGetRequest(url, map);
			Gson gson3 = new Gson();
			T parsedResponse = gson3.fromJson(s, classType);
			return parsedResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public T getResponse(Context c, String url) {
		return getResponse(c, url, null);
	}

	public T deleteResponse(Context c, String url, Map<String, String> map) {
		String s;
		try {
			s = this.httpDeleteRequest(url, map);
			Gson gson3 = new Gson();
			T parsedResponse = gson3.fromJson(s, classType);
			return parsedResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public T postResponse(Context c, String url, Map<String, String> map,
			String body) {
		String s = "";
		try {
			s = this.httpPostRequest(url, map, body);
			Gson gson3 = new Gson();
			T parsedResponse = gson3.fromJson(s, classType);
			return parsedResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public T postResponse(Context c, String url, Map<String, String> map,
			String body, File file) {
		String s = "";
		try {
			s = this.httpPostRequest(url, map, body, file);
			Gson gson3 = new Gson();
			T parsedResponse = gson3.fromJson(s, classType);
			return parsedResponse;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public T putResponse(Context c, String url, Map<String, String> map,
			String body) {
		String s = "";
		try {
			s = this.httpPutRequest(url, map, body);
			Gson gson3 = new Gson();
			T parsedResponse = gson3.fromJson(s, classType);
			return parsedResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}