package server.monitor.backend.client;

import org.springframework.web.bind.annotation.*;
import server.monitor.backend.Printer;
import server.monitor.backend.services.ClaimRequest;
import server.monitor.backend.services.ClaimResponse;
import server.monitor.backend.services.ClaimService;
import server.monitor.backend.task.TaskManager;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    public Printer printer;
    public TaskManager taskManager;
    private final ClaimService claimService;
    public PurchaseController(Printer printer, TaskManager taskManager, ClaimService claimService) {
        this.taskManager = taskManager;
        this.printer = printer;
        this.claimService = claimService;
    }

    @GetMapping("/get")
    public ClaimResponse getPurchase(@RequestParam UUID id, @RequestParam String code) {
        System.out.println("GET PURCHASE RECEIVED, id: " + id + ", code: " + code);
        return claimService.claim(new ClaimRequest(id, code));
    }
    @PostMapping("/close")
    public void closePurchase(@RequestBody ClosePurchase body) {
        System.out.println("CLAIM CLOSE RECEIVED, body: " + body.toString());
        claimService.close(body);
    }
    public record ClosePurchase(CloseStatus status, Long id){
        public enum CloseStatus {
            SUCCESS,
            FAILURE
        }
    }

}
