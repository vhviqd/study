package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface PageService {

    /**
     * 页面列表分页查询
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    CmsPageResult addCrmPage(CmsPage cmsPage) ;

    CmsPage findById(String id);

    CmsPageResult edit(String id, CmsPage cmsPage);

    ResponseResult delete(String id);

    String getPageHtml(String pageId);




}
