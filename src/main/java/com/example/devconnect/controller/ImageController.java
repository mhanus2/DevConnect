package com.example.devconnect.controller;

import com.example.devconnect.model.Image;
import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.ImageService;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.UserAccountDetailsService;
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
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ImageController {
    private final ImageService imageService;
    private final ProjectService projectService;
    private final UserAccountDetailsService userAccountDetailsService;

    public ImageController(ImageService imageService, ProjectService projectService, UserAccountDetailsService userAccountDetailsService) {
        this.imageService = imageService;
        this.projectService = projectService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("/projects/{projectId}/images/create")
    public String showAddImageForm(@PathVariable("projectId") Integer projectId, Model model, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                Project project = projectService.getProjectById(projectId);
                if (project.getOwner() == loggedUser.get() || loggedUser.get().isAdmin()) {
                    model.addAttribute("projectId", projectId);
                    model.addAttribute("image", new Image());
                    model.addAttribute("userId", loggedUser.get().getId());
                    model.addAttribute("isAdmin", loggedUser.get().isAdmin());
                    return "image/add";
                }
            }
        }
        return "error";
    }

    @PostMapping("/projects/{projectId}/images/create")
    public String addImage(@PathVariable("projectId") Integer projectId, @ModelAttribute Image image, @RequestParam("file") MultipartFile file, Model model, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                Project project = projectService.getProjectById(projectId);
                if (project.getOwner() == loggedUser.get() || loggedUser.get().isAdmin()) {
                    if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                        model.addAttribute("error", "Soubor musí být obrázek");
                        return "image/add";
                    }
                    String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    String fileName = originalFileName;

                    String uploadDir = "C:/Users/marti/IdeaProjects/media/";
                    Path path = Paths.get(uploadDir + fileName);

                    if (Files.exists(path)) {
                        String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                        fileName = nameWithoutExtension + "_" + System.currentTimeMillis() + extension;
                        path = Paths.get(uploadDir + fileName);
                    }

                    try {
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        model.addAttribute("error", "Chyba při ukládání obrázku");
                        return "image/add";
                    }

                    image.setFilePath(fileName);
                    image.setProject(project);
                    imageService.save(image);

                    return "redirect:/projects/" + projectId;
                }
            }
        }
        return "error";
    }

    @GetMapping("/projects/{projectId}/images/delete/{imageId}")
    public String deleteImage(@PathVariable("projectId") Integer projectId, @PathVariable("imageId") Integer imageId, Principal principal) {
        if (principal != null) {
            Project project = projectService.getProjectById(projectId);
            UserAccount owner = project.getOwner();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                imageService.delete(imageId);
                return "redirect:/projects/" + projectId;
            }
        }
        return "error";
    }

}
