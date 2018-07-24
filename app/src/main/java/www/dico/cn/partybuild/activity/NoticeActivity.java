package www.dico.cn.partybuild.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.modleview.NoticeView;
import www.dico.cn.partybuild.mvp.ViewFind;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.persistance.NoticeBean;
import www.dico.cn.partybuild.presenter.NoticePresenter;

//通知
@CreatePresenter(NoticePresenter.class)
public class NoticeActivity extends AbstractMvpActivity<NoticeView, NoticePresenter> implements NoticeView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ViewFind.bind(this);
    }

    public void goback(View view) {
        this.finish();
    }

    @Override
    public void resultSuccess(NoticeBean result) {

    }

    @Override
    public void resultFailure(String result) {
        showToast(result);
    }
}
