package com.syl.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.syl.oss.service.OssService;
import com.syl.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss具体实现
    @Override
    public String uploadFileAvatar(MultipartFile multipartFile) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = multipartFile.getInputStream();
            //1、获取文件名称
            String filename = multipartFile.getOriginalFilename();
            //获取后缀名
            String suffix = filename.substring(filename.lastIndexOf("."));
            //2、在文件名称前面添加唯一随机的值
            filename = UUID.randomUUID().toString().replaceAll("-","");
            //3、把文件按日期进行分类
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            filename = "picture/"+dataPath+"/"+filename+suffix;
            // 创建PutObject请求。
            //4、第二个参数：上传文件的路径和名称
            ossClient.putObject(bucketName,filename,inputStream);
            //5、把上传之后的文件路径返回
            //https://grain-school.oss-cn-hangzhou.aliyuncs.com/picture/1221842.png
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            System.out.println("url:" + url);
            return url;
        } catch (OSSException oe) {
            System.out.println("捕获一个OSSException，这意味着您的请求已提交给OSS，”\n" +
                    "+ \"，但由于某些原因被拒绝并给出错误响应.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return null;
        } catch (ClientException ce) {
            System.out.println("捕获ClientException，这意味着客户端遇到\"\n" +
                    "+“试图与OSS沟通时出现严重的内部问题，”\n" +
                    "+”，例如不能访问网络.");
            System.out.println("Error Message:" + ce.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出现了其他异常");
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }
}
