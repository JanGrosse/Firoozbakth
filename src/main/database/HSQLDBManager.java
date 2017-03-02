package main.database;

import java.math.BigInteger;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private String driverName = "jdbc:hsqldb:";
    private String username = "sa";
    private String password = "";

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = driverName + "database/database.db";
            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void reset() {
        dropTables();
        createTables();
    }

    public synchronized void execute(String SQL) {
        // System.out.println("SQL: " + sqlStatement);
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(SQL);
            if (result == -1)
                System.out.println("ERROR WHILE QUERYING:" + SQL);
            statement.closeOnCompletion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void storePrimes(List<String> primes) {
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String now = df.format(new Date());
            long iteration = 0;
            int i, size, totalSize;
            size = totalSize = primes.size();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO primes (PRIME_VALUE, DATE_FOUND) VALUES (?, ?);");
            while (size > 0) {
                for (i = 0; i < 100000 && i < size; i++) {
                    iteration++;
                    statement.setString(1, primes.get(i));
                    statement.setString(2, now);
                    statement.addBatch();
                }
                primes = primes.subList(i, size);
                size = primes.size();
                statement.executeBatch();
                connection.commit();
                System.out.println(iteration + " of " + totalSize + "(" + (short) ((iteration * 1.0 / totalSize * 1.0) * 100) + "%) stored.");
            }
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void outputPrimes(long limit) {
        ArrayList<BigInteger> outList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM PRIMES " + (limit == 0 ? "" : "LIMIT " + limit));
            while (result.next()) {
                System.out.print(result.getString("PRIME_VALUE") + ", ");
            }
            ResultSet res = connection.createStatement().executeQuery("SELECT COUNT(PRIME_ID) AS cou FROM PRIMES");
            res.next();
            System.out.println("Entries: " + res.getString("cou"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized long[] getPrimes(long limit, long start) {
        ArrayList<BigInteger> outList = new ArrayList<>();
        ResultSet result;
        long[] primes;
        try {
            result = connection.createStatement().executeQuery("SELECT COUNT(PRIME_ID) AS amount FROM PRIMES " + (limit == 0 ? "" : "LIMIT " + limit) + (start == 0 && limit != 0 ? "" : " OFFSET " + start));
            result.next();
            primes = new long[result.getInt("amount")];
            result = connection.createStatement().executeQuery("SELECT * FROM PRIMES " + (limit == 0 ? "" : "LIMIT " + limit) + (start == 0 && limit != 0 ? "" : " OFFSET " + start));
            for (int i = 0; i < primes.length; i++) {
                result.next();
                primes[i] = result.getLong("PRIME_VALUE");
            }
            return primes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dropTables() {
        System.out.println("--- DROP TABLES ---");
        execute("DROP TABLE PRIMES");
        System.out.println("--- FINISHED DROP TABLES ---");
    }

    public void createTables() {
        System.out.println("--- CREATE TABLES ---");
        execute("CREATE CACHED TABLE PRIMES\n" +
                "(\n" +
                "    PRIME_ID INTEGER PRIMARY KEY NOT NULL IDENTITY,\n" +
                "    PRIME_VALUE BIGINT,\n" +
                "    DATE_FOUND CHAR(20) \n" +
                ");\n");
        execute("CREATE CACHED TABLE FIROOZBAKHT_CONJECTURE\n" +
                "(\n" +
                "    PRIME_ID INTEGER PRIMARY KEY NOT NULL IDENTITY,\n" +
                "    VALUE CHAR(50),\n" +
                ");\n");
        System.out.println("--- FINISHED CREATE TABLES ---");
    }

    public void shutdown() {
        System.out.println("--- SHUTDOWN ---");
        execute("SHUTDOWN");
    }
}

