package com.ruoyi.project.system.question.service;

import java.util.List;
import com.ruoyi.project.system.question.domain.Question;

/**
 * 问答编辑Service接口
 * 
 * @author czx
 * @date 2021-07-08
 */
public interface IQuestionService 
{
    /**
     * 查询问答编辑
     * 
     * @param id 问答编辑ID
     * @return 问答编辑
     */
    public Question selectQuestionById(Long id);

    /**
     * 查询问答编辑列表
     * 
     * @param question 问答编辑
     * @return 问答编辑集合
     */
    public List<Question> selectQuestionList(Question question);

    /**
     * 新增问答编辑
     * 
     * @param question 问答编辑
     * @return 结果
     */
    public int insertQuestion(Question question);

    /**
     * 修改问答编辑
     * 
     * @param question 问答编辑
     * @return 结果
     */
    public int updateQuestion(Question question);

    /**
     * 批量删除问答编辑
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQuestionByIds(String ids);

    /**
     * 删除问答编辑信息
     * 
     * @param id 问答编辑ID
     * @return 结果
     */
    public int deleteQuestionById(Long id);
}
