package server.monitor.backend.client;

import org.springframework.web.bind.annotation.*;
import server.monitor.backend.Printer;
import server.monitor.backend.db.claims.ClaimRequest;
import server.monitor.backend.db.claims.ClaimResponse;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.services.ClaimService;
import server.monitor.backend.task.TaskManager;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseClient {
    public Printer printer;
    public TaskManager taskManager;
    private final UserRepository userRepository;
    private final ClaimService claimService;
    public PurchaseClient(Printer printer, TaskManager taskManager, UserRepository userRepository, ClaimService claimService) {
        this.taskManager = taskManager;
        this.printer = printer;
        this.userRepository = userRepository;
        this.claimService = claimService;
    }

    @GetMapping("/get")
    public ClaimResponse getPurchase(@RequestBody ClaimRequest body) {
        System.out.println("GET PURCHASE RECEIVED, body: " + body.toString());
        return claimService.claim(body);
    }
    @PostMapping("/close")
    public void closePurchase(@RequestBody Long body) {
        System.out.println("CLAIM CLOSE RECEIVED, body: " + body.toString());
        claimService.close(body);
    }

}
