package com.BuracosDCApi.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@Component
public class CustomDateDeserializer extends StdDeserializer<Date> {

	public CustomDateDeserializer() {
		this(null);
	}

	protected CustomDateDeserializer(Class<?> vc) {
		super(vc);
	}

	private static final long serialVersionUID = 1L;

	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		String date = jsonparser.getText();
		
		if(date.equals(""))
			return null;

		// Expressões regulares para reconhecer o formato da data que foi recebida
		String space = "\\x{0020}+";
		boolean format1 = date.matches("([A-Z][a-z]{2}" + space + "[0-9]{1,2}," + space + "[0-9]{4}," + space
				+ "[0-9]{1,2}:[0-9]{2}:[0-9]{2}" + space + "[A-Za-z]{2}" + ")");
		boolean format2 = date.matches("([0-9]{2}/[0-9]{2}/[0-9]{4})");
		boolean format3 = date.matches("([0-9]{4}-[0-9]{2}-[0-9]{2})");
		boolean format4 = date.matches("([0-9]{2}/[0-9]{2}/[0-9]{4}"+space+"[0-9]{1,2}:[0-9]{2}:[0-9]{2})");
		boolean format5 = date.matches("([0-9]{2}/[0-9]{2}/[0-9]{4}"+space+"[0-9]{1,2}:[0-9]{2}:[0-9]{2}.[0-9]{3})");
		
		// Escolhendo outro formato baseado na data recebida
		if (format1)
			formatter = new SimpleDateFormat("MMM dd, yyyy, HH:mm:ss a", Locale.US); 
		else if (format2)
			formatter = new SimpleDateFormat("dd/MM/yyyy");

		else if (format3)
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		else if (format4)
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		else if(format5) {
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		}

		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}