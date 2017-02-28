package main.database;

import main.Configuration;

import java.sql.*;
import java.util.ArrayList;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private String driverName = "jdbc:hsqldb:";
    private String username = "sa";
    private String password = "";

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = driverName + "datastore.db";
            connection = DriverManager.getConnection(databaseURL,username,password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void init() {
        dropTables();
        createTables();
    }

    public synchronized void update(String sqlStatement) {
        // System.out.println("SQL: " + sqlStatement);
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);
            if (result == -1)
                System.out.println("error executing " + sqlStatement);
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void dropTables() {
        System.out.println("--- dropTable dataset");
        update("DROP TABLE dataset");
        System.out.println("--- dropTable scenarios");
        update("DROP TABLE scenarios");
    }

    public void createTables() {
        System.out.println("--- createTable dataSet");
        String statement = "CREATE TABLE dataSet(" +
                "id INTEGER IDENTITY PRIMARY KEY," +
                "scenarioId VARCHAR(20) NOT NULL," +
                "value INT NOT NULL);";
        update(statement);

        System.out.println("--- createTable scenarios");
        statement = "CREATE TABLE scenarios(" +
                "id VARCHAR(20) NOT NULL," +
                "selectionType VARCHAR(20) NOT NULL," +
                "crossoverType VARCHAR(20) NOT NULL," +
                "mutationType VARCHAR(20) NOT NULL," +
                "crossoverPropability REAL," +
                "mutationPropability REAL);";
        update (statement);
    }

    public void addScenario(String scenarioId, String selectionType, String crossoverType, String mutationType, double crossoverProbability, double mutationProbability) {
        String statement = "INSERT INTO scenarios(id, selectionType, crossoverType, mutationType, crossoverPropability, mutationPropability) VALUES (" +
                "'" + scenarioId + "'," +
                "'" + selectionType + "'," +
                "'" + crossoverType + "'," +
                "'" + mutationType + "'," +
                crossoverProbability + "," +
                0.5 + ");";
        update(statement);
    }

    public void addDataSet(String scenarioId, ArrayList<Integer> dataSet) {
        String statement;
        for (Integer entry : dataSet) {
            statement = "INSERT INTO dataSet(scenarioId, value) VALUES ( " +
                    scenarioId + "," +
                    entry + ");";
            update(statement);
        }
    }

    public void addData(String scenarioId, int data) {
        String statement = "INSERT INTO dataSet(scenarioId, value) VALUES ('" + scenarioId + "'," + data + ");";
        update(statement);
    }

    public ArrayList<String> getScenarioIds() {
        String query = "SELECT id FROM scenarios";
        ArrayList<String> output = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                output.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public ArrayList<Integer> getDataSet(String scenarioId) {
        String query = "SELECT value FROM dataSet WHERE scenarioId = '" + scenarioId + "'";
        ArrayList<Integer> output = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                output.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String[] getScenarioProperties(String scenarioId) {
        String query = "SELECT selectionType,crossoverType,mutationType,crossoverPropability,mutationPropability FROM " +
                "scenarios WHERE id = '" + scenarioId + "'";
        String[] output = new String[5];
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                output[0] = result.getString("selectionType");
                output[1] = result.getString("crossoverType");
                output[2] = result.getString("mutationType");
                output[3] = String.valueOf(result.getFloat("crossoverPropability"));
                output[4] = String.valueOf(result.getFloat("mutationPropability"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}