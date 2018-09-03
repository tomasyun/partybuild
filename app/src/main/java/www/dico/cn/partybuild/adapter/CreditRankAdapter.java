package www.dico.cn.partybuild.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.AppManager;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.bean.CreditRankBean;
import www.dico.cn.partybuild.utils.GlideUtils;
import www.yuntdev.com.baseadapterlibrary.base.CommonAdapter;
import www.yuntdev.com.baseadapterlibrary.base.ViewHolder;

public class CreditRankAdapter extends CommonAdapter<CreditRankBean.DataBean.CreditInfoListBean> {
    public CreditRankAdapter(Context context, int layoutId, List<CreditRankBean.DataBean.CreditInfoListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CreditRankBean.DataBean.CreditInfoListBean creditRankBean, int position) {
        holder.setText(R.id.tv_credit_rank_num_item, creditRankBean.getRank());
        GlideUtils.loadCircleImage(AppManager.getManager().curActivity(), AppConfig.urlFormat("http://47.104.72.111/", creditRankBean.getAvatar()), (ImageView) holder.getView(R.id.iv_credit_rank_avatar_item));
        holder.setText(R.id.tv_name_credit_rank_item, creditRankBean.getName());
        SpannableString score = new SpannableString(creditRankBean.getScore() + "分");
        score.setSpan(new AbsoluteSizeSpan(40), 0, score.length() - 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        score.setSpan(new AbsoluteSizeSpan(28), score.length() - 1, score.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView tv_score_credit_rank_item = holder.getView(R.id.tv_score_credit_rank_item);
        tv_score_credit_rank_item.setText(score);
    }
}
