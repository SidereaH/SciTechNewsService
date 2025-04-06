package scitech.newsservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scitech.newsservice.models.Status;
import scitech.newsservice.models.dto.DefaultResponse;
import scitech.newsservice.repositories.StatusRepo;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final StatusRepo statusRepo;

    public StatusController(StatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    @GetMapping("/{name}")
    public ResponseEntity<DefaultResponse<Status, String>> getStatusByName(@PathVariable String status) {
        Status curStat;
        try{
          curStat =statusRepo.findByName(status.toUpperCase()).orElseThrow(() -> new RuntimeException("Status not found"));
       }
       catch (RuntimeException e){

           return ResponseEntity.badRequest().body(new DefaultResponse<>(null, "Invalid status"));
       }
        return ResponseEntity.ok(new DefaultResponse<>(curStat, "success"));
    }
    @GetMapping
    public ResponseEntity<List<Status>> getStatuses() {
        return ResponseEntity.ok(statusRepo.findAll());
    }
    @PostMapping
    public ResponseEntity<DefaultResponse<Status, String>> addStatus(@RequestBody Status status) {
        status.setName(status.getName().toUpperCase());
        Status newStatus = statusRepo.save(status);
        return ResponseEntity.ok(new DefaultResponse<>(newStatus, "success"));
    }
    @DeleteMapping
    public ResponseEntity<DefaultResponse<Status, String>> deleteStatus(@RequestBody Status status) {
        statusRepo.delete(status);
        return ResponseEntity.ok(new DefaultResponse<>(status, "success"));
    }

}
