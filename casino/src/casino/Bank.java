package casino;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public static long bankLoan (Connection connection, long playerId, long balance) throws SQLException {
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
                return balance;
            }
            System.out.println("1,000만원을 빌려드립니다. 지금으로부터 10판이 끝나면 자동으로 상환됩니다.");

            // DB 업데이트
            updateLoanStatus(connection, playerId, 1000000, 10);

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

    private static void updateLoanStatus(Connection connection, long playerId, int loanAmount, int remainingGames) throws SQLException, SQLException {
        String updateLoanQuery = "UPDATE play_wallet SET loan = ?, loan_amount = ?, remaining_games = ? WHERE player_id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateLoanQuery);
        updateStatement.setBoolean(1, true);  // loan을 1로 설정 (대출 존재)
        updateStatement.setInt(2, loanAmount);
        updateStatement.setInt(3, remainingGames);
        updateStatement.setLong(4, playerId);

        int rowsUpdated = updateStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("대출 정보가 성공적으로 업데이트되었습니다.");
        } else {
            System.out.println("대출 정보 업데이트에 실패했습니다.");
        }
        updateStatement.close();
    }

}
