package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2018/3/31.
 * 定义新增页面的响应模型
 */
@Data
@NoArgsConstructor   //加入无参构造器
public class CmsPageResult extends ResponseResult {
    /**
     * 页面的pojo类
     */
    CmsPage cmsPage;

    /**
     * 响应的构造方法
     * @param resultCode
     * @param cmsPage
     */
    public CmsPageResult(ResultCode resultCode,CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
