package www.dico.cn.partybuild.presenter;

import android.app.Dialog;

import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.AppManager;
import www.dico.cn.partybuild.MainActivity;
import www.dico.cn.partybuild.modleview.ExamView;
import www.dico.cn.partybuild.mvp.presenter.BaseMvpPresenter;
import www.dico.cn.partybuild.widget.LoadingDialog;
import www.yuntdev.com.library.EasyHttp;
import www.yuntdev.com.library.callback.ProgressDialogCallBack;
import www.yuntdev.com.library.exception.ApiException;
import www.yuntdev.com.library.subsciber.IProgressDialog;

public class ExamPresenter extends BaseMvpPresenter<ExamView> {
    //待考
    IProgressDialog dialog = new IProgressDialog() {
        @Override
        public Dialog getDialog() {
            LoadingDialog.Builder builder = new LoadingDialog.Builder(AppManager.getManager().findActivity(MainActivity.class))
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setMessage("获取中..")
                    .setShowMessage(true);

            return builder.create();
        }
    };

    public void examsOnRequest(String type) {
        EasyHttp.post("examList")
                .headers("Authorization", AppConfig.getSpUtils().getString("token"))
                .params("type", type)
                .execute(new ProgressDialogCallBack<String>(dialog, true, true) {
                    @Override
                    public void onSuccess(String result) {
                        getMvpView().examOnResultSuccess(result);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (e.getCode() == ApiException.ERROR.NETWORD_ERROR)
                            getMvpView().netWorkUnAvailable();
                        else
                            getMvpView().examOnResultFailure(e.getMessage());
                    }
                });
    }

    //已考
    public void examsOkRequest(String type) {
        EasyHttp.post("examList")
                .headers("Authorization", AppConfig.getSpUtils().getString("token"))
                .params("type", type)
                .execute(new ProgressDialogCallBack<String>(dialog, true, true) {
                    @Override
                    public void onSuccess(String result) {
                        getMvpView().examOkResultSuccess(result);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (e.getCode() == ApiException.ERROR.NETWORD_ERROR)
                            getMvpView().netWorkUnAvailable();
                        else
                            getMvpView().examOkResultFailure(e.getMessage());
                    }
                });
    }
}
