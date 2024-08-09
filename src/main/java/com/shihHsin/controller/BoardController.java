package com.shihHsin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.BoardDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Board;
import com.shihHsin.service.IBoardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

    @Value("${upload.path}")
    private String uploadPath;
    @Resource
    public IBoardService boardService;

    @Async
    public CompletableFuture<List<BoardDto>> getBoardList(String uploadPath, List<Board> boardList) {
        List<BoardDto> updatedBoardList = boardList.stream().map(board -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setName(board.getName());
            boardDto.setId(board.getId());
            // 讀取文件並轉換為 base64 字串
            Path path = Paths.get(uploadPath + board.getLogo());
            try {
                byte[] fileContent = Files.readAllBytes(path);
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                boardDto.setLogoBase64(encodedString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return boardDto;
        }).collect(Collectors.toList());

        return CompletableFuture.completedFuture(updatedBoardList);
    }
    @RequestMapping("/list")
    public R get() {
        List<Board> boardList = boardService.list();
        CompletableFuture<List<BoardDto>> future = getBoardList(uploadPath, boardList);

        List<BoardDto> updatedBoardList;
        try {
            updatedBoardList = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return R.error("Failed to get board list");
        }

        return R.success(updatedBoardList);
    }

//    @RequestMapping("/list")
//    public R get() {
//        List<Board> boardList = boardService.list();
//        List<BoardDto> updatedBoardList = boardList.stream().map(board -> {
//            BoardDto boardDto = new BoardDto();
//            boardDto.setName(board.getName());
//            boardDto.setId(board.getId());
//            // 讀取文件並轉換為 base64 字串
//            Path path = Paths.get(uploadPath  + board.getLogo());
////            log.debug("path: " + path.toString());
//            try {
//                byte[] fileContent = Files.readAllBytes(path);
//                String encodedString = Base64.getEncoder().encodeToString(fileContent);
//                boardDto.setLogoBase64(encodedString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return boardDto;
//        }).collect(Collectors.toList());
//        return R.success(updatedBoardList);
//    }
}
