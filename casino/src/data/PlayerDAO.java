package data;

import db.DatabaseUtil;
import model.CustomerInfo;
import model.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public int createUser(String name) {
        final String createUserQuery = "INSERT INTO player (name) VALUES (?)"; // 쿼리 수정

        try {
            connection = DatabaseUtil.getConnection(); // 새로운 연결 가져오기
            preparedStatement = connection.prepareStatement(createUserQuery);
            preparedStatement.setString(1, name);
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public CustomerInfo getUserInfo(String playerName) {
        final String balanceQuery = "SELECT id, name FROM player WHERE name = ?"; // name 컬럼 추가

        try {
            connection = DatabaseUtil.getConnection(); // 새로운 연결 가져오기
            preparedStatement = connection.prepareStatement(balanceQuery);
            preparedStatement.setString(1, playerName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                return new CustomerInfo(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
