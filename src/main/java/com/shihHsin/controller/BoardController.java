package com.shihHsin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Board;
import com.shihHsin.service.IBoardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:5173") //dev
@RestController
@RequestMapping("/board")
public class BoardController {
    @Resource
    public IBoardService boardService;

    @RequestMapping("/list")
    public R get() {
        List<Board> boardList = boardService.list();
        return R.success(boardList);
    }
}
