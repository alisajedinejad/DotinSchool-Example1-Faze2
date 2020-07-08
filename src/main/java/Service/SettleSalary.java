package Service;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileWriters;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SettleSalary implements Callable {
    private static List<BalanceEntity> balanceEntities;
    private static List<PayEntity> payEntities;
    private static String debtorDepositNumber;
    private List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();
    private PayEntity payEntity;
    public static boolean permition = true;
    public static int debtorCounter;
    public static boolean debtorPermition = true;
    public static boolean permitionForCunstructor = true;
    public static boolean permitionForTransaction = true;
    public static BigDecimal debtorSumMoney;

    public SettleSalary() {
        if (permitionForCunstructor) {
            permitionForCunstructor = false;
            for (PayEntity payEntity : this.payEntities) {
                if (payEntity.getDepositType().equals("debtor")) {
                    this.debtorDepositNumber = payEntity.getDepositNumber();
                }
            }
        }
    }

    public static List<PayEntity> getPayEntities() {
        return payEntities;
    }

    public static void setPayEntities(List<PayEntity> payEntities) {
        SettleSalary.payEntities = payEntities;
    }

    public List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }

    public static List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public static void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        SettleSalary.balanceEntities = balanceEntities;
    }

    public PayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(PayEntity payEntity) {
        this.payEntity = payEntity;
    }

    public static String getDebtorDepositNumber() {
        return debtorDepositNumber;
    }

    public static void setDebtorDepositNumber(String debtorDepositNumber) {
        SettleSalary.debtorDepositNumber = debtorDepositNumber;
    }

    @Override
    public String toString() {
        return "SettleSalary{" +
                "balanceEntities=" + balanceEntities +
                ", payEntities=" + payEntity
                +
                '}';
    }

    public String call() throws IOException, DepositBalanceNotEnough, InterruptedException {
        BasicConfigurator.configure();
        if (payEntity.getDepositType().equals("creditor")) {
            String creatorNumber = payEntity.getDepositNumber();
            BigDecimal creatorMoney = payEntity.getAmount();
            for (BalanceEntity balanceEntity : this.balanceEntities) {
                if (balanceEntity.getDepositNumber().equals(creatorNumber)) {
                    balanceEntity.setAmount(balanceEntity.getAmount().add(creatorMoney));
                    setToBalance(balanceEntity);
                    setToDebtor(creatorMoney);
                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setDebtorDepositNumber(debtorDepositNumber);
                    transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                    transactionEntity.setAmount(creatorMoney);
                    setToTransaction(transactionEntity);
                    break;
                }
            }
        }

        return null;
    }

    public synchronized void setToBalance(BalanceEntity balanceEntity) throws InterruptedException, DepositBalanceNotEnough, IOException {

        if (permition) {
            permition = false;

            FileWriters fileWriters = new FileWriters();

            fileWriters.writeToBalance(balanceEntity);
            permition = true;

        } else {
            Thread.sleep(1);
            setToBalance(balanceEntity);
        }


    }

    public synchronized void setToDebtor(BigDecimal DebtorMoney) throws InterruptedException, DepositBalanceNotEnough, IOException {

        if (SettleSalary.debtorSumMoney == null) {
            SettleSalary.debtorSumMoney = BigDecimal.ZERO;

        }
        if (debtorPermition) {
            debtorPermition = false;

            debtorCounter++;
            SettleSalary.debtorSumMoney = SettleSalary.debtorSumMoney.add(DebtorMoney);

            if (debtorCounter == balanceEntities.size() - 1) {
                for (BalanceEntity balanceEntity : SettleSalary.balanceEntities) {
                    if (balanceEntity.getDepositNumber().equals(SettleSalary.debtorDepositNumber)) {
                        BigDecimal nowDebtorMoney = balanceEntity.getAmount();
                        balanceEntity.setAmount(nowDebtorMoney.subtract(SettleSalary.debtorSumMoney));
                        setToBalance(balanceEntity);
                        break;
                    }

                }
            }


            debtorPermition = true;


        } else {
            Thread.sleep(1);
            setToDebtor(DebtorMoney);
        }


    }

    public synchronized void setToTransaction(TransactionEntity transaction) throws InterruptedException, DepositBalanceNotEnough, IOException {

        if (permitionForTransaction) {
            permitionForTransaction = false;
            FileWriters fileWriters = new FileWriters();
            fileWriters.writeToTransaction(transaction);
            permitionForTransaction = true;
        } else {
            Thread.sleep(1);
            setToTransaction(transaction);
        }
    }
}



