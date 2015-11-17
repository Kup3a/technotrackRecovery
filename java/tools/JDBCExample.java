package tools; /**
 * Created by r.kildiev on 02.11.2015.
 */

import authorization.DBUserStore;
import authorization.UserStore;
import session.MessageDBStorage;
import session.MessagesStorage;
import session.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class JDBCExample {


    public User getUserFromDB(String login) throws SQLException, ClassNotFoundException {
        User u = new User();

        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");

        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM USERS WHERE u_login =" + "'" + login + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            u.setId(rs.getInt("U_ID"));
            u.setLogin(login);
            u.setHashedPassword(rs.getString("HASHED_PASSWORD"));
            u.setSalt(rs.getString("SALT"));
            return u;
        } else {
            return null;
        }
    }

    public void addUserToDB(User u) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");
        Statement stmt = c.createStatement();

        String s = null;
        try {
            s = HashClass.getSaltHP(u.getPassword().toCharArray());
            String[] ss = s.split(" ");
            u.setSalt(ss[0]);
            u.setHashedPassword(ss[1]);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO USERS (U_LOGIN,SALT,HASHED_PASSWORD) "
                + "VALUES (" + "'" + u.getLogin() + "','" + u.getSalt() + "','" + u.getHashedPassword() + "'" + ");";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void main(String[] argv) throws SQLException, ClassNotFoundException {
/*
        Class.forName("org.postgresql.Driver");

        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/mydb",
                "senthil", "ubuntu");

        Statement stmt;
        String sql;

        stmt = c.createStatement();
        sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        stmt.executeUpdate(sql);
        stmt.close();
//        c.close();

        c.setAutoCommit(false);

        stmt = c.createStatement();
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();
//        c.close();

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String address = rs.getString("address");
            float salary = rs.getFloat("salary");
            System.out.println("ID = " + id);
            System.out.println("NAME = " + name);
            System.out.println("AGE = " + age);
            System.out.println("ADDRESS = " + address);
            System.out.println("SALARY = " + salary);
            System.out.println();
        }
        rs.close();
        stmt.close();
//        c.close();

        stmt = c.createStatement();
        sql = "UPDATE COMPANY SET SALARY = 25000.00 WHERE ID=1;";
        stmt.executeUpdate(sql);
        c.commit();

        rs = stmt.executeQuery("SELECT * FROM COMPANY;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String address = rs.getString("address");
            float salary = rs.getFloat("salary");
            System.out.println("ID = " + id);
            System.out.println("NAME = " + name);
            System.out.println("AGE = " + age);
            System.out.println("ADDRESS = " + address);
            System.out.println("SALARY = " + salary);
            System.out.println();
        }
        rs.close();
        stmt.close();
//        c.close();

        stmt = c.createStatement();
        sql = "DELETE FROM COMPANY WHERE ID=2;";
        stmt.executeUpdate(sql);
        c.commit();

        rs = stmt.executeQuery("SELECT * FROM COMPANY;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String address = rs.getString("address");
            float salary = rs.getFloat("salary");
            System.out.println("ID = " + id);
            System.out.println("NAME = " + name);
            System.out.println("AGE = " + age);
            System.out.println("ADDRESS = " + address);
            System.out.println("SALARY = " + salary);
            System.out.println();
        }
        rs.close();
        stmt.close();
        c.close();

//        PreparedStatement prepStmnt = c.prepareStatement("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
//                + "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );");
//        int parameterIndex = 234;
//        prepStmnt.setString(parameterIndex, "asd");
//        rs = prepStmnt.executeQuery();
*/
        //дальше начинаются мои изыскания
        ArrayList<String> requiredTables = new ArrayList<>();
        requiredTables.add("messages");
        requiredTables.add("users");
        requiredTables.add("chats");
        requiredTables.add("user_chat");
        int flag = 0;
        Class.forName("org.postgresql.Driver");

        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");

        DatabaseMetaData dbData = c.getMetaData();
        ResultSet rs = dbData.getTables(null, "public", "", null);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            if (requiredTables.contains(name)) {
                flag++;
            }
        }
        rs.close();

        Statement stmt;
        String sql;

        if (flag != requiredTables.size()) {
            System.out.println("Wrong db");

            stmt = c.createStatement();
            sql = "CREATE TABLE USERS " +
                    "(U_ID            SERIAL PRIMARY KEY     NOT NULL," +
                    " U_LOGIN         TEXT    NOT NULL UNIQUE , " +
                    " U_NICK          TEXT    UNIQUE , " +
                    " SALT        TEXT, " +
                    " HASHED_PASSWORD         TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE CHATS " +
                    "(CH_ID            SERIAL PRIMARY KEY     NOT NULL," +
                    " CH_NAME         TEXT    NOT NULL UNIQUE , " +
                    " CH_DATE          TEXT     )";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE MESSAGES " +
                    "(M_ID             SERIAL PRIMARY KEY     NOT NULL," +
                    " M_TEXT         TEXT    NOT NULL, " +
                    " SENDER_ID INT     NOT NULL, " +
                    " CHAT_ID        INT, " +
                    " CHAT_DATE        TIMESTAMP, " +
                    " CONSTRAINT MES_FK_U FOREIGN KEY (SENDER_ID) REFERENCES USERS (U_ID) ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    " CONSTRAINT MES_FK_CH FOREIGN KEY (CHAT_ID) REFERENCES CHATS (CH_ID) ON UPDATE NO ACTION ON DELETE NO ACTION)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE USER_CHAT " +
                    "(CH_ID            INT     NOT NULL," +
                    " U_ID             INT     NOT NULL," +
                    " PRIMARY KEY (CH_ID, U_ID)," +
                    " CONSTRAINT U_CH_FK_U FOREIGN KEY (U_ID) REFERENCES USERS (U_ID) ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    " CONSTRAINT U_CH_FK_CH FOREIGN KEY (CH_ID) REFERENCES CHATS (CH_ID) ON UPDATE NO ACTION ON DELETE NO ACTION)";
            stmt.executeUpdate(sql);
            stmt.close();

            c.setAutoCommit(false);

            stmt = c.createStatement();
            sql = "INSERT INTO USERS (U_LOGIN) "
                    + "VALUES ('alex');";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        } else {
            System.out.println("OK db");
        }



    /*    stmt = c.createStatement();
        sql = "CREATE TABLE MESSAGES " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " M_TEXT           TEXT    NOT NULL, " +
                " FROM_ID            INT     NOT NULL, " +
                " CHAT_ID        INT )";
        stmt.executeUpdate(sql);
        stmt.close();

        c.setAutoCommit(false);

        stmt = c.createStatement();
        sql = "INSERT INTO MESSAGES (ID,M_TEXT,FROM_ID,CHAT_ID) "
                + "VALUES (1, 'hi! how are u?', 32, 3);";
        stmt.executeUpdate(sql);

        stmt.close();
        c.commit();*/

       /* stmt = c.createStatement();
        sql = "INSERT INTO USERS (U_LOGIN) "
                + "VALUES ('malex');";
        stmt.executeUpdate(sql);
        */
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM MESSAGES;");

        while (rs.next()) {
            String text = rs.getString("m_text");
            System.out.println(" M_TEXT  = " + text);
        }
        rs.close();


//        stmt = c.createStatement();
//        sql = "INSERT INTO CHATS (CH_NAME) "
//                + "VALUES ('myChat');";
//        stmt.executeUpdate(sql);
        //ТЕСТИРОВАНИЕ КЛАССА DBUserStore
        //МЕТОДА isUserExist
        UserStore userStore = new DBUserStore();
        System.out.println("is user exist: " + userStore.isUserExist("alex"));
        //МЕТОДА addUser
        User u = new User();
        u.setLogin("alexey");
        if (!userStore.isUserExist("alexey")) {
            try {
                String s = HashClass.getSaltHP("114499".toCharArray());
                String[] ss = s.split(" ");
                u.setSalt(ss[0]);
                u.setHashedPassword(ss[1]);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            userStore.addUser(u);
        }
        //МЕТОДА getUser
        u = userStore.getUser("alexey");
        System.out.println(u.getHashedPassword());
        //ТЕСТИРОВАНИЕ КЛАССА
        //тестирование метода storeMesage
        MessagesStorage messagesStorage = new MessageDBStorage(2, 2);
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        while (!str.equals("exit")) {
            messagesStorage.storeMesage(str);
            str = scanner.nextLine();
        }
        //тестирование метода
        String[] list =  messagesStorage.getAllMessages();
        System.out.println("list.length: " + list.length);
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }

        stmt.close();
        c.close();

    }

}