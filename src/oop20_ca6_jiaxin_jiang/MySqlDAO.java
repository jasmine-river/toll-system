package oop20_ca6_jiaxin_jiang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class MySqlDAO
{

    public Connection getConnection() throws DAOException
    {

        String driver = "org.mariadb.jdbc.Driver";
        String url = "jdbc:mariadb://localhost/toll_database?useFractionalSeconds=true";
        String username = "root";
        String password = "";
        Connection con = null;

        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException ex1)
        {
            System.out.println("Failed to find driver class " + ex1.getMessage());
            System.exit(1);
        }
        catch (SQLException ex2)
        {
            System.out.println("Connection failed " + ex2.getMessage());
            System.exit(2);
        }
        return con;
    }

    public void freeConnection(Connection con) throws DAOException
    {
        try
        {
            if (con != null)
            {
                con.close();
                con = null;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }
    }
}
