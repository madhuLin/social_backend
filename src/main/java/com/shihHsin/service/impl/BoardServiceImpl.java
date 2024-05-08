package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.BoardMapper;
import com.shihHsin.pojo.Board;
import com.shihHsin.service.IBoardService;
import org.springframework.stereotype.Service;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Service
public class BoardServiceImpl extends ServiceImpl<BoardMapper, Board> implements IBoardService {
}
