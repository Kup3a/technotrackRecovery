package authorization;

import session.MessageDBStorage;
import session.Session;
import session.User;
import tools.HashClass;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by user on 20.10.2015.
 */
public class AuthorizationService {

    private UserStore userStore;

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
    }

    public User login(String login, String password, Session session) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (userStore.isUserExist(login)) {
            // checkingUser - ������������, �������� ������� �� ��������� �� ������
            User checkingUser = userStore.getUser(login);
            String checkingSalt = checkingUser.getSalt();
            if (checkingUser.getHashedPassword().equals(HashClass.createHash(password.toCharArray(), HashClass.fromHex(checkingSalt)))) {
                System.out.println("you have authorized");
                session.setSessionUser(checkingUser);
                // ��������� ������� - ��� ���������� ��������� � �����
                //session.setMessagesStorage(new MessageFileStorage(checkingUser.getLogin()));
                // ��������� ������� - ��� ���������� ��������� � ����
                session.setMessagesStorage(new MessageDBStorage(1, checkingUser.getId()));
            } else {
                System.out.println("fail authorization");
            }
        } else {
            System.out.println("There is no such login.");
        }
//            1. Ask for name
//            2. Ask for password
//            3. Ask UserStore for user:  userStore.getUser(name, pass)


        return null;
    }

    public User createUser(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, FileNotFoundException {
        if (userStore.isUserExist(login)) {
            System.out.println(login + " login is already used, try another please.");
            return null;
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        userStore.addUser(user);
        System.out.println("You have successfully registred.");
        // 1. Ask for name
        // 2. Ask for pass
        // 3. Add user to UserStore: userStore.addUser(user)

        return null;
    }

}

