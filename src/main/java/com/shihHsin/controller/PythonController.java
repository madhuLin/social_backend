package com.shihHsin.controller;

import com.shihHsin.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@Slf4j
@RestController
@RequestMapping("/python")
public class PythonController {

    @RequestMapping("/gpt")
    public R ArticleChatgpt(@RequestParam String title, String content) {
        log.debug("aasdsadsd");
        log.debug("title:" + title + " content:" + content);
        String line = "";
        String output = "";
        try {
            // 指定 Python 可执行文件和脚本路径
            String pythonExe = "python.exe"; // 在 Windows 上可能需要使用 "python.exe"
            String scriptPath = "C:\\PF\\Java\\Social\\src\\main\\resources\\python\\AI.py";
            // 构建命令
            String[] cmd = new String[]{pythonExe, scriptPath};
            // 创建进程构建器
            ProcessBuilder pb = new ProcessBuilder(cmd);
            // 启动进程
            Process process = pb.start();
            // 向 Python 脚本传递输入
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), "cp950"))) {
                String inputContent = "標題是：" + title + "內容是：" + content + "\n";
                writer.write(inputContent);
                writer.flush();
            }
            // 读取 Python 脚本的输出
            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), "cp950"));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "cp950"))) {
                System.out.println("Standard output:");
                System.out.println(stdInput);
                while ((line = stdInput.readLine()) != null) {
                    output += line;
                    System.out.println(line);
                }
                System.out.println("Standard error:");
                while ((line = stdError.readLine()) != null) {
                    System.out.println(line);
                }
            }
            // 等待进程结束并获取退出代码
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("test1" + output);
//        System.out.println("test2" + line);
        return R.success(output);
    }
}