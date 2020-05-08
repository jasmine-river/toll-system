package oop20_ca6_jiaxin_jiang;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author headl
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
        // get list of customers with bill status marked as incomplete
        // while not all bills are paid and not quit - ask user for name to generate bill
        // when bill generated, ask user to pay [y/n]
        // if [y], mark the bill as completed in toll event table; delete this user from customer list for asking
        // else, proceed to next one

        try
        {
            System.out.println("Welcome to the toll billing system!");
            Set<String> customerSet = ITollEventDAO.getAllCustomersWithIncompleteBilling();
            boolean quit = false;
            boolean customerFound = false;
            boolean paymentSuccess = false;
            double bill = 0;
            while (!customerSet.isEmpty() && !quit)
            {
                System.out.println("\n-----------------------------------------------------------");
                System.out.println("Enter customer name to generate bill\nEnter 'L' to show list of customers with incomplete billing\nEnter 'Q' to quit");
                String input = kb.nextLine().toUpperCase();
                if (input.equalsIgnoreCase("Q"))
                {
                    quit = true;
                }
                else if(input.equalsIgnoreCase("L"))
                {
                    displayCustomersWithIncompleteBilling();
                }
                else
                {
                    customerFound = ITollEventDAO.findCustomer(input);
                    if (!customerFound)
                    {
                        System.out.println("\nCustomer not found");
                    }
                    else
                    {
                        bill = ITollEventDAO.generateBill(input);
                        System.out.println("\nTotal bill for this period: €" + bill);
                        System.out.print("\nPay bill now?  [Y/N]:  ");
                        String option = kb.nextLine();
                        while (!(option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("N")))
                        {
                            System.out.println("Invalid - [Y/N] only");
                            System.out.print("Pay bill now?  [Y/N]:  ");
                            option = kb.nextLine();
                        }
                        if (option.equalsIgnoreCase("Y"))
                        {
                            paymentSuccess = ITollEventDAO.insertBillingStatus("Complete", input);
                            if (paymentSuccess)
                            {
                                System.out.println("\nBill paid");
                                customerSet.remove(input);
                            }
                            else
                            {
                                System.out.println("\nSomething wrong with payment\nBill not paid");
                            }
                        }
                        else
                        {
                            System.out.println("\nBill not paid");
                        }
                    }
                }
            }
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
