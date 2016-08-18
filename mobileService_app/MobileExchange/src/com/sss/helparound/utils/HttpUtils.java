package com.sss.helparound.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sss.helparound.constant.TechnicalMessage;

public class HttpUtils {

    private HttpUtils() {
    }

    public static String parseResponse(HttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeTo(out);
        out.close();
        return out.toString();
    }
    
    public static JSONObject parseObject(HttpResponse response) throws IOException, JSONException {
    	return new JSONObject(parseResponse(response));
    }
    
    public static JSONArray parseArray(HttpResponse response) throws IOException, JSONException {
    	return new JSONArray(parseResponse(response));
    }

    
	public static String getMessageWithStatusCode(final HttpResponse response) {
		String message = TechnicalMessage.KO;
		if (response.getStatusLine().getStatusCode() == 200){
			message = TechnicalMessage.OK;
		}else if (response.getStatusLine().getStatusCode() == 400){
			message = TechnicalMessage.UNAUTHORIZED;
		}
		return message;
	}

}
