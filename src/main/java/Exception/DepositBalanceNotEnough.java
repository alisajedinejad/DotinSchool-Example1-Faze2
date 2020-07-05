package Exception;

public class DepositBalanceNotEnough extends Exception {
    public DepositBalanceNotEnough() {
    }

    public DepositBalanceNotEnough(String depositNumber) {
        super("Account that has deposit number " + depositNumber + " has not enough balance to pay");
    }
}
