package casino;

import model.CustomerInfo;

import java.util.HashMap;
import java.util.Scanner;

public class Player {
    private static HashMap<String, Integer> playerIds = new HashMap<>();
    private static HashMap<String, Integer> playerBalances = new HashMap<>();
    private static int idCounter = 1;
    private static final int INITIAL_BALANCE = 10_000_000; // 1000만원

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Casino!");

        while (true) {
            String playerName = getPlayerName(sc);
            int playerId = getPlayerID(playerName);
            initialBalance(playerName);

            System.out.println(playerName + "님 환영합니다!");
            System.out.println(playerName + "의 잔액은" + playerBalances + "원 입니다.");

            if (!handleMenu(sc, playerName)) {
                break;
            }
        }
        sc.close();
    }

    //player 이름 입력 함수 2-5자리
    private static String getPlayerName(Scanner sc) {

        while (true) {
            System.out.println("이름을 입력해 주세요(최대 5자리) : ");
            String playerName = sc.nextLine();

            if (playerName.length() > 5 || playerName.length() < 2) {
                System.out.println("이름은 2자리 이상 5자리 이하로 입력해야합니다. 다시 시도해 주세요.");
            } else {
                return playerName;
            }
        }
    }

    //player name 생성에 따른 고유 id 생성 함수
    private static int getPlayerID(String playerName) {
        if (playerIds.containsKey(playerName)) {
            return playerIds.get(playerName);
        } else {
            playerIds.put(playerName, idCounter);
            return idCounter++;
        }
    }

    // 새 player 생성시 잔액 1000만원 지급 함수
    private static void initialBalance(String playerName) {
        if (!playerBalances.containsKey(playerName)) {
            playerBalances.put(playerName, INITIAL_BALANCE);
        }
    }

    // 메뉴 선택 함수
    private static boolean handleMenu(Scanner sc, String playerName) {
        while (true) {
            System.out.println("메뉴를 선택해주세요 :");
            System.out.println("a. 게임시작");
            System.out.println("b. 대출하기");
            System.out.println("c. 오늘의 게임왕 보기");
            System.out.println("d. 종료");

            String choice = sc.nextLine();

            switch (choice) {
                case "a":
                    System.out.println(playerName + "님 게임을 시작합니다.");
                    //게임 시작 코드
                    break;
                case "b":
                    System.out.println(playerName + "님 은행으로 이동합니다.");
                    //대출 코드
                    break;
                case "c":
                    System.out.println("오늘의 게임왕입니다!");
                    //게임왕 코드
                    break;
                case "d":
                    System.out.println("카지노를 떠나시겠습니까? (y/n) : ");
                    String exitCasino = sc.nextLine();
                    if (exitCasino.equalsIgnoreCase("y")) {
                        System.out.println("다음에 또 방문해주세요");
                        return false;
                    }
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    continue;
            }
            System.out.println("다른 메뉴를 선택하시겠습니까? (y/n)");
            String continueMenu = sc.nextLine();
            if (continueMenu.equalsIgnoreCase("n")) {
                return true;
            }
        }
    }

    private static boolean exitCasino(Scanner sc) {
        System.out.println("카지노를 떠나시겠습니까? (y/n): ");
        String exitCasino = sc.nextLine();
        if (exitCasino.equalsIgnoreCase("y")) {
            System.out.println("다음에 또 방문해 주세요.");
            return true; // 프로그램 종료
        }
        return false;
    }
}




