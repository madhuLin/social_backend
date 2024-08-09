package com.shihHsin.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Board;

import java.util.Map;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
public interface IBoardService extends IService<Board> {
    public Map<Integer, String> getBoardNames();
}
