package com.DevCollab.cntrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;


    @GetMapping
    public Object getDashboard(
            Authentication authentication) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "DASHBOARD CONTROLLER"
        );

        System.out.println(
                "USER -> " +
                authentication.getName()
        );

        System.out.println(
                "AUTHORITIES -> " +
                authentication.getAuthorities()
        );

        System.out.println(
                "======================================"
        );

        return dashboardService.getDashboard();
    }


    @GetMapping(
            "/project/{projectId}/progress"
    )
    public ResponseEntity<?> getProjectProgress(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                dashboardService
                        .getProjectProgress(
                                projectId
                        )
        );
    }
}