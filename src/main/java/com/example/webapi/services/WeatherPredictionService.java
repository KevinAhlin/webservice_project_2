package com.example.webapi.services;

import com.example.webapi.model.WeatherPrediction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherPredictionService {
    private List<WeatherPrediction> predictions = new ArrayList<>();
    public WeatherPredictionService() {
        // Load predictions from the JSON file when the service is initialized
        predictions = loadWeatherPredictions();
    }

    public List<WeatherPrediction> loadWeatherPredictions() {
        // Create an ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Attempt to load predictions.json from the resources folder
        try (InputStream inputStream = getClass().getResourceAsStream("/predictions.json")) {
            if (inputStream == null) {
                throw new IOException("File not found: predictions.json");
            }
            // Deserialize the JSON file into a List of WeatherPrediction objects
            return objectMapper.readValue(inputStream, new TypeReference<List<WeatherPrediction>>() {});
        } catch (IOException e) {
            // Handle or log the exception
            System.err.println("Error loading predictions: " + e.getMessage());
            throw new RuntimeException("Failed to load weather predictions from JSON file", e);
        }
    }

    // Method for checking if a date is valid or not
    public boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateStr, formatter); // Try to parse the string
            return true;  // If parsing is successful, it's a valid date
        } catch (DateTimeParseException e) {
            return false;  // If parsing fails, it's not a valid date
        }
    }

    // Method to add a new prediction
    public WeatherPrediction addPrediction(String date, String time, double temperature) {
        WeatherPrediction prediction = new WeatherPrediction(predictions.size()+1, date, time, temperature);
        predictions.add(prediction);
        return prediction;
    }

    // Method to remove a prediction by id
    public boolean removePrediction(int id) {
        /*
        This is the same as looping through the list to find the object with the matching id.
         */
        Optional<WeatherPrediction> predictionToRemove = predictions.stream()
                .filter(prediction -> prediction.getId() == id)
                .findFirst();

        if (predictionToRemove.isPresent()) {
            predictions.remove(predictionToRemove.get());
            return true;
        } else {
            return false; // Return false if no prediction is found with the given id
        }
    }

    // Method for update/change prediction
    public boolean updatePrediction(int id, String date, String time, Double temperature){
        for (WeatherPrediction prediction : predictions) {
            if (prediction.getId() == id) {
                // Update the prediction's fields with new data
                prediction.setDate(date);
                prediction.setTime(time);
                prediction.setTemperature(temperature);
                return true; // Successfully updated
            }
        }
        return false; // Prediction with the given ID not found
    }

    // Method to find by id if it exists.
    public boolean findById(int id) {
        for (WeatherPrediction prediction : predictions) {
            if (prediction.getId() == id) {
                return true; // Return true if prediction with the given id is found
            }
        }
        return false; // Return false if no prediction with the given id is found
    }

    // Method to list all predictions
    public List<WeatherPrediction> getAllPredictions() {
        return predictions;
    }

    // Method to get the average temperature for a date
    public List<String> getAverageTemperatureByHour(String date) {
        // Filter predictions by the selected date
        List<WeatherPrediction> filteredPredictions = predictions.stream()
                .filter(prediction -> prediction.getDate().equals(date))
                .toList();

        // Group predictions by hour
        Map<String, List<WeatherPrediction>> predictionsByHour = filteredPredictions.stream()
                .collect(Collectors.groupingBy(prediction -> prediction.getTime().substring(0, 2))); // Extract hour

        // Build the list of strings for each hour
        List<String> responseList = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%02d", hour);  // Format hour as 2 digits (e.g., "00", "01", ...)
            List<WeatherPrediction> hourPredictions = predictionsByHour.getOrDefault(hourStr, new ArrayList<>());

            // Calculate the average temperature for this hour
            double avgTemp = hourPredictions.stream()
                    .mapToDouble(WeatherPrediction::getTemperature)
                    .average()
                    .orElse(0.0);  // Default to 0.0 if no predictions for this hour

            // Format the string "date hour:00 avgTemp" and add it to the response list
            responseList.add(String.format("%s %s:00 %.1f", date, hourStr, avgTemp));
        }

        return responseList;  // Return the list of formatted strings
    }
}
