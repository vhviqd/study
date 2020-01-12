package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试mongoDb数据库连接
 * @author 12863
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size,
                                        QueryPageRequest queryPageRequest) {
        return pageService.findList(page-1,size,queryPageRequest);
    }


    @PostMapping("/add")
    @Override
    public CmsPageResult addCrmPage(@RequestBody CmsPage cmsPage) {
        return pageService.addCrmPage(cmsPage);
    }

    /**
     * 根据主键查询页面
     * @param id
     * @return
     */
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.findById(id);
    }

    /**
     * 根据主键修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        //这里@PathVariable("id") 表示接收get路径上面的参数，@RequestBody表示接收body里面的参数，并转换成json格式
        return pageService.edit(id,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }


    /**
     * 获取页面
     * @param pageId
     * @param response
     * @return
     */
    @Override
    @GetMapping("/getHtml/{id}")
    public String getHtml(@PathVariable("id") String pageId, HttpServletResponse response){
        String pageHtml = pageService.getPageHtml(pageId);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出到页面
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageHtml;
    }
}
