package casino;

import data.PlayerDAO;
import model.CustomerInfo;

import java.sql.SQLException;
import java.util.Scanner;

public class Player {
    private static final int balance = 1_000_000; // 1000만원
    private static Scanner sc = new Scanner(System.in);
    private static PlayerDAO playerDAO = new PlayerDAO();

    public static void main(String[] args) throws SQLException {
        String name;

        System.out.println("Welcome to Casino!");

//        Connection connection = DatabaseUtil.getConnection();
//        System.out.println("connection = " + connection);

        name = inputName();

        int rowsAffected = playerDAO.createUser(name);
        userValidate(rowsAffected, name);


//        menuSelect(connection, name);

    }

    private static void userValidate(int rowsAffected, String name) throws SQLException {
        System.out.println(rowsAffected);
        if (rowsAffected > 0) {
//            new CustomerInfo(id, name);
            getUserInfo(name);
            //잘 들어오나 확인하는 곳
        } else {
            System.out.println("계정 생성에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private static String inputName() {
        String name;
        while (true) {
            System.out.print("이름을 입력하세요: ");
            name = sc.nextLine().trim(); // 공백 제거
            if (!name.isEmpty()) { // 빈 문자열 체크
                break;
            }
            System.out.println("이름은 비어 있을 수 없습니다. 다시 입력하세요.");
        }
        return name;
    }

    private static void printMenu() {
        System.out.println("==== MENU ====");
        System.out.println("1. 게임시작");
        System.out.println("2. 대출하기");
        System.out.println("3. 오늘의 게임왕 보기");
        System.out.println("4. 종료");
    }

    // 메뉴 선택 함수
    private static void getUserInfo(String name) {
        CustomerInfo customerInfo = playerDAO.getUserInfo(name);
        int playerId = customerInfo.getPlayerId();

        // play_wallet에서 잔액 가져오기
//        String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
//        PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
//        balanceStatement.setLong(1, playerId);
//        ResultSet balanceResultSet = balanceStatement.executeQuery();


//        if (balanceResultSet.next()) {
//            int balance = balanceResultSet.getInt("balance");

        while (true) {
            System.out.println();
            System.out.println(name + "님의 현재 잔액: " + balance);
            if (balance < 0) {
                System.out.println("잔액이 부족하여 강제로 종료합니다.");
                System.exit(0);
            }
            printMenu();

            int choice = sc.nextInt();
            sc.nextLine();

//                switch (choice) {
//                    case 1:
//                        System.out.println(name + "님 게임을 시작합니다.");
//                        //게임 시작 코드
//                        Casino casino = new Casino(balance, (int) playerId);
//                        casino.gameRun();
//                        break;
//                    case 2:
//                        System.out.println(name + "님 은행으로 이동합니다.");
//                        // 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
//                        long newBalance = Bank.bankLoan(balance);
//                        System.out.println("대출 후 잔액: " + newBalance);
//
//                        // 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
//                        int remainingGames = Bank.leftGameCnt();
//                        System.out.println("남은 판 수: " + remainingGames);
//
//                        // 업데이트된 잔액을 play_wallet 테이블에 저장
//                        String updateQuery = "UPDATE play_wallet SET balance = ? WHERE player_id = ?";
//                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//                        updateStatement.setLong(1, newBalance);
//                        updateStatement.setLong(2, playerId);
//                        updateStatement.executeUpdate();
//
//                        updateStatement.close();
//                        balance = (int) newBalance;
//
//                        break;
//                    case 3:
//                        System.out.println("오늘의 게임왕입니다!");
//                        //게임왕 코드
//                        break;
//                    case 4:
//                        System.out.println("카지노를 떠나시겠습니까? (y/n) : ");
//                        String exitCasino = sc.nextLine();
//                        if (exitCasino.equalsIgnoreCase("y")) {
//                            System.out.println("다음에 또 방문해주세요");
//                            return;
//                        }
//                        break;
//                    default:
//                        System.out.println("잘못된 선택입니다. 다시 시도하세요.");
//                        break;
//                }
//            }

        }
    }
}




