package www.dico.cn.partybuild.modleview;

import www.dico.cn.partybuild.bean.OrgActBean;
import www.dico.cn.partybuild.mvp.view.BaseMvpView;

public interface OrgActView extends BaseMvpView{
    void resultSuccess(String result);

    void resultFailure(String result);
}
