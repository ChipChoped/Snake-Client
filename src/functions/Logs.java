package functions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unchecked")
public class Logs {
    public static JSONObject logIn(Socket socket, String username, String password) throws IOException, NoSuchAlgorithmException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());

        String type = "";
        JSONObject jsonResponse = null;

        while (!type.equals("return-log-in") && !type.equals("error")) {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("type", "log-in");
            jsonRequest.put("username", username);
            jsonRequest.put("password", SHA256.encrypt(password));
            out.println(jsonRequest);

            String output = in.readLine();

            jsonResponse = new JSONObject(output);
            type = (String) jsonResponse.get("type");
        }

        return jsonResponse;
    }

    public static JSONObject logOut(Socket socket, int ID) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());

        String type = "";
        JSONObject jsonResponse = null;

        while (!type.equals("return-log-out") && !type.equals("error")) {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("type", "log-out");
            jsonRequest.put("id", ID);
            out.println(jsonRequest);

            jsonResponse = new JSONObject(in.readLine());
            type = (String) jsonResponse.get("type");
        }

        return jsonResponse;
    }
}
