package com.ruoyi.project.system.qanda.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.qanda.domain.Qanda;
import com.ruoyi.project.system.qanda.service.IQandaService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 问答Controller
 * 
 * @author ruoyi
 * @date 2021-07-07
 */
@Controller
@RequestMapping("/system/qanda")
public class QandaController extends BaseController
{
    private String prefix = "system/qanda";

    @Autowired
    private IQandaService qandaService;

    @RequiresPermissions("system:qanda:view")
    @GetMapping()
    public String qanda()
    {
        return prefix + "/qanda";
    }

    /**
     * 查询问答列表
     */
    @RequiresPermissions("system:qanda:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Qanda qanda)
    {
        startPage();
        List<Qanda> list = qandaService.selectQandaList(qanda);
        return getDataTable(list);
    }

    /**
     * 导出问答列表
     */
    @RequiresPermissions("system:qanda:export")
    @Log(title = "问答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Qanda qanda)
    {
        List<Qanda> list = qandaService.selectQandaList(qanda);
        ExcelUtil<Qanda> util = new ExcelUtil<Qanda>(Qanda.class);
        return util.exportExcel(list, "问答数据");
    }

    /**
     * 新增问答
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存问答
     */
    @RequiresPermissions("system:qanda:add")
    @Log(title = "问答", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Qanda qanda)
    {
        return toAjax(qandaService.insertQanda(qanda));
    }

    /**
     * 修改问答
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Qanda qanda = qandaService.selectQandaById(id);
        mmap.put("qanda", qanda);
        return prefix + "/edit";
    }

    /**
     * 修改保存问答
     */
    @RequiresPermissions("system:qanda:edit")
    @Log(title = "问答", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Qanda qanda)
    {
        return toAjax(qandaService.updateQanda(qanda));
    }

    /**
     * 删除问答
     */
    @RequiresPermissions("system:qanda:remove")
    @Log(title = "问答", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(qandaService.deleteQandaByIds(ids));
    }
}
