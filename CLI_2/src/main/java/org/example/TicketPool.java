package org.example;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {
    private int availableTickets = 0;
    private final int maxCapacity;
    private boolean vendorActive = true;
    private int totalTicketsReleased = 0;
    private int totalTicketsSold = 0;
    private boolean allTicketsSold = false;
    private final Logger LOGGER = Logger.getLogger(TicketPool.class.getName());
    // Initially false, tickets are not sold yet

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(int number) {
        if (!vendorActive || totalTicketsReleased >= maxCapacity) {
            vendorActive = false;  // Deactivate vendor if already at capacity
            return;
        }

        // Wait until all tickets are sold, then vendor can release more if there is space
        while (availableTickets > 0 && totalTicketsReleased < maxCapacity && vendorActive) {
            LOGGER.info("Vendor is waiting for tickets to be sold...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Check if we have reached the maximum ticket capacity
        if (totalTicketsReleased >= maxCapacity) {
            vendorActive = false;
            notifyAll();  // Notify customers that no more tickets will be released
            return;
        }

        int ticketsRemaining = maxCapacity - totalTicketsReleased;
        int ticketsToRelease = Math.min(number, ticketsRemaining);
        availableTickets += ticketsToRelease;
        totalTicketsReleased += ticketsToRelease;
        allTicketsSold = false;  // There are still tickets available

        LOGGER.info("Vendor released " + ticketsToRelease + " tickets. Available tickets: " + availableTickets);
        notifyAll();
    }

    public synchronized boolean getTickets(int number, String customerName) {
        while (availableTickets < number && vendorActive) {
            // If all tickets are sold out, don't allow customers to wait
            if (availableTickets == 0 && totalTicketsReleased >= maxCapacity) {
                LOGGER.log(Level.WARNING,customerName + " cannot buy tickets. No tickets available.");
                return false;
            }

            LOGGER.info(customerName + " waiting for more tickets. Available: " + availableTickets);
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        int ticketsToBuy = Math.min(number, availableTickets);  // Buy whatever is lesser, the request or what's available
        availableTickets -= ticketsToBuy;
        totalTicketsSold += ticketsToBuy;
        LOGGER.info(customerName + " bought " + ticketsToBuy + " tickets. Available tickets: " + availableTickets);

        if (availableTickets == 0) {
            allTicketsSold = true;
            LOGGER.info("All tickets sold, notifying vendor.");
            notifyAll();  // Notify vendor that all tickets have been sold
        }

        return true;
    }

    public synchronized boolean isVendorActive() {
        return vendorActive;
    }

    public synchronized int getAvailableTickets() {
        return availableTickets;
    }

    public synchronized int getTotalTicketsSold() {
        return totalTicketsSold;
    }
}
