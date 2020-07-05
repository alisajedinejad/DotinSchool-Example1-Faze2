package model;

import java.math.BigDecimal;

public class PayEntity {
    private String depositType;
    private String depositNumber;
    private BigDecimal amount;

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

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
        return "PayEntity{" +
                "depositType='" + depositType + '\'' +
                ", depositNumber='" + depositNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
