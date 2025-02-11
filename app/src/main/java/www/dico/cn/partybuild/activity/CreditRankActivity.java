package www.dico.cn.partybuild.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.adapter.CreditRankAdapter;
import www.dico.cn.partybuild.bean.CreditRankBean;
import www.dico.cn.partybuild.modleview.CreditRankView;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.presenter.CreditRankPresenter;
import www.dico.cn.partybuild.utils.GlideUtils;

//积分排名
@CreatePresenter(CreditRankPresenter.class)
public class CreditRankActivity extends AbstractMvpActivity<CreditRankView, CreditRankPresenter> implements CreditRankView {
    @BindView(R.id.tv_user_rank_num)
    TextView tv_user_rank_num;//名次
    @BindView(R.id.iv_user_icon_rank)
    ImageView iv_user_icon_rank;//个人头像
    @BindView(R.id.tv_user_rank_score)
    TextView tv_user_rank_score;//积分
    @BindView(R.id.rv_rank)
    RecyclerView rv_rank;
    @BindView(R.id.credit_rank_empty_data)
    View credit_rank_empty_data;
    @BindView(R.id.credit_rank_net_error)
    View credit_rank_net_error;
    private CreditRankAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditrank);
        ButterKnife.bind(this);
        rv_rank.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpPresenter().creditRankRequest(dialog);
    }

    public void goBackRank(View view) {
        this.finish();
    }

    @Override
    public void resultSuccess(String result) {
        CreditRankBean bean = new Gson().fromJson(result, CreditRankBean.class);
        if (bean.code.equals("0000")) {
            if (bean.getData() != null) {
                GlideUtils.loadCircleImage(this, AppConfig.urlFormat(bean.getData().getMavatar()), iv_user_icon_rank);
                SpannableString numContent = new SpannableString(bean.getData().getMrank() + "名");
                numContent.setSpan(new AbsoluteSizeSpan(45), 0, numContent.length() - 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                numContent.setSpan(new AbsoluteSizeSpan(35), numContent.length() - 1, numContent.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_user_rank_num.setText(numContent);
                SpannableString scoreContent = new SpannableString(bean.getData().getMscore() + "分");
                scoreContent.setSpan(new AbsoluteSizeSpan(45), 0, scoreContent.length() - 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                scoreContent.setSpan(new AbsoluteSizeSpan(35), scoreContent.length() - 1, scoreContent.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_user_rank_score.setText(scoreContent);
                List<CreditRankBean.DataBean.CreditInfoListBean> list = bean.getData().getCreditInfoList();
                if (null != list && list.size() > 0) {
                    rv_rank.setVisibility(View.VISIBLE);
                    credit_rank_empty_data.setVisibility(View.GONE);
                    credit_rank_net_error.setVisibility(View.GONE);
                    adapter = new CreditRankAdapter(this, R.layout.item_credit_rank, list);
                    adapter.setId(bean.getData().getId());
                    rv_rank.setAdapter(adapter);
                } else {
                    //空白页面
                    rv_rank.setVisibility(View.GONE);
                    credit_rank_empty_data.setVisibility(View.VISIBLE);
                    credit_rank_net_error.setVisibility(View.GONE);
                }
            }
        } else {
            showToast("暂无排名");
        }
    }

    @Override
    public void resultFailure(String result) {
        showToast(result);
    }

    @Override
    public void netWorkUnAvailable() {
        rv_rank.setVisibility(View.GONE);
        credit_rank_empty_data.setVisibility(View.GONE);
        credit_rank_net_error.setVisibility(View.VISIBLE);
        credit_rank_net_error.setOnClickListener(view -> getMvpPresenter().creditRankRequest(dialog));
    }
}
