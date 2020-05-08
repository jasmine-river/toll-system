package oop20_ca6_jiaxin_jiang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Jiaxin Jiang, D00217246
 */
public class Server
{

    private final TollEventDAOInterface ITollEventDAO = new MySqlTollEventDAO();
    private final List<String> invalidRegList = new ArrayList();
    private final Set<String> regSet = loadRegFromFile();

    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    private void start()
    {
        try
        {
            ServerSocket ss = new ServerSocket(50000);

            System.out.println("Server: Server started. Listening for connections on port 50000...");

            int clientNumber = 0;

            while (true)
            {
                Socket socket = ss.accept();

                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber));
                t.start();

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        }
        catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    private class ClientHandler implements Runnable
    {

        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true);

                this.clientNumber = clientNumber;

                this.socket = clientSocket;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String message;

            try
            {
                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);

                    // Reference for reading a string into json reader:
                    // https://www.codota.com/code/java/methods/javax.json.Json/createReader
                    JsonReader jsonReader = Json.createReader(new StringReader(message));
                    JsonObject object = jsonReader.readObject();
                    String packetType = object.getString("PacketType");

                    switch (packetType)
                    {
                        case "GetRegisteredVehicles":
                        {
                            JsonBuilderFactory factory = Json.createBuilderFactory(null);
                            JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
                            for (String reg : regSet)
                            {
                                arrayBuilder.add(reg);
                            }
                            JsonArray jsonArray = arrayBuilder.build();
                            JsonObject jsonRootObject
                                    = Json.createObjectBuilder()
                                            .add("PacketType", "ReturnRegisteredVehicles")
                                            .add("Vehicles", jsonArray)
                                            .build();
                            message = jsonRootObject.toString();
                            socketWriter.println(message);
                            break;
                        }
                        case "Heartbeat":
                        {
                            JsonBuilderFactory factory = Json.createBuilderFactory(null);
                            JsonObject jsonRootObject
                                    = Json.createObjectBuilder()
                                            .add("PacketType", "Heartbeat response")
                                            .build();
                            message = jsonRootObject.toString();
                            socketWriter.println(message);
                            break;
                        }
                        case "RegisterValidTollEvent":
                        {
                            String reg = object.getString("Vehicle Registration");
                            long imageId = object.getJsonNumber("Vehicle Image ID").longValue();
                            String timestamp = object.getString("LocalDateTime");
                            try
                            {
                                // Add valid toll event to database
                                boolean success = ITollEventDAO.insertTollEvent(reg, imageId, timestamp);
                                if (!success)
                                {
                                    System.err.println("Toll event insertion failure (record already exists)");
                                }
                                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                                JsonObject jsonRootObject
                                        = Json.createObjectBuilder()
                                                .add("PacketType", "RegisteredValidTollEvent")
                                                .build();
                                message = jsonRootObject.toString();
                                socketWriter.println(message);
                            }
                            catch (DAOException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case "RegisterInvalidTollEvent":
                        {
                            String reg = object.getString("Vehicle Registration");
                            // Add invalid registration to list
                            invalidRegList.add(reg);
                            JsonBuilderFactory factory = Json.createBuilderFactory(null);
                            JsonObject jsonRootObject
                                    = Json.createObjectBuilder()
                                            .add("PacketType", "RegisteredInvalidTollEvent")
                                            .build();
                            message = jsonRootObject.toString();
                            socketWriter.println(message);
                            break;
                        }
                        case "Close":
                            // do nothing
                            break;
                        default:
                            socketWriter.println("\"I'm sorry I don't understand :(\"");
                            break;
                    }
                }
                socket.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }

    // Read in registration numbers from Vehicles.csv file
    private Set<String> loadRegFromFile()
    {
        Set<String> regSet = new HashSet();
        try (Scanner in = new Scanner(new File("data/Vehicles.csv")))
        {
            while (in.hasNext())
            {
                String reg = in.next();
                regSet.add(reg);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Vehicles.csv file not found");
        }
        return regSet;
    }
}
