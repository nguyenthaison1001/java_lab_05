package utility;

import exceptions.DatabaseHandlingException;
import interaction.User;
import server.AppServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseHandler.USER_TABLE + " (" +
            DatabaseHandler.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";

    private DatabaseHandler databaseHandler;

    public DatabaseUserManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    /**
     * @param userID User id.
     * @return User by id.
     * @throws SQLException When there's exception inside.
     */
    public User getUserById(int userID) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            AppServer.LOGGER.info("Выполнен запрос SELECT_USER_BY_ID.");

            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseHandler.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseHandler.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return user;
    }

    /**
     * Check user by username and password.
     *
     * @param user User.
     * @return Result set.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            AppServer.LOGGER.info("Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD.");
            return resultSet.next();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Get user id by username.
     *
     * @param user User.
     * @return User id.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public int getUserIdByUsername(User user) throws DatabaseHandlingException {
        int userId;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement =
                    databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedStatement.setString(1, user.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            AppServer.LOGGER.info("Выполнен запрос SELECT_USER_BY_USERNAME.");

            if (resultSet.next()) {
                userId = resultSet.getInt(DatabaseHandler.USER_TABLE_ID_COLUMN);
            } else userId = -1;

            System.out.println(userId);

            return userId;
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Insert user.
     *
     * @param user User.
     * @return Status of insert.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedStatement =
                    databaseHandler.getPreparedStatement(INSERT_USER, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            if (preparedStatement.executeUpdate() == 0) throw new SQLException();
            AppServer.LOGGER.info("Выполнен запрос INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса INSERT_USER!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
    }
}
