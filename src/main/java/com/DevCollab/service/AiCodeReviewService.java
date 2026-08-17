
package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.AiCodeReviewRepository;
import com.DevCollab.Repository.GitRepositoryRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.dto.AiCodeReviewRequest;
import com.DevCollab.entity.AiCodeReviewEntity;
import com.DevCollab.entity.GitRepositoryEntity;
import com.DevCollab.entity.NotificationEntity;
import com.DevCollab.entity.PullRequestEntity;

@Service
public class AiCodeReviewService {

    @Autowired
    private AiCodeReviewRepository aiCodeReviewRepository;


    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private GitRepositoryService gitRepositoryService;
    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;


    @Autowired
    private GitHubService gitHubService;


    @Autowired
    private NotificationService notificationService;


    // =====================================================
    // MANUAL CODE ANALYSIS
    // =====================================================

    public AiCodeReviewEntity analyzeCode(
            AiCodeReviewRequest request,
            Long submittedBy) {

        return analyzeCodeInternal(
                request,
                submittedBy,
                submittedBy
        );
    }


    // =====================================================
    // AUTOMATIC GITHUB PR ANALYSIS
    // =====================================================

    public AiCodeReviewEntity analyzePullRequest(
            Long pullRequestId,
            Long requestedBy) {

        // =================================================
        // VALIDATION
        // =================================================

        if (pullRequestId == null ||
            pullRequestId <= 0) {

            throw new RuntimeException(
                    "Pull Request ID is required"
            );
        }


        if (requestedBy == null) {

            throw new RuntimeException(
                    "Logged-in user is required"
            );
        }


        // =================================================
        // GET DEV COLLAB PR
        // =================================================

        Optional<PullRequestEntity> optionalPr =
                pullRequestRepository.findById(
                        pullRequestId
                );


        if (!optionalPr.isPresent()) {

            throw new RuntimeException(
                    "Pull Request not found"
            );
        }


        PullRequestEntity pr =
                optionalPr.get();


        // =================================================
        // VALIDATE PROJECT
        // =================================================

        if (pr.getProjectId() == null) {

            throw new RuntimeException(
                    "Pull Request project is missing"
            );
        }


        // =================================================
        // VALIDATE GITHUB PR NUMBER
        // =================================================

        if (pr.getGithubPrNumber() == null ||
            pr.getGithubPrNumber() <= 0) {

            throw new RuntimeException(
                    "GitHub Pull Request number is missing"
            );
        }


        // =================================================
        // GET CONNECTED REPOSITORY
        // =================================================

        Optional<GitRepositoryEntity>
                optionalRepository =
                gitRepositoryRepository
                        .findByProjectId(
                                pr.getProjectId()
                        );


        if (!optionalRepository.isPresent()) {

            throw new RuntimeException(
                    "No GitHub repository connected "
                    + "to this project"
            );
        }


        GitRepositoryEntity repository =
                optionalRepository.get();


        if (repository.getGithubOwner() == null ||
            repository.getGithubRepo() == null) {

            throw new RuntimeException(
                    "GitHub repository information is incomplete"
            );
        }


        // =================================================
        // FETCH GITHUB DIFF
        // =================================================

        String diff =
                gitHubService.getPullRequestDiff(

                        repository.getGithubOwner(),

                        repository.getGithubRepo(),

                        pr.getGithubPrNumber()
                );


        // =================================================
        // EXTRACT ADDED SOURCE CODE
        // =================================================

        String sourceCode =
                extractChangedSourceCode(
                        diff
                );


        if (sourceCode == null ||
            sourceCode.trim().isEmpty()) {

            /*
             * If no added code can be extracted,
             * analyze the raw diff.
             */

            sourceCode =
                    diff;
        }


        // =================================================
        // LIMIT VERY LARGE INPUT
        // =================================================

        sourceCode =
                limitCodeSize(
                        sourceCode
                );


        // =================================================
        // BUILD REVIEW REQUEST
        // =================================================

        AiCodeReviewRequest request =
                new AiCodeReviewRequest();


        request.setPullRequestId(
                pr.getPullRequestId()
        );


        request.setTaskId(
                pr.getTaskId()
        );


        request.setCode(
                sourceCode
        );


        // =================================================
        // ANALYZE
        // =================================================

        return analyzeCodeInternal(

                request,

                requestedBy,

                pr.getCreatedBy()
        );
    }


    // =====================================================
    // INTERNAL ANALYSIS
    // =====================================================

    private AiCodeReviewEntity analyzeCodeInternal(
            AiCodeReviewRequest request,
            Long submittedBy,
            Long notificationUserId) {

        // =================================================
        // REQUEST VALIDATION
        // =================================================

        if (request == null) {

            throw new RuntimeException(
                    "Review request is required"
            );
        }


        if (submittedBy == null) {

            throw new RuntimeException(
                    "Submitted user is required"
            );
        }


        String code =
                request.getCode();


        if (code == null ||
            code.trim().isEmpty()) {

            throw new RuntimeException(
                    "Source code is required"
            );
        }


        // =================================================
        // PR VALIDATION
        // =================================================

        if (request.getPullRequestId() != null) {

            Optional<PullRequestEntity> pr =
                    pullRequestRepository.findById(
                            request.getPullRequestId()
                    );


            if (!pr.isPresent()) {

                throw new RuntimeException(
                        "Pull Request not found"
                );
            }
        }


        // =================================================
        // FINDINGS
        // =================================================

        List<String> bugs =
                new ArrayList<String>();


        List<String> securityIssues =
                new ArrayList<String>();


        List<String> qualityIssues =
                new ArrayList<String>();


        List<String> suggestions =
                new ArrayList<String>();


        // =================================================
        // SECURITY
        // =================================================

        checkSqlInjection(
                code,
                securityIssues
        );


        checkHardcodedPassword(
                code,
                securityIssues
        );


        checkUnsafeRuntimeExecution(
                code,
                securityIssues
        );


        // =================================================
        // BUGS
        // =================================================

        checkNullRisk(
                code,
                bugs
        );


        checkExceptionHandling(
                code,
                bugs
        );


        // =================================================
        // QUALITY
        // =================================================

        checkSystemOut(
                code,
                qualityIssues
        );


        checkLongMethod(
                code,
                qualityIssues
        );


        checkTodoFixme(
                code,
                qualityIssues
        );


        // =================================================
        // SUGGESTIONS
        // =================================================

        if (!securityIssues.isEmpty()) {

            suggestions.add(
                    "Review all user input validation "
                    + "and use parameterized queries."
            );
        }


        if (!bugs.isEmpty()) {

            suggestions.add(
                    "Improve null handling and "
                    + "exception handling."
            );
        }


        if (!qualityIssues.isEmpty()) {

            suggestions.add(
                    "Improve logging, code structure "
                    + "and method complexity."
            );
        }


        if (suggestions.isEmpty()) {

            suggestions.add(
                    "No major issues detected by "
                    + "the current static analyzer."
            );
        }


        // =================================================
        // COUNTS
        // =================================================

        int bugCount =
                bugs.size();


        int securityCount =
                securityIssues.size();


        int qualityCount =
                qualityIssues.size();


        // =================================================
        // SCORE
        // =================================================

        int score =
                calculateScore(
                        bugCount,
                        securityCount,
                        qualityCount
                );


        // =================================================
        // FINDINGS
        // =================================================

        String findings =
                buildFindings(
                        bugs,
                        securityIssues,
                        qualityIssues
                );


        String suggestionText =
                String.join(
                        "\n",
                        suggestions
                );


        // =================================================
        // ENTITY
        // =================================================

        AiCodeReviewEntity review =
                new AiCodeReviewEntity();


        review.setPullRequestId(
                request.getPullRequestId()
        );


        review.setTaskId(
                request.getTaskId()
        );


        review.setSubmittedBy(
                submittedBy
        );


        review.setScore(
                score
        );


        review.setBugCount(
                bugCount
        );


        review.setSecurityCount(
                securityCount
        );


        review.setQualityCount(
                qualityCount
        );


        review.setFindings(
                findings
        );


        review.setSuggestions(
                suggestionText
        );


        review.setReviewStatus(
                "COMPLETED"
        );


        review.setCreatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        // =================================================
        // SAVE REVIEW
        // =================================================

        AiCodeReviewEntity savedReview =
                aiCodeReviewRepository.save(
                        review
                );


        System.out.println(
                "======================================"
        );

        System.out.println(
                "AI CODE REVIEW COMPLETED"
        );

        System.out.println(
                "REVIEW ID -> "
                + savedReview.getReviewId()
        );

        System.out.println(
                "SUBMITTED BY -> "
                + savedReview.getSubmittedBy()
        );

        System.out.println(
                "PR ID -> "
                + savedReview.getPullRequestId()
        );

        System.out.println(
                "TASK ID -> "
                + savedReview.getTaskId()
        );

        System.out.println(
                "SCORE -> "
                + savedReview.getScore()
        );

        System.out.println(
                "BUGS -> "
                + savedReview.getBugCount()
        );

        System.out.println(
                "SECURITY -> "
                + savedReview.getSecurityCount()
        );

        System.out.println(
                "QUALITY -> "
                + savedReview.getQualityCount()
        );

        System.out.println(
                "======================================"
        );


        // =================================================
        // NOTIFICATION
        // =================================================

        createAiReviewNotification(
                savedReview,
                notificationUserId
        );


        return savedReview;
    }


    // =====================================================
    // EXTRACT CHANGED SOURCE CODE
    // =====================================================

    private String extractChangedSourceCode(
            String diff) {

        if (diff == null ||
            diff.trim().isEmpty()) {

            return "";
        }


        String[] lines =
                diff.split(
                        "\\r?\\n"
                );


        StringBuilder code =
                new StringBuilder();


        boolean javaFile =
                false;


        for (String line : lines) {

            if (line == null) {
                continue;
            }


            // ---------------------------------------------
            // FILE HEADER
            // ---------------------------------------------

            if (line.startsWith(
                    "diff --git "
            )) {

                javaFile = false;
            }


            // ---------------------------------------------
            // JAVA FILE DETECTION
            // ---------------------------------------------

            if (
                line.startsWith(
                    "+++ b/"
                )
                ||
                line.startsWith(
                    "--- a/"
                )
            ) {

                if (line.endsWith(".java")) {

                    javaFile = true;

                } else {

                    javaFile = false;
                }


                continue;
            }


            // ---------------------------------------------
            // SKIP NON-JAVA FILE
            // ---------------------------------------------

            if (!javaFile) {
                continue;
            }


            // ---------------------------------------------
            // SKIP DIFF METADATA
            // ---------------------------------------------

            if (
                line.startsWith(
                    "@@"
                )
                ||
                line.startsWith(
                    "index "
                )
                ||
                line.startsWith(
                    "new file mode"
                )
                ||
                line.startsWith(
                    "deleted file mode"
                )
                ||
                line.startsWith(
                    "\\ No newline"
                )
            ) {

                continue;
            }


            // ---------------------------------------------
            // ONLY ADDED LINES
            // ---------------------------------------------

            if (
                line.startsWith("+")
                &&
                !line.startsWith("+++")
            ) {

                code.append(
                        line.substring(1)
                );

                code.append(
                        "\n"
                );
            }
        }


        return code.toString();
    }


    // =====================================================
    // LIMIT CODE SIZE
    // =====================================================

    private String limitCodeSize(
            String code) {

        if (code == null) {

            return "";
        }


        /*
         * Keep request reasonably small.
         */

        int maxCharacters =
                120000;


        if (code.length() <=
                maxCharacters) {

            return code;
        }


        return code.substring(
                0,
                maxCharacters
        )
        + "\n\n"
        + "// AI REVIEW INPUT TRUNCATED";
    }


    // =====================================================
    // CREATE AI REVIEW NOTIFICATION
    // =====================================================

    private void createAiReviewNotification(
            AiCodeReviewEntity review,
            Long notificationUserId) {

        if (review == null) {

            return;
        }


        Long userId =
                notificationUserId;


        /*
         * Manual review fallback:
         * if no explicit target was provided,
         * notify the submitted user.
         */

        if (userId == null) {

            userId =
                    review.getSubmittedBy();
        }


        if (userId == null) {

            System.out.println(
                    "AI REVIEW NOTIFICATION SKIPPED: "
                    + "recipient user is null"
            );

            return;
        }


        Long prId =
                review.getPullRequestId();


        Integer score =
                review.getScore();


        String prText =
                prId != null
                        ? String.valueOf(prId)
                        : "-";


        int reviewScore =
                score != null
                        ? score
                        : 0;


        String title =
                "AI Code Review Completed";


        String message =
                "AI Code Review completed for "
                + "Pull Request #"
                + prText
                + ". Score: "
                + reviewScore
                + "/100";


        try {

            NotificationEntity notification =
                    notificationService
                            .createNotification(

                                    userId,

                                    title,

                                    message,

                                    "AI_CODE_REVIEW"
                            );


            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "AI REVIEW NOTIFICATION CREATED"
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
                    "AI REVIEW NOTIFICATION ERROR -> "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // SQL INJECTION
    // =====================================================

    private void checkSqlInjection(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        boolean hasSqlKeyword =
                lower.contains("select ")
                ||
                lower.contains("insert ")
                ||
                lower.contains("update ")
                ||
                lower.contains("delete ");


        boolean hasStringConcatenation =
                lower.contains("+ \"")
                ||
                lower.contains("\" +")
                ||
                lower.contains("concat(");


        if (
            hasSqlKeyword &&
            hasStringConcatenation
        ) {

            findings.add(
                    "SQL Injection Risk: "
                    + "SQL query appears to be constructed "
                    + "using string concatenation."
            );
        }
    }


    // =====================================================
    // HARDCODED PASSWORD
    // =====================================================

    private void checkHardcodedPassword(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        if (
            lower.contains(
                "password = \""
            )
            ||
            lower.contains(
                "password=\""
            )
            ||
            lower.contains(
                "password = '"
            )
            ||
            lower.contains(
                "password='"
            )
        ) {

            findings.add(
                    "Security Risk: "
                    + "Possible hardcoded password detected."
            );
        }
    }


    // =====================================================
    // UNSAFE RUNTIME
    // =====================================================

    private void checkUnsafeRuntimeExecution(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        if (
            lower.contains(
                "runtime.getruntime().exec"
            )
            ||
            lower.contains(
                "processbuilder("
            )
        ) {

            findings.add(
                    "Security Risk: "
                    + "Runtime/process execution detected. "
                    + "Validate all external input carefully."
            );
        }
    }


    // =====================================================
    // NULL RISK
    // =====================================================

    private void checkNullRisk(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        if (
            lower.contains(
                ".get()"
            )
            &&
            !lower.contains(
                ".ispresent()"
            )
        ) {

            findings.add(
                    "Potential Bug: "
                    + "Optional.get() may be used "
                    + "without presence validation."
            );
        }
    }


    // =====================================================
    // EXCEPTION HANDLING
    // =====================================================

    private void checkExceptionHandling(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        boolean hasTry =
                lower.contains("try {")
                ||
                lower.contains("try{");


        boolean hasThrow =
                lower.contains("throw ")
                ||
                lower.contains("throws ");


        if (
            (
                lower.contains(
                    "repository"
                )
                ||
                lower.contains(
                    "jdbc"
                )
                ||
                lower.contains(
                    "sql"
                )
            )
            &&
            !hasTry
            &&
            !hasThrow
        ) {

            findings.add(
                    "Potential Bug: "
                    + "Database-related code may lack "
                    + "proper exception handling."
            );
        }
    }


    // =====================================================
    // SYSTEM.OUT
    // =====================================================

    private void checkSystemOut(
            String code,
            List<String> findings) {

        if (
            code.contains(
                "System.out.println"
            )
        ) {

            findings.add(
                    "Code Quality: "
                    + "System.out.println detected. "
                    + "Use structured logging in production."
            );
        }
    }


    // =====================================================
    // LARGE FILE
    // =====================================================

    private void checkLongMethod(
            String code,
            List<String> findings) {

        String[] lines =
                code.split(
                        "\\r?\\n"
                );


        if (lines.length > 150) {

            findings.add(
                    "Code Quality: "
                    + "Large source file detected. "
                    + "Consider splitting responsibilities."
            );
        }
    }


    // =====================================================
    // TODO / FIXME
    // =====================================================

    private void checkTodoFixme(
            String code,
            List<String> findings) {

        String lower =
                code.toLowerCase();


        if (
            lower.contains("todo")
            ||
            lower.contains("fixme")
        ) {

            findings.add(
                    "Code Quality: "
                    + "TODO/FIXME marker detected."
            );
        }
    }


    // =====================================================
    // SCORE
    // =====================================================

    private int calculateScore(
            int bugCount,
            int securityCount,
            int qualityCount) {

        int score = 100;


        score -=
                securityCount * 15;


        score -=
                bugCount * 12;


        score -=
                qualityCount * 5;


        if (score < 0) {
            score = 0;
        }


        if (score > 100) {
            score = 100;
        }


        return score;
    }


    // =====================================================
    // BUILD FINDINGS
    // =====================================================

    private String buildFindings(
            List<String> bugs,
            List<String> securityIssues,
            List<String> qualityIssues) {

        StringBuilder result =
                new StringBuilder();


        if (!securityIssues.isEmpty()) {

            result.append(
                    "SECURITY:\n"
            );


            for (
                String finding :
                securityIssues
            ) {

                result.append(
                        "❌ "
                        + finding
                        + "\n"
                );
            }
        }


        if (!bugs.isEmpty()) {

            result.append(
                    "\nBUGS:\n"
            );


            for (
                String finding :
                bugs
            ) {

                result.append(
                        "⚠️ "
                        + finding
                        + "\n"
                );
            }
        }


        if (!qualityIssues.isEmpty()) {

            result.append(
                    "\nCODE QUALITY:\n"
            );


            for (
                String finding :
                qualityIssues
            ) {

                result.append(
                        "⚠️ "
                        + finding
                        + "\n"
                );
            }
        }


        if (result.length() == 0) {

            result.append(
                    "✅ No major issues detected."
            );
        }


        return result.toString();
    }


    // =====================================================
    // GET REVIEW BY ID
    // =====================================================

    public Optional<AiCodeReviewEntity>
    getReviewById(
            Long reviewId) {

        return aiCodeReviewRepository
                .findById(
                        reviewId
                );
    }


    // =====================================================
    // GET REVIEWS BY PR
    // =====================================================

    public List<AiCodeReviewEntity>
    getReviewsByPullRequest(
            Long pullRequestId) {

        return aiCodeReviewRepository
                .findByPullRequestIdOrderByCreatedAtDesc(
                        pullRequestId
                );
    }


    // =====================================================
    // GET REVIEWS BY TASK
    // =====================================================

    public List<AiCodeReviewEntity>
    getReviewsByTask(
            Long taskId) {

        return aiCodeReviewRepository
                .findByTaskIdOrderByCreatedAtDesc(
                        taskId
                );
    }
    public AiCodeReviewEntity analyzePullRequest(
            Long projectId,
            Long pullRequestNumber,
            Long taskId,
            Long submittedBy) {

        if (projectId == null) {
            throw new RuntimeException(
                    "Project ID is required"
            );
        }

        if (pullRequestNumber == null ||
            pullRequestNumber <= 0) {

            throw new RuntimeException(
                    "Pull Request number is required"
            );
        }

        String diff =
                gitRepositoryService.getPullRequestDiff(
                        projectId,
                        pullRequestNumber
                );

        if (diff == null ||
            diff.trim().isEmpty()) {

            throw new RuntimeException(
                    "No source changes found in Pull Request"
            );
        }

        AiCodeReviewRequest request =
                new AiCodeReviewRequest();

        request.setPullRequestId(
                pullRequestNumber
        );

        request.setTaskId(
                taskId
        );

        request.setCode(
                diff
        );

        return analyzeCode(
                request,
                submittedBy
        );
    }
}
