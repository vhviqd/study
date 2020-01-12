package com.xuecheng.manage_cms.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.PageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        //创建实体类
        CmsPage cmsPage = new CmsPage();
        //创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching();
        //匹配别名，这个是注意的点，一定要返回这个对象
        matcher = matcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //设置查询条件
        //站点ID
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
       //模板id
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        //创建实例条件
        Example<CmsPage> cmsPageExample = Example.of(cmsPage,matcher);
        Pageable pageable = new PageRequest(page, size);

        //查询页面信息
        Page<CmsPage> all = cmsPageRepository.findAll(cmsPageExample, pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());

        //封装返回的对象
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;

    }

    /**
     * 页面唯一索引     校验页面是否存在
     * @param cmsPage
     * @return
     */
    public CmsPage  findCrmPage(CmsPage cmsPage) {
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(page != null){
            //校验页面是否存在，已存在则抛出异常   ，这里抛出异常进行封装，然后在弄一个捕获异常的类
           //throw new CustomException(CmsCode.CMS_ADDPAGE_EXISTSNAME);  //再次进行封装，不用每一次都throw
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        return null;
    }


    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult addCrmPage(CmsPage cmsPage) {
        CmsPage page = findCrmPage(cmsPage);
        if(null == page){
            //没有，可以新建
            cmsPage.setPageId(null);  //添加页面主键由spring data 自动生成
            CmsPage cmsPage1 = cmsPageRepository.save(cmsPage);
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage1);
            return cmsPageResult;
        }
        return null;
    }

    @Override
    public CmsPage findById(String id) {
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(id);
        if(cmsPage.isPresent()){
            return cmsPage.get();
        }
        ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        //执行了上面的异常抛出，下面的代码就不会执行了
        return new CmsPage();
    }

    @Override
    public CmsPageResult edit(String id, CmsPage cmsPage) {
        //根据主键ID查询
        CmsPage one = this.findById(id);
        if(one == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }
        //更新页面
        //更新模板id
        one.setTemplateId(cmsPage.getTemplateId());
        //更新所属站点
        one.setSiteId(cmsPage.getSiteId());
        //更新页面别名
        one.setPageAliase(cmsPage.getPageAliase());
        //更新页面名称
        one.setPageName(cmsPage.getPageName());
        //更新访问路径
        one.setPageWebPath(cmsPage.getPageWebPath());
        //更新物理路径
        one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        //执行更新
        CmsPage save = cmsPageRepository.save(one);

        if(save == null){
            //更新失败
            ExceptionCast.cast(CmsCode.UPDATE_FAILD);
            System.out.println("你好");
        }
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
        return cmsPageResult;
    }

    @Override
    public ResponseResult delete(String id) {
        //根据主键ID查询
        CmsPage one = this.findById(id);
        if(one == null){
            return new ResponseResult(CommonCode.FAIL);
        }
        //删除页面
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }



    /**
     * 1.获取页面信息
     * 2.从页面信息中获取页面的DataUrl
     * 3.根据DataUrl远程请求： @Bean
     *                           public RestTemplate restTemplate(){
     *                               return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
     *                          }
     *     利用RestTemplate 远程调用数据模型data
     * 4.获取页面的模板信息：根据页面信息中的模板id查询在分布式系统文件GridFS中查询页面模板信息
     * 4.执行页面静态化：加数据模型 + 模板
     */

    /**
     * @param pageId
     * @return
     */
    @Override
    public String getPageHtml(String pageId){

        pageId = "5da80b1ab018547ec4c4c2e5";
        //1获取页面信息
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }

        //2获取页面数据模型 存放在mongoDB数据库的cms_config集合中
        Map model = this.getModelByPage(cmsPage);
        if(model == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

       //3.获取页面模板
        String templateContent = getTemplateByPage(cmsPage);
        if(null == templateContent){
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //4.执行页面静态化
        String html = generateHtml(templateContent, model);
        if(StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 执行页面静态化
     * @param template
     * @param model
     * @return
     */
    public String generateHtml(String template,Map model){
        //生成配置类
        Configuration configuration = new Configuration(Configuration.getVersion());

        //模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",template);
        //配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);

        //获取模板
        try {
            Template template1 = configuration.getTemplate("template");
            //组装模板和数据模型
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取页面数据模板
     * @param cmsPage
     * @return
     */
    public String getTemplateByPage(CmsPage cmsPage){
        //获取页面模板ID
        String templateId = cmsPage.getTemplateId();
        if(null == templateId){
            ExceptionCast.cast(CmsCode.CMS_PAGE_TEMPLATEIDISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //从模板中找出模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            if(templateFileId == null){
                ExceptionCast.cast(CmsCode.CMS_TEMPLATE_TEMPLATEFILEIDISNULL);
            }

            //取出文件内容
            //根据id查询文件   Criteria:条件对象
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResource，用于获取流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //获取流中的数据
            String content = null;
            try {
                content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return  null;

    }


    /**
     * 获取页面模型
     * @param cmsPage
     * @return
     */
    public Map getModelByPage(CmsPage cmsPage){
        String dataUrl = cmsPage.getDataUrl();
        if(null == dataUrl){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //根据dataUrl获取数据模型(这里是远程的请求，分布式的，如果写成接口的话，就要在一个程序中。)
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }
}
