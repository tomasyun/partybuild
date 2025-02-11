package www.dico.cn.partybuild.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.dico.cn.partybuild.AppConfig;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.adapter.InfoCommentAdapter;
import www.dico.cn.partybuild.bean.BaseProtocol;
import www.dico.cn.partybuild.bean.InfodetailBean;
import www.dico.cn.partybuild.bean.InfodetailForm;
import www.dico.cn.partybuild.modleview.InfodetailsView;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.presenter.InfodetailsPresenter;
import www.dico.cn.partybuild.utils.SizeUtils;
import www.dico.cn.partybuild.utils.StringUtils;
import www.dico.cn.partybuild.widget.HtmlImageGetter;
import www.dico.cn.partybuild.widget.LoadingDialog;
import www.yuntdev.com.library.EasyHttp;
import www.yuntdev.com.library.callback.DownloadProgressCallBack;
import www.yuntdev.com.library.exception.ApiException;
import www.yuntdev.com.library.utils.HttpLog;

//资讯详情
@CreatePresenter(InfodetailsPresenter.class)
public class InfodetailsActivity extends AbstractMvpActivity<InfodetailsView, InfodetailsPresenter> implements InfodetailsView {
    @BindView(R.id.tv_info_detail_title)
    TextView tv_info_detail_title;
    @BindView(R.id.tv_info_detail_source)
    TextView tv_info_detail_source;
    @BindView(R.id.tv_info_detail_date)
    TextView tv_info_detail_date;
    @BindView(R.id.tv_info_detail_browse)
    TextView tv_info_detail_browse;
    @BindView(R.id.tv_info_detail_content)
    TextView tv_info_detail_content;
    @BindView(R.id.rv_info_detail)
    RecyclerView rv_info_detail;
    @BindView(R.id.info_detail_reply_comment)
    View info_detail_reply_comment;
    @BindView(R.id.sv_info_detail)
    ScrollView sv_info_detail;
    @BindView(R.id.lin_info_detail)
    LinearLayout lin_info_detail;
    @BindView(R.id.rel_attachment_info)
    RelativeLayout rel_attachment_info;
    private InfodetailForm form;
    private InfoCommentAdapter adapter;
    private EditText et_reply_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetails);
        ButterKnife.bind(this);
        QbSdk.initX5Environment(InfodetailsActivity.this, null);
        form = getParam();
        if (form != null) {
            switch (form.type) {
                case 0://资讯
                    getMvpPresenter().infoDetailsRequest(dialog, form.id);
                    break;
                case 1://文章
                    getMvpPresenter().infoArticleRequest(dialog, form.id);
                    break;
                case 2://广告
                    getMvpPresenter().infoDetailsRequest(dialog, form.id);
                    break;
            }
        }
        tv_info_detail_title.post(() -> {
            if (tv_info_detail_title.getLineCount() == 1) {
                tv_info_detail_title.setGravity(Gravity.CENTER);
            } else {
                tv_info_detail_title.setGravity(Gravity.LEFT);
            }
        });

        rv_info_detail.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goBackInfodetail(View view) {
        switch (form.type) {
            case 0:
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
                manager.sendBroadcast(new Intent("cn.diconet.www").putExtra("skip", "1"));
                this.finish();
                break;
            case 1:
                this.finish();
                break;
            case 2:
                LocalBroadcastManager manager1 = LocalBroadcastManager.getInstance(this);
                manager1.sendBroadcast(new Intent("cn.diconet.www").putExtra("skip", "0"));
                this.finish();
                break;
        }
    }

    @Override
    public void resultSuccess(String result) {
        final InfodetailBean bean = new Gson().fromJson(result, InfodetailBean.class);
        if (bean.code.equals("0000")) {
            if (bean.getData() != null) {
                tv_info_detail_title.setText(bean.getData().getTitle());
                String source = bean.getData().getPublicUnit();
                source = (source == null || source.equals("")) ? "---" : bean.getData().getPublicUnit();
                tv_info_detail_source.setText(source);
                tv_info_detail_date.setText(bean.getData().getPublishDate());
                tv_info_detail_browse.setText(bean.getData().getBrowse());
//                tv_info_content.setText(StringUtils.delHtmlTag(bean.getData().getContent()));
                HtmlImageGetter imageGetter = new HtmlImageGetter(this, tv_info_detail_content);
                Spanned spanned = Html.fromHtml(StringUtils.trimStyle(bean.getData().getContent()), imageGetter, null);
                tv_info_detail_content.setText(spanned);

                if (bean.getData().getAttachment() != null && !bean.getData().getAttachment().equals("")) {
                    //TODO 附件处理
                    rel_attachment_info.setVisibility(View.VISIBLE);
                    final String url = AppConfig.urlFormat(bean.getData().getAttachment());
                    final String fileName = bean.getData().getAttachmentName();
                    final String downPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    Log.i("################", downPath);
                    TextView tv_attachment_info = rel_attachment_info.findViewById(R.id.tv_attachment_info);
                    if (null != fileName) {
                        tv_attachment_info.setText(fileName);
                        rel_attachment_info.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {//文件预览
                                File docFile = new File(downPath + File.separator + fileName, fileName);
                                if (docFile.exists()) {
                                    DisplayFileActivity.openDisplayFileActivity(InfodetailsActivity.this, downPath, fileName);
                                } else {
                                    LoadingDialog.Builder builder = new LoadingDialog.Builder(InfodetailsActivity.this)
                                            .setCancelable(true)
                                            .setCancelOutside(true)
                                            .setMessage("加载中..")
                                            .setShowMessage(true);
                                    final Dialog dialog = builder.create();
                                    EasyHttp.downLoad(url)
                                            .savePath(downPath)
                                            .saveName(fileName)
                                            .execute(new DownloadProgressCallBack<String>() {
                                                @Override
                                                public void update(long bytesRead, long contentLength, boolean done) {
                                                    int progress = (int) (bytesRead * 100 / contentLength);
                                                    HttpLog.e(progress + "% ");
                                                }

                                                @Override
                                                public void onStart() {
                                                    if (dialog.isShowing()) {

                                                    }
                                                    if (dialog != null) {
                                                        if (!dialog.isShowing()) {
                                                            dialog.show();
                                                        }
                                                    }
                                                    HttpLog.i("======" + Thread.currentThread().getName());
                                                }

                                                @Override
                                                public void onComplete(String path) {
                                                    HttpLog.e("文件保存路径：" + path);
                                                    if (!dialog.isShowing()) {

                                                    }
                                                    if (dialog != null) {
                                                        if (dialog.isShowing()) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                                                    if (suffix.toLowerCase().equals("png") || suffix.toLowerCase().equals("jpg")||suffix.toLowerCase().equals("jpeg")||suffix.toLowerCase().equals("bmp"))
                                                        DisplayImgFileActivity.openDisplayImgFileActivity(InfodetailsActivity.this, path);
                                                    else
                                                        DisplayFileActivity.openDisplayFileActivity(InfodetailsActivity.this, path, fileName);
                                                }

                                                @Override
                                                public void onError(ApiException e) {
                                                    if (!dialog.isShowing()) {

                                                    }
                                                    if (dialog != null) {
                                                        if (dialog.isShowing()) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                    Toast.makeText(InfodetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        });
                    }
                } else {
                    rel_attachment_info.setVisibility(View.GONE);
                }

                et_reply_comment = info_detail_reply_comment.findViewById(R.id.et_reply_comment);
                TextView tv_reply_comment = info_detail_reply_comment.findViewById(R.id.tv_reply_comment);
                ImageView iv_reply_comment = info_detail_reply_comment.findViewById(R.id.iv_reply_comment);
                tv_reply_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_reply_comment.getText().toString().trim().equals("")) {
                            showToast("请填写评论内容");
                        } else {
                            getMvpPresenter().doSubmitCommentRequest(dialog, "0", bean.getData().getId(), et_reply_comment.getText().toString().trim());
                        }
                    }
                });

                et_reply_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            if (et_reply_comment.getText().toString().trim().equals("")) {
                                showToast("请填写评论内容");
                            } else {
                                getMvpPresenter().doSubmitCommentRequest(dialog, "0", bean.getData().getId(), et_reply_comment.getText().toString().trim());
                                return true;
                            }
                        }
                        return false;
                    }
                });

                iv_reply_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast("暂未开通");
                    }
                });
                List<InfodetailBean.DataBean.CommitListBean> beans = bean.getData().getCommitList();
                if (null != beans && beans.size() > 0) {
                    adapter = new InfoCommentAdapter(beans);
//                    View footerView = LayoutInflater.from(InfodetailsActivity.this).inflate(R.layout.comment_footer, null);
                    TextView footerView = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    footerView.setLayoutParams(layoutParams);
                    footerView.setText("已显示所有评论");
                    footerView.setTextColor(getResources().getColor(R.color.light_gray));
                    footerView.setPadding(0, SizeUtils.dp2px(this, 30), 0, SizeUtils.dp2px(this, 120));
                    footerView.setGravity(Gravity.CENTER);
                    footerView.setTextSize(13);
                    adapter.setFooterView(footerView);
                    rv_info_detail.setAdapter(adapter);
                } else {
                    //空白页面
                }
            }
        } else {
            showToast("服务器异常");
        }
    }

    @Override
    public void resultFailure(String result) {
        showToast(result);
    }

    @Override
    public void submitCommentSuccess(String result) {
        et_reply_comment.setText("");
        BaseProtocol protocol = new Gson().fromJson(result, BaseProtocol.class);
        if (protocol.code.equals("0000")) {
            switch (form.type) {
                case 0://资讯
                    getMvpPresenter().infoDetailsRequest(dialog, form.id);
                    break;
                case 1://文章
                    getMvpPresenter().infoArticleRequest(dialog, form.id);
                    break;
                case 2://广告
                    getMvpPresenter().infoDetailsRequest(dialog, form.id);
                    break;
            }
            sv_info_detail.scrollTo(0, lin_info_detail.getMeasuredHeight() - sv_info_detail.getHeight());
        } else {
            showToast("服务器异常");
        }
    }

    @Override
    public void submitCommentFailure(String result) {
        showToast(result);
    }

    @Override
    public void netWorkUnAvailable() {
        showToast("网络出现异常");
    }
}
