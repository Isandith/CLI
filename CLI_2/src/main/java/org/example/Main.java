package org.example;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        Logger LOGGER = Logger.getLogger(Main.class.getName());
        Configure config = new Configure();

        // Ask user if they want to start or stop
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to START or STOP the program? (Type START or STOP)");

        String userInput = scanner.nextLine().toUpperCase();

        if ("STOP".equals(userInput)) {
            LOGGER.log(Level.WARNING, "Program stopped by user.");
            return; // Exit the program immediately
        }

        if ("START".equals(userInput)) {
            LOGGER.log(Level.INFO, "Program started.");
        } else {
            LOGGER.log(Level.WARNING, "Invalid input, exiting program.");
            return; // Exit the program if the input is not START or STOP
        }

        // Initialize the TicketPool
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

        // Start the Vendor thread
        Vendor vendor = new Vendor(ticketPool, config.getReleaseCapacity(), config.getTicketReleaseRate());
        Thread vendorThread = new Thread(vendor, "Vendor");
        vendorThread.start();

        // Start Customer threads (fixed to 2 as per the example)
        int numberOfCustomers = 2;
        Thread[] customerThreads = new Thread[numberOfCustomers];
        for (int i = 0; i < numberOfCustomers; i++) {
            String customerName = "Customer-" + (i + 1);
            Customer customer = new Customer(ticketPool, config.getTicketRetrievalRate(), customerName, 1); // Fixed to 3 tickets
            customerThreads[i] = new Thread(customer, customerName);
            customerThreads[i].start();
        }

        // Wait for Vendor thread to finish
        try {
            vendorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Wait for all Customer threads to finish
        for (Thread t : customerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Display total tickets sold
        LOGGER.log(Level.WARNING, "Total tickets sold: " + ticketPool.getTotalTicketsSold());
        LOGGER.log(Level.WARNING, "Ticket booking system has completed all operations.");
    }
}
