package www.dico.cn.partybuild.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;
import www.dico.cn.partybuild.AppManager;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.activity.NoticeInfoActivity;
import www.dico.cn.partybuild.bean.NoticeInfoBean;

public class NoticeInfoCommentAdapter extends RecyclerView.Adapter<NoticeInfoCommentAdapter.NoticeInfoCommentHolder>{
    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mFooterView;
    private List<NoticeInfoBean.DataBean.CommitListBean> mDatas;

    public NoticeInfoCommentAdapter(List<NoticeInfoBean.DataBean.CommitListBean> mDatas) {
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public NoticeInfoCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER)
            return new NoticeInfoCommentHolder(mFooterView);
        View view = LayoutInflater.from(AppManager.getManager().findActivity(NoticeInfoActivity.class)).inflate(R.layout.item_comment, viewGroup, false);
        return new NoticeInfoCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeInfoCommentHolder commentHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) return;
//        final int pos = getRealPosition(commentHolder);
        if (position < mDatas.size()) {
            if (commentHolder instanceof NoticeInfoCommentHolder) {
                commentHolder.tv_comment_user_name.setText(mDatas.get(position).getName());
                commentHolder.tv_comment_user_content.setText(mDatas.get(position).getCommitContent());
                commentHolder.tv_comment_date.setText(mDatas.get(position).getCommitDate());
            }
        }
    }

    //获取列表真实每项position
//    private int getRealPosition(RecyclerView.ViewHolder holder) {
//        int position = holder.getLayoutPosition();
//        return mFooterView == null ? position : position + 1;
//    }

    @Override
    public int getItemCount() {
        return mFooterView == null ? mDatas.size() : mDatas.size() + 1;
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        notifyItemInserted(mDatas.size());
    }

    //根据position返回不同的ItemViewType
    @Override
    public int getItemViewType(int position) {
        if (mFooterView == null) return TYPE_NORMAL;
        if (position == mDatas.size()) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    public class NoticeInfoCommentHolder extends RecyclerView.ViewHolder {
        public ImageView iv_comment_user_avatar;
        public TextView tv_comment_user_name;
        public ExpandableTextView tv_comment_user_content;
        public TextView tv_comment_date;

        public NoticeInfoCommentHolder(@NonNull View itemView) {
            super(itemView);
            iv_comment_user_avatar = itemView.findViewById(R.id.iv_comment_user_avatar);
            tv_comment_user_name = itemView.findViewById(R.id.tv_comment_user_name);
            tv_comment_user_content = itemView.findViewById(R.id.tv_comment_user_content);
            tv_comment_date = itemView.findViewById(R.id.tv_comment_date);
        }
    }
}
