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

    public void updateRemainingGames(int count, int playerId) {
        // 조회 SQL
        final String updateQuery = "update play_wallet set remaining_games = ? where player_id = ?";

        // 쿼리 수행 객체 생성 및 쿼리 실행
        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, count);
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

    public void updateIsLoan(int playerId, int balance) {
        // 조회 SQL
        final String updateQuery = "update play_wallet set loan = ?, remaining_games = ?, balance = ? where player_id = ?";
        final int addLoanMoney = 1000000;   //100만원
        // 쿼리 수행 객체 생성 및 쿼리 실행
        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, balance + addLoanMoney);
            preparedStatement.setInt(4, playerId);

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

    public void initPlayerWallet(int playerId) {
        String insertWalletQuery = "INSERT INTO play_wallet (player_id, balance, loan, loan_amount, remaining_games) VALUES (?, ?, ?, ?, ?)";

        // 쿼리 수행 객체 생성 및 쿼리 실행
        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(insertWalletQuery);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setLong(2, 1000000); // 초기 금액 100만원
            // 대출 받을 시 loan 상태를 true로 바꾸고 금액을 관리할 것
            preparedStatement.setBoolean(3, false); // 초기 loan 상태 false
            preparedStatement.setInt(4, 0); // 초기 loan 금액
            preparedStatement.setInt(5, 0); // 초기 남은 판 수

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

    public Wallet selectWallet(int palyerId) {
        // 조회 SQL
        final String selectQuery = "select * from play_wallet where player_id =?";
        // 쿼리 수행 객체 생성 및 쿼리 실행

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, palyerId);

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
/*

        // play_wallet에서 잔액 가져오기
//        String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
//        PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
//        balanceStatement.setLong(1, playerId);
//        ResultSet balanceResultSet = balanceStatement.executeQuery();


//        if (balanceResultSet.next()) {
//        int balance = balanceResultSet.getInt("balance");
 */

}
