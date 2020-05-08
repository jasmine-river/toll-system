package oop20_ca6_jiaxin_jiang;

import java.util.Set;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public interface TollEventDAOInterface
{

    public boolean insertTollEvent(String reg, long imageId, String timestamp) throws DAOException;

    public Set<String> getAllCustomersWithIncompleteBilling() throws DAOException;

    public boolean findCustomer(String customerName) throws DAOException;

    public double generateBill(String customerName) throws DAOException;

    public boolean insertBillingStatus(String status, String customerName) throws DAOException;
}
