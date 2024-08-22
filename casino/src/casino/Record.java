package casino;

import data.RecordDAO;

import java.sql.Connection;

public class Record {

    public static void showKing(Connection connection) {
        RecordDAO recordDAO = new RecordDAO(connection);
        recordDAO.showWins();
        recordDAO.showLoses();
    }
}
