package com.ruoyi.project.system.qanda.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.qanda.mapper.QandaMapper;
import com.ruoyi.project.system.qanda.domain.Qanda;
import com.ruoyi.project.system.qanda.service.IQandaService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 问答Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-07-07
 */
@Service
public class QandaServiceImpl implements IQandaService 
{
    @Autowired
    private QandaMapper qandaMapper;

    /**
     * 查询问答
     * 
     * @param id 问答ID
     * @return 问答
     */
    @Override
    public Qanda selectQandaById(Long id)
    {
        return qandaMapper.selectQandaById(id);
    }

    /**
     * 查询问答列表
     * 
     * @param qanda 问答
     * @return 问答
     */
    @Override
    public List<Qanda> selectQandaList(Qanda qanda)
    {
        return qandaMapper.selectQandaList(qanda);
    }

    /**
     * 新增问答
     * 
     * @param qanda 问答
     * @return 结果
     */
    @Override
    public int insertQanda(Qanda qanda)
    {
        return qandaMapper.insertQanda(qanda);
    }

    /**
     * 修改问答
     * 
     * @param qanda 问答
     * @return 结果
     */
    @Override
    public int updateQanda(Qanda qanda)
    {
        return qandaMapper.updateQanda(qanda);
    }

    /**
     * 删除问答对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQandaByIds(String ids)
    {
        return qandaMapper.deleteQandaByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除问答信息
     * 
     * @param id 问答ID
     * @return 结果
     */
    @Override
    public int deleteQandaById(Long id)
    {
        return qandaMapper.deleteQandaById(id);
    }
}
