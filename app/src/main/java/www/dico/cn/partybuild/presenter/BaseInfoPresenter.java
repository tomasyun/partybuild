package www.dico.cn.partybuild.presenter;

import android.app.Dialog;
import android.content.Context;

import www.dico.cn.partybuild.AppManager;
import www.dico.cn.partybuild.modleview.BaseInfoView;
import www.dico.cn.partybuild.mvp.presenter.BaseMvpPresenter;
import www.dico.cn.partybuild.persistance.BaseInfoBean;
import www.dico.cn.partybuild.widget.LoadingDialog;
import www.yuntdev.com.library.EasyHttp;
import www.yuntdev.com.library.callback.ProgressDialogCallBack;
import www.yuntdev.com.library.callback.SimpleCallBack;
import www.yuntdev.com.library.exception.ApiException;
import www.yuntdev.com.library.subsciber.IProgressDialog;

public class BaseInfoPresenter extends BaseMvpPresenter<BaseInfoView> {
    IProgressDialog dialog = new IProgressDialog() {
        @Override
        public Dialog getDialog() {
            LoadingDialog.Builder builder = new LoadingDialog.Builder(AppManager.getManager().curActivity())
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setMessage("获取中..")
                    .setShowMessage(true);
            return builder.create();
        }
    };

    public void getBaseInfoRequest(String id) {
        disposable = EasyHttp.post("")
                .params("id", id)
                .execute(new ProgressDialogCallBack<BaseInfoBean>(dialog, true, true) {
                    @Override
                    public void onSuccess(BaseInfoBean baseInfoBean) {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();
        if (null!=disposable&&disposable.isDisposed())
            disposable.dispose();
    }
}
