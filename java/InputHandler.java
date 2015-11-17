import comands.Command;
import session.Session;

import java.util.Map;

/**
 * Created by user on 20.10.2015.
 */
public class InputHandler {

    private Session session;

    private Map<String, Command> commandMap;

    //private MessagesStorage messagesStorage = new MessageFileStorage();

    public InputHandler(Session session, Map<String, Command> commandMap) {
        this.session = session;
        this.commandMap = commandMap;
    }

    public InputHandler(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    public void handle(String data) {
        // ��������� �� ���������� �������
        // ��� ������!
        if (data.startsWith("\\")) {
            String[] tokens = data.split(" ");

            // ������� ���������� �������, �� ��� �� ����� ��� �� �������,
            // � ��� ���� ����� execute()
            Command cmd = commandMap.get(tokens[0]);
            if (cmd == null) {
                System.out.println("There's no such command. Please, enter '\\help' for getting list of available commands");
            } else {
                cmd.execute(session, tokens);
            }
        } else if (session.getSessionUser() == null){
            System.out.println(">" + data);
        } else if (session.getSessionUser().getNick() == null){
            session.getMessagesStorage().storeMesage(data);
            System.out.println("(message is stored)>" + data);
        } else {
            session.getMessagesStorage().storeMesage(data);
            System.out.println("(message is stored)<" + session.getSessionUser().getNick() + ">" + data);
        }
    }

    public void handle(String data, Session s) {
        // ��������� �� ���������� �������
        // ��� ������!

        if (s == null) {
            System.out.println("session is null");
        } else {
            System.out.println("session NOT null");
        }

        if (data.startsWith("\\")) {
            String[] tokens = data.split(" ");

            // ������� ���������� �������, �� ��� �� ����� ��� �� �������,
            // � ��� ���� ����� execute()
            Command cmd = commandMap.get(tokens[0]);
            if (cmd == null) {
                System.out.println("There's no such command. Please, enter '\\help' for getting list of available commands");
            } else {
                cmd.execute(s, tokens);
            }
        } else if (s.getSessionUser() == null){
            System.out.println(">" + data);
        } else if (s.getSessionUser().getNick() == null){
            s.getMessagesStorage().storeMesage(data);
            System.out.println("(message is stored)>" + data);
        } else {
            s.getMessagesStorage().storeMesage(data);
            System.out.println("(message is stored)<" + s.getSessionUser().getNick() + ">" + data);
        }
    }
}
