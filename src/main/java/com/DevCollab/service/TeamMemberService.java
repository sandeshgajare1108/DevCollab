
package com.DevCollab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.entity.TeamMemberEntity;
import com.DevCollab.Repository.TeamMemberRepository;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    // ================= GET ALL =================

    public List<TeamMemberEntity> getAllMembers() {

        return teamMemberRepository.findAll();
    }


    // ================= GET BY PROJECT =================

    public List<TeamMemberEntity> getMembersByProject(
            Long projectId) {

        return teamMemberRepository
                .findByProjectId(projectId);
    }


    // ================= GET BY ID =================

    public TeamMemberEntity getMemberById(
            Long teamMemberId) {

        return teamMemberRepository
                .findById(teamMemberId)
                .orElse(null);
    }


    // ================= ADD MEMBER =================

    public TeamMemberEntity addMember(
            TeamMemberEntity member) {

        boolean exists =
                teamMemberRepository
                    .existsByProjectIdAndUserId(
                        member.getProjectId(),
                        member.getUserId()
                    );

        if (exists) {

            throw new RuntimeException(
                "User is already a member of this project."
            );
        }


        if (member.getRole() == null ||
            member.getRole().trim().isEmpty()) {

            member.setRole("MEMBER");
        }


        return teamMemberRepository.save(member);
    }


    // ================= UPDATE MEMBER =================

    public TeamMemberEntity updateMember(
            Long teamMemberId,
            TeamMemberEntity updatedMember) {

        TeamMemberEntity existing =
                teamMemberRepository
                    .findById(teamMemberId)
                    .orElseThrow(
                        () -> new RuntimeException(
                            "Team member not found."
                        )
                    );


        existing.setProjectId(
            updatedMember.getProjectId()
        );

        existing.setUserId(
            updatedMember.getUserId()
        );

        existing.setRole(
            updatedMember.getRole()
        );


        return teamMemberRepository.save(existing);
    }


    // ================= DELETE =================

    public void deleteMember(
            Long teamMemberId) {

        if (!teamMemberRepository
                .existsById(teamMemberId)) {

            throw new RuntimeException(
                "Team member not found."
            );
        }


        teamMemberRepository.deleteById(
            teamMemberId
        );
    }
}
