package oop20_ca6_jiaxin_jiang;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class MainApp
{

    private final TollEventDAOInterface ITollEventDAO = new MySqlTollEventDAO();
    private final Scanner kb = new Scanner(System.in);

    public static void main(String[] args)
    {
        MainApp app = new MainApp();
        app.run();
    }

    private void run()
    {
        boolean quit = false;
        int option;
        Set<String> lookUpTable = new HashSet();
        Map<String, ArrayList<TollEvent>> tollEventMap = new HashMap();

        ArrayList<String> mainMenu = new ArrayList();
        mainMenu.add("EXIT");
        mainMenu.add("Load Reg From Database Into Look-up Table");
        mainMenu.add("Process Toll Events");
        mainMenu.add("Batch Write Toll Events Into Database");
        mainMenu.add("Get All Toll Events");
        mainMenu.add("Get All Toll Events Given Reg");
        mainMenu.add("Get All Toll Events Since Date-time");
        mainMenu.add("Get All Toll Events Between Start Date-time And Finish Date-time");
        mainMenu.add("Get All Reg Passing Through Toll");
        mainMenu.add("Get All Toll Events As Map");

        System.out.println("WELCOME TO THE TOLL EVENT SYSTEM");

        do
        {
            printMenu("\nMAIN MENU", mainMenu);
            try
            {
                System.out.print("\nChoose One Option: ");
                option = kb.nextInt();
                kb.nextLine();
                System.out.println("\nYour Option Is: " + mainMenu.get(option) + "\n");
                switch (option)
                {
                    case 0:
                        quit = true;
                        System.out.println("Goodbye");
                        break;
                    case 1:
                        lookUpTable = createLookupTable();
                        backToMenu();
                        break;
                    case 2:
                        tollEventMap = populateTollEventMapAndInvalidList(lookUpTable);
                        backToMenu();
                        break;
                    case 3:
                        uploadTollEvents(tollEventMap);
                        backToMenu();
                        break;
                    case 4:
                        getAllTollEvents();
                        backToMenu();
                        break;
                    case 5:
                        getAllTollEventsWithReg();
                        backToMenu();
                        break;
                    case 6:
                        getAllTollEventsSinceDateTime();
                        backToMenu();
                        break;
                    case 7:
                        getAllTollEventsBetweenDateTimes();
                        backToMenu();
                        break;
                    case 8:
                        getAllRegFromToll();
                        backToMenu();
                        break;
                    case 9:
                        getAllTollEventsAsMap();
                        backToMenu();
                        break;
                    default:
                        break;
                }
            }
            catch (InputMismatchException e)
            {
                kb.nextLine();
                System.out.println("INVALID - [0,9] ONLY");
                backToMenu();
            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println("INVALID - [0,9] ONLY");
                backToMenu();
            }
        } while (!quit);
    }

    private void printMenu(String menuHeading, ArrayList<String> menu)
    {
        System.out.println(menuHeading);
        for (int i = 0; i < menu.size(); i++)
        {
            System.out.println(i + ". " + menu.get(i));
        }
    }

    private void backToMenu()
    {
        System.out.print("\nPress 'enter' to return to menu ");
        String temp = kb.nextLine();
    }

    /**
     * Populate vehicles database table using Vehicles.csv file. NOTE: this
     * method should be called ONLY when populating the "vehicles" database
     * table, and NOT used in the rest of the application.
     */
    private void populateVehiclesTable()
    {
        try (Scanner in = new Scanner(new File("data/Vehicles.csv")))
        {
            boolean success = true;
            boolean allSuccess = true;
            while (in.hasNext())
            {
                String reg = in.next();
                success = ITollEventDAO.insertVehicleReg(reg);
                if (!success)
                {
                    allSuccess = false;
                    System.err.println("Reg nums insertion failure (records already exist)");
                }
            }
            if (allSuccess)
            {
                System.out.println("All reg nums inserted successfully");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Vehicles.csv file not found");
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create in-memory lookup table
     *
     * @return A HashSet of reg
     */
    private Set<String> createLookupTable()
    {
        Set<String> lookUpTable = new HashSet();
        try
        {
            lookUpTable = ITollEventDAO.createVehicleLookUpTable();
            if (lookUpTable.isEmpty())
            {
                System.out.println("Look-up table loading failure");
            }
            else
            {
                System.out.println("Look-up table loading success");
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
        return lookUpTable;
    }

    /**
     * Read in Toll-Events.csv file; populate toll event map; populate invalid
     * reg arraylist
     *
     * @param lookUpTable A HashSet of reg
     * @return A HashMap of reg (as key) and a list of toll events (as value)
     */
    private Map<String, ArrayList<TollEvent>> populateTollEventMapAndInvalidList(Set<String> lookUpTable)
    {
        Map<String, ArrayList<TollEvent>> tollEventMap = new HashMap();

        try (Scanner in = new Scanner(new File("data/Toll-Events.csv")))
        {
            List<String> invalidRegList = new ArrayList();

            in.useDelimiter("[;\n\r]+");

            while (in.hasNext() && in.hasNextLine())
            {
                String reg = in.next();
                long imageId = in.nextLong();
                String timestamp = in.next();

                if (lookUpTable.contains(reg))
                {
                    TollEvent t = new TollEvent(reg, imageId, timestamp);

                    ArrayList<TollEvent> tList = tollEventMap.get(reg);

                    if (tList == null)
                    {
                        tList = new ArrayList();
                    }

                    tList.add(t);
                    tollEventMap.put(reg, tList);
                }
                else
                {
                    invalidRegList.add(reg);
                }
            }

            if (tollEventMap.isEmpty())
            {
                System.out.println("Toll events processing not completed");
            }
            else
            {
                System.out.println("Toll events processing completed");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Toll-Events.csv file not found");
        }
        return tollEventMap;
    }

    /**
     * Write all toll events to toll_events database table as a batch job
     *
     * @param tollEventMap A HashMap of reg (as key) and a list of toll events
     * (as value)
     */
    private void uploadTollEvents(Map<String, ArrayList<TollEvent>> tollEventMap)
    {
        try
        {
            boolean success = true;
            boolean allSuccess = true;
            Set<String> keyset = tollEventMap.keySet();
            for (String key : keyset)
            {
                ArrayList<TollEvent> tList = tollEventMap.get(key);
                for (TollEvent t : tList)
                {
                    String reg = t.getReg();
                    long imageId = t.getImageId();
                    String timestamp = t.getTimestamp();
                    success = ITollEventDAO.insertTollEvent(reg, imageId, timestamp);
                    if (!success)
                    {
                        allSuccess = false;
                        System.err.println("Toll events insertion failure (records already exist)");
                    }
                }
            }
            if (allSuccess)
            {
                System.out.println("All toll events inserted successfully");
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all toll event details
     */
    private void getAllTollEvents()
    {
        try
        {
            List<TollEvent> tList = ITollEventDAO.getAllTollEvents();
            if (tList.isEmpty())
            {
                System.out.println("Empty set");
            }
            else
            {
                System.out.println("Reg               ImageID        Timestamp");
                System.out.println("-----------------------------------------------------------");
                for (TollEvent t : tList)
                {
                    System.out.printf("%-18s%-15d%-30s", t.getReg(), t.getImageId(), t.getTimestamp());
                    System.out.println();
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all toll event details given a vehicle registration
     */
    private void getAllTollEventsWithReg()
    {
        try
        {
            System.out.print("Enter reg: ");
            String reg = kb.nextLine();
            List<TollEvent> tList = ITollEventDAO.getAllTollEventsWithReg(reg);
            if (tList.isEmpty())
            {
                System.out.println("\nEmpty set");
            }
            else
            {
                System.out.println("\nReg               ImageID        Timestamp");
                System.out.println("-----------------------------------------------------------");
                for (TollEvent t : tList)
                {
                    System.out.printf("%-18s%-15d%-30s", t.getReg(), t.getImageId(), t.getTimestamp());
                    System.out.println();
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all toll event details since a specified date-time
     */
    private void getAllTollEventsSinceDateTime()
    {
        try
        {
            System.out.println("Enter start date-time (in the format of yyyy-MM-dd HH:mm:ss): ");
            String datetime = kb.nextLine();
            if (!isValidTimestamp(datetime))
            {
                System.out.println("\nInvalid date-time format");
            }
            else
            {
                List<TollEvent> tList = ITollEventDAO.getAllTollEventsSinceDateTime(datetime);
                if (tList.isEmpty())
                {
                    System.out.println("\nEmpty set");
                }
                else
                {
                    System.out.println("\nReg               ImageID        Timestamp");
                    System.out.println("-----------------------------------------------------------");
                    for (TollEvent t : tList)
                    {
                        System.out.printf("%-18s%-15d%-30s", t.getReg(), t.getImageId(), t.getTimestamp());
                        System.out.println();
                    }
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all toll event details between a start date-time and a finish
     * date-time
     */
    private void getAllTollEventsBetweenDateTimes()
    {
        try
        {
            System.out.println("Enter start date-time (in the format of yyyy-MM-dd HH:mm:ss): ");
            String datetime1 = kb.nextLine();
            System.out.println("Enter finish date-time (in the format of yyyy-MM-dd HH:mm:ss): ");
            String datetime2 = kb.nextLine();

            Timestamp ts1 = Timestamp.valueOf(datetime1);
            Timestamp ts2 = Timestamp.valueOf(datetime2);

            if (!isValidTimestamp(datetime1) || !isValidTimestamp(datetime2))
            {
                System.out.println("\nAt least one date-time entered is of invalid format");
            }
            else if (ts1.after(ts2))
            {
                System.out.println("\nStart date-time cannot be later than finish date-time");
            }
            else
            {
                List<TollEvent> tList = ITollEventDAO.getAllTollEventsBetweenDateTimes(datetime1, datetime2);
                if (tList.isEmpty())
                {
                    System.out.println("\nEmpty set");
                }
                else
                {
                    System.out.println("\nReg               ImageID        Timestamp");
                    System.out.println("-----------------------------------------------------------");
                    for (TollEvent t : tList)
                    {
                        System.out.printf("%-18s%-15d%-30s", t.getReg(), t.getImageId(), t.getTimestamp());
                        System.out.println();
                    }
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all registrations that passed through the toll
     */
    private void getAllRegFromToll()
    {
        try
        {
            Set<String> regSet = ITollEventDAO.getAllRegFromToll();
            if (regSet.isEmpty())
            {
                System.out.println("Empty set");
            }
            else
            {
                System.out.println("Reg");
                System.out.println("-----------");
                for (String reg : regSet)
                {
                    System.out.println(reg);
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get all toll event details returned as Map
     */
    private void getAllTollEventsAsMap()
    {
        try
        {
            Map<String, ArrayList<TollEvent>> tollEventMap = ITollEventDAO.getAllTollEventsAsMap();
            if (tollEventMap.isEmpty())
            {
                System.out.println("Empty set");
            }
            else
            {
                Set<String> keyset = tollEventMap.keySet();
                System.out.println("Reg               ImageID        Timestamp");
                System.out.println("-----------------------------------------------------------");
                for (String key : keyset)
                {
                    ArrayList<TollEvent> tList = tollEventMap.get(key);
                    for (TollEvent t : tList)
                    {
                        System.out.printf("%-18s%-15d%-30s", t.getReg(), t.getImageId(), t.getTimestamp());
                        System.out.println();
                    }
                }
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check if the date-time that user input is of valid format
     *
     * REFERENCE:
     * https://stackoverflow.com/questions/5902310/how-do-i-validate-a-timestamp
     *
     * @param inputString user input date-time
     * @return true if valid, false if not
     */
    private boolean isValidTimestamp(String inputString)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            format.parse(inputString);
            Pattern p = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,6}$");
            return p.matcher(inputString).matches();
        }
        catch (ParseException e)
        {
            return false;
        }
    }
}
