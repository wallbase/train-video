package com.wallbase.train.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 文件下载器
 * Created by wangkun23 on 2017/9/18.
 */
public class DownloadTask implements Runnable {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private ClientHttpRequestFactory requestFactory;
    private String key;

    public DownloadTask(final String key, final ClientHttpRequestFactory requestFactory) {
        this.key = key;
        this.requestFactory = requestFactory;
    }

    @Override
    public void run() {
        try {
            String baseUrl = "http://video.ljcdn.com/";
            String fileName = StringUtils.getFilename(key);

            ClientHttpRequest request = requestFactory.createRequest(new URI(baseUrl.concat(key)), HttpMethod.GET);
            ClientHttpResponse response = request.execute();
            logger.info("StatusCode {}", response.getStatusCode());
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                byte[] buffer = FileCopyUtils.copyToByteArray(response.getBody());
                File dest = new File(fileName);
                FileCopyUtils.copy(buffer, dest);
                logger.info("getBody {}", dest.getAbsolutePath());
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }
}
