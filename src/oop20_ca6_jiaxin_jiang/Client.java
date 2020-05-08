package oop20_ca6_jiaxin_jiang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class Client
{

    private final Scanner kb = new Scanner(System.in);

    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }

    private void start()
    {
        boolean quit = false;
        int option;

        // A set of registration numbers, received from Server
        Set<String> regSet = getRegSet();

        ArrayList<String> mainMenu = new ArrayList();
        mainMenu.add("Heartbeat");
        mainMenu.add("Process Toll Events");
        mainMenu.add("Close Session");

        do
        {
            printMenu("\nMAIN MENU", mainMenu);
            try
            {
                System.out.print("\nChoose One Option: ");
                option = kb.nextInt();
                kb.nextLine();
                System.out.println("\nYour Option Is: " + mainMenu.get(option - 1) + "\n");
                switch (option)
                {
                    case 1:
                        heartbeat();
                        backToMenu();
                        break;
                    case 2:
                        processTollEvents(regSet);
                        backToMenu();
                        break;
                    case 3:
                        closeSession();
                        quit = true;
                        System.out.println("Goodbye");
                        break;
                    default:
                        break;
                }
            }
            catch (InputMismatchException e)
            {
                kb.nextLine();
                System.out.println("INVALID - [1,3] ONLY");
                backToMenu();
            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println("INVALID - [1,3] ONLY");
                backToMenu();
            }
        } while (!quit);
    }

    private void printMenu(String menuHeading, ArrayList<String> menu)
    {
        System.out.println(menuHeading);
        for (int i = 0; i < menu.size(); i++)
        {
            System.out.println((i + 1) + ". " + menu.get(i));
        }
    }

    private void backToMenu()
    {
        System.out.print("\nPress 'enter' to return to menu ");
        String temp = kb.nextLine();
    }

    // Get a set of registration numbers from Server
    private Set<String> getRegSet()
    {
        Set<String> regSet = new HashSet();
        try
        {
            try (Socket socket = new Socket("localhost", 50000))
            {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                JsonObject jsonRootObject
                        = Json.createObjectBuilder()
                                .add("PacketType", "GetRegisteredVehicles")
                                .build();

                String message = jsonRootObject.toString();

                OutputStream os = socket.getOutputStream();

                try (PrintWriter socketWriter = new PrintWriter(os, true))
                {
                    socketWriter.println(message);

                    JsonReader reader = Json.createReader(socket.getInputStream());
                    JsonObject object = reader.readObject();

                    String packetType = object.getString("PacketType");
                    System.out.println(packetType);

                    JsonArray regArray = object.getJsonArray("Vehicles");

                    // Add registrations from JSON array to local set
                    for (int i = 0; i < regArray.size(); i++)
                    {
                        String reg = regArray.getString(i);
                        regSet.add(reg);
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return regSet;
    }

    // Get Heartbeat response from Server
    private void heartbeat()
    {
        try
        {
            try (Socket socket = new Socket("localhost", 50000))
            {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                JsonObject jsonRootObject
                        = Json.createObjectBuilder()
                                .add("PacketType", "Heartbeat")
                                .build();

                String message = jsonRootObject.toString();

                OutputStream os = socket.getOutputStream();

                try (PrintWriter socketWriter = new PrintWriter(os, true))
                {
                    socketWriter.println(message);

                    JsonReader reader = Json.createReader(socket.getInputStream());
                    JsonObject object = reader.readObject();

                    String packetType = object.getString("PacketType");
                    System.out.println(packetType);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
    }

    // Read in toll events from Toll-Events.csv file, and process them one by one
    private void processTollEvents(Set<String> regSet)
    {
        try (Scanner in = new Scanner(new File("data/Toll-Events.csv")))
        {
            in.useDelimiter("[;\n\r]+");
            List<TollEvent> tollEventList = new ArrayList();
            boolean success;

            // Read in toll events and store them locally in a list
            while (in.hasNext() && in.hasNextLine())
            {
                String reg = in.next();
                long imageId = in.nextLong();
                String timestamp = in.next();

                TollEvent t = new TollEvent(reg, imageId, timestamp);
                tollEventList.add(t);
            }

            // Process toll events one by one from the list
            int i = 0;
            while (i < tollEventList.size())
            {
                TollEvent t = tollEventList.get(i);
                // Check if toll event registration is valid, which means it can be found in the set of registrations
                if (regSet.contains(t.getReg()))
                {
                    // If so, request to register the valid toll event
                    success = regValidEvent(t.getReg(), t.getImageId(), t.getTimestamp());
                    if (success)
                    {
                        // Once successfully processed, the locally stored event can be deleted
                        tollEventList.remove(t);
                    }
                    else
                    {
                        i++;
                    }
                }
                else
                {
                    // If not, request to register the invalid toll event
                    success = regInvalidEvent(t.getReg(), t.getImageId(), t.getTimestamp());
                    if (success)
                    {
                        // Once successfully processed, the locally stored event can be deleted
                        tollEventList.remove(t);
                    }
                    else
                    {
                        i++;
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Toll-Events.csv file not found");
        }
    }

    // Request the Server to register valid toll event
    private boolean regValidEvent(String reg, long imageId, String timestamp)
    {
        boolean success = false;
        try
        {
            try (Socket socket = new Socket("localhost", 50000))
            {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                JsonObject jsonRootObject
                        = Json.createObjectBuilder()
                                .add("PacketType", "RegisterValidTollEvent")
                                .add("TollBoothID", "TB_M50")
                                .add("Vehicle Registration", reg)
                                .add("Vehicle Image ID", imageId)
                                .add("LocalDateTime", timestamp)
                                .build();

                String message = jsonRootObject.toString();

                OutputStream os = socket.getOutputStream();

                try (PrintWriter socketWriter = new PrintWriter(os, true))
                {
                    socketWriter.println(message);

                    JsonReader reader = Json.createReader(socket.getInputStream());
                    JsonObject object = reader.readObject();

                    String packetType = object.getString("PacketType");
                    System.out.println(packetType);

                    if (packetType.equals("RegisteredValidTollEvent"))
                    {
                        success = true;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return success;
    }

    // Request the Server to register invalid toll event
    private boolean regInvalidEvent(String reg, long imageId, String timestamp)
    {
        boolean success = false;
        try
        {
            try (Socket socket = new Socket("localhost", 50000))
            {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                JsonObject jsonRootObject
                        = Json.createObjectBuilder()
                                .add("PacketType", "RegisterInvalidTollEvent")
                                .add("TollBoothID", "TB_M50")
                                .add("Vehicle Registration", reg)
                                .add("Vehicle Image ID", imageId)
                                .add("LocalDateTime", timestamp)
                                .build();

                String message = jsonRootObject.toString();

                OutputStream os = socket.getOutputStream();

                try (PrintWriter socketWriter = new PrintWriter(os, true))
                {
                    socketWriter.println(message);

                    JsonReader reader = Json.createReader(socket.getInputStream());
                    JsonObject object = reader.readObject();

                    String packetType = object.getString("PacketType");
                    System.out.println(packetType);

                    if (packetType.equals("RegisteredInvalidTollEvent"))
                    {
                        success = true;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return success;
    }

    // Request the Server to close the current session
    private void closeSession()
    {
        try
        {
            try (Socket socket = new Socket("localhost", 50000))
            {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                JsonObject jsonRootObject
                        = Json.createObjectBuilder()
                                .add("PacketType", "Close")
                                .build();

                String message = jsonRootObject.toString();

                OutputStream os = socket.getOutputStream();

                try (PrintWriter socketWriter = new PrintWriter(os, true))
                {
                    socketWriter.println(message);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
    }
}
