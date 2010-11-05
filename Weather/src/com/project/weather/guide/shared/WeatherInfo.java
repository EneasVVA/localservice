package com.project.weather.guide.shared;

public class WeatherInfo {
	private String image ;
	private Integer temp ;
	private String warnMsg;
	private String time;
	private String rain ;
	
	public void setImage(String image) {
		setRain(Utils.rain(image));
		this.image = image;
	}
	public String getImage() {
		return image;
	}
	public void setTime(String t) {
		this.time = t.substring(0,16).replace('T', ' ');;
	}
	public String getTime() {
		return time;
	}
	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}
	public String getWarnMsg() {
		return warnMsg;
	}
	public void setTemp(Integer temp) {
		this.temp = temp;
	}
	public Integer getTemp() {
		return temp;
	}
	public void setRain(String rain) {
		this.rain = rain;
	}
	public String getRain() {
		return rain;
	}
}
