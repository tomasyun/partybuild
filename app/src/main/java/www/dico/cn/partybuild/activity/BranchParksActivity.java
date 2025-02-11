package www.dico.cn.partybuild.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.adapter.InfoAdapter;
import www.dico.cn.partybuild.adapter.NoticeAdapter;
import www.dico.cn.partybuild.bean.InfoBean;
import www.dico.cn.partybuild.bean.InfodetailForm;
import www.dico.cn.partybuild.bean.NoticeBean;
import www.dico.cn.partybuild.bean.NoticeForm;
import www.dico.cn.partybuild.modleview.BranchParksView;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.presenter.BranchParksPresenter;
import www.yuntdev.com.refreshlayoutlibrary.refreshlayout.SmartRefreshLayout;
import www.yuntdev.com.refreshlayoutlibrary.refreshlayout.api.RefreshLayout;
import www.yuntdev.com.refreshlayoutlibrary.refreshlayout.listener.OnRefreshLoadmoreListener;

@CreatePresenter(BranchParksPresenter.class)
public class BranchParksActivity extends AbstractMvpActivity<BranchParksView, BranchParksPresenter> implements BranchParksView {
    @BindView(R.id.rg_branch_parks)
    RadioGroup rg_branch_parks;
    @BindView(R.id.srl_branch_parks)
    SmartRefreshLayout srl_branch_parks;
    @BindView(R.id.rv_branch_parks)
    RecyclerView rv_branch_parks;
    @BindView(R.id.branch_parks_empty_data)
    View branch_parks_empty_data;
    @BindView(R.id.branch_parks_net_error)
    View branch_parks_net_error;
    private int start = 0;
    private int position = 0;
    private List<InfoBean.DataBeanX.DataBean> list;
    private List<NoticeBean.DataBean> noticeList;
    private InfoAdapter adapter;
    private NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branchparks);
        ButterKnife.bind(this);
        rv_branch_parks.setLayoutManager(new LinearLayoutManager(this));
        rg_branch_parks.check(R.id.rbt_notice_branch_parks);
        rg_branch_parks.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.rbt_notice_branch_parks://通知公告
                    position = 0;
                    start = 0;
                    createRequest(position, start);
                    break;
                case R.id.rbt_activity_branch_parks://支部活动
                    position = 1;
                    start = 0;
                    createRequest(position, start);
                    break;
                case R.id.rbt_develop_branch_parks://发展党员
                    position = 2;
                    start = 0;
                    createRequest(position, start);
                    break;
                case R.id.rbt_logbook_branch_parks://各种台账
                    position = 3;
                    start = 0;
                    createRequest(position, start);
                    break;
            }
        });
        srl_branch_parks.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                start = start + 10;
                createRequest(position, start);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                start = 0;
                createRequest(position, start);
            }
        });
        createRequest(position, start);
    }

    public void goBackBranchParks(View view) {
        this.finish();
    }

    @Override
    public void resultSuccess(String result) {
        srl_branch_parks.finishLoadmore();
        srl_branch_parks.finishRefresh();
        InfoBean bean = new Gson().fromJson(result, InfoBean.class);
        if (bean.code.equals("0000")) {
            if (bean.getData() != null) {
                if (start == 0) {
                    list = bean.getData().getData();
                    if (null != list && list.size() > 0) {
                        srl_branch_parks.setVisibility(View.VISIBLE);
                        branch_parks_empty_data.setVisibility(View.GONE);
                        branch_parks_net_error.setVisibility(View.GONE);
                        adapter = new InfoAdapter(this, R.layout.item_info, list);
                        rv_branch_parks.setAdapter(adapter);
                        adapter.setOnItemClickListener((view, holder, position) -> {
                            InfodetailForm form = new InfodetailForm();
                            form.id = list.get(position).getId();
                            form.type = 1;
                            goTo(InfodetailsActivity.class, form);
                        });
                    } else {
                        srl_branch_parks.setVisibility(View.GONE);
                        branch_parks_empty_data.setVisibility(View.VISIBLE);
                        branch_parks_net_error.setVisibility(View.GONE);
                    }
                } else {
                    List<InfoBean.DataBeanX.DataBean> list = bean.getData().getData();
                    if (null != list && list.size() > 0) {
                        this.list.addAll(list);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener((view, holder, position) -> {
                            InfodetailForm form = new InfodetailForm();
                            form.id = BranchParksActivity.this.list.get(position).getId();
                            form.type = 1;
                            goTo(InfodetailsActivity.class, form);
                        });
                    } else {

                    }
                }
            }
        } else {
            showToast(bean.msg);
        }
    }

    @Override
    public void resultFailure(String result) {
        srl_branch_parks.finishLoadmore();
        srl_branch_parks.finishRefresh();
        showToast(result);
    }

    @Override
    public void getNoticeSuccess(String result) {
        srl_branch_parks.finishLoadmore();
        srl_branch_parks.finishRefresh();
        NoticeBean bean = new Gson().fromJson(result, NoticeBean.class);
        if (bean.code.equals("0000")) {
            if (start == 0) {
                noticeList = bean.getData();
                if (noticeList != null && noticeList.size() > 0) {
                    srl_branch_parks.setVisibility(View.VISIBLE);
                    branch_parks_empty_data.setVisibility(View.GONE);
                    branch_parks_net_error.setVisibility(View.GONE);
                    noticeAdapter = new NoticeAdapter(this, R.layout.item_notice, noticeList);
                    rv_branch_parks.setAdapter(noticeAdapter);
                    onItemClickListener(noticeAdapter, noticeList);
                } else {
                    //空白页面
                    srl_branch_parks.setVisibility(View.GONE);
                    branch_parks_empty_data.setVisibility(View.VISIBLE);
                    branch_parks_net_error.setVisibility(View.GONE);
                }
            } else {
                List<NoticeBean.DataBean> list = bean.getData();
                if (list != null && list.size() > 0) {
                    this.noticeList.addAll(list);
                    noticeAdapter.notifyDataSetChanged();
                    onItemClickListener(noticeAdapter, noticeList);
                } else {

                }
            }
        } else {
            showToast("服务器异常");
        }
    }

    public void onItemClickListener(NoticeAdapter adapter, List<NoticeBean.DataBean> list) {
        adapter.setOnItemClickListener((view, holder, position) -> {
            NoticeForm form = new NoticeForm();
            form.id = list.get(position).getId();
            form.isReply = list.get(position).getIsReply();
            goTo(NoticeInfoActivity.class, form);
        });
    }

    @Override
    public void getNoticeFailure(String result) {
        srl_branch_parks.finishRefresh();
        srl_branch_parks.finishLoadmore();
        showToast(result);
    }

    @Override
    public void netWorkUnAvailable() {
        srl_branch_parks.setVisibility(View.GONE);
        branch_parks_empty_data.setVisibility(View.GONE);
        branch_parks_net_error.setVisibility(View.VISIBLE);
        branch_parks_net_error.setOnClickListener(view -> createRequest(position, start));
    }

    public void createRequest(int position, int start) {
        switch (position) {
            case 0:
                getMvpPresenter().doBranchParksNoticeRequest(dialog, "", "3", "0", start, 10);
                break;
            case 1:
                getMvpPresenter().doBranchParksRequest(dialog, "15", "32", "0", start, 10);
                break;
            case 2:
                getMvpPresenter().doBranchParksRequest(dialog, "15", "33", "0", start, 10);
                break;
            case 3:
                getMvpPresenter().doBranchParksRequest(dialog, "15", "34", "0", start, 10);
                break;
        }
    }
}
