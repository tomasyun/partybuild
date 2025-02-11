package www.dico.cn.partybuild.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import java.util.List;

import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.bean.NoticeBean;
import www.dico.cn.partybuild.utils.DateTimeUtils;
import www.dico.cn.partybuild.utils.GlideUtils;
import www.dico.cn.partybuild.utils.StringUtils;
import www.yuntdev.com.baseadapterlibrary.base.CommonAdapter;
import www.yuntdev.com.baseadapterlibrary.base.ViewHolder;

public class NoticeAdapter extends CommonAdapter<NoticeBean.DataBean> {
    private Context context;

    public NoticeAdapter(Context context, int layoutId, List<NoticeBean.DataBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, NoticeBean.DataBean noticeBean, int position) {
        if (null != noticeBean.getAvatar() && !noticeBean.getAvatar().equals(""))
            GlideUtils.loadCircleImage(context, AppConfig.urlFormat(noticeBean.getAvatar()), holder.getView(R.id.iv_avatar_notice_item));
        holder.setText(R.id.tv_name_notice_item, noticeBean.getName());
        String publishDate = noticeBean.getPublishDate();
        publishDate = (null == publishDate || publishDate.equals("")) ? DateTimeUtils.getNow() : noticeBean.getPublishDate();
        String diffDate = DateTimeUtils.getMinutes(publishDate, DateTimeUtils.getNow());
        if (diffDate != null && !diffDate.equals("")) {
            int minutes = Integer.valueOf(diffDate);
            if (minutes < 60) {
                holder.setText(R.id.tv_date_notice_item, minutes + "分钟前");
            } else if (minutes > 60 && minutes / 60 <= 24) {
                holder.setText(R.id.tv_date_notice_item, minutes / 60 + "小时前");
            } else {
                holder.setText(R.id.tv_date_notice_item, minutes / (24 * 60) + "天前");
            }
        }
        TextView tv_content_notice_item = holder.getView(R.id.tv_content_notice_item);
        tv_content_notice_item.setText(Html.fromHtml(StringUtils.trimStyle(noticeBean.getContent())));
    }
}
