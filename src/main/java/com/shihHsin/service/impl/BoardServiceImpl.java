package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.BoardMapper;
import com.shihHsin.pojo.Board;
import com.shihHsin.service.IBoardService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    BoardMapper boardMapper;

    @Override
    public Map<Integer, String> getBoardNames() {
        List<Board> boards = boardMapper.selectList(null);
        return boards.stream()
                .collect(Collectors.toMap(Board::getId, Board::getName));
    }
}
