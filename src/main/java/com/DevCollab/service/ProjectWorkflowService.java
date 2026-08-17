package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.BugRepository;
import com.DevCollab.Repository.ProjectRepository;
import com.DevCollab.Repository.TaskRepository;
import com.DevCollab.entity.BugEntity;
import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.TaskEntity;

@Service
public class ProjectWorkflowService {

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;


    // =====================================================
    // HANDLE BUG STATUS CHANGE
    // =====================================================

    public void handleBugStatusChange(
            BugEntity bug) {

        if (bug == null) {
            return;
        }

        Long taskId =
                bug.getTaskId();

        Long projectId =
                bug.getProjectId();


        if (taskId != null) {

            updateTaskBasedOnBug(
                    taskId
            );
        }


        if (projectId != null) {

            updateProjectBasedOnTasks(
                    projectId
            );
        }
    }


    // =====================================================
    // UPDATE TASK BASED ON BUGS
    // =====================================================

    private void updateTaskBasedOnBug(
            Long taskId) {

        Optional<TaskEntity> optionalTask =
                taskRepository.findById(
                        taskId
                );


        if (!optionalTask.isPresent()) {
            return;
        }


        TaskEntity task =
                optionalTask.get();


        /*
         * Count all bugs linked with this task.
         */

        List<BugEntity> bugs =
                bugRepository
                    .findByTaskIdOrderByCreatedAtDesc(
                            taskId
                    );


        /*
         * No bugs linked with task.
         * Don't automatically modify it.
         */

        if (bugs == null ||
            bugs.isEmpty()) {

            return;
        }


        boolean allClosed = true;

        boolean hasActiveBug = false;


        for (BugEntity bug : bugs) {

            String status =
                    bug.getStatus();


            if (
                !"CLOSED".equalsIgnoreCase(
                    status
                )
            ) {

                allClosed = false;
            }


            if (
                "OPEN".equalsIgnoreCase(status) ||
                "ASSIGNED".equalsIgnoreCase(status) ||
                "IN_PROGRESS".equalsIgnoreCase(status) ||
                "REOPENED".equalsIgnoreCase(status) ||
                "RETESTING".equalsIgnoreCase(status)
            ) {

                hasActiveBug = true;
            }
        }


        /*
         * All linked bugs closed
         */

        if (allClosed) {

            task.setStatus(
                    "COMPLETED"
            );


            task.setUpdatedAt(
                    new Timestamp(
                        System.currentTimeMillis()
                    )
            );


            taskRepository.save(
                    task
            );


            return;
        }


        /*
         * Active bug exists
         */

        if (hasActiveBug) {

            /*
             * Don't overwrite CODE_REVIEW etc.
             * Only move terminal COMPLETED task
             * back into IN_PROGRESS.
             */

            if (
                "COMPLETED".equalsIgnoreCase(
                    task.getStatus()
                )
            ) {

                task.setStatus(
                        "IN_PROGRESS"
                );


                task.setUpdatedAt(
                        new Timestamp(
                            System.currentTimeMillis()
                        )
                );


                taskRepository.save(
                        task
                );
            }
        }
    }


    // =====================================================
    // UPDATE PROJECT BASED ON TASKS
    // =====================================================

    public void updateProjectBasedOnTasks(
            Long projectId) {

        Optional<ProjectEntity> optionalProject =
                projectRepository.findById(
                        projectId
                );


        if (!optionalProject.isPresent()) {
            return;
        }


        ProjectEntity project =
                optionalProject.get();


        List<TaskEntity> tasks =
                taskRepository.findByProjectId(
                        projectId
                );


        /*
         * No tasks:
         *
         * Don't mark project completed.
         */

        if (tasks == null ||
            tasks.isEmpty()) {

            return;
        }


        int totalTasks =
                tasks.size();


        int completedTasks = 0;

        int activeTasks = 0;


        for (TaskEntity task : tasks) {

            String status =
                    task.getStatus();


            if (
                "COMPLETED".equalsIgnoreCase(
                    status
                )
            ) {

                completedTasks++;

            } else {

                activeTasks++;
            }
        }


        /*
         * ALL TASKS COMPLETED
         */

        if (
            completedTasks == totalTasks
        ) {

            project.setStatus(
                    "COMPLETED"
            );


            project.setUpdatedAt(
                    new Timestamp(
                        System.currentTimeMillis()
                    )
            );


            projectRepository.save(
                    project
            );


            return;
        }


        /*
         * Some tasks completed
         *
         * Move project to IN_PROGRESS
         */

        if (completedTasks > 0 &&
            activeTasks > 0) {

            if (
                !"IN_PROGRESS".equalsIgnoreCase(
                    project.getStatus()
                )
            ) {

                project.setStatus(
                        "IN_PROGRESS"
                );


                project.setUpdatedAt(
                        new Timestamp(
                            System.currentTimeMillis()
                        )
                );


                projectRepository.save(
                        project
                );
            }
        }
    }


    // =====================================================
    // MANUAL PROJECT RECALCULATION
    // =====================================================

    public String recalculateProject(
            Long projectId) {

        updateProjectBasedOnTasks(
                projectId
        );


        Optional<ProjectEntity> project =
                projectRepository.findById(
                        projectId
                );


        if (!project.isPresent()) {

            return "Project not found";
        }


        return project.get().getStatus();
    }
}