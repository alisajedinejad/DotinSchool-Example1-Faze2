package model;

import java.math.BigDecimal;

public class BalanceEntity {
    private String depositNumber;
    private BigDecimal amount;


    public BigDecimal getAmount() {
        return amount;
    }



    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    @Override
    public String toString() {
        return "BalanceEntity{" +
                "depositNumber='" + depositNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
