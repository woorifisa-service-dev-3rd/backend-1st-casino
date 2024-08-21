package data;

import db.DatabaseUtil;
import model.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public int createUser(String name, int playerId) {
        final String insertQuery = "INSERT INTO player (id, name) VALUES (id = ? ,name =?)";

        // 쿼리 수행 객체 생성 및 쿼리 실행
        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, name);

            // 쿼리 수행 결과값을 가지고 있는 객체(ResultSet)
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 반납, 해제(순서->역순)
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
