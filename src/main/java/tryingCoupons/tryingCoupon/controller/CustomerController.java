package tryingCoupons.tryingCoupon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@CrossOrigin
/**
 * the customer controller.
 */
public class CustomerController {

    private final CustomerServiceMPL customerServiceMPL;

//    @PostMapping("/customerLogin")
//    @ResponseStatus(HttpStatus.OK)
//    public void customerLogin(@RequestParam String email, @RequestParam String password) throws LoginException {
//        if(customerServiceMPL.login(email,password)){
//            System.out.println("ok");
//        }
//    }


    /**
     * Customer buying coupon.
     * @param id -The coupon id.
     * @throws CouponOutOfAmountException - Will be thrown if coupon is out of amount.
     * @throws CouponException - will be thrown if coupon does not exist.
     * @throws CouponExpiredException - will be thrown if coupon is expired
     * @throws CustomerCouponException - Will be thrown if customer already own this coupon.
     */
    @PutMapping("/purchasecoupon/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void purchaseCoupon(@PathVariable int id) throws CouponOutOfAmountException, CouponException, CouponExpiredException, CustomerCouponException {
        customerServiceMPL.purchaseCoupons(id);
    }

    /**
     * Getting all customer coupons
     * @return - List of coupon object that belongs to the current customer.
     * @throws CouponException - Will be thrown if there are no coupons.
     */
    @GetMapping("/AllCustomerCoupon")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> AllCustomerCoupons() throws CouponException {
        return customerServiceMPL.getAllCustomerCoupon();
    }
    /**
     * Getting Company coupons By category
     * @param categoryId -Getting the category id
     * @return List of coupons with the exact category that the user chose.
     * @throws CouponException - Will be thrown if there are no such coupons.
     */
    @GetMapping("/couponsCategory/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> AllCustomerCouponsByCategory(@PathVariable int categoryId) throws CouponException {
        return customerServiceMPL.getCustomerCouponByCategory(categoryId);
    }


    /**
     * Getting coupons by max price.
     * @param maxPrice - The max price that we choose.
     * @return - List of coupons with that max priced.
     * @throws CouponException -  Will be thrown if there are no coupons with this max price.
     */
    @GetMapping("/maxPrice/{maxPrice}")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> couponsByMaxPrice(@PathVariable int maxPrice) throws CouponException {
        return customerServiceMPL.getCustomerCouponByMaxPriced(maxPrice);
    }

    /**
     *
     * @return - Customer object initialized with current customer values.
     * @throws CustomerException - Will be thrown if there are problem with current user.
     */
    @GetMapping("/customerDetails")
    @ResponseStatus(HttpStatus.OK)
    public Customer userDetails() throws CustomerException {
        return customerServiceMPL.getCustomerDetails();
    }

}
