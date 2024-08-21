package casino;

import data.PlayerWalletDAO;
import data.RecordDAO;

import java.sql.Connection;
import java.util.Random;
import java.util.Scanner;

/**
 * casino의 게임을 진행시킬
 * controler라고 생각하면 됨
 */

public class Casino {
    Scanner scn = new Scanner(System.in);
    PlayerWalletDAO playerWalletDAO = new PlayerWalletDAO();
    int balance;    //player의 잔액을 나타내는
    int playerId;

    public Casino(int balance, int playerId) {    //사용자의 잔액을 받아오는
        this.playerId = playerId;
        this.balance = balance;
    }

    public void gameRun(Connection connection) {
        int money = askBetAmount();

        boolean oddEvenCorrect = isOddEvenCorrect(getRandomNumber(), askOddEven());
        calculateBetOutcome(connection, oddEvenCorrect, money);
    }

    private int askBetAmount() {
        System.out.println("얼마를 거실건가요?");
        int betPrice = Integer.parseInt(scn.nextLine());
        if (betPrice > balance) {
            System.out.println("현재 잔액보다 크게 배팅할 수는 없습니다.");
            askBetAmount();
        }
        return betPrice;
    }

    private String askOddEven() {
        System.out.println("1. 홀 2. 짝 중 하나를 선택해주세요");
        return scn.nextLine();
    }

    private boolean isOddEvenCorrect(int randomNum, String inputNum) {
        if (randomNum % 2 == 0 && inputNum.equals("짝")) {
            return true;
        }
        if (randomNum % 2 != 0 && inputNum.equals("홀")) {
            return true;
        }
        return false;
    }

    private int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(10); // 1부터 10까지의 랜덤 수
    }

    private void calculateBetOutcome(Connection connection, boolean isCorrect, int money) {
        RecordDAO recordDAO = new RecordDAO(connection);
        if (isCorrect) {
            System.out.printf("축하합니다! %d의 돈을 가져갑니다!\n", money * 2);
            playerWalletDAO.updateBalance(balance + money * 2, playerId);
            recordDAO.updateWinsLoses(playerId, money * 2, 0);
        } else {
            System.out.printf("우우~~ 실패! %d를 잃었습니다!\n", money);
            playerWalletDAO.updateBalance(balance - money, playerId);
            recordDAO.updateWinsLoses(playerId, 0, money);
        }
    }

}
