package www.dico.cn.partybuild.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.adapter.VoteListAdapter;
import www.dico.cn.partybuild.bean.VoteForm;
import www.dico.cn.partybuild.bean.VoteListBean;
import www.dico.cn.partybuild.modleview.VoteManagerView;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.presenter.VoteManagerPresenter;
import www.yuntdev.com.baseadapterlibrary.MultiItemTypeAdapter;

@CreatePresenter(VoteManagerPresenter.class)
public class VoteManagerActivity extends AbstractMvpActivity<VoteManagerView, VoteManagerPresenter> implements VoteManagerView {
    @BindView(R.id.rv_vote)
    RecyclerView rv_vote;
    @BindView(R.id.vote_empty_data)
    View vote_empty_data;
    @BindView(R.id.vote_net_error)
    View vote_net_error;
    private VoteListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votemanager);
        ButterKnife.bind(this);
        rv_vote.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpPresenter().doVoteManagerRequest();
    }

    public void goBackVoteManager(View view) {
        this.finish();
    }

    @Override
    public void resultSuccess(String result) {
        final VoteListBean bean = new Gson().fromJson(result, VoteListBean.class);
        if (bean.code.equals("0000")) {
            if (null != bean.getData() && bean.getData().size() > 0) {
                rv_vote.setVisibility(View.VISIBLE);
                vote_empty_data.setVisibility(View.GONE);
                vote_net_error.setVisibility(View.GONE);
                adapter = new VoteListAdapter(this, R.layout.item_vote, bean.getData());
                rv_vote.setAdapter(adapter);
                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        VoteForm form = new VoteForm();
                        form.voteId = bean.getData().get(position).getId();
                        form.isVoter = bean.getData().get(position).getIsVoter();
                        goTo(VoteDetailActivity.class, form);
                    }
                });
            } else {
                rv_vote.setVisibility(View.GONE);
                vote_empty_data.setVisibility(View.VISIBLE);
                vote_net_error.setVisibility(View.GONE);
            }
        } else {
            showToast(bean.msg);
        }
    }

    @Override
    public void resultFailure(String result) {
        showToast(result);
    }

    @Override
    public void netWorkUnAvailable() {
        rv_vote.setVisibility(View.GONE);
        vote_empty_data.setVisibility(View.GONE);
        vote_net_error.setVisibility(View.VISIBLE);
        vote_net_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMvpPresenter().doVoteManagerRequest();
            }
        });
    }
}
