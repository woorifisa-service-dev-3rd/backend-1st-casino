package model;

public class Wallet {
    int id;             //walletId
    int playerId;
    int balance;        //잔액
    boolean loan;       //  대출여부
    int loanAmount = 0;     //대출 잔액(대출 시)
    int remainingGame;  //남은 판수(대출시)

    public Wallet(int id, int playerId, int balance, boolean loan, int loanAmount, int remainingGame) {
        this.id = id;
        this.playerId = playerId;
        this.balance = balance;
        this.loan = loan;
        this.loanAmount = loanAmount;
        this.remainingGame = remainingGame;
    }
}
