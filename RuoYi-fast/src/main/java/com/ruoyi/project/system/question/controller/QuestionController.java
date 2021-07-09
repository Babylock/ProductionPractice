package com.ruoyi.project.system.question.controller;

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
import com.ruoyi.project.system.question.domain.Question;
import com.ruoyi.project.system.question.service.IQuestionService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 问答编辑Controller
 * 
 * @author czx
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/system/question")
public class QuestionController extends BaseController
{
    private String prefix = "system/question";

    @Autowired
    private IQuestionService questionService;

    @RequiresPermissions("system:question:view")
    @GetMapping()
    public String question()
    {
        return prefix + "/question";
    }

    /**
     * 查询问答编辑列表
     */
    @RequiresPermissions("system:question:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Question question)
    {
        startPage();
        List<Question> list = questionService.selectQuestionList(question);
        return getDataTable(list);
    }

    /**
     * 导出问答编辑列表
     */
    @RequiresPermissions("system:question:export")
    @Log(title = "问答编辑", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Question question)
    {
        List<Question> list = questionService.selectQuestionList(question);
        ExcelUtil<Question> util = new ExcelUtil<Question>(Question.class);
        return util.exportExcel(list, "问答编辑数据");
    }

    /**
     * 新增问答编辑
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存问答编辑
     */
    @RequiresPermissions("system:question:add")
    @Log(title = "问答编辑", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Question question)
    {
        return toAjax(questionService.insertQuestion(question));
    }

    /**
     * 修改问答编辑
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Question question = questionService.selectQuestionById(id);
        mmap.put("question", question);
        return prefix + "/edit";
    }

    /**
     * 修改保存问答编辑
     */
    @RequiresPermissions("system:question:edit")
    @Log(title = "问答编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Question question)
    {
        return toAjax(questionService.updateQuestion(question));
    }

    /**
     * 删除问答编辑
     */
    @RequiresPermissions("system:question:remove")
    @Log(title = "问答编辑", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(questionService.deleteQuestionByIds(ids));
    }
}
