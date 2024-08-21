package data;

import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void updateLoanStatus(int playerId, int loanAmount, int remainingGames) throws SQLException, SQLException {
        String updateLoanQuery = "UPDATE play_wallet SET loan = ?, loan_amount = ?, remaining_games = ? WHERE player_id = ?";

        try {
            connection = DatabaseUtil.getConnection();
            preparedStatement = connection.prepareStatement(updateLoanQuery);
            preparedStatement.setBoolean(1, true);  // loan을 1로 설정 (대출 존재)
            preparedStatement.setInt(2, loanAmount);
            preparedStatement.setInt(3, remainingGames);
            preparedStatement.setLong(4, playerId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("대출 정보가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("대출 정보 업데이트에 실패했습니다.");
            }

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
