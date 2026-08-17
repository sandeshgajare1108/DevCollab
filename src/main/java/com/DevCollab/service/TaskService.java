
package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.TaskRepository;
import com.DevCollab.entity.NotificationEntity;
import com.DevCollab.entity.TaskEntity;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;


    // =====================================================
    // GET ALL TASKS
    // =====================================================

    public List<TaskEntity> getAllTasks() {

        return taskRepository.findAll();
    }


    // =====================================================
    // GET TASK BY ID
    // =====================================================

    public Optional<TaskEntity> getTaskById(
            Long taskId) {

        return taskRepository.findById(taskId);
    }


    // =====================================================
    // CREATE TASK
    // =====================================================

    public TaskEntity createTask(
            TaskEntity task) {

        Timestamp now =
                new Timestamp(
                        System.currentTimeMillis()
                );


        // =================================================
        // DEFAULT VALUES
        // =================================================

        if (task.getCreatedAt() == null) {

            task.setCreatedAt(now);
        }


        if (task.getUpdatedAt() == null) {

            task.setUpdatedAt(now);
        }


        if (task.getStatus() == null ||
            task.getStatus().trim().isEmpty()) {

            task.setStatus("TODO");
        }


        if (task.getPriority() == null ||
            task.getPriority().trim().isEmpty()) {

            task.setPriority("MEDIUM");
        }


        // =================================================
        // SAVE TASK
        // =================================================

        TaskEntity savedTask =
                taskRepository.save(task);


        // =================================================
        // TASK ASSIGNED NOTIFICATION
        // =================================================

        if (savedTask.getAssignedTo() != null) {

            createTaskAssignedNotification(
                    savedTask
            );
        }


        return savedTask;
    }


    // =====================================================
    // UPDATE TASK
    // =====================================================

    public TaskEntity updateTask(
            Long taskId,
            TaskEntity updatedTask) {

        Optional<TaskEntity> optionalTask =
                taskRepository.findById(taskId);


        if (!optionalTask.isPresent()) {

            return null;
        }


        TaskEntity existingTask =
                optionalTask.get();


        // =================================================
        // STORE OLD VALUES
        // =================================================

        Long oldAssignedTo =
                existingTask.getAssignedTo();

        String oldStatus =
                existingTask.getStatus();


        // =================================================
        // PROJECT
        // =================================================

        if (updatedTask.getProjectId() != null) {

            existingTask.setProjectId(
                    updatedTask.getProjectId()
            );
        }


        // =================================================
        // TASK NAME
        // =================================================

        if (updatedTask.getTaskName() != null &&
            !updatedTask.getTaskName()
                    .trim()
                    .isEmpty()) {

            existingTask.setTaskName(
                    updatedTask.getTaskName()
            );
        }


        // =================================================
        // DESCRIPTION
        // =================================================

        if (updatedTask.getDescription() != null) {

            existingTask.setDescription(
                    updatedTask.getDescription()
            );
        }


        // =================================================
        // ASSIGNED USER
        // =================================================

        if (updatedTask.getAssignedTo() != null) {

            existingTask.setAssignedTo(
                    updatedTask.getAssignedTo()
            );
        }


        // =================================================
        // CREATED BY NEVER CHANGES
        // =================================================

        // intentionally not updating createdBy


        // =================================================
        // STATUS
        // =================================================

        if (updatedTask.getStatus() != null &&
            !updatedTask.getStatus()
                    .trim()
                    .isEmpty()) {

            existingTask.setStatus(
                    updatedTask
                        .getStatus()
                        .trim()
                        .toUpperCase()
            );
        }


        // =================================================
        // PRIORITY
        // =================================================

        if (updatedTask.getPriority() != null &&
            !updatedTask.getPriority()
                    .trim()
                    .isEmpty()) {

            existingTask.setPriority(
                    updatedTask
                        .getPriority()
                        .trim()
                        .toUpperCase()
            );
        }


        // =================================================
        // START DATE
        // =================================================

        if (updatedTask.getStartDate() != null) {

            existingTask.setStartDate(
                    updatedTask.getStartDate()
            );
        }


        // =================================================
        // DUE DATE
        // =================================================

        if (updatedTask.getDueDate() != null) {

            existingTask.setDueDate(
                    updatedTask.getDueDate()
            );
        }


        // =================================================
        // UPDATED AT
        // =================================================

        existingTask.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        // =================================================
        // SAVE
        // =================================================

        TaskEntity savedTask =
                taskRepository.save(
                        existingTask
                );


        // =================================================
        // ASSIGNMENT CHANGE NOTIFICATION
        // =================================================

        Long newAssignedTo =
                savedTask.getAssignedTo();


        if (newAssignedTo != null &&
            !newAssignedTo.equals(oldAssignedTo)) {

            createTaskAssignedNotification(
                    savedTask
            );
        }


        // =================================================
        // STATUS CHANGE NOTIFICATION
        // =================================================

        if (hasStatusChanged(
                oldStatus,
                savedTask.getStatus()
        )) {

            notifyTaskStatusChanged(
                    savedTask
            );
        }


        return savedTask;
    }


    // =====================================================
    // UPDATE TASK STATUS ONLY
    // =====================================================

    public TaskEntity updateStatus(
            TaskEntity task) {

        if (task == null ||
            task.getTaskId() == null) {

            return null;
        }


        // =================================================
        // GET EXISTING TASK
        // =================================================

        Optional<TaskEntity> optionalTask =
                taskRepository.findById(
                        task.getTaskId()
                );


        if (!optionalTask.isPresent()) {

            return null;
        }


        TaskEntity existingTask =
                optionalTask.get();


        // =================================================
        // OLD STATUS
        // =================================================

        String oldStatus =
                existingTask.getStatus();


        // =================================================
        // NEW STATUS
        // =================================================

        String newStatus =
                task.getStatus();


        if (newStatus == null ||
            newStatus.trim().isEmpty()) {

            return existingTask;
        }


        newStatus =
                newStatus
                        .trim()
                        .toUpperCase();


        // =================================================
        // UPDATE STATUS
        // =================================================

        existingTask.setStatus(
                newStatus
        );


        existingTask.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        // =================================================
        // SAVE TASK
        // =================================================

        TaskEntity updatedTask =
                taskRepository.save(
                        existingTask
                );


        // =================================================
        // STATUS CHANGE DETECTION
        // =================================================

        boolean statusChanged =
                hasStatusChanged(
                        oldStatus,
                        newStatus
                );


        System.out.println(
                "======================================"
        );

        System.out.println(
                "TASK STATUS UPDATE"
        );

        System.out.println(
                "TASK ID        -> "
                + updatedTask.getTaskId()
        );

        System.out.println(
                "OLD STATUS     -> "
                + oldStatus
        );

        System.out.println(
                "NEW STATUS     -> "
                + newStatus
        );

        System.out.println(
                "ASSIGNED TO    -> "
                + updatedTask.getAssignedTo()
        );

        System.out.println(
                "CREATED BY     -> "
                + updatedTask.getCreatedBy()
        );

        System.out.println(
                "STATUS CHANGED -> "
                + statusChanged
        );


        // =================================================
        // CREATE STATUS NOTIFICATION
        // =================================================

        if (statusChanged) {

            System.out.println(
                    "CALLING STATUS NOTIFICATION"
            );


            notifyTaskStatusChanged(
                    updatedTask
            );

        } else {

            System.out.println(
                    "STATUS DID NOT CHANGE"
            );
        }


        System.out.println(
                "======================================"
        );


        return updatedTask;
    }


    // =====================================================
    // CREATE TASK ASSIGNED NOTIFICATION
    // =====================================================

    private void createTaskAssignedNotification(
            TaskEntity task) {

        if (task == null) {

            return;
        }


        Long assignedTo =
                task.getAssignedTo();


        if (assignedTo == null) {

            return;
        }


        try {

            NotificationEntity notification =
                    notificationService.createNotification(

                            assignedTo,

                            "New Task Assigned",

                            "You have been assigned task: "
                            + task.getTaskName(),

                            "TASK_ASSIGNED"
                    );


            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "TASK ASSIGNED NOTIFICATION CREATED"
            );

            System.out.println(
                    "NOTIFICATION ID -> "
                    + notification.getNotificationId()
            );

            System.out.println(
                    "USER ID -> "
                    + notification.getUserId()
            );

            System.out.println(
                    "TYPE -> "
                    + notification.getType()
            );

            System.out.println(
                    "======================================"
            );


        } catch (Exception e) {

            System.out.println(
                    "TASK ASSIGNED NOTIFICATION ERROR -> "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // CREATE TASK STATUS NOTIFICATION
    // =====================================================

    private void notifyTaskStatusChanged(
            TaskEntity task) {

        System.out.println(
                "CALLING notifyTaskStatusChanged()"
        );


        if (task == null) {

            System.out.println(
                    "TASK IS NULL"
            );

            return;
        }


        Long assignedTo =
                task.getAssignedTo();

        Long createdBy =
                task.getCreatedBy();

        String taskName =
                task.getTaskName();

        String status =
                task.getStatus();


        System.out.println(
                "ASSIGNED USER -> "
                + assignedTo
        );

        System.out.println(
                "CREATOR USER -> "
                + createdBy
        );

        System.out.println(
                "TASK NAME -> "
                + taskName
        );

        System.out.println(
                "STATUS -> "
                + status
        );


        // =================================================
        // ASSIGNED USER NOTIFICATION
        // =================================================

        if (assignedTo != null) {

            try {

                NotificationEntity notification =
                        notificationService.createNotification(

                                assignedTo,

                                "Task Status Updated",

                                "Task '"
                                + taskName
                                + "' status changed to "
                                + status,

                                "TASK_STATUS"
                        );


                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "TASK STATUS NOTIFICATION CREATED"
                );

                System.out.println(
                        "NOTIFICATION ID -> "
                        + notification.getNotificationId()
                );

                System.out.println(
                        "USER ID -> "
                        + notification.getUserId()
                );

                System.out.println(
                        "TYPE -> "
                        + notification.getType()
                );

                System.out.println(
                        "MESSAGE -> "
                        + notification.getMessage()
                );

                System.out.println(
                        "======================================"
                );


            } catch (Exception e) {

                System.out.println(
                        "TASK STATUS NOTIFICATION ERROR -> "
                        + e.getMessage()
                );

                e.printStackTrace();
            }
        }


        // =================================================
        // CREATOR NOTIFICATION
        // =================================================

        if (createdBy != null &&
            !createdBy.equals(assignedTo)) {

            try {

                NotificationEntity notification =
                        notificationService.createNotification(

                                createdBy,

                                "Task Status Updated",

                                "Task '"
                                + taskName
                                + "' status changed to "
                                + status,

                                "TASK_STATUS"
                        );


                System.out.println(
                        "CREATOR STATUS NOTIFICATION CREATED -> "
                        + notification.getNotificationId()
                );


            } catch (Exception e) {

                System.out.println(
                        "CREATOR STATUS NOTIFICATION ERROR -> "
                        + e.getMessage()
                );

                e.printStackTrace();
            }
        }
    }


    // =====================================================
    // CHECK STATUS CHANGE
    // =====================================================

    private boolean hasStatusChanged(
            String oldStatus,
            String newStatus) {

        if (oldStatus == null &&
            newStatus == null) {

            return false;
        }


        if (oldStatus == null) {

            return true;
        }


        if (newStatus == null) {

            return true;
        }


        return !oldStatus.equalsIgnoreCase(
                newStatus
        );
    }


    // =====================================================
    // DELETE TASK
    // =====================================================

    public boolean deleteTask(
            Long taskId) {

        if (!taskRepository.existsById(
                taskId
        )) {

            return false;
        }


        taskRepository.deleteById(
                taskId
        );


        return true;
    }


    // =====================================================
    // GET TASKS BY PROJECT
    // =====================================================

    public List<TaskEntity> getTasksByProject(
            Long projectId) {

        return taskRepository.findByProjectId(
                projectId
        );
    }
}
