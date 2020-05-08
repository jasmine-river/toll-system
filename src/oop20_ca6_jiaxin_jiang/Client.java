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
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Client
{
    private final Scanner kb = new Scanner(System.in);

    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }

    public void start()
    {
       
        boolean quit = false;
        int option;
        
        Set<String> vehicleRegSet=getRegSet();

        ArrayList<String> mainMenu = new ArrayList();
        mainMenu.add("HeartBeat");
        mainMenu.add("ProcessTollEvents");
        mainMenu.add("CloseSession");


        do
        {
            printMenu("\nMAIN MENU", mainMenu);
            try
            {
                System.out.print("\nChoose One Option: ");
                option = kb.nextInt();
                kb.nextLine();
                System.out.println("\nYour Option Is: " + mainMenu.get(option-1) + "\n");
                switch (option)
                {
                    case 1:
                        heartBeat();
                        backToMenu();
                        break;
                    case 2:
                        processTollEvents(vehicleRegSet);
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
    
    private Set<String> getRegSet()
    {
        Set<String> regSet=new HashSet();
        try
        {
            Socket socket = new Socket("localhost", 50000);
            
            JsonBuilderFactory factory = Json.createBuilderFactory(null);

            JsonObject jsonRootObject
                    = Json.createObjectBuilder()
                            .add("PacketType", "GetRegisteredVehicles")
                            .build();

            String message = jsonRootObject.toString();
            
            
            OutputStream os = socket.getOutputStream();

            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            socketWriter.println(message);  // write command to socket
            
              
            JsonReader reader = Json.createReader(socket.getInputStream());
            JsonObject object = reader.readObject();
            
            String packetType = object.getString("PacketType");
            System.out.println("PacketType: " + packetType);
            System.out.println();
             
            JsonArray regArray = object.getJsonArray("Vehicles");
             
            for (int i = 0; i < regArray.size(); i++)
            {
                String reg = regArray.getString(i);
                regSet.add(reg);
            }          
              
             socketWriter.close();
             socket.close();
             
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return regSet;
    }
    
    private void heartBeat()
    {
        try
        {
            Socket socket = new Socket("localhost", 50000);
            
            JsonBuilderFactory factory = Json.createBuilderFactory(null);

            JsonObject jsonRootObject
                    = Json.createObjectBuilder()
                            .add("PacketType", "Heartbeat")
                            .build();

            String message = jsonRootObject.toString();
            
            
            OutputStream os = socket.getOutputStream();

            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            socketWriter.println(message);  // write command to socket
            
            
            JsonReader reader = Json.createReader(socket.getInputStream());
            JsonObject object = reader.readObject();
            
            String packetType = object.getString("PacketType");
            System.out.println("PacketType: " + packetType);
             
             socketWriter.close();
             socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        
    }
    
    private void processTollEvents(Set<String> regList)
    {
        try (Scanner in = new Scanner(new File("data/Toll-Events.csv")))
        {
            in.useDelimiter("[;\n\r]+");
            ArrayList<TollEvent> tollEventList = new ArrayList();
            boolean success;

            while (in.hasNext() && in.hasNextLine())
            {
                String reg = in.next();
                long imageId = in.nextLong();
                String timestamp = in.next();

                TollEvent t = new TollEvent(reg, imageId, timestamp);
                tollEventList.add(t);
            }
            
            int i=0;
            while(i<tollEventList.size())
            {
                TollEvent t = tollEventList.get(i);
                if (regList.contains(t.getReg()))
                {
                    success=regValidEvent(t.getReg(),t.getImageId(),t.getTimestamp());
                    if(success)
                    {
                        tollEventList.remove(t);
                    }
                    else
                    {
                        i++;
                    }
                }
                else
                {
                    success=regInvalidEvent(t.getReg(),t.getImageId(),t.getTimestamp());
                    if(success)
                    {
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
    
    private boolean regValidEvent(String reg,long imageId,String timestamp)
    {
        boolean success=false;
        try
        {
            Socket socket = new Socket("localhost", 50000);
            
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

            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            socketWriter.println(message);  // write command to socket
            
              
            JsonReader reader = Json.createReader(socket.getInputStream());
            JsonObject object = reader.readObject();
            
            String packetType = object.getString("PacketType");
            System.out.println("PacketType: " + packetType);
            
            if(packetType.equals("RegisteredValidTollEvent"))
            {
                success=true;
            }
          
             socketWriter.close();
             socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return success;
    }
    
    private boolean regInvalidEvent(String reg,long imageId,String timestamp)
    {
        boolean success=false;
        try
        {
            Socket socket = new Socket("localhost", 50000);
            
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

            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            socketWriter.println(message);  // write command to socket
            
              
            JsonReader reader = Json.createReader(socket.getInputStream());
            JsonObject object = reader.readObject();
            
            String packetType = object.getString("PacketType");
            System.out.println("PacketType: " + packetType);
            
            if(packetType.equals("RegisteredInvalidTollEvent"))
            {
                success=true;
            }
          
             socketWriter.close();
             socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        return success;
    }
    
    private void closeSession()
    {
        try
        {
            Socket socket = new Socket("localhost", 50000);
            
            JsonBuilderFactory factory = Json.createBuilderFactory(null);

            JsonObject jsonRootObject
                    = Json.createObjectBuilder()
                            .add("PacketType", "Close")
                            .build();

            String message = jsonRootObject.toString();
            
            
            OutputStream os = socket.getOutputStream();

            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            socketWriter.println(message);  // write command to socket
            
            
             socketWriter.close();
             socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
        
    }
    
    
    
    

    private void printMenu(String menuHeading, ArrayList<String> menu)
    {
        System.out.println(menuHeading);
        for (int i = 0; i < menu.size(); i++)
        {
            System.out.println((i+1) + ". " + menu.get(i));
        }
    }

    private void backToMenu()
    {
        System.out.print("\nPress 'enter' to return to menu ");
        String temp = kb.nextLine();
    }
}
