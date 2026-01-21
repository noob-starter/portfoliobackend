package com.backend.portfolio.services;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Project;
import com.backend.portfolio.models.entities.ProjectPoint;
import com.backend.portfolio.models.requests.ProjectPointRequestInsert;
import com.backend.portfolio.models.responses.ProjectPointResponse;
import com.backend.portfolio.models.updates.ProjectPointRequestUpdate;
import com.backend.portfolio.repositories.ProjectPointRepository;
import com.backend.portfolio.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectPointService {

    private final ProjectPointRepository projectPointRepository;
    private final ProjectRepository projectRepository;

    /**
     * Get all project points for a specific project
     */
    @Transactional(readOnly = true)
    public List<ProjectPointResponse> getAllProjectPointsByProjectId(Long projectId) {
        log.info("Fetching all project points for project id: {}", projectId);
        return projectPointRepository.findAllByProjectIdOrderByCreatedAtAsc(projectId)
                .stream()
                .map(ProjectPointResponse::fromProjectPoint)
                .collect(Collectors.toList());
    }

    /**
     * Get a single project point by ID
     */
    @Transactional(readOnly = true)
    public ProjectPointResponse getProjectPointById(Long id) {
        log.info("Fetching project point with id: {}", id);
        ProjectPoint projectPoint = projectPointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project point not found with id: " + id));
        return ProjectPointResponse.fromProjectPoint(projectPoint);
    }

    /**
     * Create a new project point
     */
    @Transactional
    public ProjectPointResponse createProjectPoint(ProjectPointRequestInsert request) {
        log.info("Creating new project point for project id: {}", request.getProjectId());

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        ProjectPoint projectPoint = ProjectPoint.builder()
                .content(request.getContent())
                .project(project)
                .build();

        ProjectPoint savedProjectPoint = projectPointRepository.save(projectPoint);
        log.info("Project point created successfully with id: {}", savedProjectPoint.getId());

        return ProjectPointResponse.fromProjectPoint(savedProjectPoint);
    }

    /**
     * Update an existing project point
     */
    @Transactional
    public ProjectPointResponse updateProjectPoint(Long id, ProjectPointRequestUpdate request) {
        log.info("Updating project point with id: {}", id);

        ProjectPoint projectPoint = projectPointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project point not found with id: " + id));

        updateIfNotNull(request.getContent(), projectPoint::setContent);

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
            projectPoint.setProject(project);
        }

        ProjectPoint updatedProjectPoint = projectPointRepository.save(projectPoint);
        log.info("Project point updated successfully with id: {}", updatedProjectPoint.getId());

        return ProjectPointResponse.fromProjectPoint(updatedProjectPoint);
    }

    /**
     * Delete a project point by ID
     */
    @Transactional
    public void deleteProjectPoint(Long id) {
        log.info("Deleting project point with id: {}", id);

        if (!projectPointRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project point not found with id: " + id);
        }

        projectPointRepository.deleteById(id);
        log.info("Project point deleted successfully with id: {}", id);
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

