package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.ProjectMemberRepository;
import com.DevCollab.Repository.UserRepository;
import com.DevCollab.dto.TeamMemberResponse;
import com.DevCollab.entity.ProjectMemberEntity;
import com.DevCollab.entity.UserEntity;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;


    // =====================================================
    // GET ALL MEMBERS WITH USER DETAILS
    // =====================================================

    public List<TeamMemberResponse> getAllMembersWithUserDetails() {

        List<ProjectMemberEntity> members =
                projectMemberRepository.findAll();

        return convertToTeamResponse(members);
    }


    // =====================================================
    // GET PROJECT MEMBERS WITH USER DETAILS
    // =====================================================

    public List<TeamMemberResponse>
    getProjectMembersWithUserDetails(Long projectId) {

        List<ProjectMemberEntity> members =
                projectMemberRepository
                        .findByProjectId(projectId);

        return convertToTeamResponse(members);
    }


    // =====================================================
    // CONVERT
    // =====================================================

    private List<TeamMemberResponse>
    convertToTeamResponse(
            List<ProjectMemberEntity> members) {

        List<TeamMemberResponse> responseList =
                new ArrayList<>();

        for (ProjectMemberEntity member : members) {

            String userName = "Unknown User";
            String email = "-";

            Optional<UserEntity> optionalUser =
                    userRepository.findById(
                            member.getUserId()
                    );

            if (optionalUser.isPresent()) {

                UserEntity user =
                        optionalUser.get();

                userName =
                        user.getFullName();

                email =
                        user.getEmail();
            }

            TeamMemberResponse response =
                    new TeamMemberResponse(
                            member.getProjectMemberId(),
                            member.getProjectId(),
                            member.getUserId(),
                            userName,
                            email,
                            member.getMemberRole() != null
                                    ? member.getMemberRole().name()
                                    : "-",
                            member.getJoinedAt()
                    );

            responseList.add(response);
        }

        return responseList;
    }


    // =====================================================
    // COUNT
    // =====================================================

    public long countMembersByProject(
            Long projectId) {

        return projectMemberRepository
                .countByProjectId(projectId);
    }


    public long countProjectsOfUser(
            Long userId) {

        return projectMemberRepository
                .countByUserId(userId);
    }


    // =====================================================
    // ROLE COUNTS
    // =====================================================

    public long countDevelopers() {

        return projectMemberRepository
                .countByMemberRole(
                        ProjectMemberEntity.MemberRole.DEVELOPER
                );
    }


    public long countTesters() {

        return projectMemberRepository
                .countByMemberRole(
                        ProjectMemberEntity.MemberRole.TESTER
                );
    }


    public long countDesigners() {

        return projectMemberRepository
                .countByMemberRole(
                        ProjectMemberEntity.MemberRole.DESIGNER
                );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<ProjectMemberEntity> getAllMembers() {

        return projectMemberRepository.findAll();
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public Optional<ProjectMemberEntity>
    getMemberById(Long projectMemberId) {

        return projectMemberRepository
                .findById(projectMemberId);
    }


    // =====================================================
    // GET BY PROJECT
    // =====================================================

    public List<ProjectMemberEntity>
    getMembersByProject(Long projectId) {

        return projectMemberRepository
                .findByProjectId(projectId);
    }


    // =====================================================
    // GET BY USER
    // =====================================================

    public List<ProjectMemberEntity>
    getMembersByUser(Long userId) {

        return projectMemberRepository
                .findByUserId(userId);
    }


    // =====================================================
    // CREATE
    // =====================================================

    public ProjectMemberEntity createMember(
            ProjectMemberEntity member) {

        if (member.getProjectId() == null) {

            throw new RuntimeException(
                    "Project ID is required"
            );
        }

        if (member.getUserId() == null) {

            throw new RuntimeException(
                    "User ID is required"
            );
        }

        if (!userRepository.existsById(
                member.getUserId())) {

            throw new RuntimeException(
                    "User not found with ID: "
                    + member.getUserId()
            );
        }

        boolean exists =
                projectMemberRepository
                .existsByProjectIdAndUserId(
                        member.getProjectId(),
                        member.getUserId()
                );

        if (exists) {

            throw new RuntimeException(
                    "User is already a member of this project"
            );
        }

        if (member.getMemberRole() == null) {

            member.setMemberRole(
                    ProjectMemberEntity.MemberRole.DEVELOPER
            );
        }

        member.setJoinedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );

        return projectMemberRepository.save(member);
    }


    // =====================================================
    // UPDATE
    // =====================================================

    public ProjectMemberEntity updateMember(
            Long projectMemberId,
            ProjectMemberEntity updatedMember) {

        Optional<ProjectMemberEntity> optionalMember =
                projectMemberRepository
                        .findById(projectMemberId);

        if (!optionalMember.isPresent()) {
            return null;
        }

        if (updatedMember.getProjectId() == null) {

            throw new RuntimeException(
                    "Project ID is required"
            );
        }

        if (updatedMember.getUserId() == null) {

            throw new RuntimeException(
                    "User ID is required"
            );
        }

        if (!userRepository.existsById(
                updatedMember.getUserId())) {

            throw new RuntimeException(
                    "User not found with ID: "
                    + updatedMember.getUserId()
            );
        }

        boolean duplicate =
                projectMemberRepository
                .existsByProjectIdAndUserIdAndProjectMemberIdNot(
                        updatedMember.getProjectId(),
                        updatedMember.getUserId(),
                        projectMemberId
                );

        if (duplicate) {

            throw new RuntimeException(
                    "Another member already exists with this User ID in this project"
            );
        }

        ProjectMemberEntity existingMember =
                optionalMember.get();

        existingMember.setProjectId(
                updatedMember.getProjectId()
        );

        existingMember.setUserId(
                updatedMember.getUserId()
        );

        if (updatedMember.getMemberRole() != null) {

            existingMember.setMemberRole(
                    updatedMember.getMemberRole()
            );
        }

        return projectMemberRepository.save(
                existingMember
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    public boolean deleteMember(
            Long projectMemberId) {

        if (!projectMemberRepository
                .existsById(projectMemberId)) {

            return false;
        }

        projectMemberRepository.deleteById(
                projectMemberId
        );

        return true;
    }
}