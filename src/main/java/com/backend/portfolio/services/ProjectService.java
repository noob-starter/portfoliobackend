package com.backend.portfolio.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.entities.Project;
import com.backend.portfolio.models.entities.ProjectPoint;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.ProjectRequestInsert;
import com.backend.portfolio.models.responses.ProjectResponse;
import com.backend.portfolio.models.updates.ProjectRequestUpdate;
import com.backend.portfolio.repositories.ProfileRepository;
import com.backend.portfolio.repositories.ProjectRepository;
import com.backend.portfolio.repositories.TechnologyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    /**
     * Get all projects ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        log.info("Fetching all projects");
        List<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc();
        return projects.stream()
                .map(ProjectResponse::fromProject)
                .collect(Collectors.toList());
    }

    /**
     * Get all projects for a specific profile
     */
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjectsByProfileId(Long profileId) {
        log.info("Fetching all projects for profile id: {}", profileId);
        List<Project> projects = projectRepository.findAllByProfileIdOrderByStartDateDesc(profileId);
        return projects.stream()
                .map(ProjectResponse::fromProject)
                .collect(Collectors.toList());
    }

    /**
     * Get a single project by ID
     */
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        log.info("Fetching project with id: {}", id);
        Project project = projectRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return ProjectResponse.fromProject(project);
    }

    /**
     * Create a new project
     */
    @Transactional
    public ProjectResponse createProject(ProjectRequestInsert request) {
        log.info("Creating new project for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Project project = Project.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .url(request.getUrl())
                .banner(request.getBanner())
                .github(request.getGithub())
                .profile(profile)
                .technologies(new HashSet<>())
                .projectPoints(new HashSet<>())
                .build();

        // Add technologies if provided
        if (request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()) {
            Set<Technology> technologies = request.getTechnologyIds().stream()
                    .map(id -> technologyRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id)))
                    .collect(Collectors.toSet());
            project.setTechnologies(technologies);
        }

        Project savedProject = projectRepository.save(project);

        // Add project points if provided
        if (request.getProjectPoints() != null && !request.getProjectPoints().isEmpty()) {
            final Project finalProject = savedProject;
            Set<ProjectPoint> points = request.getProjectPoints().stream()
                    .map(content -> ProjectPoint.builder()
                            .content(content)
                            .project(finalProject)
                            .build())
                    .collect(Collectors.toSet());
            savedProject.getProjectPoints().addAll(points);
            savedProject = projectRepository.save(savedProject);
        }

        log.info("Project created successfully with id: {}", savedProject.getId());

        return ProjectResponse.fromProject(savedProject);
    }

    /**
     * Update an existing project
     */
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequestUpdate request) {
        log.info("Updating project with id: {}", id);

        Project project = projectRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        updateIfNotNull(request.getName(), project::setName);
        updateIfNotNull(request.getStartDate(), project::setStartDate);
        updateIfNotNull(request.getEndDate(), project::setEndDate);
        updateIfNotNull(request.getUrl(), project::setUrl);
        updateIfNotNull(request.getBanner(), project::setBanner);
        updateIfNotNull(request.getGithub(), project::setGithub);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            project.setProfile(profile);
        }

        // Update technologies if provided
        if (request.getTechnologyIds() != null) {
            Set<Technology> technologies = request.getTechnologyIds().stream()
                    .map(techId -> technologyRepository.findById(techId)
                            .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + techId)))
                    .collect(Collectors.toSet());
            project.getTechnologies().clear();
            project.getTechnologies().addAll(technologies);
        }

        Project updatedProject = projectRepository.save(project);
        log.info("Project updated successfully with id: {}", updatedProject.getId());

        return ProjectResponse.fromProject(updatedProject);
    }

    /**
     * Delete a project by ID
     */
    @Transactional
    public void deleteProject(Long id) {
        log.info("Deleting project with id: {}", id);

        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }

        projectRepository.deleteById(id);
        log.info("Project deleted successfully with id: {}", id);
    }

    /**
     * Helper method to update field only if value is not null
     */
    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}

