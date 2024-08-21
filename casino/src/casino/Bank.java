package casino;

import java.util.Scanner;

/**
 * 은행 대출을 담당하는 사람들
 */
public class Bank {
    // 플레이어 잔액 가져오기
    // 대출 선택 시 1000만원 대출
    // 10판 지나면 1000만원 자동 상환

    // private long playerBalance;
    private static int leftGame = 10;
    private static boolean hasLoan = false;

    public static long bankLoan (long balance) {
        System.out.println("===========");
        System.out.println("대출하기를 선택하셨습니다.");
        System.out.println("대출하기를 선택하시면, 1회에 한하여 1,000만원을 빌려드리며");
        System.out.println("앞으로 10판 이내에 모두 상환하셔야 합니다.");
        System.out.println("10판이 종료되면 1,000만원을 회수하며, 회수 시 잔액이 부족하면 더 이상 게임에 참여할 수 없습니다.\n계속하시겠습니까?(y/n)");
        Scanner input = new Scanner(System.in);
        String loanYesNo = input.nextLine();
        if (loanYesNo.equals("y")) {
            if (hasLoan) {
                System.out.println("이미 1회 대출하셨습니다. 대출이 불가능합니다.");
                // 처음으로 돌아가기
            }
            System.out.println("1,000만원을 빌려드립니다. 지금으로부터 10판이 끝나면 자동으로 상환됩니다.");
            balance += 1000000;
            hasLoan = true;
            // 게임 시작
        } else if (loanYesNo.equals("n")) {
            System.out.println("취소합니다.");
        } else {
            System.out.println("잘못 선택하셨습니다.");
        }
        // 처음 화면으로 돌아가기
        return balance;
    }

    public static int leftGameCnt() {
        leftGame -= 1;
        return leftGame;
    }

}
