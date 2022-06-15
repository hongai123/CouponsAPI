package tryingCoupons.tryingCoupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.services.AdminService;

import java.util.List;

@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
/**
 * Controller for guests.
 */
public class GuestController {

    private final AdminService adminService;
    private final CouponRepo couponRepo;

    /**
     * Getting - All coupons that are available.
     * @return - List of all available coupons.
     */
    @GetMapping("/allAvailableCoupons")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getAllCoupons(){
        return couponRepo.findAll();
    }
}
