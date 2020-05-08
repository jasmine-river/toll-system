package oop20_ca6_jiaxin_jiang;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class MySqlTollEventDAOTest
{

    public MySqlTollEventDAOTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAllCustomersWithIncompleteBilling method, of class
     * MySqlTollEventDAO.
     */
    @Test
    public void test1GetAllCustomersWithIncompleteBilling() throws DAOException
    {
        System.out.println("getAllCustomersWithIncompleteBilling (all customers have incomplete billing status)");
        MySqlTollEventDAO instance = new MySqlTollEventDAO();
        int expResult = 10;
        int result = instance.getAllCustomersWithIncompleteBilling().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of findCustomer method, of class MySqlTollEventDAO.
     */
    @Test
    public void test2FindCustomer() throws DAOException
    {
        System.out.println("findCustomer (customer exists)");
        String customerName = "Clara Oswald";
        MySqlTollEventDAO instance = new MySqlTollEventDAO();
        boolean expResult = true;
        boolean result = instance.findCustomer(customerName);
        assertEquals(expResult, result);

        System.out.println("findCustomer (customer not found)");
        customerName = "Harold Saxon";
        instance = new MySqlTollEventDAO();
        expResult = false;
        result = instance.findCustomer(customerName);
        assertEquals(expResult, result);
    }

    /**
     * Test of generateBill method, of class MySqlTollEventDAO.
     */
    @Test
    public void test3GenerateBill() throws DAOException
    {
        System.out.println("generateBill (customer exists)");
        String customerName = "Clara Oswald";
        MySqlTollEventDAO instance = new MySqlTollEventDAO();
        double expResult = 9.0;
        double result = instance.generateBill(customerName);
        assertEquals(expResult, result, 0.0);

        System.out.println("generateBill (customer not found)");
        customerName = "Harold Saxon";
        instance = new MySqlTollEventDAO();
        expResult = 0.0;
        result = instance.generateBill(customerName);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of insertBillingStatus method, of class MySqlTollEventDAO.
     */
    @Test
    public void test4InsertBillingStatus() throws DAOException
    {
        System.out.println("insertBillingStatus (customer exists)");
        String status = "Incomplete";
        String customerName = "Clara Oswald";
        MySqlTollEventDAO instance = new MySqlTollEventDAO();
        boolean expResult = true;
        boolean result = instance.insertBillingStatus(status, customerName);
        assertEquals(expResult, result);

        System.out.println("insertBillingStatus (customer not found)");
        status = "Complete";
        customerName = "Harold Saxon";
        instance = new MySqlTollEventDAO();
        expResult = false;
        result = instance.insertBillingStatus(status, customerName);
        assertEquals(expResult, result);
    }
}
