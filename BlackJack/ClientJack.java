package BlackJack;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class ClientJack extends JFrame {
    private JTextArea displayArea;
    private JTextField inputField;
    private JButton hitButton;
    private JButton standButton;
    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter serverOut;

    public ClientJack() {
        super("Blackjack Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(20);
        inputField.setEditable(false);
        inputPanel.add(inputField);
        add(inputPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverOut.println("hit");
            }
        });
        buttonPanel.add(hitButton);
        standButton = new JButton("Stand");
        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverOut.println("stand");
            }
        });
        buttonPanel.add(standButton);
        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            displayArea.append("Connected to server\n");

            Thread t = new Thread(new ServerListener());
            t.start();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        ClientJack client = new ClientJack();
        client.connectToServer();
    }

    private class ServerListener implements Runnable {
        public void run() {
            try {
                while (true) {
                    String serverMessage = serverIn.readLine();
                    displayArea.append(serverMessage + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}
