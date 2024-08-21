package model;

/**
 * 사용자의 정보를 담는 data class
 */
public class CustomerInfo {
	int playerId;
	int playerName;
	public int getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getPlayerName() {
		return playerName;
	}
	public void setPlayerName(int playerName) {
		this.playerName = playerName;
	}
	

}
