package com.juzix.wallet.entity;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

@IntDef({
        TransferType.SEND,
        TransferType.RECEIVE,
        TransferType.TRANSFER
})
public @interface TransferType {

    /**
     * 发送
     */
    int SEND = 0;
    /**
     * 接收
     */
    int RECEIVE = 1;
    /**
     * 转账
     */
    int TRANSFER = 2;
}
