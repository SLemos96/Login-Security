package com.BuracosDCApi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

@SuppressWarnings("unchecked")
public class JSONProcessor {

	private static final ObjectMapper mapper = new ObjectMapper();

	public synchronized static <T> T toObject(String jsonText, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(jsonText, clazz);
	}

	public synchronized static Map<String, Object> toMap(String jsonText) {
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(jsonText, LinkedTreeMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public synchronized static <T> T toObjectWithFormattedDate(String jsonText, String dateFormat, Class<T> clazz) {
		Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
		T t = null;
		try {
			t = gson.fromJson(jsonText, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public synchronized static String toJSON(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	public synchronized static String toJSON(Object object, String formatoData) {
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(formatoData);
		Gson gson = builder.create();
		return gson.toJson(object);
	}

	public synchronized static <T> List<T> toList(String jsonText, Class<T> clazz, String... params) throws JSONException {
		String dateFormat = !List.of(params).isEmpty() ? params[0] : null;
        JSONArray jsonArray = new JSONArray(jsonText);
        List<T> result = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            T object = null;
            if(dateFormat != null) {
                object = toObjectWithFormattedDate(jsonObject.toString(), dateFormat, clazz);   
            }
            else {
                object = toObject(jsonObject.toString(), clazz);
            }
            result.add(object);
        }
        return result;
    }

}
