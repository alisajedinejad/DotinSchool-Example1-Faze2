package model;

import java.math.BigDecimal;

/**
 * Created by ali on 30/06/2020.
 */
public class TransactionEntity {
    private String debtorDepositNumber;
    private String creditorDepositNumber;
    private BigDecimal amount;
    private boolean checked;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCreditorDepositNumber() {
        return creditorDepositNumber;
    }

    public void setCreditorDepositNumber(String creditorDepositNumber) {
        this.creditorDepositNumber = creditorDepositNumber;
    }

    public String getDebtorDepositNumber() {
        return debtorDepositNumber;
    }

    public void setDebtorDepositNumber(String debtorDepositNumber) {
        this.debtorDepositNumber = debtorDepositNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "TransactionEntity{" +
                "debtorDepositNumber='" + debtorDepositNumber + '\'' +
                ", creditorDepositNumber='" + creditorDepositNumber + '\'' +
                ", amount=" + amount +
                ", checked=" + checked +
                '}';
    }
}
