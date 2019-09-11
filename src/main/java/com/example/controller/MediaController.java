package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/media")
public class MediaController {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 把fileUpload.html拿下来单独运行，选择图片上传
     */
    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        log.info(fileName);

        multipartFile.transferTo(new File("D://uploads/" + fileName));
        return "uploaded";
    }

    /**
     * 文件下载
     * 不加上:.+会截断后缀
     * 需要设置返回头信息，所以用ResponseEntity返回
     * 如果不加上Content-Disposition那么返回的文件内容就会直接展示在浏览器上
     * 这种方式只适合小文件下载的情况，文件大小不能大于2G不然byte数组就存不下了。
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName) throws Exception {
        log.info("文件名:{}", fileName);
        File file = new File("D:\\uploads\\" + fileName);
        log.info("文件大小:{}", file.length());
        byte[] body = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(body);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=" + fileName);

        ResponseEntity<byte[]> response = new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
        return response;
    }

    /**
     * 下载大文件的做法,通过HttpServletResponse不断地write数据到客户端
     */
    @GetMapping("/bigDownload/{fileName:.+}")
    public void bigDownload(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File("D:\\uploads\\" + fileName)));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());

        try{
            byte[] buffer = new byte[1024];
            int readCount = 0;//读到多少字节数据，读不到数据为-1
            while(-1 != (readCount = bufferedInputStream.read(buffer, 0, buffer.length))){
                bufferedOutputStream.write(buffer, 0, readCount);
                bufferedOutputStream.flush();
            }
        }finally {
            bufferedInputStream.close();
            bufferedOutputStream.close();
        }
    }
}
