package casino;

import data.PlayerDAO;
import data.PlayerWalletDAO;
import model.CustomerInfo;
import model.Wallet;
import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Player {

    private static Scanner sc = new Scanner(System.in);
    private static PlayerDAO playerDAO = new PlayerDAO();
    private static PlayerWalletDAO playerWalletDAO = new PlayerWalletDAO();

    public static void main(String[] args) throws SQLException {
        String name;
        Connection connection = DatabaseUtil.getConnection();

        System.out.println("Welcome to Casino!");

        name = inputName();

        int rowsAffected = playerDAO.createUser(name);
        createUser(connection, rowsAffected, name);

    }

    private static void createUser(Connection connection, int rowsAffected, String name) throws SQLException {

        if (rowsAffected > 0) {
            int userId = getUserId(name);
            playerWalletDAO.initPlayerWallet(userId);
            initWallet(connection, userId, name);
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
        System.out.println("3. 게임왕과 게임거지 보기");
        System.out.println("4. 종료");
    }

    private static void initWallet(Connection connection, int playerId, String name) throws SQLException {
        while (true) {
            //지갑 정보 불러오기
            Wallet getPlayerWallet = playerWalletDAO.selectWallet(playerId);
            int balance = getPlayerWallet.getBalance();

            System.out.println();
            System.out.println(name + "님의 현재 잔액: " + balance);

            if (balance <= 0) {
                System.out.println("잔액이 부족하여 강제로 종료합니다.");
                System.exit(0);
            }

            printMenu();
            selectMenu(connection, name, getPlayerWallet);
        }
    }

        private static int getUserId (String name){
            CustomerInfo customerInfo = playerDAO.getUserInfo(name);

            int playerId = customerInfo.getPlayerId();
            return playerId;
        }

        private static void selectMenu(Connection connection, String name, Wallet playerInfo) throws SQLException {
            int choice = sc.nextInt();
            int balance = playerInfo.getBalance();
            int playerId = playerInfo.getPlayerId();
            Casino casino;
            switch (choice) {
                case 1:
                    // player_id로 loan을 조회해서 1이라면, 그때부터 10판 세기 시작
                    // 게임 한 판 마다 1씩 감소
                    // 게임 시작 코드
                    System.out.println(name + "님 게임을 시작합니다.");
                    //게임 시작 코드
                    casino = new Casino(balance, playerId);
                    casino.gameRun(connection);
                    break;

                case 2:
                    System.out.println(name + "님 은행으로 이동합니다.");
                    System.out.println(name + "님의 잔액은 " + balance);
                    int newBalance = Bank.bankLoan(connection, playerId, balance);

                    PlayerWalletDAO playerWalletDAO = new PlayerWalletDAO();
                    playerWalletDAO.updateBalance(newBalance, playerId);
                    for (int i = 10; i > 0; i--) {
                        Wallet getPlayerWallet = playerWalletDAO.selectWallet(playerId);
                        System.out.println("대출 후 10판을 할 수 있으며 현재 " + i + "판 남았습니다.");
                        System.out.println("현재 잔액은 " + getPlayerWallet.getBalance() + "원 입니다.");
                        if (getPlayerWallet.getRemainingGame() > 0 && getPlayerWallet.getBalance() > 0) {
                            casino = new Casino(getPlayerWallet.getBalance(), playerId);
                            casino.gameRun(connection);
                            playerWalletDAO.updateRemainingGames(i, playerId);
                        } else if (getPlayerWallet.getBalance() <= 0) {
                            System.out.println("잔액이 0원 이하여서 강제 종료 됩니다.");
                            return;
                        } else if (getPlayerWallet.getRemainingGame() <= 0) {
                            System.out.println("대출 받고 난 후 10게임이 끝났습니다.");
                            return;
                        } else {
                            System.out.println("개발자가 개발을 잘 못했으니 집에 가세요");
                            return;
                        }
//                    System.out.println("잔액 " + getPlayerWallet.getBalance());
                    }
//                    System.out.println("대출 후 잔액: " + newBalance);
                    break;
                case 3:
                    System.out.println("게임왕과 게임거지 입니다!");
                    Record.showKing(connection);
                    break;
                case 4:
                    System.out.println("카지노를 떠나시겠습니까? (y/n) ");
                    Scanner sc = new Scanner(System.in);
                    String exitCasino = sc.nextLine();
                    if (exitCasino.equalsIgnoreCase("y")) {
                        System.out.println("다음에 또 방문해주세요");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    break;
            }
        }
    }







