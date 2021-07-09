package com.ruoyi.project.system.qanda.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 问答对象 qanda
 * 
 * @author ruoyi
 * @date 2021-07-07
 */
public class Qanda extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 问题编号 */
    private Long id;

    /** 问题 */
    @Excel(name = "问题")
    private String question;

    /** 回答 */
    @Excel(name = "回答")
    private String answer;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getQuestion()
    {
        return question;
    }
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getAnswer()
    {
        return answer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("question", getQuestion())
            .append("answer", getAnswer())
            .toString();
    }
}
