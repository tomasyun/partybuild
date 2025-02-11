package www.dico.cn.partybuild.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.adapter.ExamResultPreviewAdapter;
import www.dico.cn.partybuild.bean.ExamResultForm;
import www.dico.cn.partybuild.bean.ExamReviewBean;
import www.dico.cn.partybuild.bean.QuestionOptionPreviewForm;
import www.dico.cn.partybuild.modleview.ExamResultView;
import www.dico.cn.partybuild.mvp.factory.CreatePresenter;
import www.dico.cn.partybuild.mvp.view.AbstractMvpActivity;
import www.dico.cn.partybuild.presenter.ExamResultPresenter;

@CreatePresenter(ExamResultPresenter.class)
public class ExamResultActivity extends AbstractMvpActivity<ExamResultView, ExamResultPresenter> implements ExamResultView {
    @BindView(R.id.tv_score_exam_result)
    TextView tv_score_exam_result;
    @BindView(R.id.tv_cost_exam_result)
    TextView tv_cost_exam_result;
    @BindView(R.id.tv_limit_score_exam_result)
    TextView tv_limit_score_exam_result;
    @BindView(R.id.tv_desc_exam_result)
    TextView tv_desc_exam_result;
    @BindView(R.id.rel_exam_result)
    RelativeLayout rel_exam_result;
    @BindView(R.id.tfl_exam_result_preview)
    TagFlowLayout tfl_exam_result_preview;
    private ExamResultForm form;
    private ExamResultPreviewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examresult);
        ButterKnife.bind(this);
        form = getParam();
        if (form != null)
            getMvpPresenter().doExamResultPreviewRequest(dialog, form.examId);
    }

    public void goBackExamResult(View view) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(new Intent("cn.diconet.www").putExtra("skip", "3"));
        this.finish();
    }

    @Override
    public void resultSuccess(String result) {
        ExamReviewBean bean = new Gson().fromJson(result, ExamReviewBean.class);
        if (bean.code.equals("0000")) {
            if (null != bean.getData()) {
                String examScore = bean.getData().getExamScore();
                examScore = (examScore == null) ? "" : bean.getData().getExamScore();
                examScore = new DecimalFormat("#").format(Double.valueOf(examScore));
                tv_score_exam_result.setText(examScore + "分");
                String examCost = bean.getData().getExamCost();
                examCost = (examCost == null) ? "" : bean.getData().getExamCost();
                tv_cost_exam_result.setText(examCost + "分钟");
                String limitScore = new DecimalFormat("#").format(Double.valueOf(form.limitScore));
                tv_limit_score_exam_result.setText(limitScore + "分");
                String isPass = bean.getData().getIsPass();
                if (null != isPass && !isPass.equals("")) {
                    switch (isPass) {
                        case "0":
                            rel_exam_result.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue_bg));
                            tv_desc_exam_result.setText("抱歉,考试未通过");
                            tv_desc_exam_result.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_pass_on));
                            break;
                        case "1":
                            rel_exam_result.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_yellow_bg));
                            tv_desc_exam_result.setText("恭喜您,考试通过");
                            tv_desc_exam_result.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_pass_ok));
                            break;
                    }
                }
                final List<ExamReviewBean.DataBean.QuestionIdsBean> idsBeans = bean.getData().getQuestionIds();
                if (null != idsBeans && idsBeans.size() > 0) {
                    adapter = new ExamResultPreviewAdapter(idsBeans);
                    tfl_exam_result_preview.setAdapter(adapter);
                    tfl_exam_result_preview.setOnTagClickListener((view, position, parent) -> {
                        QuestionOptionPreviewForm form = new QuestionOptionPreviewForm();
                        form.position = position + 1;
                        form.questionId = idsBeans.get(position).getId();
                        goTo(QuestionOptionPreviewActivity.class, form);
                        return true;
                    });
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
    public void netWorkUnAvailable() {
        showToast("网络出现异常");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(new Intent("cn.diconet.www").putExtra("skip", "3"));
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public int test(int a, int b) {
        return a > b ? 0 : 1;
        }
}
