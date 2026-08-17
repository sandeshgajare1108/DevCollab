
package com.DevCollab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.ProjectRepository;
import com.DevCollab.entity.ProjectEntity;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repository;


    // =====================================================
    // CREATE PROJECT
    // =====================================================

    public ProjectEntity createProject(
            ProjectEntity project) {

        System.out.println(
                "========== CREATE PROJECT =========="
        );

        System.out.println(
                "Project Name : " +
                project.getProjectName()
        );

        System.out.println(
                "Owner ID     : " +
                project.getOwnerId()
        );

        System.out.println(
                "Status       : " +
                project.getStatus()
        );


        ProjectEntity saved =
                repository.save(project);


        System.out.println(
                "SAVED PROJECT ID : " +
                saved.getProjectId()
        );


        return saved;
    }


    // =====================================================
    // GET ALL PROJECTS
    // =====================================================

    public List<ProjectEntity> getAllProjects() {

        return repository.findAll();
    }


    // =====================================================
    // GET PROJECT BY ID
    // =====================================================

    public Optional<ProjectEntity> getProjectById(
            Long projectId) {

        return repository.findById(projectId);
    }


    // =====================================================
    // UPDATE PROJECT
    // =====================================================

    public ProjectEntity updateProject(
            Long projectId,
            ProjectEntity updatedProject) {

        Optional<ProjectEntity> optionalProject =
                repository.findById(projectId);


        if (!optionalProject.isPresent()) {

            return null;
        }


        ProjectEntity existingProject =
                optionalProject.get();


        // ---------------------------------------------
        // PROJECT NAME
        // ---------------------------------------------

        if (updatedProject.getProjectName() != null &&
            !updatedProject.getProjectName()
                    .trim()
                    .isEmpty()) {

            existingProject.setProjectName(
                    updatedProject.getProjectName()
            );
        }


        // ---------------------------------------------
        // DESCRIPTION
        // ---------------------------------------------

        existingProject.setDescription(
                updatedProject.getDescription()
        );


        // ---------------------------------------------
        // OWNER
        // ---------------------------------------------

        if (updatedProject.getOwnerId() != null) {

            existingProject.setOwnerId(
                    updatedProject.getOwnerId()
            );
        }


        // ---------------------------------------------
        // STATUS
        // ---------------------------------------------

        if (updatedProject.getStatus() != null &&
            !updatedProject.getStatus()
                    .trim()
                    .isEmpty()) {

            existingProject.setStatus(
                    updatedProject.getStatus()
            );
        }


        // ---------------------------------------------
        // VISIBILITY
        // ---------------------------------------------

        if (updatedProject.getVisibility() != null &&
            !updatedProject.getVisibility()
                    .trim()
                    .isEmpty()) {

            existingProject.setVisibility(
                    updatedProject.getVisibility()
            );
        }


        // ---------------------------------------------
        // START DATE
        // ---------------------------------------------

        existingProject.setStartDate(
                updatedProject.getStartDate()
        );


        // ---------------------------------------------
        // END DATE
        // ---------------------------------------------

        existingProject.setEndDate(
                updatedProject.getEndDate()
        );


        return repository.save(existingProject);
    }


    // =====================================================
    // DELETE PROJECT
    // =====================================================

    public boolean deleteProjectById(
            Long projectId) {

        if (!repository.existsById(projectId)) {

            return false;
        }


        repository.deleteById(projectId);

        return true;
    }


    // =====================================================
    // OLD DELETE METHOD
    // =====================================================

    public String deleteProject(
            Long projectId) {

        boolean deleted =
                deleteProjectById(projectId);


        if (deleted) {

            return "Project deleted successfully";
        }


        return "Project not found";
    }


    // =====================================================
    // GET PROJECTS BY OWNER
    // =====================================================

    public List<ProjectEntity> getProjectsByOwner(
            Long ownerId) {

        return repository.findByOwnerId(ownerId);
    }


    // =====================================================
    // GET PROJECTS BY STATUS
    // =====================================================

    public List<ProjectEntity> getProjectsByStatus(
            String status) {

        return repository.findByStatus(status);
    }


    // =====================================================
    // GET PROJECTS BY VISIBILITY
    // =====================================================

    public List<ProjectEntity> getProjectsByVisibility(
            String visibility) {

        return repository.findByVisibility(visibility);
    }


    // =====================================================
    // GET OWNER PROJECTS BY STATUS
    // =====================================================

    public List<ProjectEntity> getOwnerProjectsByStatus(
            Long ownerId,
            String status) {

        return repository.findByOwnerIdAndStatus(
                ownerId,
                status
        );
    }


    // =====================================================
    // SEARCH PROJECT
    // =====================================================

    public List<ProjectEntity> searchProjects(
            String projectName) {

        return repository.findByProjectNameContaining(
                projectName
        );
    }


    // =====================================================
    // COUNT PROJECTS BY STATUS
    // =====================================================

    public long countProjectsByStatus(
            String status) {

        return repository.countByStatus(status);
    }


    // =====================================================
    // COUNT PROJECTS BY OWNER
    // =====================================================

    public long countProjectsByOwner(
            Long ownerId) {

        return repository.countByOwnerId(ownerId);
    }
}
