package com.shihHsin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.NotificationDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Activitie;
import com.shihHsin.service.IActivitieService;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    public IActivitieService activitiesService;

    @Resource
    public IUserService userService;

    @RequestMapping("/userid={id}")
    public R getNotification(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<Activitie> activitiesWrapper = new LambdaQueryWrapper<>();
        activitiesWrapper.eq(Activitie::getTargetId, id);
        List<Activitie> activitiesList = activitiesService.list(activitiesWrapper);
        List<Integer> activitiesIds = activitiesList.stream()
                .map(Activitie::getUserId)
                .toList();

        List<NotificationDto> notiList = new ArrayList<>();
        for (Integer activitiesId : activitiesIds) {
            NotificationDto notiDto = new NotificationDto();
            LambdaQueryWrapper<Activitie> notiWrapper = new LambdaQueryWrapper<>();
            notiWrapper.eq(Activitie::getUserId, activitiesId);
            notiDto.setUserid(activitiesId);
            notiDto.setType(activitiesService.getOne(notiWrapper).getType());
            notiDto.setUsername(String.valueOf(userService.getById(activitiesId).getName()));
            notiList.add(notiDto);
        }

        return R.success(notiList);
    }
}