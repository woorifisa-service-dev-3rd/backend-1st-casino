package data;

import db.DatabaseUtil;
import model.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerWalletDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

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

    public Wallet selectWallet() {
        // 조회 SQL
        final String selectQuery = "select * from play_wallet";
        // 쿼리 수행 객체 생성 및 쿼리 실행

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(selectQuery);

            // 쿼리 수행 결과값을 가지고 있는 객체(ResultSet)
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int playerId = resultSet.getInt("player_id");
                int balance = resultSet.getInt("balance");
                boolean isLoan = resultSet.getBoolean("loan");
                int loanAmount = resultSet.getInt("loan_amount");
                int remainingGame = resultSet.getInt("remaining_games");

                // DB에서 받아온 데이터를 Todo 모델 객체로 바인딩
                return new Wallet(id, playerId, balance, isLoan, loanAmount, remainingGame);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 반납, 해제(순서->역순)
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
