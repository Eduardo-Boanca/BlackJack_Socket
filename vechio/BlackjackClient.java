package vechio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class BlackjackClient extends JFrame implements ActionListener {
    private JLabel playerHandLabel;
    private JLabel dealerHandLabel;
    private JLabel resultLabel;
    private JButton hitButton;
    private JButton stayButton;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private JTextField userText;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private Thread clientThread;

    public BlackjackClient(String host) {
        super("Blackjack Client");
        serverIP = host;
        hitButton = new JButton("Hit");
        hitButton.addActionListener(this);
        stayButton = new JButton("Stay");
        stayButton.addActionListener(this);
        playerHandLabel = new JLabel("Your hand: ");
        dealerHandLabel = new JLabel("Dealer's hand: ");
        resultLabel = new JLabel("Result: ");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(this);
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(hitButton, BorderLayout.WEST);
        add(stayButton, BorderLayout.EAST);
        add(playerHandLabel, BorderLayout.NORTH);
        add(dealerHandLabel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
        add(chatWindow, BorderLayout.SOUTH);
        setSize(600, 300);
        setVisible(true);
    }

    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            clientThread = new BlackjackClientThread();
            clientThread.start();
        } catch (EOFException eofException) {
            showMessage("\nClient terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            close();
        }
    }

    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 9999);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nYour streams are now good to go!\n");
    }

    private void close() {
        showMessage("\nClosing connections...\n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (IOException ioException) {
            chatWindow.append("\nError sending message");
        }
    }

    private void showMessage(final String m) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        chatWindow.append(m);
                    }
                });
    }

    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userText.setEditable(tof);
                    }
                });
    }

    public void updatePlayerHand(String hand) {
        playerHandLabel.setText("Your hand: " + hand);
    }

    public void updateDealerHand(String hand) {
        dealerHandLabel.setText("Dealer's hand: " + hand);
    }

    public void updateResult(String result) {
        resultLabel.setText("Result: " + result);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == hitButton) {
            sendMessage("HIT");
        } else if (event.getSource() == stayButton) {
            sendMessage("STAY");
        }
    }

    private class BlackjackClientThread extends Thread {
        public void run() {
            try {
                whileChatting();
            } catch (EOFException eofException) {
                showMessage("\nClient terminated connection");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                close();
            }
        }

        private void whileChatting() throws IOException {
            ableToType(true);
            do {
                try {
                    message = (String) input.readObject();
                    if (message.startsWith("PLAYER HAND:")) {
                        String playerHand = message.substring("PLAYER HAND:".length());
                        updatePlayerHand(playerHand);
                    } else if (message.startsWith("DEALER HAND:")) {
                        String dealerHand = message.substring("DEALER HAND:".length());
                        updateDealerHand(dealerHand);
                    } else if (message.startsWith("RESULT:")) {
                        String result = message.substring("RESULT:".length());
                        updateResult(result);
                    } else {
                        showMessage("\n" + message);
                    }
                } catch (ClassNotFoundException classNotFoundException) {
                    showMessage("\nUnknown object type received");
                }
            } while (!message.equals("SERVER - END"));
        }

        public static void main(String[] args) {
            String ip = "127.0.0.1"; // default localhost
            if (args.length > 0) {
                ip = args[0];
            }
            BlackjackClient client = new BlackjackClient(ip);
            client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.startRunning();
        }
    }
}
