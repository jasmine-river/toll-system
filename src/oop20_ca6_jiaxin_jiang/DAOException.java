package oop20_ca6_jiaxin_jiang;

import java.sql.SQLException;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class DAOException extends SQLException
{

    public DAOException()
    {
    }

    public DAOException(String message)
    {
        super(message);
    }
}
