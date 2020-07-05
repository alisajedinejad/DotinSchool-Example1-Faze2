package model;

import java.math.BigDecimal;

public class BalanceEntity {
    private String depositNumber;
    private BigDecimal amount;
    private boolean checkedWrite;

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isCheckedWrite() {
        return checkedWrite;
    }

    public void setCheckedWrite(boolean checkedWrite) {
        this.checkedWrite = checkedWrite;
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
                ", checkedWrite=" + checkedWrite +
                '}';
    }
}
