package comands;

import authorization.AuthorizationService;
import session.Session;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by user on 20.10.2015.
 */
public class LoginCommand implements Command {

    private AuthorizationService service;

    public LoginCommand(AuthorizationService service) {
        this.service = service;
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session.getSessionUser() != null) {
            System.out.println("You are already authorizated");
        } else if (args.length == 3) {
            //��� ����������
            try {
                service.login(args[1], args[2], session);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else if (args.length == 4) {
            //��� ������� ������ �����
            try {
                service.createUser(args[2], args[3]);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong arguments for \\login command.");
        }
        /*
        � ��� ����� � ��� ��� �����������
        1 ��������, ���� �� � ��� ��� ���� ������
        2 ��������� �� ��������� �������
        3 ������ � authorizationService � ��������� �������� �����
         */
    }
}
