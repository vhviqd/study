package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms.service.impl.PageServiceImpl;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
* @Description:    测试RestTemplate
* @Author:         zhangzhiqiang
* @CreateDate:     2019/10/13
* @Version:        1.0
*/
@Data
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFSTest {

    //注入模板，可以快速实现增删改查 基本功能
    @Autowired
    private GridFsTemplate gridFsTemplate;
    //用于打开下载流对象
    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 测试存储文件到mongoDB的数据库中，查看文件（fs.fiels存放的是文件的基本信息；fs.chunks存放二进制文件，
     * 可以导出来，就是文件信息。）
     * @throws FileNotFoundException
     */
    @Test
    public void testGridFs() throws FileNotFoundException {
        //ObjectId store(InputStream var1, String var2);  参数：输入流，文件名
        //要存储的文件
        File file = new File("C:\\Users\\12863\\Desktop\\fsdownload/name.ftl");
        //定义输入流
        FileInputStream inputStream = new FileInputStream(file);
        //像2GridFS存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "name.ftl");
        System.out.println(objectId);
    }

    /**
     * 测试下载文件
     */
    @Test
    public void queryFile() throws IOException {
        String fileId = "5da7194418a77103a82d8a60";
        //根据id查询文件   Criteria:条件对象
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(s);
    }

    /**
     * 删除文件:会将两个集合里面的都删除掉
     * @throws IOException
     */
    @Test
    public void testDelFile() throws IOException {
    //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5da7194418a77103a82d8a60")));
    }




}
