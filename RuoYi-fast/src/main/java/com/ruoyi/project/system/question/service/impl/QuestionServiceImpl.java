package com.ruoyi.project.system.question.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.question.mapper.QuestionMapper;
import com.ruoyi.project.system.question.domain.Question;
import com.ruoyi.project.system.question.service.IQuestionService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 问答编辑Service业务层处理
 * 
 * @author czx
 * @date 2021-07-08
 */
@Service
public class QuestionServiceImpl implements IQuestionService 
{
    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 查询问答编辑
     * 
     * @param id 问答编辑ID
     * @return 问答编辑
     */
    @Override
    public Question selectQuestionById(Long id)
    {
        return questionMapper.selectQuestionById(id);
    }

    /**
     * 查询问答编辑列表
     * 
     * @param question 问答编辑
     * @return 问答编辑
     */
    @Override
    public List<Question> selectQuestionList(Question question)
    {
        return questionMapper.selectQuestionList(question);
    }

    /**
     * 新增问答编辑
     * 
     * @param question 问答编辑
     * @return 结果
     */
    @Override
    public int insertQuestion(Question question)
    {
        return questionMapper.insertQuestion(question);
    }

    /**
     * 修改问答编辑
     * 
     * @param question 问答编辑
     * @return 结果
     */
    @Override
    public int updateQuestion(Question question)
    {
        return questionMapper.updateQuestion(question);
    }

    /**
     * 删除问答编辑对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQuestionByIds(String ids)
    {
        return questionMapper.deleteQuestionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除问答编辑信息
     * 
     * @param id 问答编辑ID
     * @return 结果
     */
    @Override
    public int deleteQuestionById(Long id)
    {
        return questionMapper.deleteQuestionById(id);
    }
}
