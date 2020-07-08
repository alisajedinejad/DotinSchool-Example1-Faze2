package Service.Handler;

import Exception.DepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWriters {
    static Logger log = Logger.getLogger(FileWriters.class.getName());
    private static List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
    private static List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();

    public void writeToBalance(List<BalanceEntity> balanceEntities) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (BalanceEntity balanceEntity : balanceEntities) {
            stringBuilder.append(balanceEntity.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(balanceEntity.getAmount());
            stringBuilder.append("\n");
        }
        FileWriter myWriter = new FileWriter("DataBase/balance.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    public void writeToBalance(BalanceEntity balanceEntity) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (BalanceEntity balanceEntity1 : FileWriters.balanceEntities) {
            if (balanceEntity1.getDepositNumber().equals(balanceEntity.getDepositNumber())) {
                balanceEntity1.setAmount(balanceEntity.getAmount());
                break;
            }
        }
        for (BalanceEntity balanceEntity2 : FileWriters.balanceEntities) {
            stringBuilder.append(balanceEntity2.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(balanceEntity2.getAmount());
            stringBuilder.append("\n");
        }
        FileWriter myWriter = new FileWriter("DataBase/balance.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    public void writeToTransaction(List<TransactionEntity> transactionEntities) throws DepositBalanceNotEnough, IOException {

        StringBuilder stringBuilder = new StringBuilder();
        File myObj = new File("DataBase/transactions.txt");
        if (myObj.createNewFile()) {
            log.info("File created: " + myObj.getName());
        }
        int i = 0;
        for (TransactionEntity transactionEntity : transactionEntities) {
            stringBuilder.append(transactionEntity.getDebtorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getCreditorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getAmount());
            stringBuilder.append("\n");
        }
        FileWriter myWriter = new FileWriter("DataBase/transactions.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    public void writeToTransaction(TransactionEntity transactionEntity) throws DepositBalanceNotEnough, IOException {
        File myObj = new File("DataBase/transactions.txt");
        if (myObj.createNewFile()) {
            log.info("File created: " + myObj.getName());
        }


        StringBuilder stringBuilder = new StringBuilder();


        FileWriters.transactionEntities.add(transactionEntity);


        for (TransactionEntity transactionEntity1 : FileWriters.transactionEntities) {
            stringBuilder.append(transactionEntity1.getDebtorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity1.getCreditorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity1.getAmount());
            stringBuilder.append("\n");
        }
        FileWriter myWriter = new FileWriter("DataBase/transactions.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    public void writeToPay(List<PayEntity> payEntities) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (PayEntity payEntity : payEntities) {
            stringBuilder.append(payEntity.getDepositType());
            stringBuilder.append("\t");
            stringBuilder.append(payEntity.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(payEntity.getAmount());
            stringBuilder.append("\n");
        }
        FileWriter myWriter = new FileWriter("DataBase/pay.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    public static List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public static void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        FileWriters.balanceEntities = balanceEntities;
    }
}
