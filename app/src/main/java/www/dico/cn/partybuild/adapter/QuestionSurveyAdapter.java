package www.dico.cn.partybuild.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import www.dico.cn.partybuild.R;
import www.dico.cn.partybuild.bean.QuestionSurveyBean;
import www.dico.cn.partybuild.utils.DateTimeUtils;
import www.yuntdev.com.baseadapterlibrary.base.CommonAdapter;
import www.yuntdev.com.baseadapterlibrary.base.ViewHolder;

public class QuestionSurveyAdapter extends CommonAdapter<QuestionSurveyBean.DataBean> {
    private SkipQuestionSurveyInfoInterface infoInterface;

    public void setInfoInterface(SkipQuestionSurveyInfoInterface infoInterface) {
        this.infoInterface = infoInterface;
    }

    public QuestionSurveyAdapter(Context context, int layoutId, List<QuestionSurveyBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QuestionSurveyBean.DataBean questionSurveyBean, int position) {
        LinearLayout lin_question_survey_root = holder.getView(R.id.lin_question_survey_root);
        holder.setText(R.id.tv_title_question_survey_item, questionSurveyBean.getTitle());
        holder.setText(R.id.tv_date_question_survey_item, questionSurveyBean.getLimitDate());
        holder.setText(R.id.tv_population_question_survey_item, questionSurveyBean.getParticipants());
        if (questionSurveyBean.getLimitDate() != null && !questionSurveyBean.getLimitDate().equals("")) {
            Boolean isExpired = DateTimeUtils.isExpired(questionSurveyBean.getLimitDate());
            if (isExpired) {
                holder.setText(R.id.tv_state_des_survey_item, "调查过期");
            } else {
                holder.setText(R.id.tv_state_des_survey_item, "调查截止");
            }
            lin_question_survey_root.setOnClickListener(view -> {
                if (isExpired) {
                    infoInterface.skip(1, position);
                } else {
                    switch (questionSurveyBean.getIsSubmit()) {
                        case "0":
                            infoInterface.skip(0, position);
                            break;
                        case "1":
                            Toast.makeText(mContext, "改问卷您已参与,不能重复参与", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }

    public interface SkipQuestionSurveyInfoInterface {
        void skip(int skipId, int position);
    }
}
