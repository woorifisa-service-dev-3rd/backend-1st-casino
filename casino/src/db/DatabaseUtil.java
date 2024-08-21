package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//db와 관련된 공통적인 처리 코드들을 별도의 유틸 클래스로 분리
public class DatabaseUtil {


    public static Connection getConnection() {

//		Properties prop = new Properties();

        try {
            // 외부 라이브러리 불러오기
//            Properties prop = DBConfigure.readProperties(argument);

            final String USER_NAME = "root";
            final String PASSWORD = "1234";
            final String DB_URL = "jdbc:mysql://localhost/casino";

            Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
