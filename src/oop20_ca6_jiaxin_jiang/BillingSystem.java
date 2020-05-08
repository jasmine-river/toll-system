package oop20_ca6_jiaxin_jiang;

import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class BillingSystem
{

    private final TollEventDAOInterface ITollEventDAO = new MySqlTollEventDAO();
    private final Scanner kb = new Scanner(System.in);

    public static void main(String[] args)
    {
        BillingSystem system = new BillingSystem();
        system.run();
    }

    private void run()
    {
        try
        {
            System.out.println("Welcome to the toll billing system!");

            // Retrieve a set of customers with incomplete billing status from database
            Set<String> customerSet = ITollEventDAO.getAllCustomersWithIncompleteBilling();

            boolean quit = false;
            boolean customerFound = false;
            boolean paymentSuccess = false;
            double bill = 0;

            // While not all bills are paid, or user doesn't request to exit, then keep the application running
            while (!customerSet.isEmpty() && !quit)
            {
                // Print 3 options on screen
                System.out.println("\n-----------------------------------------------------------");
                System.out.println("Enter customer name to generate bill\nEnter 'L' to show list of customers with incomplete billing\nEnter 'Q' to quit");
                String input = kb.nextLine().toUpperCase();

                // If user enters "Q", then exit the applicaition
                if (input.equalsIgnoreCase("Q"))
                {
                    quit = true;
                }
                // If user enters "L", then show list of customers with incomplete billing
                else if (input.equalsIgnoreCase("L"))
                {
                    displayCustomersWithIncompleteBilling();
                }
                // If user enters other things, then assume it's a customer name
                else
                {
                    // Check if the customer name that user enters can be found in database
                    customerFound = ITollEventDAO.findCustomer(input);
                    // If not found, prompt the user
                    if (!customerFound)
                    {
                        System.out.println("\nCustomer not found");
                    }
                    // If found, proceed to billing
                    else
                    {
                        // Generate bill from database and print on screen
                        bill = ITollEventDAO.generateBill(input);
                        System.out.println("\nTotal bill for this period: €" + bill);

                        // Ask user if they would like to pay now
                        System.out.print("\nPay bill now?  [Y/N]:  ");
                        String option = kb.nextLine();
                        while (!(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("N")))
                        {
                            System.out.println("Invalid - [Y/N] only");
                            System.out.print("Pay bill now?  [Y/N]:  ");
                            option = kb.nextLine();
                        }

                        // If user wants to pay
                        if (option.equalsIgnoreCase("Y"))
                        {
                            // Change billing status of the toll events of the customer to "Complete"
                            paymentSuccess = ITollEventDAO.insertBillingStatus("Complete", input);
                            // If operation is success, prompt the user and remove this customer from the set
                            if (paymentSuccess)
                            {
                                System.out.println("\nBill paid");
                                customerSet.remove(input);
                            }
                            // If operation fails, prompt the user
                            else
                            {
                                System.out.println("\nSomething wrong with payment\nBill not paid");
                            }
                        }
                        // If user doesn't want to pay now, prompt the user and proceed
                        else
                        {
                            System.out.println("\nBill not paid");
                        }
                    }
                }
            }
            // If set is empty, this means all bills are paid by all customers
            if (customerSet.isEmpty())
            {
                System.out.println("\n----------------------------------------------------");
                System.out.println("All bills for this period are paid by all customers.");
            }
            System.out.println("\nThanks for using toll billing system, goodbye.");
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    // Display a set of customers with incomplete billing status
    private void displayCustomersWithIncompleteBilling()
    {
        try
        {
            Set<String> customerSet = ITollEventDAO.getAllCustomersWithIncompleteBilling();
            System.out.println("\nList of customers with incomplete billing:");
            if (customerSet.isEmpty())
            {
                System.out.println("Empty list");
            }
            else
            {
                for (String customer : customerSet)
                {
                    System.out.println(customer);
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }
}
