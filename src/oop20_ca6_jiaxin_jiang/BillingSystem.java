package oop20_ca6_jiaxin_jiang;

import java.util.Scanner;

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
    }
}
