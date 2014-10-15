package com.blackcj.temperature.model;

/**
 * Data model for a recorded temperature.
 *
 * Created by Chris on 10/2/2014.
 */
public class Temperature {
    public double temperature;
    public double humidity;
    public int timestamp;
    public String formatted_date;
    public int ac_status; // Was the AC unit on or off

    public String toString() {
        return temperature + "°F";
    }
}
