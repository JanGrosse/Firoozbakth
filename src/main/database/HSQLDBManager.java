package main.database;

import main.Configuration;
import main.DataStructures.ArrayPair;

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

    public synchronized void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = driverName + "database/database.db";
            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void reset() {
        dropTables();
        createTables();
    }

    public synchronized void execute(String SQL) {
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

    public synchronized void storeFiroozbakht(List<String> firoozbakhts) {
        try {
            long iteration = 0;
            int i, size, totalSize;
            size = totalSize = firoozbakhts.size();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO FIROOZBAKHT_CONJECTURE (VALUE) VALUES (?);");
            while (size > 0) {
                for (i = 0; i < 100000 && i < size; i++) {
                    iteration++;
                    statement.setString(1, firoozbakhts.get(i));
                    statement.addBatch();
                }
                firoozbakhts = firoozbakhts.subList(i, size);
                size = firoozbakhts.size();
                statement.executeBatch();
                connection.commit();
                System.out.println(iteration + " of " + totalSize + "(" + (short) ((iteration * 1.0 / totalSize * 1.0) * 100) + "%) stored.");
            }
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayPair<Long, Double> getFiroozbakhtPrimePairs(int limit, int offset) {
        try {
            int length, iteration = 0;
            if (limit == 0) {
                ResultSet count = connection.createStatement().executeQuery("SELECT COUNT(p.PRIME_ID) AS cou FROM FIROOZBAKHT_CONJECTURE f, PRIMES p WHERE p.PRIME_ID = f.PRIME_ID;");
                count.next();
                length = count.getInt("cou") - offset;
                if (length > limit) length = limit;
            } else {
                length = limit;
            }
            Double[] firoozs = new Double[length];
            Long[] primes = new Long[length];
            ResultSet result = connection.createStatement().executeQuery("SELECT p.PRIME_VALUE, f.VALUE FROM FIROOZBAKHT_CONJECTURE f, PRIMES p WHERE WHERE p.PRIME_ID = f.PRIME_ID LIMIT " + limit + " OFFSET " + offset);
            while (result.next()) {
                firoozs[iteration] = result.getDouble(2);
                primes[iteration] = result.getLong(1);
                iteration++;
            }
            return new ArrayPair<>(primes, firoozs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void storePrimeGaps(List<String> gaps) {
        try {
            long iteration = 0;
            int i, size, totalSize;
            size = totalSize = gaps.size();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO PRIME_GAPS (VALUE) VALUES (?);");
            while (size > 0) {
                for (i = 0; i < 100000 && i < size; i++) {
                    iteration++;
                    statement.setString(1, gaps.get(i));
                    statement.addBatch();
                }
                gaps = gaps.subList(i, size);
                size = gaps.size();
                statement.executeBatch();
                connection.commit();
                System.out.println(iteration + " of " + totalSize + "(" + (short) ((iteration * 1.0 / totalSize * 1.0) * 100) + "%) stored.");
            }
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayPair<Long, Long> getPrimePrimeGapPairs(int limit, int offset) {
        try {
            int length, iteration = 0;
            if (limit == 0) {
                ResultSet count = connection.createStatement().executeQuery("SELECT  COUNT(p.PRIME_ID) AS cou FROM PRIME_GAPS f, PRIMES p WHERE p.PRIME_ID = f.PRIME_ID");
                count.next();
                length = count.getInt("cou") - offset;
                if ((limit > 0) && (length > limit)) length = limit;
            } else {
                length = limit;
            }
            Long[] primeGaps = new Long[length];
            Long[] primes = new Long[length];
            ResultSet result = connection.createStatement().executeQuery("SELECT p.PRIME_VALUE, f.VALUE FROM PRIME_GAPS f, PRIMES p WHERE p.PRIME_ID = f.PRIME_ID LIMIT " + limit + " OFFSET " + offset);
            while (result.next()) {
                primes[iteration] = result.getLong(1);
                primeGaps[iteration] = result.getLong(2);
                iteration++;
            }
            return new ArrayPair<>(primes, primeGaps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized ArrayPair<Long, Long> getPrimePrimeGapPairs() {
        return getPrimePrimeGapPairs(Configuration.GUIMaximumPrimeGapsToShow, Configuration.GUISkipNPrimes);
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

    public synchronized void outputPrimes(long limit, long offset) {
        ArrayList<BigInteger> outList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM PRIMES LIMIT " + limit + " OFFSET " + offset);
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

    public synchronized long[] getPrimes(int limit, int offset) {
        ResultSet result;
        long[] primes;
        int length;
        try {
            if (limit == 0) {
                ResultSet count = connection.createStatement().executeQuery("SELECT COUNT(PRIME_ID) AS cou FROM PRIMES LIMIT " + limit + " OFFSET " + offset);
                count.next();
                length = count.getInt("cou") - offset;
            } else {
                length = limit;
            }
            primes = new long[length];
            result = connection.createStatement().executeQuery("SELECT * FROM PRIMES LIMIT " + limit + " OFFSET " + offset);
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

    public synchronized void dropTables() {
        System.out.println("--- DROP TABLES ---");
        execute("DROP TABLE PUBLIC.PRIMES");
        execute("DROP TABLE PUBLIC.FIROOZBAKHT_CONJECTURE");
        execute("DROP TABLE PUBLIC.PRIME_GAPS");
        System.out.println("--- FINISHED DROP TABLES ---");
    }

    public synchronized void createTables() {
        System.out.println("--- CREATE TABLES ---");
        execute("CREATE CACHED TABLE PUBLIC.PRIMES\n" +
                "(\n" +
                "    PRIME_ID INTEGER PRIMARY KEY NOT NULL IDENTITY,\n" +
                "    PRIME_VALUE BIGINT,\n" +
                "    DATE_FOUND CHAR(20) \n" +
                ");\n");
        execute("CREATE CACHED TABLE PUBLIC.FIROOZBAKHT_CONJECTURE\n" +
                "(\n" +
                "    PRIME_ID INTEGER PRIMARY KEY NOT NULL IDENTITY,\n" +
                "    VALUE CHAR(50)\n" +
                ");\n");
        execute("CREATE CACHED TABLE PUBLIC.PRIME_GAPS\n" +
                "(\n" +
                "    PRIME_ID INTEGER PRIMARY KEY NOT NULL IDENTITY,\n" +
                "    VALUE BIGINT\n" +
                ");\n");
        System.out.println("--- FINISHED CREATE TABLES ---");
    }

    public synchronized void shutdown() {
        System.out.println("--- SHUTDOWN ---");
        execute("SHUTDOWN");
    }
}

