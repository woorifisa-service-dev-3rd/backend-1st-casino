package casino;

import data.PlayerDAO;
import db.DatabaseUtil;
import model.CustomerInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Player {
    private static final int balance = 1_000_000; // 1000만원

    public static void main(String[] args) throws SQLException {
        PlayerDAO playerDAO = new PlayerDAO();
        CustomerInfo customerInfo;
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Casino!");

        Connection connection = DatabaseUtil.getConnection();
        System.out.println("connection = " + connection);

        System.out.print("이름을 입력하세요: ");
        String name = sc.nextLine();
        System.out.print("아이디를 입력하세요: ");
        int id = sc.nextInt();

        // player 입장!
//        String insertPlayerQuery = "INSERT INTO player (name) VALUES (?)";
//        PreparedStatement insertPlayerStatement = connection.prepareStatement(insertPlayerQuery, PreparedStatement.RETURN_GENERATED_KEYS);
//        insertPlayerStatement.setString(id, name);
        int rowsAffected = playerDAO.createUser(name, id);
        System.out.println(rowsAffected);
        if (rowsAffected > 0) {
            new CustomerInfo(id, name);
            System.out.println("여기냐");

        } else {
            System.out.println("계정 생성에 실패했습니다. 다시 시도해주세요.");
        }

        handleMenu(connection, sc, name);
//        menuSelect(connection, name);

    }

    // 메뉴 선택 함수
    private static void handleMenu(Connection connection, Scanner sc, String name) throws SQLException {
        String playerIdQuery = "SELECT id FROM player WHERE name = ?";
        PreparedStatement playerIdStatement = connection.prepareStatement(playerIdQuery);
        playerIdStatement.setString(1, name);
        ResultSet playerIdResultSet = playerIdStatement.executeQuery();

        if (playerIdResultSet.next()) {
            long playerId = playerIdResultSet.getLong("id");

            // play_wallet에서 잔액 가져오기
            String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
            PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
            balanceStatement.setLong(1, playerId);
            ResultSet balanceResultSet = balanceStatement.executeQuery();

            if (balanceResultSet.next()) {
                int balance = balanceResultSet.getInt("balance");

                while (true) {
                    System.out.println();
                    System.out.println(name + "님의 현재 잔액: " + balance);
                    if (balance < 0) {
                        System.out.println("잔액이 부족하여 강제로 종료합니다.");
                        System.exit(0);
                    }
                    System.out.println("==== MENU ====");
                    System.out.println("1. 게임시작");
                    System.out.println("2. 대출하기");
                    System.out.println("3. 오늘의 게임왕 보기");
                    System.out.println("4. 종료");

                    int choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.println(name + "님 게임을 시작합니다.");
                            //게임 시작 코드
                            Casino casino = new Casino(balance, (int) playerId);
                            casino.gameRun();
                            break;
                        case 2:
                            System.out.println(name + "님 은행으로 이동합니다.");
                            // 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
                            long newBalance = Bank.bankLoan(balance);
                            System.out.println("대출 후 잔액: " + newBalance);

                            // 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
                            int remainingGames = Bank.leftGameCnt();
                            System.out.println("남은 판 수: " + remainingGames);

                            // 업데이트된 잔액을 play_wallet 테이블에 저장
                            String updateQuery = "UPDATE play_wallet SET balance = ? WHERE player_id = ?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setLong(1, newBalance);
                            updateStatement.setLong(2, playerId);
                            updateStatement.executeUpdate();

                            updateStatement.close();
                            balance = (int) newBalance;

                            break;
                        case 3:
                            System.out.println("오늘의 게임왕입니다!");
                            //게임왕 코드
                            break;
                        case 4:
                            System.out.println("카지노를 떠나시겠습니까? (y/n) : ");
                            String exitCasino = sc.nextLine();
                            if (exitCasino.equalsIgnoreCase("y")) {
                                System.out.println("다음에 또 방문해주세요");
                                return;
                            }
                            break;
                        default:
                            System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                            break;
                    }
                }
            }
        }
    }
}




