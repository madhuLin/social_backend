package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.FollowBoardMapper;
import com.shihHsin.pojo.FollowBoard;
import com.shihHsin.service.IFollowBoardService;
import org.springframework.stereotype.Service;

@Service
public class FollowBoardServiceImpl extends ServiceImpl<FollowBoardMapper, FollowBoard> implements IFollowBoardService {
}
