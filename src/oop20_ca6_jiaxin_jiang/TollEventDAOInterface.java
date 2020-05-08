package oop20_ca6_jiaxin_jiang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TollEventDAOInterface
{

    public boolean insertVehicleReg(String reg) throws DAOException;

    public Set<String> createVehicleLookUpTable() throws DAOException;

    public boolean insertTollEvent(String reg, long imageId, String timestamp) throws DAOException;

    public List<TollEvent> getAllTollEvents() throws DAOException;
    
    public List<TollEvent> getAllTollEventsWithReg(String registration) throws DAOException;
    
    public List<TollEvent> getAllTollEventsSinceDateTime(String timestamp) throws DAOException;
    
    public List<TollEvent> getAllTollEventsBetweenDateTimes(String timestamp1, String timestamp2) throws DAOException;

    public Set<String> getAllRegFromToll() throws DAOException;
    
    public Map<String, ArrayList<TollEvent>> getAllTollEventsAsMap() throws DAOException;
    
    public double generateBill(String customerName) throws DAOException;
    
    public boolean insertBillingStatus() throws DAOException;
}
