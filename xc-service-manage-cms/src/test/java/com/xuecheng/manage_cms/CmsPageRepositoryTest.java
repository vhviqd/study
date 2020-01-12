package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@Data
@SpringBootTest
@RunWith(SpringRunner.class)   //@RunWith：一个运行器，用SpringRunner来运行
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository ;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void findByName(){
        CmsPage byPageName = cmsPageRepository.findByPageName("index.html");
        System.out.println(byPageName);
    }

    @Test
    public void findByPath(){
        CmsPage page = cmsPageRepository.findByPageWebPath("/index.html");
        System.out.println(page);
    }

    //分页测试
    @Test
    public void testFindPage() {
        int page = 0;//从0开始
        int size = 10;//每页记录数
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    /**
     * 自定义模糊查询： 还有分页条件了
     * ---这里根据 MongoRepository 中的一个接口进行测试
     * <S extends T> Page<S> findAll(Example<S> var1, Pageable var2);
     * 查询条件：
     * 站点Id：精确匹配
     * 模板Id：精确匹配
     * 页面别名：模糊匹配
     */
    @Test
    public void testFindAllByExample(){
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        CmsPage cmsPage = new CmsPage();

        /**
         * 页面别名进行模糊查询  (利用匹配器中的包含方法)
         * ExampleMatcher.GenericPropertyMatchers.contains() 包含
         * ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配
         */
        //切记：这里一定要吧这个对象返回去
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //实体类的条件值
//        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");   //站点id
//        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");   //模板id
        cmsPage.setPageAliase("分类");

        //创建实例: 实体类 + 匹配器
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        //分页
        Pageable pageable = new PageRequest(0, 10);

        //根据模糊条件组装 实体类进行查询
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = cmsPages.getContent();
        System.out.println(content);
        System.out.println(cmsPages);

    }


    @Test
    public void testUpdate(){
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        //将非空判断标准化，提醒你做非空判断
        if(optional.isPresent()){
            //获取对象
            CmsPage cmsPage = optional.get();
            //设置属性
            cmsPage.setPageName("你好");
            //修改：
            cmsPageRepository.save(cmsPage);
        }
    }

    /**
     * 模糊查询，设置条件匹配器
     */
    @Test
    public void findPageBySiteId(){

        //条件匹配器
        ExampleMatcher exampleMatcher  = ExampleMatcher.matching();
        //匹配别名
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //分页查询
        int page = 0;//从0开始
        int size = 33;//每页记录数
        Pageable pageable = PageRequest.of(page,size);

        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("轮播");
        Example<CmsPage> example =  Example.of(cmsPage,exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        System.out.println(all);

    }

    /**
     * 创建唯一索引，用于校验 页面是否存在
     * @return
     */
    @Test
    public void findByPageNameAndSiteIdAndPageWebPathaaa(){
        String pageName = "index_category.html";
        String siteId = "5a751fab6abb5044e0d19ea1";
        String pageWebPath = "/include/index_category.html";
        CmsPage cmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);

        System.out.println(cmsPage);


    }




}
