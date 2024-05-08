package com.shihHsin.service.impl;



import com.shihHsin.service.IIpfsService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;



@Slf4j
@Service
public class IpfsServiceImpl implements IIpfsService {
    // ipfs 的伺服器位址和端口，與yaml檔案中的設定對應
    @Value("${ipfs.config.multi-addr}")
    private String multiAddr;

    private IPFS ipfs;

    private final Logger logger = Logger.getLogger(IpfsServiceImpl.class.getName());

    @PostConstruct
    public void setMultiAddr() {
        ipfs = new IPFS(multiAddr);
    }

    @Override
    public String uploadToIpfs(String filePath) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePath));

        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();
    }

//    @Override
//    public String uploadToIpfs(byte[] data) throws IOException {
//        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
//        MerkleNode addResult = ipfs.add(file).get(0);
//        return addResult.hash.toString();
//    }

    public String uploadToIpfs(byte[] data) throws IOException {
        try {
            NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
            MerkleNode addResult = ipfs.add(file).get(0);
            return addResult.hash.toString();
        } catch (IOException e) {
            throw new IOException("無法上傳到IPFS", e);
        }
    }

    @Override
    public byte[] downFromIpfs(String hash) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
//            e.printStackTrace();
            logger.warning( "無法寫入檔案: " + e.getMessage());
        }
        return data;
    }

    @Override
    public void downFromIpfs(String hash, String destFile) {
        try {
            byte[] data = ipfs.cat(Multihash.fromBase58(hash));
            if (data != null && data.length > 0) {
                File file = new File(destFile);
                if (file.exists()) {
                    if (!file.delete()) {

                        logger.warning("無法刪除檔案: " + destFile);
                    }
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(data);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "無法寫入檔案: " + destFile, e);
                }
            } else {
                logger.log(Level.SEVERE, "無法從IPFS獲取檔案資料: " + hash);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "無法從IPFS獲取檔案: " + hash, e);
        }
    }
}
