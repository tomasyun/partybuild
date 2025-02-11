package www.dico.cn.partybuild.presenter;

import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.modleview.NoticeView;
import www.dico.cn.partybuild.mvp.presenter.BaseMvpPresenter;
import www.yuntdev.com.library.EasyHttp;
import www.yuntdev.com.library.callback.ProgressDialogCallBack;
import www.yuntdev.com.library.exception.ApiException;
import www.yuntdev.com.library.subsciber.IProgressDialog;

public class NoticePresenter extends BaseMvpPresenter<NoticeView> {
    public void noticeRequest(IProgressDialog dialog, String title, String type, String draw, int start, int length) {
        EasyHttp.post("noticeByType")
                .headers("Authorization", AppConfig.getSpUtils().getString("token"))
                .params("title", title)
                .params("type", type)
                .params("draw", draw)
                .params("start", String.valueOf(start))
                .params("length", String.valueOf(length))
                .execute(new ProgressDialogCallBack<String>(dialog, true, true) {
                    @Override
                    public void onSuccess(String result) {
                        getMvpView().resultSuccess(result);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (e.getCode() == ApiException.ERROR.NETWORD_ERROR)
                            getMvpView().netWorkUnAvailable();
                        else
                            getMvpView().resultFailure(e.getMessage());
                    }
                });
    }
}
