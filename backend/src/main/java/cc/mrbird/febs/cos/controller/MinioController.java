package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.config.MinioConfig;
import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@CrossOrigin
@RequestMapping("/cos/minio")
public class MinioController {

    @Resource
    private MinioConfig minioConfig;

    /**
     * 上传
     *
     * @param multipartFile 文件
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/voice/upload")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return this.minioConfig.putObject(multipartFile);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param response 返回
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        this.minioConfig.download(fileName, response);
    }

    /**
     * 列出所有存储桶名称
     *
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/list")
    public List<String> list() throws Exception {
        return this.minioConfig.listBucketNames();
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 桶名称
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/createBucket")
    public boolean createBucket(String bucketName) throws Exception {
        return this.minioConfig.makeBucket(bucketName);
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 桶名称
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/deleteBucket")
    public boolean deleteBucket(String bucketName) throws Exception {
        return this.minioConfig.removeBucket(bucketName);
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 桶名称
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/listObjectNames")
    public List<String> listObjectNames(String bucketName) throws Exception {
        return this.minioConfig.listObjectNames(bucketName);
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/removeObject")
    public boolean removeObject(String bucketName, String objectName) throws Exception {
        return this.minioConfig.removeObject(bucketName, objectName);
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/getObjectUrl")
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        return this.minioConfig.getObjectUrl(bucketName, objectName);
    }
}
