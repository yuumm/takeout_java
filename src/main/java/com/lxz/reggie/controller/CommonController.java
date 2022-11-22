package com.lxz.reggie.controller;

import com.lxz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

// 该controller主要用于文件的上传和下载
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    // 通过value可以将外部的值赋给变量basePath。${reggie.path}表示获取配置文件application.yml中的reggie.path的内容
    @Value("${reggie.path}")
    private String basePath;

    // 文件上传的方法
    @PostMapping("/upload")
    // 如果前端发送过来的数据是文件或者图片之类的，那么需要用到MultipartFile进行接收，这个类是spring封装好的
    // 这里的参数的名称还是不能随便乱取，要和前端设置的传递的数据的名称要一致，否则无法对应
    public R<String> upload(MultipartFile file) throws IOException {
        // 这样保存的文件是一个临时文件，之后会删除的，所以要指定一个文件路径，这样才不会被删除
        log.info(file.toString());

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取原始文件名中的后缀名（包括后缀名的那个.）
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用uuid可以实现随机生成文件名，避免文件名重复
        String fileName = UUID.randomUUID().toString() + substring;

        // 判断配置文件中的目录是否存在，若不存在则创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 通过transferTo可以对文件进行转存。new File表示新建一个文档
        file.transferTo(new File(basePath + fileName));
        return R.success(fileName);
    }

    // 该函数用于下载文件（例如页面要展示图片，需要先下载对应图片，然后才能展示）
    @GetMapping("/download")
    // response的主要作用是输出流需要通过response获得
    public void download(String name, HttpServletResponse response) throws IOException {
        // 输入流，通过输入流读取文件内容
        // 创建一个文件输入流。basePath + name可以获得文件的具体路径
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

        // 输出流，通过输出流将文件内容写回浏览器，并在浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置传入流的数据是什么
        response.setContentType("iamge/jpeg");

        // 将输入流读进一个数组，然后通过循环读取里面的数据，再将数据写入输出流
        int len = 0;
        byte[] bytes = new byte[1024];
        // read(bytes)表示将数据读进bytes数组，!= -1表示依然有数据在读
        while ((len = fileInputStream.read(bytes)) != -1) {
            // 0, len表示从第一个数据开始写，写到len这么长
            outputStream.write(bytes, 0, len);
            // 通过flush进行刷新
            outputStream.flush();
        }

        outputStream.close();
        fileInputStream.close();
    }
}
