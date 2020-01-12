package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;

public interface CmsConfigService {
    /**
     * 根据id查询页面配置信息
     * @param id
     * @return
     */
    CmsConfig getModel(String id);
}
