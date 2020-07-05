package Main;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileReader;
import Service.Handler.FileWriters;
import Service.SettleSalary;
import Service.Validation.CheckDepositBalanceNotEnough;
import model.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) throws InterruptedException {
        BasicConfigurator.configure();
        log.info("Start create 1000 Random Pay And Balance .");
        createRandomPayAndBalance();
        log.info("1000 Random Pay And Balance generated .");
        FileReader fileReader = new FileReader();
        List<PayEntity> payEntities = fileReader.getPaysEntities();
        String debtorNumber = fileReader.getDebtorNumber(payEntities);
        BigDecimal debtorMoney = fileReader.getDebtorMoney(payEntities);
        int countPayEntity = payEntities.size();
        List<BalanceEntity> balanceEntities = fileReader.getBalanceEntities();
        List<TransactionEntity> transactionEntities = fileReader.getTransactionEntities();
        try {
            CheckDepositBalanceNotEnough checkNegativeDepositBalance = new CheckDepositBalanceNotEnough();
            checkNegativeDepositBalance.check(payEntities, balanceEntities);
        } catch (DepositBalanceNotEnough depositBalanceNotEnough) {
            depositBalanceNotEnough.printStackTrace();
            return;
        }
        SettleSalary threads[] = new SettleSalary[countPayEntity];
        SettleSalary.setDebtorNumber(debtorNumber);
        SettleSalary.setBalanceEntities(balanceEntities);
        SettleSalary.setDebtorSumMoney(debtorMoney);
        SettleSalary.setTransactionEntities(transactionEntities);
        for (int i = 0; i < countPayEntity; i++) {
            threads[i] = new SettleSalary(payEntities.get(i));
            threads[i].start();
        }
        SettleSalary.calculatorDebtorMoney(balanceEntities);
        WriteAgentToBalance writeAgentToBalance = new WriteAgentToBalance();
        writeAgentToBalance.setBalanceEntities(balanceEntities);
        writeAgentToBalance.start();
        WriteAgentToTransaction writeAgentToTransaction = new WriteAgentToTransaction();
        writeAgentToTransaction.setTransactionEntities(transactionEntities);
        WriteAgentToTransaction.setCount(countPayEntity);
        writeAgentToTransaction.run();
    }

    public static void createRandomPayAndBalance() {
        List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
        List<PayEntity> payEntities = new ArrayList<PayEntity>();
        List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();
        Random rand = new Random();
        BigDecimal sumOfSalary = BigDecimal.ZERO;
        BalanceEntity debtor = new BalanceEntity();
        debtor.setAmount(new BigDecimal("10000000000000000000"));
        debtor.setDepositNumber("1.10.100.1");
        balanceEntities.add(debtor);
        for (int i = 0; i < 10000; i++) {
            int depositNumberPartOne = rand.nextInt(7) + 2;
            int depositNumberPartTwo = rand.nextInt(899) + 100;
            int depositNumberPartThree = rand.nextInt(899) + 100;
            int depositNumberPartFour = rand.nextInt(89) + 10;

            BigDecimal max = new BigDecimal("100000000");
            BigDecimal randFromDouble = new BigDecimal(Math.random());
            BigDecimal amount = randFromDouble.multiply(max);
            amount = amount
                    .setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal max2 = new BigDecimal("1000000");
            BigDecimal randFromDouble2 = new BigDecimal(Math.random());
            BigDecimal creatorSalary = randFromDouble2.multiply(max2);
            creatorSalary = creatorSalary
                    .setScale(0, BigDecimal.ROUND_DOWN);
            sumOfSalary = sumOfSalary.add(creatorSalary);
            BalanceEntity balanceEntity = new BalanceEntity();
            balanceEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            balanceEntity.setAmount(amount);
            balanceEntity.setCheckedWrite(false);
            balanceEntities.add(balanceEntity);
            PayEntity payEntity = new PayEntity();
            payEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            payEntity.setDepositType("creditor");
            payEntity.setAmount(creatorSalary);
            payEntities.add(payEntity);
            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setAmount(null);
            transactionEntity.setCreditorDepositNumber("def");
            transactionEntity.setDebtorDepositNumber("def");
            transactionEntity.setChecked(false);
            transactionEntities.add(transactionEntity);
        }
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(null);
        transactionEntity.setCreditorDepositNumber("end");
        transactionEntity.setDebtorDepositNumber("end");
        transactionEntity.setChecked(false);
        transactionEntities.add(transactionEntity);
        PayEntity debtorForPay = new PayEntity();
        debtorForPay.setAmount(sumOfSalary);
        debtorForPay.setDepositType("debtor");
        debtorForPay.setDepositNumber("1.10.100.1");
        payEntities.add(debtorForPay);
        FileWriters fileWriters = new FileWriters();
        try {
            fileWriters.writeToBalance(balanceEntities);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DepositBalanceNotEnough e) {
            e.printStackTrace();
        }
        try {
            fileWriters.writeToPay(payEntities);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DepositBalanceNotEnough e) {
            e.printStackTrace();
        }
        try {
            fileWriters.writeToTransaction(transactionEntities);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DepositBalanceNotEnough e) {
            e.printStackTrace();
        }
    }

}
