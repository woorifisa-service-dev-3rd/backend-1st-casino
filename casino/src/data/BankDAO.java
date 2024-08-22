package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAO {
    private final Connection connection;

    public BankDAO(Connection connection) {
        this.connection = connection;
    }

    public void updateLoanStatus(int playerId, int loanAmount, int remainingGames) throws SQLException {
        String updateLoanQuery = "UPDATE play_wallet SET loan = ?, loan_amount = ?, remaining_games = ? WHERE player_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateLoanQuery)) {
            updateStatement.setBoolean(1, true);  // loan을 1로 설정 (대출 존재)
            updateStatement.setInt(2, loanAmount);
            updateStatement.setInt(3, remainingGames);
            updateStatement.setInt(4, playerId);

            // 쿼리 실행
            updateStatement.executeUpdate();

        }

    }

    public void searchLoan(int playerId) {
        String searchLoanQuery = "SELECT loan FROM play_wallet WHERE player_id = ?";
        try (PreparedStatement searchLoanStatement = connection.prepareStatement(searchLoanQuery);
             ResultSet resultSet = searchLoanStatement.executeQuery();
        ) {
            if (resultSet.next()) {
                int hasLoan = resultSet.getInt("loan");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
