package utility;

import data.Coordinates;
import data.Difficulty;
import data.Discipline;

import data.LabWork;
import exceptions.DatabaseHandlingException;
import interaction.LabRaw;
import interaction.User;
import server.AppServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;

public class DatabaseCollectionManager {
    // LAB_TABLE
    private final String SELECT_ALL_LABS = "SELECT * FROM " + DatabaseHandler.LAB_TABLE;
    private final String SELECT_LAB_BY_ID = SELECT_ALL_LABS + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";    // "SELECT * FROM space_marine WHERE id = ?"
    private final String SELECT_LAB_BY_ID_AND_USER_ID = SELECT_LAB_BY_ID + " AND " +
            DatabaseHandler.LAB_TABLE_USER_ID_COLUMN + " = ?";   // "SELECT * FROM space_marine WHERE id = ? AND user_id = ?"
    private final String INSERT_LAB = "INSERT INTO " +
            DatabaseHandler.LAB_TABLE + " (" +
            DatabaseHandler.LAB_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_MINIMAL_POINT_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_TUNED_IN_WORK_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_AVERAGE_POINT_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_DIFFICULTY_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_DISCIPLINE_ID_COLUMN + ", " +
            DatabaseHandler.LAB_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?::difficulty, ?, ?)";

    private final String DELETE_LAB_BY_ID = "DELETE FROM " + DatabaseHandler.LAB_TABLE +
            " WHERE " + DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_NAME_BY_ID = "UPDATE " + DatabaseHandler.LAB_TABLE + " SET " +
            DatabaseHandler.LAB_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MINIMAL_POINT_BY_ID = "UPDATE " + DatabaseHandler.LAB_TABLE + " SET " +
            DatabaseHandler.LAB_TABLE_MINIMAL_POINT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TUNED_IN_WORK_BY_ID = "UPDATE " + DatabaseHandler.LAB_TABLE + " SET " +
            DatabaseHandler.LAB_TABLE_TUNED_IN_WORK_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_AVERAGE_POINT_BY_ID = "UPDATE " + DatabaseHandler.LAB_TABLE + " SET " +
            DatabaseHandler.LAB_TABLE_AVERAGE_POINT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_DIFFICULTY_BY_ID = "UPDATE " + DatabaseHandler.LAB_TABLE + " SET " +
            DatabaseHandler.LAB_TABLE_DIFFICULTY_COLUMN + " = ?::difficulty" + " WHERE " +
            DatabaseHandler.LAB_TABLE_ID_COLUMN + " = ?";

    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_LAB_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_LAB_WORK_ID_COLUMN + " = ?";

    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_LAB_WORK_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_LAB_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_LAB_WORK_ID_COLUMN + " = ?";
    
    // DISCIPLINE_TABLE
    private final String SELECT_ALL_DISCIPLINE = "SELECT * FROM " + DatabaseHandler.DISCIPLINE_TABLE;
    private final String SELECT_DISCIPLINE_BY_ID = SELECT_ALL_DISCIPLINE +
            " WHERE " + DatabaseHandler.DISCIPLINE_TABLE_ID_COLUMN + " = ?";

    private final String INSERT_DISCIPLINE = "INSERT INTO " +
            DatabaseHandler.DISCIPLINE_TABLE + " (" +
            DatabaseHandler.DISCIPLINE_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.DISCIPLINE_TABLE_LABS_COUNT_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_DISCIPLINE_BY_ID = "UPDATE " + DatabaseHandler.DISCIPLINE_TABLE + " SET " +
            DatabaseHandler.DISCIPLINE_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.DISCIPLINE_TABLE_LABS_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.DISCIPLINE_TABLE_ID_COLUMN + " = ?";

//    private final String DELETE_CHAPTER_BY_ID = "DELETE FROM " + DatabaseHandler.CHAPTER_TABLE +
//            " WHERE " + DatabaseHandler.CHAPTER_TABLE_ID_COLUMN + " = ?";

    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    private LabWork createLab(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DatabaseHandler.LAB_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.LAB_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByLabID(id);
        ZonedDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.LAB_TABLE_CREATION_DATE_COLUMN).toLocalDateTime().atZone(ZoneId.systemDefault());
        long minimalPoint = resultSet.getLong(DatabaseHandler.LAB_TABLE_MINIMAL_POINT_COLUMN);
        int tunedInWork = resultSet.getInt(DatabaseHandler.LAB_TABLE_TUNED_IN_WORK_COLUMN);
        int averagePoint = resultSet.getInt(DatabaseHandler.LAB_TABLE_AVERAGE_POINT_COLUMN);
        Difficulty difficulty = Difficulty.valueOf(resultSet.getString(DatabaseHandler.LAB_TABLE_DIFFICULTY_COLUMN));
        Discipline discipline = getDisciplineByID(resultSet.getLong(DatabaseHandler.DISCIPLINE_TABLE_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseHandler.LAB_TABLE_USER_ID_COLUMN));
        return new LabWork(
                id,
                name,
                coordinates,
                creationDate,
                minimalPoint,
                tunedInWork,
                averagePoint,
                difficulty,
                discipline,
                owner
        );
    }

    public LinkedList<LabWork> getCollection() throws DatabaseHandlingException {
        LinkedList<LabWork> labCollection = new LinkedList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_ALL_LABS, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                labCollection.add(createLab(resultSet));
            }
            labCollection.sort(Comparator.comparingInt(LabWork::getId));
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return labCollection;
    }

    private int getDisciplineIDByLabID(long labID) throws SQLException {
        int disciplineID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_LAB_BY_ID, false);
            preparedStatement.setLong(1, labID);
            ResultSet resultSet = preparedStatement.executeQuery();
            AppServer.LOGGER.info("Выполнен запрос SELECT_LAB_BY_ID.");
            if (resultSet.next()) {
                disciplineID = resultSet.getInt(DatabaseHandler.LAB_TABLE_DISCIPLINE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_MARINE_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return disciplineID;
    }

    private Coordinates getCoordinatesByLabID(int id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_LAB_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_LAB_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return coordinates;
    }

    private Discipline getDisciplineByID(long id) throws SQLException {
        Discipline discipline;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_DISCIPLINE_BY_ID, false);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                discipline = new Discipline(
                        resultSet.getString(DatabaseHandler.DISCIPLINE_TABLE_NAME_COLUMN),
                        resultSet.getInt(DatabaseHandler.DISCIPLINE_TABLE_LABS_COUNT_COLUMN)
                );
            } else throw new SQLException();

        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_MARINE_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return discipline;
    }

    public LabWork insertLab(LabRaw labRaw, User user) throws DatabaseHandlingException {
        LabWork labWork;
        PreparedStatement preInsertLabStmt = null;
        PreparedStatement preInsertCoordinatesStmt = null;
        PreparedStatement preInsertDisciplineStmt = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            ZonedDateTime creationTime = ZonedDateTime.now();

            preInsertLabStmt = databaseHandler.getPreparedStatement(INSERT_LAB, true);
            preInsertCoordinatesStmt = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preInsertDisciplineStmt = databaseHandler.getPreparedStatement(INSERT_DISCIPLINE, true);

            preInsertDisciplineStmt.setString(1, labRaw.getDiscipline().getName());
            preInsertDisciplineStmt.setInt(2, labRaw.getDiscipline().getLabsCount());

            if (preInsertDisciplineStmt.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedDisciplineKeys = preInsertDisciplineStmt.getGeneratedKeys();
            long disciplineID;
            if (generatedDisciplineKeys.next()) {
                disciplineID = generatedDisciplineKeys.getLong(1);
            } else throw new SQLException();
            AppServer.LOGGER.info("Выполнен запрос INSERT_DISCIPLINE.");

            preInsertLabStmt.setString(1, labRaw.getName());
            preInsertLabStmt.setTimestamp(2, Timestamp.valueOf(creationTime.toLocalDateTime()));
            preInsertLabStmt.setLong(3, labRaw.getMinimalPoint());
            preInsertLabStmt.setInt(4, labRaw.getTunedInWorks());
            preInsertLabStmt.setInt(5, labRaw.getAveragePoint());
            preInsertLabStmt.setString(6, labRaw.getDifficulty().toString());
            preInsertLabStmt.setLong(7, disciplineID);
            preInsertLabStmt.setLong(8, databaseUserManager.getUserIdByUsername(user));
            if (preInsertLabStmt.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedLabKeys = preInsertLabStmt.getGeneratedKeys();
            int labID;
            if (generatedLabKeys.next()) {
                labID = generatedLabKeys.getInt(1);
            } else throw new SQLException();
            AppServer.LOGGER.info("Выполнен запрос INSERT_LAB.");

            preInsertCoordinatesStmt.setInt(1, labID);
            preInsertCoordinatesStmt.setInt(2, labRaw.getCoordinates().getX());
            preInsertCoordinatesStmt.setLong(3, labRaw.getCoordinates().getY());
            if (preInsertCoordinatesStmt.executeUpdate() == 0) throw new SQLException();
            AppServer.LOGGER.info("Выполнен запрос INSERT_COORDINATES.");

            labWork = new LabWork(
                    labID,
                    labRaw.getName(),
                    labRaw.getCoordinates(),
                    creationTime,
                    labRaw.getMinimalPoint(),
                    labRaw.getTunedInWorks(),
                    labRaw.getAveragePoint(),
                    labRaw.getDifficulty(),
                    labRaw.getDiscipline(),
                    user
            );
            databaseHandler.commit();
            return labWork;
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении группы запросов на добавление нового объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preInsertLabStmt);
            databaseHandler.closePreparedStatement(preInsertCoordinatesStmt);
            databaseHandler.closePreparedStatement(preInsertDisciplineStmt);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * @param labRaw Marine raw.
     * @param labID  Id of Marine.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void updateLabByID(int labID, LabRaw labRaw) throws DatabaseHandlingException {
        // TODO: Если делаем орден уникальным, тут че-то много всего менять
        PreparedStatement preUpdateNameByIDStmt = null;
        PreparedStatement preUpdateCoordinatesByIDStmt = null;
        PreparedStatement preUpdateMinimalPointByIDStmt = null;
        PreparedStatement preUpdateTunedInWorkedByIdStmt = null;
        PreparedStatement preUpdateAveragePointByIDStmt = null;
        PreparedStatement preUpdateDifficultyByIDStmt = null;
        PreparedStatement preUpdateDisciplineByIDStmt = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preUpdateNameByIDStmt = databaseHandler.getPreparedStatement(UPDATE_NAME_BY_ID, false);
            preUpdateCoordinatesByIDStmt = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_LAB_ID, false);
            preUpdateMinimalPointByIDStmt = databaseHandler.getPreparedStatement(UPDATE_MINIMAL_POINT_BY_ID, false);
            preUpdateTunedInWorkedByIdStmt = databaseHandler.getPreparedStatement(UPDATE_TUNED_IN_WORK_BY_ID, false);
            preUpdateAveragePointByIDStmt = databaseHandler.getPreparedStatement(UPDATE_AVERAGE_POINT_BY_ID, false);
            preUpdateDifficultyByIDStmt = databaseHandler.getPreparedStatement(UPDATE_DIFFICULTY_BY_ID, false);
            preUpdateDisciplineByIDStmt = databaseHandler.getPreparedStatement(UPDATE_DISCIPLINE_BY_ID, false);

            if (labRaw.getName() != null) {
                preUpdateNameByIDStmt.setString(1, labRaw.getName());
                preUpdateNameByIDStmt.setInt(2, labID);
                if (preUpdateNameByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_NAME_BY_ID.");
            }
            if (labRaw.getCoordinates() != null) {
                preUpdateCoordinatesByIDStmt.setInt(1, labRaw.getCoordinates().getX());
                preUpdateCoordinatesByIDStmt.setLong(2, labRaw.getCoordinates().getY());
                preUpdateCoordinatesByIDStmt.setInt(3, labID);
                if (preUpdateCoordinatesByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_COORDINATES_BY_LAB_ID.");
            }
            if (labRaw.getMinimalPoint() != null) {
                preUpdateMinimalPointByIDStmt.setLong(1, labRaw.getMinimalPoint());
                preUpdateMinimalPointByIDStmt.setInt(2, labID);
                if (preUpdateMinimalPointByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_MINIMAL_POINT_BY_ID.");
            }
            if (labRaw.getTunedInWorks() != null) {
                preUpdateTunedInWorkedByIdStmt.setInt(1, labRaw.getTunedInWorks());
                preUpdateTunedInWorkedByIdStmt.setInt(2, labID);
                if (preUpdateTunedInWorkedByIdStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_TUNED_IN_WORK_BY_ID.");
            }
            if (labRaw.getAveragePoint() != null) {
                preUpdateAveragePointByIDStmt.setInt(1, labRaw.getAveragePoint());
                preUpdateAveragePointByIDStmt.setInt(2, labID);
                if (preUpdateAveragePointByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_AVERAGE_POINT_BY_ID.");
            }
            if (labRaw.getDifficulty() != null) {
                preUpdateDifficultyByIDStmt.setString(1, labRaw.getDifficulty().toString());
                preUpdateDifficultyByIDStmt.setInt(2, labID);
                if (preUpdateDifficultyByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_DIFFICULTY_BY_ID.");
            }
            if (labRaw.getDiscipline() != null) {
                preUpdateDisciplineByIDStmt.setString(1, labRaw.getDiscipline().getName());
                preUpdateDisciplineByIDStmt.setLong(2, labRaw.getDiscipline().getLabsCount());
                preUpdateDisciplineByIDStmt.setInt(3, getDisciplineIDByLabID(labID));
                if (preUpdateDisciplineByIDStmt.executeUpdate() == 0) throw new SQLException();
                AppServer.LOGGER.info("Выполнен запрос UPDATE_DISCIPLINE_BY_ID.");
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preUpdateNameByIDStmt);
            databaseHandler.closePreparedStatement(preUpdateCoordinatesByIDStmt);
            databaseHandler.closePreparedStatement(preUpdateMinimalPointByIDStmt);
            databaseHandler.closePreparedStatement(preUpdateTunedInWorkedByIdStmt);
            databaseHandler.closePreparedStatement(preUpdateAveragePointByIDStmt);
            databaseHandler.closePreparedStatement(preUpdateDifficultyByIDStmt);
            databaseHandler.closePreparedStatement(preUpdateDisciplineByIDStmt);
            databaseHandler.setNormalMode();
        }
    }
    
    public boolean checkLabUserID(int labID, User user) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_LAB_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, labID);
            preparedStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса SELECT_LAB_BY_ID_AND_USER_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
    }

    public void deleteLabByID(int labID) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(DELETE_LAB_BY_ID, false);
            preparedStatement.setLong(1, getDisciplineIDByLabID(labID));
            if (preparedStatement.executeUpdate() == 0) Outputer.println(3);
            AppServer.LOGGER.info("Выполнен запрос DELETE_DISCIPLINE_BY_ID.");
        } catch (SQLException exception) {
            AppServer.LOGGER.severe("Произошла ошибка при выполнении запроса DELETE_DISCIPLINE_BY_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
    }


    public void clearCollection() throws DatabaseHandlingException {
        LinkedList<LabWork> labList = getCollection();
        for (LabWork labWork : labList) {
            deleteLabByID(labWork.getId());
        }
    }
}
