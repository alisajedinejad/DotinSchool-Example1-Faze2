package Service;

import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SettleSalary extends Thread {
    private static List<BalanceEntity> balanceEntities = null;
    private static List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();
    private PayEntity payEntity;
    private static String debtorNumber;
    private static BigDecimal debtorSumMoney;
    private static boolean isWriteToTrans = false;

    public static BigDecimal getDebtorSumMoney() {
        return debtorSumMoney;
    }

    public static void setDebtorSumMoney(BigDecimal debtorSumMoney) {
        SettleSalary.debtorSumMoney = debtorSumMoney;
    }

    public SettleSalary(PayEntity payEntity) {
        this.payEntity = payEntity;
    }

    public static void setDebtorNumber(String debtorNumber) {
        SettleSalary.debtorNumber = debtorNumber;
    }

    public static String getDebtorNumber() {
        return debtorNumber;
    }

    public static List<TransactionEntity> getTransactionEnties() {
        return SettleSalary.transactionEntities;
    }

    public List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public static void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        SettleSalary.transactionEntities = transactionEntities;
    }

    public static void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        SettleSalary.balanceEntities = balanceEntities;
    }

    public PayEntity getPayEntity() {
        return payEntity;
    }

    @Override
    public String toString() {
        return "SettleSalary{" +
                "balanceEntities=" + balanceEntities +
                ", payEntities=" + payEntity +
                '}';
    }

    @Override
    public void run() {
        if (this.payEntity.getDepositType().equals("creditor")) {
            String creatorNumber = this.payEntity.getDepositNumber();
            BigDecimal creatorMoney = this.payEntity.getAmount();
            for (int i = 0; i < balanceEntities.size(); i++) {
                if (balanceEntities.get(i).getDepositNumber().equals(creatorNumber)) {
                    balanceEntities.get(i).setAmount(balanceEntities.get(i).getAmount().add(creatorMoney));
                    balanceEntities.get(i).setCheckedWrite(true);
                    transactionEntities.get(i).setAmount(creatorMoney);
                    transactionEntities.get(i).setDebtorDepositNumber(SettleSalary.debtorNumber);
                    transactionEntities.get(i).setCreditorDepositNumber(balanceEntities.get(i).getDepositNumber());
                    transactionEntities.get(i).setChecked(true);
                    break;
                }
            }
        }
        setBalanceEntities(balanceEntities);
    }

    public static void calculatorDebtorMoney(List<BalanceEntity> balanceEntities) {
        for (BalanceEntity balanceEntity : balanceEntities) {
            if (balanceEntity.getDepositNumber().equals(SettleSalary.debtorNumber)) {
                balanceEntity.setAmount(balanceEntity.getAmount().subtract(SettleSalary.debtorSumMoney));
                break;
            }
        }
    }
}
