package tv.kuainiu.ui.liveold.model;

/**
 * Created by jack on 2016/7/14.
 */

import com.bokecc.sdk.mobile.live.pojo.Answer;
import com.bokecc.sdk.mobile.live.pojo.Question;

import java.util.LinkedHashSet;
import java.util.Set;

public class QAMsg {
    private Question question;
    private Set<Answer> answers = new LinkedHashSet<Answer>();

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public Set<Answer> getAnswers() {
        return answers;
    }
    public void setAnswer(Answer answer) {
        answers.add(answer);
    }

}

