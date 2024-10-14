import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



class Addexpense {

    // Method to count the number of records in the file
    static int getLastSerialNumber() {
        int serialNumber = 0;

        try {
            File myObj = new File("expense.txt");
            Scanner fileScanner = new Scanner(myObj);
            
            // Count the number of lines in the file, each representing one expense
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.isEmpty()) {
                    serialNumber++;  // Increment serial number for each record
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting with serial number 1.");
        }

        return serialNumber;
    }

    static void expenses() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter expense description:");
        String description = scanner.nextLine();

        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline left-over

        System.out.println("Enter category:");
        String category = scanner.nextLine();

        System.out.println("Enter Date(yyyy-mm-dd):");
        String date = scanner.nextLine();

        try {
            // Use 'true' in FileWriter to enable append mode instead of overwrite
            FileWriter myWriter = new FileWriter("expense.txt", true);
            
            // Get the next serial number based on existing records
            int serialNumber = getLastSerialNumber() + 1;

            // Write data in a comma-separated format or any other format you prefer
            myWriter.write("Serial No: " + serialNumber + ", Description: " + description + 
                           ", Amount: " + amount + ", Category: " + category + ", Date: " + date + "\n");
            myWriter.close();
            System.out.println("Expense details stored successfully with Serial No: " + serialNumber);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        scanner.close();
    }
}

class ViewExpense{
    static void viewExpenses() {
        try {
            File myObj = new File("expense.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              System.out.println(data);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
}
}

class Deleteexpense {
    static void deleteExpense() {
        // Read all expenses from the file into an ArrayList
        ArrayList<String> expenses = new ArrayList<>();
        try {
            File file = new File("expense.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                expenses.add(line);  // Add each line (expense) to the ArrayList
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        // Display all current expenses
        System.out.println("===Current Expenses===");
        for (String expense : expenses) {
            System.out.println(expense);
        }

        // Ask the user for the serial number of the expense to delete
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Serial Number of the expense to delete:");
        int serialToDelete = scanner.nextInt();

        // Create a new ArrayList to store expenses after deletion
        ArrayList<String> updatedExpenses = new ArrayList<>();

        // Loop through each expense and check if the serial number matches
        boolean expenseDeleted = false;
        for (String expense : expenses) {
            // Assuming the serial number is at the beginning of each record in the format "Serial No: X"
            if (expense.startsWith("Serial No: " + serialToDelete + ",")) {
                // Skip this record, as it's the one we want to delete
                expenseDeleted = true;
            } else {
                // Add non-deleted records to the updated list
                updatedExpenses.add(expense);
            }
        }

        // If the record with the serial number was found and deleted, update the file
        if (expenseDeleted) {
            try {
                FileWriter writer = new FileWriter("expense.txt", false);  // Overwrite the file
                for (String updatedExpense : updatedExpenses) {
                    writer.write(updatedExpense + "\n");
                }
                writer.close();
                System.out.println("Expense with Serial No: " + serialToDelete + " was successfully deleted.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Expense with Serial No: " + serialToDelete + " not found.");
        }

        scanner.close();
    }
}

class Summary {
    static void summaryByCategory() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter the category you want a summary for:");
        String categoryToSearch = scanner.nextLine().trim();

        double totalAmount = 0;  // To store the total amount for the selected category

        try {
            File file = new File("expense.txt");
            Scanner fileScanner = new Scanner(file);

            // Loop through each expense record in the file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                // Ignore empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Split the record by commas to extract the details
                String[] expenseDetails = line.split(",");

                String category = ""; // To store the category of the current record
                double amount = 0;

                // Loop through the details and find the category and amount
                for (String detail : expenseDetails) {
                    detail = detail.trim(); // Trim to remove extra spaces around each detail
                    if (detail.startsWith("Category:")) {
                        category = detail.split(":")[1].trim(); // Extract the category value
                    } else if (detail.startsWith("Amount:")) {
                        try {
                            amount = Double.parseDouble(detail.split(":")[1].trim()); // Extract the amount value
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Error parsing amount in line: " + line);
                        }
                    }
                }
                // Check if the category matches the user's input
                if (category.equalsIgnoreCase(categoryToSearch)) {
                    totalAmount += amount;  // Add the amount to the total if the category matches
                }
            }
            fileScanner.close();

            // Display the total sum for the category
            System.out.println("Total amount spent on category '" + categoryToSearch + "': $" + totalAmount);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        scanner.close();
    }
}

class Daterange {
    static void dateRange() {
        Scanner scanner = new Scanner(System.in);
        
        // Get the start and end dates from the user
        System.out.println("Enter the start date (YYYY-MM-DD):");
        String startDateStr = scanner.nextLine().trim();   
        
        System.out.println("Enter the end date (YYYY-MM-DD):");
        String endDateStr = scanner.nextLine().trim();
        
        // DateTimeFormatter to parse the dates in the file and user input
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            // Parse user-input dates
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            
            double totalAmount = 0;  // To store the total amount in the date range
            
            // Read expenses from file
            File file = new File("expense.txt");
            Scanner fileScanner = new Scanner(file);
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                
                // Split each line to extract details
                String[] expenseDetails = line.split(",");
                
                String dateStr = "";
                double amount = 0;
                
                // Extract date and amount from the record
                for (String detail : expenseDetails) {
                    detail = detail.trim();
                    if (detail.startsWith("Date:")) {
                        dateStr = detail.split(":")[1].trim(); // Extract date
                    } else if (detail.startsWith("Amount:")) {
                        try {
                            amount = Double.parseDouble(detail.split(":")[1].trim()); // Extract amount
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing amount in line: " + line);
                        }
                    }
                }
                
                // Parse the expense date
                try {
                    LocalDate expenseDate = LocalDate.parse(dateStr, formatter);
                    
                    // Check if the expense date falls within the range
                    if ((expenseDate.isEqual(startDate) || expenseDate.isAfter(startDate)) &&
                        (expenseDate.isEqual(endDate) || expenseDate.isBefore(endDate))) {
                        totalAmount += amount;  // Add the amount if date is within range
                    }
                    
                } catch (DateTimeParseException e) {
                    System.out.println("Error parsing date in line: " + line);
                }
            }
            
            fileScanner.close();
            
            // Display the total sum for the date range
            System.out.println("Total amount spent from " + startDateStr + " to " + endDateStr + ": $" + totalAmount);
            
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        
        scanner.close();
    }
}

public class expense {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===Expense Tracker==");
        System.out.println("1. Add New Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Delete An Expense");
        System.out.println("4. Show Summary By Category");
        System.out.println("5. Show Total Expenses for a date range");
        System.out.println("6. Exit");
        System.out.println("Enter your choice(1,2,3,4,5,6):");
        int choice = scanner.nextInt();

        if(choice == 1){
            Addexpense.expenses();
        }   
        else if (choice == 2){
            ViewExpense.viewExpenses();
        }
        else if (choice == 3){
            Deleteexpense.deleteExpense();
        }
        else if (choice == 4){
            Summary.summaryByCategory();
        }
        else if (choice == 5){
            Daterange.dateRange();
        }
        else if (choice == 6){
            System.out.println("Exiting the program...");
        }
        scanner.close(); // Close the scanner at the end of main method
    }
}