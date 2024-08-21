package casino;

import db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Player {

	private static int balance = 1000000;

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		/*
		 * Console.read() // 문자열 입력받는 메소드 Console.readInt() // 정수 입력받는 메소드
		 * Console.readDouble() // 실수 입력받는 메소드
		 * 
		 * 출력할때
		 * 
		 * Console.write() // 문자열/정수/실수 출력하는 메소드 Console.writeln() // 출력 후 줄바꿈도 하고 싶을때
		 * 쓰는 메소드
		 */

		Connection connection = DatabaseUtil.getConnection();
		System.out.println("connection = " + connection);

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
				long playerId = generatedKeys.getLong(1);

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

		menuSelect(connection, name);

		
	}

	public static void menuSelect(Connection connection, String name) throws SQLException {
		String playerIdQuery = "SELECT id FROM player WHERE name = ?";
		PreparedStatement playerIdStatement = connection.prepareStatement(playerIdQuery);
		playerIdStatement.setString(1, name);
		ResultSet playerIdResultSet = playerIdStatement.executeQuery();

		if (playerIdResultSet.next()) {
			long playerId = playerIdResultSet.getLong("id");

			// play_wallet에서 잔액 가져오기
			String balanceQuery = "SELECT balance FROM play_wallet WHERE player_id = ?";
			PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
			balanceStatement.setLong(1, playerId);
			ResultSet balanceResultSet = balanceStatement.executeQuery();

			if (balanceResultSet.next()) {
				int balance = balanceResultSet.getInt("balance");
				System.out.println(name + "님의 현재 잔액: " + balance);

				// 메뉴 선택
				System.out.println("메뉴 선택");
				System.out.println("선택하세요(1, 2, 3)");
				Scanner choice = new Scanner(System.in);
				int playerChoice = choice.nextInt();

				// 2번 돈 빌리기 선택할 경우
				if (playerChoice == 2) {
					// 대출 후 잔액 계산 (예시로 Bank 클래스의 bankLoan 메서드 사용)
					long newBalance = Bank.bankLoan(balance);
					System.out.println("대출 후 잔액: " + newBalance);

					// 남은 판 수 계산 (예시로 Bank 클래스의 leftGameCnt 메서드 사용)
					int remainingGames = Bank.leftGameCnt();
					System.out.println("남은 판 수: " + remainingGames);

					// 업데이트된 잔액을 play_wallet 테이블에 저장
					String updateQuery = "UPDATE play_wallet SET balance = ? WHERE player_id = ?";
					PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
					updateStatement.setLong(1, newBalance);
					updateStatement.setLong(2, playerId);
					updateStatement.executeUpdate();

					updateStatement.close();
				}
				else {
					System.out.println("일단 2만 선택하슈");
				}
			}
		}
	}

}
