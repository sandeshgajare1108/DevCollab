
package com.DevCollab.cntrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.TeamMemberEntity;
import com.DevCollab.service.TeamMemberService;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;


    // =================================================
    // GET ALL TEAM MEMBERS
    // =================================================

    @GetMapping
    public ResponseEntity<?> getAllMembers() {

        try {

            List<TeamMemberEntity> members =
                    teamMemberService.getAllMembers();

            return ResponseEntity.ok(members);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    // =================================================
    // GET TEAM BY PROJECT
    // =================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getMembersByProject(
            @PathVariable Long projectId) {

        try {

            List<TeamMemberEntity> members =
                    teamMemberService
                        .getMembersByProject(projectId);

            return ResponseEntity.ok(members);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    // =================================================
    // GET MEMBER BY ID
    // =================================================

    @GetMapping("/{teamMemberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable Long teamMemberId) {

        TeamMemberEntity member =
                teamMemberService
                    .getMemberById(teamMemberId);


        if (member == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Team member not found.");
        }


        return ResponseEntity.ok(member);
    }


    // =================================================
    // ADD MEMBER
    // =================================================

    @PostMapping
    public ResponseEntity<?> addMember(
            @RequestBody TeamMemberEntity member) {

        try {

            TeamMemberEntity saved =
                    teamMemberService
                        .addMember(member);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    // =================================================
    // UPDATE MEMBER
    // =================================================

    @PutMapping("/{teamMemberId}")
    public ResponseEntity<?> updateMember(
            @PathVariable Long teamMemberId,
            @RequestBody TeamMemberEntity member) {

        try {

            TeamMemberEntity updated =
                    teamMemberService
                        .updateMember(
                            teamMemberId,
                            member
                        );

            return ResponseEntity.ok(updated);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    // =================================================
    // DELETE MEMBER
    // =================================================

    @DeleteMapping("/{teamMemberId}")
    public ResponseEntity<?> deleteMember(
            @PathVariable Long teamMemberId) {

        try {

            teamMemberService
                .deleteMember(teamMemberId);

            return ResponseEntity.ok(
                "Team member deleted successfully."
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
