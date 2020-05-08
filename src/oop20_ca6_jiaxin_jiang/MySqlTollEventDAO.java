package oop20_ca6_jiaxin_jiang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class MySqlTollEventDAO extends MySqlDAO implements TollEventDAOInterface
{

    @Override
    public boolean insertTollEvent(String reg, long imageId, String timestamp) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean success = false;

        try
        {
            con = this.getConnection();

            String query = "INSERT IGNORE INTO toll_event (vehicle_registration, image_id, timestamp) VALUES (?, ?, ?)";
            ps = con.prepareStatement(query);

            String dateTime = timestamp;
            Instant inst = Instant.parse(dateTime);
            Timestamp ts = Timestamp.from(inst);

            ps.setString(1, reg);
            ps.setLong(2, imageId);
            ps.setTimestamp(3, ts);

            success = ps.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            throw new DAOException("insertTollEvent() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DAOException("insertTollEvent() " + e.getMessage());
            }
        }
        return success;
    }

    @Override
    public Set<String> getAllCustomersWithIncompleteBilling() throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Set<String> customerSet = new HashSet();

        try
        {
            con = this.getConnection();

            String query = "SELECT customer_name FROM toll_event "
                    + "NATURAL JOIN customer_vehicle "
                    + "NATURAL JOIN customer "
                    + "NATURAL JOIN vehicle "
                    + "WHERE billing_status = 'Incomplete'";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String customerName = rs.getString("customer_name").toUpperCase();
                customerSet.add(customerName);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllCustomersWithIncompleteBilling() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DAOException("getAllCustomersWithIncompleteBilling() " + e.getMessage());
            }
        }
        return customerSet;
    }

    @Override
    public boolean findCustomer(String customerName) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean found = false;

        try
        {
            con = this.getConnection();

            String query = "SELECT customer_name FROM customer WHERE customer_name = ?";
            ps = con.prepareStatement(query);

            ps.setString(1, customerName);

            rs = ps.executeQuery();

            if (rs.next())
            {
                found = true;
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("findCustomer() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DAOException("findCustomer() " + e.getMessage());
            }
        }
        return found;
    }

    @Override
    public double generateBill(String customerName) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        double bill = 0;

        try
        {
            con = this.getConnection();

            String query = "SELECT SUM(cost) AS bill FROM toll_event "
                    + "NATURAL JOIN customer_vehicle "
                    + "NATURAL JOIN customer "
                    + "NATURAL JOIN vehicle "
                    + "NATURAL JOIN vehicle_type_cost "
                    + "WHERE customer_name = ? "
                    + "AND timestamp BETWEEN SUBDATE(CURDATE(), INTERVAL 3 MONTH) "
                    + "AND NOW()";
            ps = con.prepareStatement(query);

            ps.setString(1, customerName);

            rs = ps.executeQuery();

            if (rs.next())
            {
                bill = rs.getDouble("bill");
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("generateBill() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DAOException("generateBill() " + e.getMessage());
            }
        }
        return bill;
    }

    @Override
    public boolean insertBillingStatus(String status, String customerName) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean success = false;

        try
        {
            con = this.getConnection();

            String query = "UPDATE toll_event "
                    + "SET billing_status = ? "
                    + "WHERE vehicle_registration IN "
                    + "(SELECT vehicle_registration FROM toll_event "
                    + "NATURAL JOIN customer_vehicle "
                    + "NATURAL JOIN customer "
                    + "NATURAL JOIN vehicle "
                    + "WHERE customer_name = ?)";
            ps = con.prepareStatement(query);

            ps.setString(1, status);
            ps.setString(2, customerName);

            success = ps.executeUpdate() >= 1;
        }
        catch (SQLException e)
        {
            throw new DAOException("insertBillingStatus() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DAOException("insertBillingStatus() " + e.getMessage());
            }
        }
        return success;
    }
}
