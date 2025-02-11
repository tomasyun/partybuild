package www.dico.cn.partybuild.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.AppManager;
import www.dico.cn.partybuild.MainActivity;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.UpdateManager;
import www.yuntdev.com.imitationiosdialoglibrary.AlertDialog;

//设置
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void goBackSetting(View view) {
        this.finish();
    }

    public void logout(View view) {
        new AlertDialog(this).builder()
                .setCancelable(true)
                .setTitle("退出登录")
                .setMsg("退出登录可能会使你现有记录归零，确定退出?")
                .setPositiveButton("确定退出", positive -> {
                    AppConfig.getSpUtils().put("isLoginOk", 0);
                    goTo(SplashActivity.class, null);
                    AppManager.getManager().finishAllActivity();
                }).setNegativeButton("取消", negative -> {

                }).show();
    }

    //版本更新
    public void checkUpdate(View view) {
        new UpdateManager(this).checkUpdate(true);
    }

    //密码更新
    public void passwordUpdate(View view) {
        goTo(PwdupdateActivity.class, null);
    }
}
