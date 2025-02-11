package www.dico.cn.partybuild.modleview;

import www.dico.cn.partybuild.mvp.view.BaseMvpView;

public interface OrgActBriefView extends BaseMvpView {
    void resultSuccess(String result);

    void resultFailure(String result);

    void signUpResultSuccess(String result);

    void signUpResultFailure(String result);

    void netWorkUnAvailable();
}
