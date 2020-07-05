package Service.Handler;

import Exception.DepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FileWriters {
    static Logger log = Logger.getLogger(FileWriters.class.getName());

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

    public void writeToTransaction(List<TransactionEntity> transactionEntities) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File myObj = new File("DataBase/transactions.txt");
        if (myObj.createNewFile()) {
            log.info("File created: " + myObj.getName());
        }
        int i=0;
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
}
