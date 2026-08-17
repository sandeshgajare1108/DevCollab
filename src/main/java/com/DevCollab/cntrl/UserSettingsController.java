package com.DevCollab.cntrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.UserSettingsEntity;
import com.DevCollab.service.UserSettingsService;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class UserSettingsController {

    @Autowired
    private UserSettingsService service;


    // =====================================================
    // GET SETTINGS
    // =====================================================

    @GetMapping("/{userId}")
    public ResponseEntity<UserSettingsEntity> getSettings(
            @PathVariable Long userId) {

        UserSettingsEntity settings =
                service.getSettings(userId);

        return ResponseEntity.ok(settings);
    }


    // =====================================================
    // UPDATE SETTINGS
    // =====================================================

    @PutMapping("/{userId}")
    public ResponseEntity<UserSettingsEntity> updateSettings(
            @PathVariable Long userId,
            @RequestBody UserSettingsEntity request) {

        UserSettingsEntity updated =
                service.updateSettings(
                        userId,
                        request
                );

        return ResponseEntity.ok(updated);
    }


    // =====================================================
    // DELETE SETTINGS
    // =====================================================

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteSettings(
            @PathVariable Long userId) {

        service.deleteSettings(userId);

        return ResponseEntity.ok(
                "Settings deleted successfully"
        );
    }
}