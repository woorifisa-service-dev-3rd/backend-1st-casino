package casino;

import data.PlayerDAO;
import data.PlayerWalletDAO;
import model.CustomerInfo;
import model.Wallet;

import java.util.Scanner;

public class Player {

    private static Scanner sc = new Scanner(System.in);
    private static PlayerDAO playerDAO = new PlayerDAO();
    private static PlayerWalletDAO playerWalletDAO = new PlayerWalletDAO();

    public static void main(String[] args) {
        String name;

        System.out.println("Welcome to Casino!");

        name = inputName();

        int rowsAffected = playerDAO.createUser(name);
        createUser(rowsAffected, name);

    }

    private static void createUser(int rowsAffected, String name) {

        System.out.println(rowsAffected);
        if (rowsAffected > 0) {
            int userId = getUserId(name);
            playerWalletDAO.initPlayerWallet(userId);
            initWallet(userId, name);
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

    private static void initWallet(int playerId, String name) {
        while (true) {
            //지갑 정보 불러오기
            Wallet getPlayerWallet = playerWalletDAO.selectWallet(playerId);
            int balance = getPlayerWallet.getBalance();

            System.out.println();
            System.out.println(name + "님의 현재 잔액: " + balance);

            if (balance < 0) {
                System.out.println("잔액이 부족하여 강제로 종료합니다.");
                System.exit(0);
            }
            //대출을 받았으면서
            checkUserGameStatus(getPlayerWallet);
            printMenu();
            selectMenu(name, balance, playerId);
        }
    }

    private static void checkUserGameStatus(Wallet getPlayerWallet) {
        int loanAmount = getPlayerWallet.getLoanAmount();
        boolean loan = getPlayerWallet.isLoan();
        int balance = getPlayerWallet.getBalance();
        int remainingGame = getPlayerWallet.getRemainingGame();

        if (loan == true && (balance < 0 || loanAmount < 0)) {
            System.out.println("돈이 없습니다. 나가게 됩니다.");
            System.exit(0);
        }
        if (remainingGame < 0) {
            System.out.println("대출 받은 이후 게임을 10판 하셨습니다. 대출금을 상환하세요");
            System.exit(0);
        }
    }


    private static int getUserId(String name) {
        CustomerInfo customerInfo = playerDAO.getUserInfo(name);

//        Formatter formatter = new Formatter();
        int playerId = customerInfo.getPlayerId();
//        int balance = 1_000_000; // 1000만원
        return playerId;
    }

    private static void selectMenu(String name, int balance, int playerId) {
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println(name + "님 게임을 시작합니다.");
                //게임 시작 코드
                Casino casino = new Casino(balance, playerId);
                casino.gameRun();
                break;
            case 2:
//                System.out.println(name + "님 은행으로 이동합니다.");
//                // 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
//                long newBalance = Bank.bankLoan(balance);
//                System.out.println("대출 후 잔액: " + newBalance);
//
//                // 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
//                int remainingGames = Bank.leftGameCnt();
//                System.out.println("남은 판 수: " + remainingGames);
//
//                // 업데이트된 잔액을 play_wallet 테이블에 저장
//                playerWalletDAO.updateBalance((int) newBalance, playerId);
//
//                break;
                //여기를 뭔가 다시 짜야될거 같은데 어떡하지
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






