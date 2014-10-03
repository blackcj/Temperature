package com.blackcj.temperature.model;

/**
 * Created by Chris on 10/2/2014.
 */
public class Temperature {
    public float temperature;
    public float humidity;
    public int timestamp;
    public String formatted_date;
    public int ac_mode;

    public String toString() {
        return temperature + "°F";
    }
}
