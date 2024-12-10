package org.example;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final long retrievalRate;
    private final String customerName;
    private final int ticketsToBuy;
    private final Logger LOGGER = Logger.getLogger(Customer.class.getName());// Fixed number of tickets per purchase

    public Customer(TicketPool ticketPool, long retrievalRate, String customerName, int ticketsToBuy) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerName = customerName;
        this.ticketsToBuy = ticketsToBuy; // Set the number of tickets per purchase
    }

    @Override
    public void run() {
        while (ticketPool.isVendorActive() || ticketPool.getAvailableTickets() > 0) {
            // Attempt to buy a fixed number of tickets
            boolean success = ticketPool.getTickets(ticketsToBuy, customerName);
            if (!success) {
                // No more tickets are available, or the vendor is inactive
                break;
            }

            try {
                // Simulate retrieval rate delay before attempting the next purchase
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupt status
                break;
            }
        }

        LOGGER.log(Level.WARNING,customerName + " has finished purchasing tickets.");
    }
}
