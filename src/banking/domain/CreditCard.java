package banking.domain;

public class CreditCard {
    private String cardNumber;
    private String pin;
    private int balance;

    public CreditCard(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = 0;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public int getBalance() {
        return this.balance;
    }

}
