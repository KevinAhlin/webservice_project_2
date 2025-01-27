package com.example.webapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WeatherPrediction implements Serializable {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;
    @JsonFormat(pattern = "HH:mm")
    private String time;
    private double temperature;

    // Constructor
    public WeatherPrediction(int id, String date, String time, double temperature) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
    }

    // Default constructor
    public WeatherPrediction() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    // toString method for easy debugging
    @Override
    public String toString() {
        return "WeatherPrediction{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", temperature=" + temperature +
                '}';
    }
}
