package com.example.devconnect.controller;

import com.example.devconnect.model.Image;
import com.example.devconnect.model.Project;
import com.example.devconnect.service.ImageService;
import com.example.devconnect.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class ImageController {
    private final ImageService imageService;
    private final ProjectService projectService;

    public ImageController(ImageService imageService, ProjectService projectService) {
        this.imageService = imageService;
        this.projectService = projectService;
    }

    @GetMapping("/project/{projectId}/images/create")
    public String showAddImageForm(@PathVariable("projectId") Integer projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("image", new Image());
        return "image/add";
    }

    @PostMapping("/project/{projectId}/images/create")
    public String addImage(@PathVariable("projectId") Integer projectId, @ModelAttribute Image image, @RequestParam("file") MultipartFile file, Model model) {

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            model.addAttribute("error", "Soubor musí být obrázek");
            return "image/add";
        }
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = originalFileName;

        String uploadDir = "src/main/resources/media/";
        Path path = Paths.get(uploadDir + fileName);

        if (Files.exists(path)) {
            String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            fileName = nameWithoutExtension + "_" + System.currentTimeMillis() + extension;
            path = Paths.get(fileName);
        }

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            model.addAttribute("error", "Chyba při ukládání obrázku");
            return "image/add";
        }

        image.setFilePath(fileName);

        Project project = projectService.getProjectById(projectId);
        image.setProject(project);
        imageService.save(image);

        return "redirect:/project/" + projectId;
    }

    @GetMapping("/project/{projectId}/images/delete/{imageId}")
    public String deleteImage(@PathVariable("projectId") Integer projectId, @PathVariable("imageId") Integer imageId) {
        imageService.delete(imageId);
        return "redirect:/project/" + projectId;
    }

}
