package casino;

import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Player {

    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        System.out.println("connection = " + connection);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from player");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        System.out.println(resultSet.getString("name"));

        System.out.println("여기서 어쩌고 run하면 됩니다.");

    }
}
