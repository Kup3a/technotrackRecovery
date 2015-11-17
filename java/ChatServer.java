import authorization.AuthorizationService;
import authorization.DBUserStore;
import authorization.UserStore;
import comands.*;
import org.codehaus.jackson.map.ObjectMapper;
import session.Message;
import session.Session;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

;

/**
 * Created by user on 27.10.2015.
 */
public class ChatServer implements Runnable {

    private Map<Integer, Session> connections = new HashMap<>();
    private int connectionAmount = 1;

    private void initNewConnection() {
        connections.put(connectionAmount, new Session());
        connectionAmount++;
    }

    @Override
    public void run() {
        final String EXIT = "q|exit";

        Map<String, Command> commands = new HashMap<>();


        // � ���� ������� �������� ���� � �����
        // �� ���� ������� ��������� ����
        Session session = new Session();

        // ���������� ���������� �������� � ����� �����
        //UserStore userStore = new FileUserStore();
        UserStore userStore = new DBUserStore();
        AuthorizationService authService = new AuthorizationService(userStore);


        //������� �������
        Command loginCommand = new LoginCommand(authService);
        Command helpCommand = new HelpCommand(commands);
        Command historyCommand = new HistoryCommand();
        Command findCommand = new FindCommand();
        Command nickCommand = new NickCommand();

        commands.put("\\login", loginCommand);
        commands.put("\\help", helpCommand);
        commands.put("\\history", historyCommand);
        commands.put("\\find", findCommand);
        commands.put("\\nick", nickCommand);

        //InputHandler handler = new InputHandler(session, commands);
        InputHandler handler = new InputHandler(commands);

        try {
            ServerSocket serverSocket = new ServerSocket(3129, 0, InetAddress.getByName("localhost"));
            System.out.println("Server inited successfully.");
            Socket socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            ObjectMapper mapper = new ObjectMapper();

            while (true) {
                // ������ ������ � 64 ���������
                byte buf[] = new byte[64*1024];
                // ������ 64�� �� �������, ��������� - ���-�� ������� �������� ������
                int r = is.read(buf);
                // ������ ������, ���������� ���������� �� ������� ����������
                String data = new String(buf, 0, r);
                // ������ - ��� json, ������� ���� ������������� � ������ � Message
                Message m = mapper.readValue(data, Message.class);
                // ������ �� ��������� ����� ���� ���������-�������
                if (m.getBody() != null && m.getBody().equals(EXIT)) {
                    break;
                }
                //�� ��������� �� ���������� ��������� ������ ����� ����, ��� ������� ��������� connectionId
                if (m.getConnectionId() != 0) {
                    System.out.println("handling message from clientId = " + m.getConnectionId());
                    System.out.println(connections.get(m.getConnectionId()));
                    session = connections.get(m.getConnectionId());
                    handler.handle(m.getBody(), session);
                } else {
                    System.out.println("sending mes from server");
                    String ans = "connectionAmount " + String.valueOf(connectionAmount);
                    socket.getOutputStream().write(ans.getBytes());
                    initNewConnection();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
