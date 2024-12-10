# CLI

# Ticket Booking System

A multi-threaded ticket booking system where a vendor releases tickets and customers purchase them.

## Requirements

- Java 17 or later
- Google Gson library (included in the project)

## Setup

1. Clone or download the repository.
2. Compile the code:

    ```bash
    java org.example.Main
    ```

## Configuration

When prompted, enter the configuration values:

- Max Ticket Capacity
- Release Capacity (tickets per release)
- Ticket Release Rate (milliseconds)
- Ticket Retrieval Rate (milliseconds)

You can load a previous configuration if available.

## Start or Stop

- Type `START` to begin the simulation.
- Type `STOP` to exit.

## Simulation

The vendor releases tickets, and customers attempt to buy them. The system will log the number of tickets sold.



- **Invalid Configuration**:  
  If you enter an invalid value for any configuration option (e.g., a non-integer for ticket capacity), the program will prompt you to re-enter the value.
  
- **Tickets Not Available**:  
  If customers attempt to buy tickets when no tickets are available, they will be notified that no tickets are left.
  
- **Incorrect Input Format**:  
  If you type something other than `START` or `STOP` when prompted, the system will ask you to enter a valid command.
  
- **Program Stops Unexpectedly**:  
  If the program stops unexpectedly, check for configuration issues such as exceeding the max ticket capacity or mismatched ticket release and retrieval rates. Adjust the configuration values and try again.
  
- **Slow Performance**:  
  If the ticket release or retrieval is slower than expected, ensure that the ticket release rate and retrieval rate are set appropriately and that the system is not overloaded.
