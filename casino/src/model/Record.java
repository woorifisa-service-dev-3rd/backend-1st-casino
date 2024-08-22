package model;

import lombok.Getter;

@Getter
public class Record {
    int id;
    int playerId;
    int wins;
    int loses;

    public Record(int id, int playerId, int wins, int loses) {
        this.id = id;
        this.playerId = playerId;
        this.wins = wins;
        this.loses = loses;
    }
}
