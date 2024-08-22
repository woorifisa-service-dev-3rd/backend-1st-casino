package model;

import lombok.Getter;

/**
 * 사용자의 정보를 담는 data class
 */
@Getter
public class CustomerInfo {
    int playerId;
    String playerName;

    public CustomerInfo(int playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }
}
