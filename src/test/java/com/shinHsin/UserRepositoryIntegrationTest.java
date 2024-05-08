package com.shinHsin;

import com.shihHsin.SHApplication;
import com.shihHsin.pojo.User;
import com.shihHsin.mapper.TestMapper;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@Slf4j
@SpringBootTest(classes = SHApplication.class)
public class UserRepositoryIntegrationTest {
//    @Resource
//    private ITestService testService;

    @Resource
    private TestMapper testMapper;
    UserRepositoryIntegrationTest() {

    }
    @Test
    public void findData() {
        log.debug("askadnjkasn");

        List<User> list = testMapper.selectList(null);
        list.forEach(System.out::println);
        System.out.println("safsfsdfdsf");
    }


    @Resource
    private IUserService userService;
    @Test
    public void findUser() {
        log.debug("askadnjkasn");

        List<User> list = userService.list();
        list.forEach(System.out::println);
        System.out.println("safsfsdfdsf");
    }
}
