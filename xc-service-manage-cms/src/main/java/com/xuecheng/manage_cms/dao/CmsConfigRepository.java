package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
* @Description:    页面配置查询 （轮播图，精品推荐，分类信息等）
* @Author:         zhangzhiqiang
* @CreateDate:     2019/10/13
* @Version:        1.0
*/
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
