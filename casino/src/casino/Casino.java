package casino;

import dev.service.cloud.Console;

import java.util.Random;
import java.util.Scanner;

/**
 * casino의 게임을 진행시킬
 * controler라고 생각하면 됨
 */

public class Casino {
    Scanner scn = new Scanner(System.in);
    int balance;    //player의 잔액을 나타내는

    public Casino(int balance) {    //사용자의 잔액을 받아오는
        this.balance = balance;
    }

    public void gameRun() {
        int money = askBetAmount();

        boolean oddEvenCorrect = isOddEvenCorrect(getRandomNumber(), askOddEven());
    }

    private int askBetAmount() {
        System.out.println("얼마를 거실건가요?");

        return Integer.parseInt(scn.nextLine());
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

    private int calculateBetOutcome(){


        return
    }

}
