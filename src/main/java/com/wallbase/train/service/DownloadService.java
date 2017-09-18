package com.wallbase.train.service;

import com.wallbase.train.task.DownloadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 文件下载器
 * Created by wangkun23 on 2017/9/18.
 */
@Service
public class DownloadService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 下载文件
     */
    public void download() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // never forget this!
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        ClassPathResource classPathResource = new ClassPathResource("cdn.xml");
        if (classPathResource.exists()) {
            Document doc = builder.parse(classPathResource.getInputStream());
            Element root = doc.getDocumentElement();

            NodeList nl = root.getElementsByTagName("Contents");
            logger.info("nl {}", nl.getLength());
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node instanceof Element) {
                    Element contents = (Element) node;

                    String key = contents.getElementsByTagName("Key").item(0).getTextContent();
                    String fileName = StringUtils.getFilename(key);

                    String lastModified = contents.getElementsByTagName("LastModified").item(0).getTextContent();
                    String size = contents.getElementsByTagName("Size").item(0).getTextContent();
                    String eTag = contents.getElementsByTagName("Size").item(0).getTextContent();


                    taskExecutor.execute(new DownloadTask(key, requestFactory));
                    logger.info("key: {} fileName: {} LastModified: {} Size {} ETag: {}", key, fileName, lastModified, size, eTag);

                }
            }
        }
    }
}
