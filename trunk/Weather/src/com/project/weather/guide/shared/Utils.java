package com.project.weather.guide.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Utils {
	public static String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	public static List<WeatherInfo> parseData(String input) throws Exception {
		List<WeatherInfo> weatherData = new ArrayList<WeatherInfo>();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(input)));
		NodeList times = doc.getElementsByTagName("start-valid-time");

		for (int i = 0; i < times.getLength(); i++) {
			Element time = (Element) times.item(i);
			WeatherInfo forecast = new WeatherInfo();

			weatherData.add(forecast);
			forecast.setTime(time.getFirstChild().getNodeValue());
		}

		NodeList temps = doc.getElementsByTagName("value");

		for (int i = 0; i < temps.getLength(); i++) {
			Element temp = (Element) temps.item(i);
			WeatherInfo forecast = weatherData.get(i);

			forecast.setTemp(new Integer(temp.getFirstChild().getNodeValue()));
		}

		NodeList icons = doc.getElementsByTagName("icon-link");

		for (int i = 0; i < icons.getLength(); i++) {
			Element icon = (Element) icons.item(i);
			WeatherInfo forecast = weatherData.get(i);

			forecast.setImage(icon.getFirstChild().getNodeValue());
		}
		return (weatherData);
	}

	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

	}

	public static String tomorrow(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	public static String generatePage(List<WeatherInfo> data) {
		StringBuilder bufResult = new StringBuilder("<html><body><table>");

		bufResult.append("<tr><th width=\"50%\">Time</th>"
				+ "<th>Temperature</th>" + "<th>Rain(%)</th>" + "<th>Forecast</th></tr>");

		for (WeatherInfo weather : data) {
			if(weather.getRain().equalsIgnoreCase("0")){
				bufResult.append("<tr>");
			}else{
				bufResult.append("<tr BGCOLOR=\"#99CCFF\">");
			}
			bufResult.append("<td align=\"center\">");
			bufResult.append(weather.getTime());
			bufResult.append("</td><td align=\"center\">");
			bufResult.append(weather.getTemp());
			bufResult.append("</td><td align=\"center\">");
			bufResult.append(weather.getRain());
			bufResult.append("</td><td><img src=\"");
			bufResult.append(weather.getImage());
			bufResult.append("\"></td></tr>");
		}

		bufResult.append("</table></body></html>");

		return (bufResult.toString());
	}

	public static String rain(String str) {
//		String str = "http://forecast.weather.gov/images/wtf/nra90.jpg";
		Pattern ptr = Pattern.compile("(?:http://).*" + "/wtf/"
				+ "([a-z]{2})([0-9]{2})" + ".*");
		Pattern ptr2 = Pattern.compile("(?:http://).*" + "/wtf/"
				+ "([a-z]{3})([0-9]{2})" + ".*");
		Pattern ptr3 = Pattern.compile("(?:http://).*" + "/wtf/"
				+ "([a-z]{4})([0-9]{2})" + ".*");

		Matcher mat = ptr.matcher(str);
		while (mat.find()) {
			String text = mat.group(1);
			String text1 = mat.group(2);
			return text1;

		}
		mat = ptr2.matcher(str);
		while (mat.find()) {
			String text = mat.group(1);
			String text1 = mat.group(2);
			return text1;
		}
		mat = ptr3.matcher(str);
		while (mat.find()) {
			String text = mat.group(1);
			String text1 = mat.group(2);
			return text1;
		}
		return "0";

	}

}
