package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * dao接口开发
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    /**
     * 根据页面名称查询
     */
    CmsPage findByPageName(String pageName);
    CmsPage findByPageWebPath(String pageName);
//    //根据页面名称和类型查询
//    CmsPage findByPageNameAndPageType(String pageName,String pageType);
//    //根据站点和页面类型查询记录数
//    int countBySiteIdAndPageType(String siteId,String pageType);
//    //根据页面名称、站点id、页面访问路径查询
//    Page<CmsPage> findBySiteIdAndPageType(String siteId, String pageType, SpringDataWebProperties.Pageable pageable);

    /**
     * 根据页面名称、站点id、页面访问路径查询  （用于校验页面是否存在）
     * @param pageName
     * @param siteId
     * @param pageWebPath
     * @return
     */
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);

}
