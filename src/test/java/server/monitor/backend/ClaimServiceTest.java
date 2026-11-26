package server.monitor.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import server.monitor.backend.client.PurchaseController;
import server.monitor.backend.db.privileges.Privilege;
import server.monitor.backend.db.productItems.ProductItem;
import server.monitor.backend.db.productPrivilege.ProductPrivilege;
import server.monitor.backend.services.ClaimRequest;
import server.monitor.backend.services.ClaimResponse;
import server.monitor.backend.db.items.Item;
import server.monitor.backend.db.products.Product;
import server.monitor.backend.db.purchases.Purchase;
import server.monitor.backend.db.purchases.PurchaseRepository;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;
import server.monitor.backend.services.ClaimService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class ClaimServiceTest {

    private UserRepository userRepository;
    private PurchaseRepository purchaseRepository;
    private ClaimService claimService;
    private final User user = new User(1L,UUID.randomUUID(),0L,"Riven","player@email.com","1111");
    private final Item item = new Item(1L,"DIAMOND_SWORD","sword","diamond sword");
    private final Privilege privilege = new Privilege(1L,"Premium","premium role", Privilege.PrivilegeType.PREMIUM);
    private final ProductPrivilege productPrivilege = new ProductPrivilege(Duration.ofDays(30),privilege,null,new ProductItem.Key(1L,1L));
    private final ProductItem productItem = new ProductItem(new ProductItem.Key(1L,1L),null,item,1);
    private final Product product = new Product(1L,user,List.of(productItem),100L,List.of(productPrivilege));

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        purchaseRepository = Mockito.mock(PurchaseRepository.class);
        claimService = new ClaimService(userRepository, purchaseRepository, 5L);
        productPrivilege.setProduct(product);
        productItem.setProduct(product);
    }

    @Test
    void playerNotLinked() {
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.empty());
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> claimService.claim(claimRequest)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Player is not linked", ex.getReason());
    }
    @Test
    void purchaseNotFound() {
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.empty());
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> claimService.claim(claimRequest)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Purchase not found", ex.getReason());
    }
    @Test
    void purchaseAlreadyClosed() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLOSED, Instant.now(),Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.of(purchase));
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> claimService.claim(claimRequest)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Purchase already claimed or delivered", ex.getReason());
    }
    @Test
    void purchaseAlreadyClaimed() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLAIMED, Instant.now(),Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.of(purchase));
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> claimService.claim(claimRequest)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Delivery in progress, try again later", ex.getReason());
    }
    @Test
    void claimAfterFailedClaimSuccess() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLAIMED, Instant.now().minus(5, ChronoUnit.MINUTES),Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.of(purchase));
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");


        ClaimResponse claimResponse = claimService.claim(claimRequest) ;

        assertEquals(List.of(new ClaimService.ItemToSend(item.getMaterial(),productItem.getAmount())),claimResponse.items() );
        assertEquals(1L,claimResponse.id() );

    }
    @Test
    void claimWithoutClaimedTimeSuccess() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLAIMED, null,Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.of(purchase));
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");


        ClaimResponse claimResponse = claimService.claim(claimRequest) ;

        assertEquals(List.of(new ClaimService.ItemToSend(item.getMaterial(),productItem.getAmount())),claimResponse.items() );
        assertEquals(1L,claimResponse.id() );

    }
    @Test
    void claimSuccess() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.OPEN, null,Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(userRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUserIdAndCode(1L,"code")).thenReturn(Optional.of(purchase));
        ClaimRequest claimRequest = new ClaimRequest(user.getPlayerId(),"code");


        ClaimResponse claimResponse = claimService.claim(claimRequest) ;

        assertEquals(List.of(new ClaimService.ItemToSend(item.getMaterial(),productItem.getAmount())),claimResponse.items() );
        assertEquals(1L,claimResponse.id() );

    }
    @Test
    void closePurchaseNotFound() {
        when(purchaseRepository.findById(1L)).thenReturn(Optional.empty());
        PurchaseController.ClosePurchase closePurchase = new PurchaseController.ClosePurchase(PurchaseController.ClosePurchase.CloseStatus.SUCCESS,1L);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> claimService.close(closePurchase)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Purchase not found", ex.getReason());
    }
    @Test
    void closePurchaseStatusFailed() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLAIMED, null,Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
        PurchaseController.ClosePurchase closePurchase = new PurchaseController.ClosePurchase(PurchaseController.ClosePurchase.CloseStatus.FAILURE,1L);
        claimService.close(closePurchase);
        assertEquals(Purchase.PurchaseStatus.OPEN,purchase.getStatus());
    }
    @Test
    void closePurchaseStatusSuccess() {
        Purchase purchase = new Purchase(1L,user,product, Purchase.PurchaseStatus.CLAIMED, null,Instant.now().minus(10, ChronoUnit.MINUTES),null,"code");
        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
        PurchaseController.ClosePurchase closePurchase = new PurchaseController.ClosePurchase(PurchaseController.ClosePurchase.CloseStatus.SUCCESS,1L);
        claimService.close(closePurchase);
        assertEquals(Purchase.PurchaseStatus.CLOSED,purchase.getStatus());
    }
}