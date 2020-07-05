package model;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileWriters;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ali on 05/07/2020.
 */
public class WriteAgentToTransaction extends Thread {
    static Logger log = Logger.getLogger(WriteAgentToBalance.class.getName());
    private List<TransactionEntity> transactionEntities;
    private static int count;

    public List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        WriteAgentToTransaction.count = count;
    }

    public void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    @Override
    public void run() {
        int counter = 0;
        for (TransactionEntity transactionEntity : this.transactionEntities) {
            if (transactionEntity.isChecked()) {
                counter++;
            }

        }
        if (counter == WriteAgentToTransaction.count - 1) {
            try {
                writeToTransaction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }
    }

    private void writeToTransaction() throws InterruptedException {
        try {
            FileWriters fileWriters = new FileWriters();
            fileWriters.writeToTransaction(this.transactionEntities);
            log.info("write to transaction");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DepositBalanceNotEnough e) {
            e.printStackTrace();
        }
    }
}

