package casino;

import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Player {
    private static final int balance = 1_000_000; // 1000만원

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Connection connection = DatabaseUtil.getConnection();

        System.out.println("Welcome to Casino!");

        System.out.print("이름을 입력하세요: ");
        String name = sc.nextLine();

//        System.out.println("connection = " + connection);
        long playerId = createAccount(connection, name);
        createWallet(connection, playerId);

        System.out.println(name + "님의 계정이 생성되었습니다! 즐거운 시간 보내세용");
        handleMenu(connection, sc, name);
//        menuSelect(connection, name);
    }

    // 메뉴 선택 함수
    private static void handleMenu(Connection connection, Scanner sc, String name) throws SQLException {
        long playerId = getPlayerIdByName(connection, name);
        int balance = getPlayerBalance(connection, playerId);

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
                    startGame(name);
                    break;
                case 2:
                    handleLoan(name, connection, playerId, balance);
                    balance = getPlayerBalance(connection, playerId);
                    break;
                case 3:
                    todayKing();
                    break;
                case 4:
                    exitCasino(sc);
                    break;

                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    break;
            }
        }
    }

    // ----------M E N U---------
    // 1. 게임시작
    private static void startGame(String name) {
        System.out.println(name + "님 게임을 시작합니다.");
        // 게임 시작 로직
    }

    // 2. 대출하기
    private static void handleLoan(String name, Connection connection, long playerId, int balance) throws SQLException {
        System.out.println(name + "님 은행으로 이동합니다.");
        // 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
        long newBalance = Bank.bankLoan(balance);
        System.out.println("대출 후 잔액: " + newBalance);

        // 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
        int remainingGames = Bank.leftGameCnt();
        System.out.println("남은 판 수: " + remainingGames);
        updatePlayerBalance(connection, playerId, newBalance);
        balance = (int) newBalance;
    }

    // 3. 오늘의게임황
    private static void todayKing() {
        System.out.println("오늘의 게임왕입니다!");
        //게임왕 코드
    }

    // 4. 종료
    private static boolean exitCasino(Scanner sc) throws SQLException {
        System.out.println("카지노를 떠나시겠습니까? (y/n) : ");
        String exitCasino = sc.nextLine();
        if (exitCasino.equalsIgnoreCase("y")) {
            System.out.println("다음에 또 방문해주세요");
            return true;
        }
        return false;
    }

    //player name으로 id 가져오는 함수
    private static long getPlayerIdByName(Connection connection, String name) throws SQLException {
        String playerIdQuery = "SELECT id FROM player WHERE name = ?";
        PreparedStatement playerIdStatement = connection.prepareStatement(playerIdQuery);
        playerIdStatement.setString(1, name);
        ResultSet playerIdResultSet = playerIdStatement.executeQuery();

        if (playerIdResultSet.next()) {
            long playerId = playerIdResultSet.getLong("id");
            playerIdStatement.close();
            return playerId;
        }
        playerIdStatement.close();
        throw new SQLException();
    }

    // play_wallet에서 잔액 가져오는 함수
    private static int getPlayerBalance(Connection connection, long playerId) throws SQLException {
        String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
        PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
        balanceStatement.setLong(1, playerId);
        ResultSet balanceResultSet = balanceStatement.executeQuery();

        if (balanceResultSet.next()) {
            int balance = balanceResultSet.getInt("balance");
            balanceStatement.close();
            return balance;
        }
        balanceStatement.close();
        throw new SQLException();
    }

    // 업데이트된 잔액을 play_wallet 테이블에 저장하는 함수
    private static void updatePlayerBalance(Connection connection, long playerId, long newBalance) throws SQLException {
        String updateQuery = "UPDATE play_wallet SET balance = ? WHERE player_id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setLong(1, newBalance);
        updateStatement.setLong(2, playerId);
        updateStatement.executeUpdate();
        updateStatement.close();
    }

    // 계정 생성 함수
    private static long createAccount(Connection connection, String name) throws SQLException {
        String insertPlayerQuery = "INSERT INTO player (name) VALUES (?)";
        PreparedStatement insertPlayerStatement = connection.prepareStatement(insertPlayerQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        insertPlayerStatement.setString(1, name);
        int rowsAffected = insertPlayerStatement.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet generatedKeys = insertPlayerStatement.getGeneratedKeys();
            // 방금 생성된 사용자의 ID 가져오기
            if (generatedKeys.next()) {
                long playerId = generatedKeys.getLong(1);
                insertPlayerStatement.close();
                return playerId;
            }
        }
        insertPlayerStatement.close();
        throw new SQLException("계정 생성에 실패했습니다.");
    }

    //월렛 생성 함수
    private static void createWallet(Connection connection, long playerId) throws SQLException {
        // 초기 금액을 10만원으로 설정하여 play_wallet에 데이터 삽입
        String insertWalletQuery = "INSERT INTO play_wallet (player_id, balance, loan, loan_amount, remaining_games) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertWalletStatement = connection.prepareStatement(insertWalletQuery);
        insertWalletStatement.setLong(1, playerId);
        insertWalletStatement.setInt(2, 1000000); // 초기 금액 100만원

        // 대출 받을 시 loan 상태를 true로 바꾸고 금액을 관리할 것
        insertWalletStatement.setBoolean(3, false); // 초기 loan 상태 false
        insertWalletStatement.setInt(4, 0); // 초기 loan 금액
        insertWalletStatement.setInt(5, 0); // 초기 남은 판 수

        int walletRowsAffected = insertWalletStatement.executeUpdate();

        insertWalletStatement.close();

        if (walletRowsAffected <= 0) {
            throw new SQLException("월렛 생성에 실패했습니다.");
        }
    }


}


