package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordDAO {
    private final Connection connection;

    public RecordDAO(Connection connection) {
        this.connection = connection;
    }

    public void updateWinsLoses(int playerId, int winsMoney, int losesMoney) {
        String updateQuery = "INSERT INTO record (player_id, wins, loses) VALUES (?, ?, ?)";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, playerId);
            updateStatement.setInt(2, winsMoney);
            updateStatement.setInt(3, losesMoney);

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWins() {
        String showWinsQuery = "SELECT player_id, SUM(wins) AS total_wins FROM record GROUP BY player_id ORDER BY total_wins DESC LIMIT 1";

        try (PreparedStatement updateWinsStatement = connection.prepareStatement(showWinsQuery);
            ResultSet resultSet = updateWinsStatement.executeQuery();
        ) {

            if (resultSet.next()) {
                int playerId = resultSet.getInt("player_id");
                int totalWins = resultSet.getInt("total_wins");
                System.out.println("가장 많이 돈을 획득한 플레이어 ID: " + playerId + ", 총 획득 금액: " + totalWins);
            } else {
                System.out.println("기록이 없습니다.");
            }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showLoses() {
        String showLosesQuery = "SELECT player_id, SUM(loses) AS total_loses FROM record GROUP BY player_id ORDER BY total_loses DESC LIMIT 1";

        try (PreparedStatement updateLosesStatement = connection.prepareStatement(showLosesQuery);
            ResultSet resultSet = updateLosesStatement.executeQuery();
        ) {

                if (resultSet.next()) {
                    int playerId = resultSet.getInt("player_id");
                    int totalLoses = resultSet.getInt("total_loses");
                    System.out.println("가장 많이 돈을 잃은 플레이어 ID: " + playerId + ", 총 손해 금액: " + totalLoses);
                } else {
                    System.out.println("기록이 없습니다.");
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
