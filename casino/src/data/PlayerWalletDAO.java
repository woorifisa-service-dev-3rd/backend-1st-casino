package data;

import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerWalletDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public void updateBalance(int balance, int playerId) {
        // 조회 SQL
        final String updateQuery = "update play_wallet set balance = ? where player_id = ?";

        // 쿼리 수행 객체 생성 및 쿼리 실행
        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, balance);
            preparedStatement.setInt(2, playerId);

            // 쿼리 수행 결과값을 가지고 있는 객체(ResultSet)
            preparedStatement.executeUpdate();

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
    }
}
