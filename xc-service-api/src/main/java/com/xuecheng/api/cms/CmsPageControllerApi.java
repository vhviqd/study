package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    /**
     * 页面查询
     * @param page  第几页
     * @param size  每页多少条
     * @param queryPageRequest  查询请求参数：包含自定义查询
     * @return  响应参数
     *
     */
    @ApiOperation("分页查询页面列表（自定义查询-别名模糊查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);



    /**
     * 新增
     * @param cmsPage
     * @return
     */
    @ApiOperation("添加页面")
    @ApiImplicitParam(name = "cmsPage",value = "页面内容",required = true, paramType = "path" )
    CmsPageResult addCrmPage(CmsPage cmsPage);


    @ApiOperation("通过ID查询页面")
    CmsPage findById(String id);


    @ApiOperation("修改页面")
    CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("删除页面")
    ResponseResult delete(String id);

    @ApiOperation("获取页面")
    String getHtml(String pageId, HttpServletResponse response);
}
