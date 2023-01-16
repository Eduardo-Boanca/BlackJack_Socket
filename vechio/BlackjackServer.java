package vechio;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class BlackjackServer {
  private ServerSocket server;
  private ArrayList<ObjectOutputStream> outputStreams;
  private Random random;

  public BlackjackServer() {
    outputStreams = new ArrayList<ObjectOutputStream>();
    random = new Random();
  }

  public void start() {
    try {
      server = new ServerSocket(6789, 100);
      while (true) {
        BlackjackServerThread thread = new BlackjackServerThread(server.accept(), outputStreams, random);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    BlackjackServer server = new BlackjackServer();
    server.start();
  }
}
