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
public class WriteAgentToBalance extends Thread {
    static Logger log = Logger.getLogger(WriteAgentToBalance.class.getName());
    private List<BalanceEntity> balanceEntities;

    public List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        this.balanceEntities = balanceEntities;
    }

    @Override
    public void run() {
        int counter = 0;
        for (BalanceEntity balanceEntity : this.balanceEntities) {
            if (balanceEntity.isCheckedWrite()) {
                counter++;
            }
        }
        if (counter == this.balanceEntities.size() - 1) {
            try {
                writeToBalance();
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

    private void writeToBalance() throws InterruptedException {
        try {
            FileWriters fileWriters = new FileWriters();
            fileWriters.writeToBalance(this.balanceEntities);
            log.info("write to balance");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DepositBalanceNotEnough e) {
            e.printStackTrace();
        }
    }

}

