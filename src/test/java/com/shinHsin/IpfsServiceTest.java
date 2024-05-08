package com.shinHsin;

import com.shihHsin.SHApplication;
import com.shihHsin.service.IIpfsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest(classes = SHApplication.class)
public class IpfsServiceTest {

    @Resource
    private IIpfsService ipfsService;

    // 上传
    @Test
    public void uploadIpfs() throws IOException {
        byte[] data = "spring boot".getBytes();
        String hash = ipfsService.uploadToIpfs(data);
        // Qmf412jQZiuVUtdgnB36FXFX7xg5V6KEbSJ4dpQuhkLyfD
        log.debug("hashValue:" + hash);
        System.out.println("hashValue:" + hash);
    }

    // 下载
    @Test
    public void downloadIpfs() {
        String hash = "QmQdGZs715cAhnYMEKg32Yd6r3qkaA7htLRTxtg27kQ7TN";
        byte[] data = ipfsService.downFromIpfs(hash);
        // spring boot

        log.debug("dataValue:" + new String(data));
        System.out.println("dataValue:" + new String(data));
    }
}
