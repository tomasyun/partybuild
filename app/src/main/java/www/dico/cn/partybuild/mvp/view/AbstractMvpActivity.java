package www.dico.cn.partybuild.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import www.dico.cn.partybuild.activity.BaseActivity;
import www.dico.cn.partybuild.mvp.factory.PresenterMvpFactoryImpl;
import www.dico.cn.partybuild.mvp.presenter.BaseMvpPresenter;
import www.dico.cn.partybuild.mvp.proxy.BaseMvpProxy;
import www.dico.cn.partybuild.mvp.proxy.PresenterProxyInterface;

/**
 * @Class: AbstractMvpActivity
 * @Description:继承自Activity的基类MvpActivity 使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 * @author: yun tuo
 * @Date: 2018\1\29 0029 11:30
 */

public class AbstractMvpActivity<V extends BaseMvpView, P extends BaseMvpPresenter<V>> extends BaseActivity implements PresenterProxyInterface<V, P> {
    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V, P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V, P>createFactory(getClass()));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("perfect-mvp", "V onCreate");
        Log.e("perfect-mvp", "V onCreate mProxy = " + mProxy);
        Log.e("perfect-mvp", "V onCreate this = " + this.hashCode());
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("perfect-mvp", "V onResume");
        mProxy.onResume((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("perfect-mvp", "V onDestroy = ");
        mProxy.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("perfect-mvp", "V onSaveInstanceState");
        outState.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState());
    }

//    @Override
//    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
//        Log.e("perfect-mvp","V setPresenterFactory");
//        mProxy.setPresenterFactory(presenterFactory);
//    }
//
//    @Override
//    public PresenterMvpFactory<V, P> getPresenterFactory() {
//        Log.e("perfect-mvp","V getPresenterFactory");
//        return mProxy.getPresenterFactory();
//    }

    @Override
    public P getMvpPresenter() {
        Log.e("perfect-mvp", "V getMvpPresenter");
        return mProxy.getMvpPresenter();
    }
}
