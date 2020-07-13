package Main;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileReader;
import Service.Handler.FileWriters;
import Service.SettleSalary;
import Service.Validation.CheckDepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) throws InterruptedException {
        ExecutorService executorService =
                new ThreadPoolExecutor(100, 1000000, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());
        BasicConfigurator.configure();
        log.info("Start create 1000 Random Pay And Balance .");
        createRandomPayAndBalance();
        log.info("1000 Random Pay And Balance generated .");
        FileReader fileReader = new FileReader();
        List<PayEntity> payEntities = fileReader.getPaysEntities();
        List<BalanceEntity> balanceEntities = fileReader.getBalanceEntities();
        try {
            CheckDepositBalanceNotEnough checkNegativeDepositBalance = new CheckDepositBalanceNotEnough();
            checkNegativeDepositBalance.check(payEntities, balanceEntities);
        } catch (DepositBalanceNotEnough depositBalanceNotEnough) {
            depositBalanceNotEnough.printStackTrace();
            return;
        }
        SettleSalary.setBalanceEntities(balanceEntities);
        SettleSalary.setPayEntities(payEntities);
        FileWriters.setBalanceEntities(balanceEntities);
        List<Callable<String>> callableTasks = new ArrayList<Callable<String>>();
        for (PayEntity payEntity : payEntities) {
            SettleSalary settleSalary = new SettleSalary();
            settleSalary.setPayEntity(payEntity);
            callableTasks.add(settleSalary);
        }
        executorService.invokeAll(callableTasks);
        executorService.shutdown();
    }

    public static void createRandomPayAndBalance() {
        List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
        List<PayEntity> payEntities = new ArrayList<PayEntity>();
        Random rand = new Random();
        BigDecimal sumOfSalary = BigDecimal.ZERO;
        BalanceEntity debtor = new BalanceEntity();
        debtor.setAmount(new BigDecimal("1000000000"));
        debtor.setDepositNumber("1.10.100.1");
        balanceEntities.add(debtor);
        for (int i = 0; i < 1000; i++) {
            int depositNumberPartOne = rand.nextInt(7) + 2;
            int depositNumberPartTwo = rand.nextInt(899) + 100;
            int depositNumberPartThree = rand.nextInt(899) + 100;
            int depositNumberPartFour = rand.nextInt(89) + 10;

            BigDecimal max = new BigDecimal("100000");
            BigDecimal randFromDouble = new BigDecimal(Math.random());
            BigDecimal amount = randFromDouble.multiply(max);
            amount = amount
                    .setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal max2 = new BigDecimal("1000");
            BigDecimal randFromDouble2 = new BigDecimal(Math.random());
            BigDecimal creatorSalary = randFromDouble2.multiply(max2);
            creatorSalary = creatorSalary
                    .setScale(0, BigDecimal.ROUND_DOWN);
            sumOfSalary = sumOfSalary.add(creatorSalary);
            BalanceEntity balanceEntity = new BalanceEntity();
            balanceEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            balanceEntity.setAmount(amount);
            balanceEntities.add(balanceEntity);
            PayEntity payEntity = new PayEntity();
            payEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            payEntity.setDepositType("creditor");
            payEntity.setAmount(creatorSalary);
            payEntities.add(payEntity);
        }
        PayEntity debtorForPay = new PayEntity();
        debtorForPay.setAmount(sumOfSalary);
        debtorForPay.setDepositType("debtor");
        debtorForPay.setDepositNumber("1.10.100.1");
        payEntities.add(debtorForPay);
        FileWriters fileWriters = new FileWriters();

        try {
            fileWriters.writeToBalance(balanceEntities);
            fileWriters.writeToPay(payEntities);
        } catch (IOException | DepositBalanceNotEnough e) {
            e.printStackTrace();
        }

    }
}
