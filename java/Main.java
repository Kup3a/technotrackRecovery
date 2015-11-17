/**
 * Created by user on 20.10.2015.
 */
public class Main {

    private static final String EXIT = "q|exit";

    public static void main(String[] args) {

/*        Map<String, Command> commands = new HashMap<>();


        // � ���� ������� �������� ���� � �����
        // �� ���� ������� ��������� ����
        Session session = new Session();

        // ���������� ���������� �������� � ����� �����
        UserStore userStore = new FileUserStore();
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

        InputHandler handler = new InputHandler(session, commands);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the chat! Enter '\\help' for getting list of available commands.");
        while (true) {
            String line = scanner.nextLine();
            if (line != null && line.equals(EXIT)) {
                break;
            }
            handler.handle(line);
        }


 */       //��� ������-��������� ������
        Runnable server = new ChatServer();
        Thread t = new Thread(server);
        t.start();
        ChatClient client = new ChatClient();
        client.connectToServer();
        System.out.println("main is over");
    }

}
