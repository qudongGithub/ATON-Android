package com.platon.aton.component.ui.contract;

import com.platon.aton.component.ui.IContext;
import com.platon.aton.component.ui.presenter.Direction;
import com.platon.aton.entity.ClaimRewardRecord;

import java.util.List;

public class ClaimRecordContract {

    public interface View extends IContext {

        void getRewardTransactionsResult(List<ClaimRewardRecord> newClaimRewardRecordList);

        void finishLoadMore();

        void finishRefresh();

    }

    public interface Presenter extends IPresenter<ClaimRecordContract.View> {

        void getRewardTransactions(@Direction String direction);
    }
}
