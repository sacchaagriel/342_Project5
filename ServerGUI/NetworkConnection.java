import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

class NetworkConnection {
    public static int numClients;
    private int port;
    public ArrayList<ClientSocket> clientList = new ArrayList<>(); //Stores all clients
    public ArrayList<String> clientChoices = new ArrayList<>();       //Holds all the client names that are connected to server.
    public ArrayList<String> clientNames = new ArrayList<>();
    private ConnThread connThread = new ConnThread();
    public int mysteryNo;
    public String mystery;

    //Function that allows us to pass in a function that is going to be called when
    //we receive message from the other end.
    private Consumer<Serializable> callback;

    NetworkConnection(int port, Consumer<Serializable> callback) {
        this.port = port;
        this.callback = callback;
        numClients = 0;

        //Generating Random no
        Random rand = new Random();
        mysteryNo = rand.nextInt(30);
        mysteryNo += 1;
        mystery = Integer.toString(mysteryNo);
        System.out.println(mystery);

        connThread.setDaemon(true);
    }

    public int getPort() {
        return this.port;
    }


    public void startConn() {
        connThread.start();
    }

    public void closeConn() {
        try {
            for (ClientSocket clients : clientList) {
                clients.getClientSocket().close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error in closing client sockets.");
        }
    }


    //Inner class which extends Thread.
    class ConnThread extends Thread {

        private ObjectOutputStream out;
        private Socket socket;
        private ObjectInputStream in;

        public void run() {

            try (
                    ServerSocket listener = new ServerSocket(getPort()))    //listener always keeps listening to see if client wants to connect.

            {
                while (true) {
                    Socket socket = listener.accept();
                    this.socket = socket;

                    ClientSocket secondaryThread = new ClientSocket(socket);
                    clientList.add(secondaryThread);    //The client connected is added to the ArrayList clientList.
                    secondaryThread.start();
                    this.out = secondaryThread.getOut();
                    this.in = secondaryThread.getIn();
                }
            }
            catch (IOException e) {
                callback.accept("Connection Closed");
            }
        }
    }


    class ClientSocket extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Serializable data;
        private String clientName;
        private String winner;
        private Boolean nameIsUnique = false;

        ClientSocket( Socket clientSocket) {
            this.clientSocket = clientSocket;
            numClients++;
        }

        public synchronized void run() {
            try (
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());    //send objects
                    //ObjectInputStream deserializes primitive data and objects previously written using an ObjectOutputStream.
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()))       //receive objects
            {
                this.in = in;
                this.out = out;

                //This is what we receive from the other end.
                while (true) {
                    data = (Serializable) in.readObject();  //Reads data sent from client.

                    if(numClients >= 4)
                    {
                        for(int i = 0; i < clientList.size(); i++)
                        {
                            clientList.get(i).sendMsg("4 players are now connected!! \n Begin playing the game.");
                        }
                    }

                    if(numClients < 4)
                    {
                        for(int i = 0; i < clientList.size(); i++)
                        {
                            clientList.get(i).sendMsg("Waiting for 4 players to connect...");
                        }
                    }

                    if(data.equals(mystery))
                    {
                        winner = "**" + this.getClientName() + " ***";
                        System.out.println(this.getClientName());
                        callback.accept(winner);
                    }

                    if(data.toString().startsWith("* "))
                    {
                        do{
                            if(clientNames.size() == 0) {
                                nameIsUnique = true;
                            }
                            // Keep making the client enter name until its unique
                            outer:
                            for (int i = 0; i < clientNames.size(); i++) {
                                System.out.println("The value of i" + i);
                                if (clientNames.get(i).equals(data.toString())) {
                                    data = (Serializable) in.readObject();
                                    i=-1;
                                    continue outer;
                                }
                                nameIsUnique = true;
                                System.out.println("!1");
                            }
                        }while(!nameIsUnique);

                        this.sendMsg("You are player: " + numClients);
                        this.clientName = data.toString();
                        clientNames.add(data.toString());
                        callback.accept(clientNames);
                    }

                    else if(data.toString().equals("quit"))
                    {
                        clientNames.remove(this.clientName);
                        clientList.remove(this);
                        setData();
                        callback.accept(clientNames);
                        closeSocket();  //If client send "quit", that client socket is closed and removed from ArrayList.
                    }
                    else {
                        clientChoices.add(data.toString());
                        callback.accept(clientChoices);
                    }
                    callback.accept(data);
                }
            }

            catch(ClassNotFoundException e)
            {
                System.out.println("Class not found exception.");
            }
            catch (IOException e) {
                System.out.println("Connection closed");
            }
            catch (NullPointerException e)
            {
                System.out.println("Connection closed.");
            }
        }

        public void closeSocket() {
            try {
                //Closes all streams, socket and removes the clientSocket from ArrayList.
                this.out.close();
                this.in.close();
                this.getClientSocket().close();

                numClients--;
            }
            catch (IOException e) {
                System.out.println("In close socket");
            }
        }

        public Socket getClientSocket() {
            return clientSocket;
        }

        public void setData() {
            this.data = null;
        }

        public Serializable getData()
        {
            return data;
        }

        public ObjectOutputStream getOut()
        {
            return this.out;
        }

        public ObjectInputStream getIn()
        {
            return this.in;
        }

        public String getClientName()
        {
            return this.clientName;
        }

        public void sendMsg(Serializable msg) {
            try {
                out.writeObject(msg);   //Sends msg to client through "out" stream.
            }
            catch (IOException e) {
                System.out.println("Error in sending Server's message.");
            }
        }
    }
}






