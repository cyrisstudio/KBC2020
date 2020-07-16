package com.Cyris.kbc2020.Questions;

public class QuestionAnswerObjectClass {
    String question,option1,option2,option3,option4,answer;
    Boolean questionAsked;

    public void setQuestion(String ques)
    {
        this.question = ques;
    }

    public String getQuestion() {
        return question;
    }
    public void setAnswer(String ans)
    {
        this.answer = ans;
    }

    public String getAnswer() {
        return answer;
    }

    public void setOption(String opt1, String opt2, String opt3, String opt4) {
        this.option1 = opt1;
        this.option2 = opt2;
        this.option3 = opt3;
        this.option4 = opt4;
    }

    public void setQuestionAsked(Boolean questionAsked) {
        this.questionAsked = questionAsked;
    }

    public Boolean getQuestionAsked() {
        return questionAsked;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }
}
