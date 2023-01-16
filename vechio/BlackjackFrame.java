package vechio;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BlackjackFrame extends JFrame {
    private JLabel playerLabel;
    private JLabel dealerLabel;
    private JButton hitButton;
    private JButton standButton;
    private JLabel resultLabel;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public BlackjackFrame() {
        setTitle("Blackjack");
        setSize(300, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        playerLabel = new JLabel("Player: ");
        dealerLabel = new JLabel("Dealer: ");
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");

        panel.add(playerLabel);
        panel.add(dealerLabel);
        panel.add(hitButton);
        panel.add(standButton);

        add(panel, BorderLayout.CENTER);

        resultLabel = new JLabel("");
        add(resultLabel, BorderLayout.SOUTH);

        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("HIT");
                String response = "";
                try {
                    response = in.readLine();
                    if (response.startsWith("RESULT")) {
                        resultLabel.setText(response.substring(7));
                    } else if (response.startsWith("PLAYER")) {
                        playerLabel.setText("Player: " + response.substring(7));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("STAND");
                String response = "";
                try {
                    response = in.readLine();
                    if (response.startsWith("RESULT")) {
                        resultLabel.setText(response.substring(7));
                    } else if (response.startsWith("DEALER")) {
                        dealerLabel.setText("Dealer: " + response.substring(7));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
