package casino;

import data.PlayerWalletDAO;
import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Player {

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Casino!");

        Connection connection = DatabaseUtil.getConnection();
//        System.out.println("connection = " + connection);

        Scanner input = new Scanner(System.in);
        System.out.print("이름을 입력하세요: ");
        String name = input.nextLine();

        // player 입장!
        String insertPlayerQuery = "INSERT INTO player (name) VALUES (?)";
        PreparedStatement insertPlayerStatement = connection.prepareStatement(insertPlayerQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        insertPlayerStatement.setString(1, name);
        int rowsAffected = insertPlayerStatement.executeUpdate();

        if (rowsAffected > 0) {
            // 방금 생성된 사용자의 ID 가져오기
            ResultSet generatedKeys = insertPlayerStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int playerId = generatedKeys.getInt(1);

                // 초기 금액을 10만원으로 설정하여 play_wallet에 데이터 삽입
                String insertWalletQuery = "INSERT INTO play_wallet (player_id, balance, loan, loan_amount, remaining_games) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertWalletStatement = connection.prepareStatement(insertWalletQuery);
                insertWalletStatement.setInt(1, playerId);
                insertWalletStatement.setLong(2, 1000000); // 초기 금액 100만원

                // 대출 받을 시 loan 상태를 true로 바꾸고 금액을 관리할 것
                insertWalletStatement.setBoolean(3, false); // 초기 loan 상태 false
                insertWalletStatement.setInt(4, 0); // 초기 loan 금액
                insertWalletStatement.setInt(5, 0); // 초기 남은 판 수

                int walletRowsAffected = insertWalletStatement.executeUpdate();

                if (walletRowsAffected > 0) {
                    System.out.println(name + "님의 계정이 생성되었습니다! 즐거운 시간 보내세용");
                } else {
                    System.out.println("월렛 생성에 실패했습니다. 다시 시도해주세요.");
                }

                insertWalletStatement.close();
            }
        } else {
            System.out.println("계정 생성에 실패했습니다. 다시 시도해주세요.");
        }

        handleMenu(connection, sc, name);
    }

    // 메뉴 선택 함수
    private static void handleMenu(Connection connection, Scanner sc, String name) throws SQLException {
        String playerIdQuery = "SELECT id FROM player WHERE name = ?";
        PreparedStatement playerIdStatement = connection.prepareStatement(playerIdQuery);
        playerIdStatement.setString(1, name);
        ResultSet playerIdResultSet = playerIdStatement.executeQuery();
        Casino casino;

        if (playerIdResultSet.next()) {
            int playerId = playerIdResultSet.getInt("id");

            while (true) {
                // play_wallet에서 잔액 가져오기
                String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
                PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
                balanceStatement.setInt(1, playerId);
                ResultSet balanceResultSet = balanceStatement.executeQuery();

                if (balanceResultSet.next()) {
                    int balance = balanceResultSet.getInt("balance");

                    System.out.println();
                    System.out.println(name + "님의 현재 잔액: " + balance);
                    if (balance <= 0) {
                        System.out.println("잔액이 부족하여 강제로 종료합니다.");
                        System.exit(0);
                    }
                    System.out.println("==== MENU ====");
                    System.out.println("1. 게임시작");
                    System.out.println("2. 대출하기");
                    System.out.println("3. 게임왕과 게임거지 보기");
                    System.out.println("4. 종료");

                    int choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.println(name + "님 게임을 시작합니다.");
                            casino = new Casino(balance, playerId);
                            casino.gameRun(connection);
                            break;
                        case 2:
                            System.out.println(name + "님 은행으로 이동합니다.");
                            // 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
                            int newBalance = Bank.bankLoan(connection, playerId, balance);
                            System.out.println("대출 후 잔액: " + newBalance);

                            // 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
//                            int remainingGames = Bank.leftGameCnt();
//                            System.out.println("남은 판 수: " + remainingGames);

                            PlayerWalletDAO playerWalletDAO = new PlayerWalletDAO();
                            playerWalletDAO.updateBalance(newBalance, playerId);

                            break;
                        case 3:
                            System.out.println("게임왕과 게임거지 입니다!");
                            Record.showKing(connection);
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




