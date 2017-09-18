package com.wallbase.train;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainVideoApplicationTests {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void contextLoads() throws URISyntaxException {

        try {
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

                        logger.info("key: {} fileName: {} LastModified: {} Size {} ETag: {}", key, fileName, lastModified, size, eTag);

                    }
                }
            }

//

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

}
