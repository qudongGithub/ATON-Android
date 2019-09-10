package com.juzix.wallet.engine;

import com.juzhen.framework.network.ApiRequestBody;
import com.juzhen.framework.network.ApiResponse;
import com.juzix.wallet.entity.AccountBalance;
import com.juzix.wallet.entity.CandidateWrap;
import com.juzix.wallet.entity.DelegateDetail;
import com.juzix.wallet.entity.DelegateHandle;
import com.juzix.wallet.entity.DelegateInfo;
import com.juzix.wallet.entity.Transaction;
import com.juzix.wallet.entity.VerifyNode;
import com.juzix.wallet.entity.VerifyNodeDetail;
import com.juzix.wallet.entity.VotedCandidate;
import com.juzix.wallet.entity.WithDrawBalance;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface BaseApi {

    /**
     * 获取版本信息
     *
     * @param url
     * @return
     */
    @Headers("name: " + ServerUtils.HEADER_UPDATE_VERSION)
    @GET
    Single<String> getVersionInfo(@Url String url);

    /**
     * 获取交易记录
     *
     * @param body
     * @return
     */
    @POST("app/v0700/transaction/list")
    Single<Response<ApiResponse<List<Transaction>>>> getTransactionList(@Body ApiRequestBody body);

    /**
     * 获取节点列表
     *
     * @return
     */
    @POST("app-{cid}/v0700/node/list")
    Single<Response<ApiResponse<CandidateWrap>>> getCandidateList(@Path("cid") String cid);

    /**
     * 获得用户有投票的节点列表
     *
     * @param cid
     * @param requestBody
     * @return
     */

    @POST("app-{cid}/v0700/node/listUserVoteNode")
    Single<Response<ApiResponse<List<VotedCandidate>>>> getVotedCandidateList(@Path("cid") String cid, @Body ApiRequestBody requestBody);

    /**
     * 参数查询投票交易列表
     *
     * @param body "cid":"",                            //链ID (必填)
     *             "beginSequence":120,                 //起始序号 (必填)
     *             "listSize":100,                      //列表大小 (必填)
     *             "nodeId":"0x",                       //节点ID
     *             "walletAddrs":[                      //地址列表
     *             "address1",
     *             "address2"
     *             ]
     * @return
     */
    @POST("app-{cid}/v0700/transaction/listVote")
    Single<Response<ApiResponse<List<VotedCandidate>>>> getVotedCandidateDetailList(@Path("cid") String cid, @Body ApiRequestBody body);


    /**--------------v0.7.0---------------------*/

    /**
     * 获取验证节点列表
     */

    @POST("app/v0700/node/nodelist")
    Single<Response<ApiResponse<List<VerifyNode>>>> getVerifyNodeList();


    /**
     * 验证节点详情
     * nodeId   //节点id
     */

    @POST("app/v0700/node/nodeDetails")
    Single<Response<ApiResponse<VerifyNodeDetail>>> getNodeCandidateDetail(@Body ApiRequestBody body);


    /**
     * 获取我的委托列表
     *
     * @param cid "walletAddrs":[                     //地址列表
     *            "address1",
     *            "address2"
     *            ]
     * @return
     */
    @POST("app/v0700/node/listDelegateGroupByAddr")
    Single<Response<ApiResponse<List<DelegateInfo>>>> getMyDelegateList(@Body ApiRequestBody body);


    /**
     * 获取委托详情数据
     *
     * @param "cid": "",                          //链Id
     *               "walletAddrs":"address1"            //地址列表
     *               "beginSequence":120,                //起始序号 (必填) 客户端首次进入页面时传-1，-1：代表最新记录
     *               "listSize":100,                     //列表大小 (必填)
     *               "direction":""                      //方向 (必填) new：朝最新记录方向, old：朝最旧记录方向,
     *               客户端首次进入页面时或者上拉时传old。客户端自动获取最新记录时传new。
     */

    @POST("app/v0700/node/delegateDetails")
    Single<Response<ApiResponse<List<DelegateDetail>>>> getDelegateDetailList(@Body ApiRequestBody body);


    /**
     * 获取委托记录的数据
     *
     * @param "cid": "",                          //链Id
     *               "beginSequence":120,                //起始序号 (必填) 客户端首次进入页面时传-1，-1：代表最新记录
     *               "listSize":100,                     //列表大小 (必填)
     *               "direction":""                      //方向 (必填) new：朝最新记录方向, old：朝最旧记录方向,
     *               客户端首次进入页面时或者上拉时传old。客户端自动获取最新记录时传new。
     *               "type":"all",                       //类型
     *               all —— 全部
     *               redeem —— 赎回
     *               delegate —— 委托
     *               "walletAddrs":[                     //地址列表
     *               "address1",
     *               "address2"
     *               ]
     */

    @POST("app/v0700/transaction/delegateRecord")
    Single<Response<ApiResponse<List<Transaction>>>> getDelegateRecordList(@Body ApiRequestBody body);


    /**
     * 批量获取账户余额
     * "addrs":[   //地址列表
     * "address1",
     * "address2"]
     */
    @POST("app/v0700/account/getBalance")
    Single<Response<ApiResponse<List<AccountBalance>>>> getAccountBalance(@Body ApiRequestBody body);


    /**
     * 获取委托金额
     * "addr":"",                             //委托的地址
     * "nodeId":"",                          //节点id
     */
    @POST("app/v0700/node/getDelegationValue")
    Single<Response<ApiResponse<List<WithDrawBalance>>>> getWithDrawBalance(@Body ApiRequestBody body);


    /**
     * 是否允许委托及原因
     * @param id
     * @param body
     * @return
     */
    @POST("app/v0700/node/canDelegation")
    Single<Response<ApiResponse<DelegateHandle>>> getIsDelegateInfo(@Body ApiRequestBody body);


}
