package com.example.webapi.controller;

import com.example.webapi.model.RandomCatFact;
import com.example.webapi.model.WeatherPrediction;
import com.example.webapi.services.RandomAPIService;
import com.example.webapi.services.WeatherPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    private RandomAPIService randomAPIService;
    private final WeatherPredictionService weatherPredictionService;

    @Autowired
    public APIController(WeatherPredictionService weatherPredictionService) {
        this.weatherPredictionService = weatherPredictionService;
    }
    @GetMapping("/catRandomFact")
    public RandomCatFact getCatFact() {
        return randomAPIService.getCatFact();
    }
    @GetMapping("/average")
    public ResponseEntity<String> getAverageTemperatureByHour(@RequestParam("date") String date) {
        // Validate the date format
        if (!weatherPredictionService.isValidDate(date)) {
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
        }

        // Get the average temperatures for each hour
        List<String> avgTempByHour = weatherPredictionService.getAverageTemperatureByHour(convertDateFormat(date));

        // Join the list into a single string separated by newlines
        String response = String.join("\n", avgTempByHour);

        // Return the response as plain text
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body(response);
    }

    /**
     * Help method to convert the string input from the user from yyyy-mm-dd to yyyymmdd
     * before calling weatherPredictionService.getAverageTemperatureByHour(date).
     * @param date
     * @return date
     */
    private String convertDateFormat(String date) {
        // Define the original date format and the target date format
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        // Parse the date from the original format
        LocalDate parsedDate = LocalDate.parse(date, originalFormat);

        date = parsedDate.format(targetFormat);
        System.out.println("NEW DATE: " + date);
        // Return the date in the desired format
        return date;
    }
}
