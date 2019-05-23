package com.juzix.wallet.component.adapter;

import android.content.Context;
import android.widget.TextView;

import com.juzhen.framework.util.NumberParserUtils;
import com.juzix.wallet.R;
import com.juzix.wallet.component.adapter.base.ViewHolder;
import com.juzix.wallet.component.widget.AutoTextView;
import com.juzix.wallet.engine.WalletManager;
import com.juzix.wallet.entity.VotedCandidate;
import com.juzix.wallet.utils.BigDecimalUtil;
import com.juzix.wallet.utils.DateUtil;

import java.util.List;

/**
 * @author matrixelement
 */
public class VoteDetailListAdapter extends CommonAdapter<VotedCandidate> {

    public VoteDetailListAdapter(int layoutId, List<VotedCandidate> datas) {
        super(layoutId, datas);
    }

    @Override
    protected void convert(Context context, ViewHolder viewHolder, VotedCandidate item, int position) {
        viewHolder.setText(R.id.tv_create_time, DateUtil.format(NumberParserUtils.parseLong(item.getTransactionTime()), DateUtil.DATETIME_FORMAT_PATTERN_WITH_SECOND));

        double invalidVoteNum = BigDecimalUtil.sub(NumberParserUtils.parseDouble(item.getTotalTicketNum()), NumberParserUtils.parseDouble(item.getValidNum()));
        viewHolder.setText(R.id.tv_valid_and_invalid_ticket, String.format("%s/%s", NumberParserUtils.getPrettyNumber(item.getValidNum(), 0), NumberParserUtils.getPrettyNumber(invalidVoteNum, 0)));

        viewHolder.setText(R.id.tv_ticket_price, context.getString(R.string.amount_with_unit, NumberParserUtils.getPrettyNumber(item.getPrice(), 0)));
        viewHolder.setText(R.id.tv_vote_staked_and_unstaked, String.format("%s/%s", NumberParserUtils.getPrettyNumber(item.getLocked(), 0), BigDecimalUtil.div(BigDecimalUtil.mul(item.getValidNum(), item.getPrice()).doubleValue(), 1E18)));
        viewHolder.setText(R.id.tv_vote_profit, context.getString(R.string.amount_with_unit, NumberParserUtils.getPrettyNumber(BigDecimalUtil.div(item.getEarnings(), "1E18"), 4)));
        TextView tv = (AutoTextView) viewHolder.getView(R.id.tv_wallet_address_and_name);
        tv.setText(String.format("%s(%s)", item.getWalletAddress(), WalletManager.getInstance().getWalletNameByWalletAddress(item.getWalletAddress()))); //表示钱包名称

        long time = NumberParserUtils.parseLong(item.getDeadLine());
        boolean exceedExpireTime = time > System.currentTimeMillis();
        viewHolder.setText(R.id.tv_time_desc, exceedExpireTime ? context.getString(R.string.estimatedTime) : context.getString(R.string.actualExpirationTime));
        viewHolder.setText(R.id.tv_expire_time, DateUtil.format(time, DateUtil.DATETIME_FORMAT_PATTERN_WITH_SECOND));
    }

}
