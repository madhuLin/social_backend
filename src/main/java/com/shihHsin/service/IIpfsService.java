package com.shihHsin.service;
import java.io.IOException;
public interface IIpfsService {
    /**
     * 指定path+檔案名稱,上傳至ipfs
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    String uploadToIpfs(String filePath) throws IOException;

    /**
     * 將byte格式的資料,上傳至ipfs
     *
     * @param data
     * @return
     * @throws IOException
     */
    String uploadToIpfs(byte[] data) throws IOException;

    /**
     * 根據Hash值,從ipfs下載內容,返回byte資料格式
     *
     * @param hash
     * @return
     */
    byte[] downFromIpfs(String hash);

    /**
     * 根據Hash值,從ipfs下載內容,並寫入指定檔案destFilePath
     *
     * @param hash
     * @param destFilePath
     */
    void downFromIpfs(String hash, String destFilePath);
}
