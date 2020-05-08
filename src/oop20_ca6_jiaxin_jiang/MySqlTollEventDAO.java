package oop20_ca6_jiaxin_jiang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MySqlTollEventDAO extends MySqlDAO implements TollEventDAOInterface
{

    @Override
    public boolean insertVehicleReg(String reg) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean success = false;

        try
        {
            con = this.getConnection();

            String query = "INSERT IGNORE INTO vehicles (Reg) VALUES (?)";
            ps = con.prepareStatement(query);

            ps.setString(1, reg);

            success = ps.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            throw new DAOException("insertVehicleReg() " + e.getMessage());
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
                throw new DAOException("insertVehicleReg() " + e.getMessage());
            }
        }
        return success;
    }

    @Override
    public Set<String> createVehicleLookUpTable() throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Set<String> lookUpTable = new HashSet();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM vehicles";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("Reg");
                lookUpTable.add(reg);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("createVehicleLookUpTable() " + e.getMessage());
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
                throw new DAOException("createVehicleLookUpTable() " + e.getMessage());
            }
        }
        return lookUpTable;
    }

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
    public List<TollEvent> getAllTollEvents() throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TollEvent> tList = new ArrayList();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM toll_event ORDER BY timestamp";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                long imageId = rs.getLong("image_id");

                Timestamp ts = rs.getTimestamp("timestamp");
                Instant inst = ts.toInstant();
                String dateTime = inst.toString();

                TollEvent t = new TollEvent(reg, imageId, dateTime);

                tList.add(t);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllTollEvents() " + e.getMessage());
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
                throw new DAOException("getAllTollEvents() " + e.getMessage());
            }
        }
        return tList;
    }

    @Override
    public List<TollEvent> getAllTollEventsWithReg(String registration) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TollEvent> tList = new ArrayList();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM toll_event WHERE vehicle_registration = ? ORDER BY timestamp";
            ps = con.prepareStatement(query);

            ps.setString(1, registration);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                long imageId = rs.getLong("image_id");

                Timestamp ts = rs.getTimestamp("timestamp");
                Instant inst = ts.toInstant();
                String dateTime = inst.toString();

                TollEvent t = new TollEvent(reg, imageId, dateTime);

                tList.add(t);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllTollEventsWithReg() " + e.getMessage());
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
                throw new DAOException("getAllTollEventsWithReg() " + e.getMessage());
            }
        }
        return tList;
    }

    @Override
    public List<TollEvent> getAllTollEventsSinceDateTime(String timestamp) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TollEvent> tList = new ArrayList();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM toll_event WHERE timestamp > ? ORDER BY timestamp";
            ps = con.prepareStatement(query);

            ps.setString(1, timestamp);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                long imageId = rs.getLong("image_id");

                Timestamp ts = rs.getTimestamp("timestamp");
                Instant inst = ts.toInstant();
                String dateTime = inst.toString();

                TollEvent t = new TollEvent(reg, imageId, dateTime);

                tList.add(t);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllTollEventsSinceDateTime() " + e.getMessage());
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
                throw new DAOException("getAllTollEventsSinceDateTime() " + e.getMessage());
            }
        }
        return tList;
    }

    @Override
    public List<TollEvent> getAllTollEventsBetweenDateTimes(String timestamp1, String timestamp2) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TollEvent> tList = new ArrayList();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM toll_event WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp";
            ps = con.prepareStatement(query);

            ps.setString(1, timestamp1);
            ps.setString(2, timestamp2);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                long imageId = rs.getLong("image_id");

                Timestamp ts = rs.getTimestamp("timestamp");
                Instant inst = ts.toInstant();
                String dateTime = inst.toString();

                TollEvent t = new TollEvent(reg, imageId, dateTime);

                tList.add(t);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllTollEventsBetweenDateTimes() " + e.getMessage());
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
                throw new DAOException("getAllTollEventsBetweenDateTimes() " + e.getMessage());
            }
        }
        return tList;
    }

    @Override
    public Set<String> getAllRegFromToll() throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Set<String> regSet = new TreeSet();

        try
        {
            con = this.getConnection();

            String query = "SELECT DISTINCT vehicle_registration FROM toll_event";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                regSet.add(reg);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllRegFromToll() " + e.getMessage());
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
                throw new DAOException("getAllRegFromToll() " + e.getMessage());
            }
        }
        return regSet;
    }

    @Override
    public Map<String, ArrayList<TollEvent>> getAllTollEventsAsMap() throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, ArrayList<TollEvent>> tollEventMap = new HashMap();

        try
        {
            con = this.getConnection();

            String query = "SELECT vehicle_registration, image_id, timestamp FROM toll_event";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next())
            {
                String reg = rs.getString("vehicle_registration");
                long imageId = rs.getLong("image_id");

                Timestamp ts = rs.getTimestamp("timestamp");
                Instant inst = ts.toInstant();
                String dateTime = inst.toString();

                TollEvent t = new TollEvent(reg, imageId, dateTime);

                ArrayList<TollEvent> tList = tollEventMap.get(reg);

                if (tList == null)
                {
                    tList = new ArrayList();
                }

                tList.add(t);
                tollEventMap.put(reg, tList);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("getAllTollEventsAsMap() " + e.getMessage());
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
                throw new DAOException("getAllTollEventsAsMap() " + e.getMessage());
            }
        }
        return tollEventMap;
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

            String query = "SELECT SUM(cost) AS bill FROM toll_event " +
                           "NATURAL JOIN customer_vehicle " +
                           "NATURAL JOIN customer " +
                           "NATURAL JOIN vehicle " +
                           "NATURAL JOIN vehicle_type_cost " +
                           "WHERE customer_name = ? " +
                           "AND timestamp BETWEEN SUBDATE(CURDATE(), INTERVAL 1 MONTH) " +
                           "AND NOW();";
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
}
