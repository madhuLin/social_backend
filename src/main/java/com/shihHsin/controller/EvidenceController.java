package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.EvidenceChainDto;
import com.shihHsin.Dto.EvidenceDto;
import com.shihHsin.Dto.EvidenceUploadDto;
import com.shihHsin.Dto.VerificationChainDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Evidence;
import com.shihHsin.pojo.Image;
import com.shihHsin.pojo.ImageEvidence;
import com.shihHsin.pojo.Verification;
import com.shihHsin.service.IEvidenceService;
import com.shihHsin.service.IImageEvidenceService;
import com.shihHsin.service.ISupportService;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/evidence")
public class EvidenceController {
    @Resource
    private IEvidenceService evidenceService;

    @Resource
    private IUserService userService;

    @Resource
    private IImageEvidenceService imageService;

    @Value("${upload.path}")
    private String uploadPath;


    private CompletableFuture<List<String>> findEvidenceImagesAsync(int evidenceId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Image> images = imageService.findImagesByEvidenceId(evidenceId);
            List<String> imageBytes = new ArrayList<>();
            for (Image image : images) {
                Path path = Paths.get(uploadPath, image.getImagePath());
                try {
                    byte[] fileContent = Files.readAllBytes(path);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imageBytes.add(encodedString);
                } catch (Exception e) {
                    log.error("Failed to read image file", e);
                }
            }
            return imageBytes;
        });
    }


    @GetMapping("/list")
    public R listEvidence(@RequestParam("id") Integer id, @RequestParam(value = "userId", required = false) Integer userId, HttpSession session) {
//        log.info("list evidence id " + id.toString() + " userId " + userId.toString());
        LambdaQueryWrapper<Evidence> queryWrapper = new LambdaQueryWrapper<>();
        List<Evidence> evidenceList = evidenceService.list(queryWrapper.eq(Evidence::getVerificationId, id));
        List<CompletableFuture<EvidenceDto>> futures = new ArrayList<>();

        for (Evidence evidence : evidenceList) {
            CompletableFuture<EvidenceDto> future = CompletableFuture.supplyAsync(() -> {
                EvidenceDto evidenceDto = new EvidenceDto(evidence);
                evidenceDto.setUsername(userService.getUserNameById(evidence.getUserId()));
                if(userId != null) evidenceDto.setSupported(supportService.isSupportedByUserId(evidence.getId(), userId));
                evidenceDto.setSupportCount(supportService.countSupportByEvidenceId(evidence.getId()));
                evidenceDto.setAvatar(userService.getAvatarByUserId(evidence.getUserId()));
                return evidenceDto;
            }).thenCombine(findEvidenceImagesAsync(evidence.getId()), (evidenceDto, images) -> {
                evidenceDto.setImages(images);
                return evidenceDto;
            });

            futures.add(future);
        }

        // 等待所有的异步任务完成
        List<EvidenceDto> evidenceDtoList = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return R.success(evidenceDtoList);
    }
    @PostMapping("/upload")
    public R uploadEvidence(@ModelAttribute EvidenceUploadDto evidence, HttpSession session) {
        log.info("upload evidence", evidence);
        List<MultipartFile> images = evidence.getImages();
        try {
            // 創建 Evidence 實體
            Evidence evidenceEntity = new Evidence(evidence);

            // 保存 Evidence 實體，只調用一次 save 方法
            boolean isSaved = evidenceService.save(evidenceEntity);
            if (!isSaved) {
                return R.error("Save failed");
            }

            // 保存圖片證據
            if (images != null) {
                int imageOrder = 1;
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String fileName = UUID.randomUUID() + "_.png";
                        Path filePath = Paths.get(uploadPath, "evidence", fileName);
                        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        log.debug("image saved to id" + evidenceEntity.getId() + " path " + filePath.toString());
                        ImageEvidence img = new ImageEvidence();
                        img.setEvidenceId(evidenceEntity.getId());  // 使用正確的 evidenceId
                        img.setImagePath("evidence/" + fileName);
                        img.setImageOrder(imageOrder++);
                        imageService.save(img);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return R.error("uploadEvidence failed");
        }
        return R.success("uploadEvidence");
    }


    @Resource
    private ISupportService supportService;

    @PostMapping("support")
    public R supportEvidence(@RequestParam("evidenceId") Integer evidenceId, @RequestParam("userId") Integer userId, HttpSession session) {
        log.info("support evidence {}", evidenceId.toString());
        boolean success = supportService.supportEvidence(evidenceId, userId);
        if (success) {
            return R.success("support success");
        }
        return R.error("support failed");
    }

    @GetMapping("/getEvidenceChainInfo")
    public R<EvidenceChainDto> getVerificationChainInfo(@RequestParam(value = "id") Integer id) {
        Evidence evidence = evidenceService.getById(id);
        EvidenceChainDto evidenceChainDto = new EvidenceChainDto();
        evidenceChainDto.setEvidenceId(evidence.getId());
        evidenceChainDto.setTransactionHash(evidence.getTransactionHash());
        evidenceChainDto.setAuthorAddress(userService.getUserAddressByUserId(evidence.getUserId()));
        evidenceChainDto.setTimestamp(evidence.getEvidenceDate().getTime());
        log.debug("evidenceChainDto:" + evidenceChainDto.toString());
        return R.success(evidenceChainDto);
    }
}
