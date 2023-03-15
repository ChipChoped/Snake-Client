import view.ViewLogIn;

import java.net.*;
import java.io.*;

public class Client extends Thread {
    private final static int port = 9632;
    private final Socket socket;

    public static void main(String[] args) {
        try {
            // InetAddress serveur = InetAddress.getByName(args[0]);
            InetAddress serveur = InetAddress.getLocalHost();
            Socket socket = new Socket(serveur.getHostAddress(), port);

            Client client = new Client(socket);
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            ViewLogIn viewLogIn = new ViewLogIn(socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}