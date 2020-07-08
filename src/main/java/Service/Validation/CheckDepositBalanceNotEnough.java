package Service.Validation;

import Exception.DepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;

import java.math.BigDecimal;
import java.util.List;

public class CheckDepositBalanceNotEnough extends DepositBalanceNotEnough {
    public void check(List<PayEntity> payEntities, List<BalanceEntity> balanceEntities) throws DepositBalanceNotEnough {
        BigDecimal debtorBalance = new BigDecimal("0");
        BigDecimal sumOfCreditorBalance = new BigDecimal("0");
        String debtorDepositNumber = "";
        for (PayEntity payEntity : payEntities) {
            if (payEntity.getDepositType().equals("debtor")) {
                debtorDepositNumber = payEntity.getDepositNumber();
                break;
            }
        }
        for (BalanceEntity balanceEntity : balanceEntities) {
            if (balanceEntity.getDepositNumber().equals(debtorDepositNumber)) {
                debtorBalance = balanceEntity.getAmount();
                break;
            }
        }
        for (PayEntity payEntity : payEntities) {
            if (!payEntity.getDepositNumber().equals(debtorDepositNumber))
                sumOfCreditorBalance = sumOfCreditorBalance.add(payEntity.getAmount());
        }

        if (sumOfCreditorBalance.compareTo(debtorBalance) > 0)
            throw new DepositBalanceNotEnough(debtorDepositNumber);
    }
}
