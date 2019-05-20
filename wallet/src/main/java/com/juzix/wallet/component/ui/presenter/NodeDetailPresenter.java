package com.juzix.wallet.component.ui.presenter;

import com.juzhen.framework.network.ApiRequestBody;
import com.juzhen.framework.network.ApiResponse;
import com.juzhen.framework.network.ApiSingleObserver;
import com.juzix.wallet.R;
import com.juzix.wallet.app.LoadingTransformer;
import com.juzix.wallet.component.ui.base.BasePresenter;
import com.juzix.wallet.component.ui.contract.NodeDetailContract;
import com.juzix.wallet.component.ui.view.SubmitVoteActivity;
import com.juzix.wallet.engine.IndividualWalletManager;
import com.juzix.wallet.engine.NodeManager;
import com.juzix.wallet.engine.ServerUtils;
import com.juzix.wallet.engine.VoteManager;
import com.juzix.wallet.entity.CandidateDetailEntity;
import com.juzix.wallet.entity.CandidateEntity;
import com.juzix.wallet.entity.IndividualWalletEntity;
import com.juzix.wallet.utils.BigDecimalUtil;
import com.juzix.wallet.utils.RxUtils;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author matrixelement
 */
public class NodeDetailPresenter extends BasePresenter<NodeDetailContract.View> implements NodeDetailContract.Presenter {

    private String mCandidateId;
    private CandidateDetailEntity mCandidateDetailEntity;

    public NodeDetailPresenter(NodeDetailContract.View view) {
        super(view);
        mCandidateId = getView().getCandidateIdFromIntent();
    }

    @Override
    public void getNodeDetailInfo() {
        if (isViewAttached()) {
            ServerUtils
                    .getCommonApi()
                    .getCandidateDetail(NodeManager.getInstance().getChainId(), ApiRequestBody.newBuilder()
                            .put("nodeId", mCandidateId)
                            .build())
                    .compose(RxUtils.bindToLifecycle(getView()))
                    .compose(RxUtils.getSingleSchedulerTransformer())
                    .compose(LoadingTransformer.bindToSingleLifecycle(currentActivity()))
                    .subscribe(new ApiSingleObserver<CandidateDetailEntity>() {
                        @Override
                        public void onApiSuccess(CandidateDetailEntity candidateDetailEntity) {
                            if (isViewAttached()) {
                                mCandidateDetailEntity = candidateDetailEntity;
                                getView().showNodeDetailInfo(candidateDetailEntity);
                            }
                        }

                        @Override
                        public void onApiFailure(ApiResponse response) {

                        }
                    });
        }
    }

    @Override
    public void voteTicket() {
        if (isViewAttached()) {
            ArrayList<IndividualWalletEntity> walletEntityList = IndividualWalletManager.getInstance().getWalletList();
            if (walletEntityList.isEmpty()) {
                showLongToast(R.string.voteTicketCreateWalletTips);
                return;
            }

            Flowable
                    .fromIterable(walletEntityList)
                    .map(new Function<IndividualWalletEntity, Double>() {

                        @Override
                        public Double apply(IndividualWalletEntity individualWalletEntity) throws Exception {
                            return individualWalletEntity.getBalance();
                        }
                    })
                    .reduce(new BiFunction<Double, Double, Double>() {
                        @Override
                        public Double apply(Double aDouble, Double aDouble2) throws Exception {
                            return BigDecimalUtil.add(aDouble, aDouble2);
                        }
                    })
                    .subscribe(new Consumer<Double>() {
                        @Override
                        public void accept(Double totalBalance) throws Exception {
                            if (totalBalance <= 0) {
                                showLongToast(R.string.voteTicketInsufficientBalanceTips);
                            } else {
                                if (mCandidateDetailEntity != null){
                                    SubmitVoteActivity.actionStart(currentActivity(), mCandidateId,mCandidateDetailEntity.getName());
                                }
                            }
                        }
                    });
        }
    }
}
