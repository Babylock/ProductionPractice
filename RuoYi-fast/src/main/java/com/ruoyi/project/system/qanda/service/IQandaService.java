package com.ruoyi.project.system.qanda.service;

import java.util.List;
import com.ruoyi.project.system.qanda.domain.Qanda;

/**
 * 问答Service接口
 * 
 * @author ruoyi
 * @date 2021-07-07
 */
public interface IQandaService 
{
    /**
     * 查询问答
     * 
     * @param id 问答ID
     * @return 问答
     */
    public Qanda selectQandaById(Long id);

    /**
     * 查询问答列表
     * 
     * @param qanda 问答
     * @return 问答集合
     */
    public List<Qanda> selectQandaList(Qanda qanda);

    /**
     * 新增问答
     * 
     * @param qanda 问答
     * @return 结果
     */
    public int insertQanda(Qanda qanda);

    /**
     * 修改问答
     * 
     * @param qanda 问答
     * @return 结果
     */
    public int updateQanda(Qanda qanda);

    /**
     * 批量删除问答
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQandaByIds(String ids);

    /**
     * 删除问答信息
     * 
     * @param id 问答ID
     * @return 结果
     */
    public int deleteQandaById(Long id);
}
