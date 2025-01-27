package com.example.webapi;

import com.example.webapi.model.RandomCatFact;
import com.example.webapi.model.WeatherPrediction;
import com.example.webapi.services.RandomAPIService;
import com.example.webapi.services.WeatherPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

@SpringBootApplication
public class WebapiApplication implements CommandLineRunner {

	// Inject the services via Spring's Dependency Injection
	// Instantiations
	@Autowired
	private RandomAPIService randomAPIService;

	@Autowired
	private WeatherPredictionService service;

	public static void main(String[] args) {
		// Start the Spring Boot application
		SpringApplication.run(WebapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Once the Spring Boot application starts, this method will be called
		runInteractiveConsole();
	}
	private void runInteractiveConsole() {
		int id;
		String dateInput;
		String timeInput;

		try (Scanner scanner = new Scanner(System.in)) { // Ensure the Scanner is closed automatically
			boolean runMenu = true;
			while (runMenu) {
				System.out.println("Choose an option:");
				System.out.println("1. Add a new weather prediction");
				System.out.println("2. List all weather predictions");
				System.out.println("3. Remove weather predictions");
				System.out.println("4. Change weather predictions");
				System.out.println("5. Get random cat fact from API");
				System.out.println("6. Exit");

				int choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				switch (choice) {
					case 1:
						System.out.print("Enter date (YYYY-MM-DD): ");
						dateInput = scanner.nextLine();
						//LocalDate date = LocalDate.parse(dateInput);

						System.out.print("Enter time (HH:mm): ");
						timeInput = scanner.nextLine();
						//LocalTime time = LocalTime.parse(timeInput);

						System.out.print("Enter temperature: ");
						double temperature = scanner.nextDouble();
						scanner.nextLine(); // Consume newline

						WeatherPrediction prediction = service.addPrediction(dateInput, timeInput, temperature);
						System.out.println("Added prediction: " + prediction);
						break;

					case 2:
						System.out.println("All Weather Predictions:");
						for (WeatherPrediction p : service.getAllPredictions()) {
							System.out.println(p);
						}
						break;
					case 3:
						System.out.print("Enter the ID of the prediction to remove: ");
						id = scanner.nextInt();
						scanner.nextLine(); // Consume newline

						boolean removed = service.removePrediction(id);
						if (removed) {
							System.out.println("Prediction with ID " + id + " has been removed.");
						} else {
							System.out.println("Prediction with ID " + id + " not found.");
						}
						break;
					case 4:
						System.out.print("Enter the ID of the prediction to change: ");
						id = scanner.nextInt();
						scanner.nextLine(); // Consume newline

						// Check if the prediction with the given ID exists
						if (service.findById(id)) {
							// Prediction exists, now we update the values
							System.out.print("Enter the new date (yyyy-MM-dd): ");
							dateInput = scanner.nextLine();
							//LocalDate newDate = LocalDate.parse(dateInput);

							System.out.print("Enter the new time (hh:mm): ");
							timeInput = scanner.nextLine();
							//LocalTime newTime = LocalTime.parse(timeInput);

							System.out.print("Enter the new temperature: ");
							double newTemperature = scanner.nextDouble();
							scanner.nextLine(); // Consume newline

							// Update the prediction
							boolean updated = service.updatePrediction(id, dateInput, timeInput, newTemperature);
							if (updated) {
								System.out.println("Prediction updated successfully!");
							} else {
								System.out.println("Failed to update prediction.");
							}
						} else {
							System.out.println("Prediction with ID " + id + " not found.");
						}
					case 5:
						RandomCatFact catFact = randomAPIService.getCatFact();
						System.out.println("Fun fact: " + catFact.getFact());
						break;
					case 6:
						runMenu = false;
						System.out.println("Exiting...");
						break;

					default:
						System.out.println("Invalid choice. Please try again.");
				}
			}
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}

}
