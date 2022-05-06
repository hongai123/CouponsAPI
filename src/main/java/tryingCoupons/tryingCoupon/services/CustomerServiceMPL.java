package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Getter
public class CustomerServiceMPL extends ClientService implements CustomerService{
    private int customerId;
    private Customer thisCustomer;
    private boolean isLogged = false;
    private String token;

    public CustomerServiceMPL(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    /**
     * Log in manager
     * @param email - String with value of email/username
     * @param password - string with value of password
     * @return - true if login successes
     */
    @Override
    public boolean login(String email, String password) {
        if(!CUSTOMER_REPO.isCustomerExists(email,password)){
            customerId = -1;
            thisCustomer = null;
            return false;
        }else {
            isLogged = true;
            thisCustomer = CUSTOMER_REPO.findByEmailLike(email);
            customerId = thisCustomer.getId();
            System.out.println("customer successfully logged!");
            return true;
        }
    }

    /**
     * Customer buying coupon.
     * @param couponId -The coupon id.
     * @throws CouponOutOfAmountException - Will be thrown if coupon is out of amount.
     * @throws CouponException - will be thrown if coupon does not exist.
     * @throws CouponExpiredException - will be thrown if coupon is expired
     * @throws CustomerCouponException - Will be thrown if customer already own this coupon.
     */
    @Override
    public void purchaseCoupons(int couponId) throws CustomerCouponException, CouponException, CouponOutOfAmountException, CouponExpiredException {
        Coupon couponToPurchase;
        if(COUPON_REPO.existsById(couponId)){
            couponToPurchase = COUPON_REPO.findById(couponId).get();
        }else{
            throw new CouponException("coupon does now exists!");
        }
        //does customer already own this coupon?
    if(COUPON_REPO.isCustomerOwnCoupon(couponId,customerId)){
        throw new CustomerCouponException("customer already own this coupon");
    }

    if(couponToPurchase.getAmount() < 1){
        throw new CouponOutOfAmountException();
    }

    if(couponToPurchase.getEnd_date().before(Date.valueOf(LocalDate.now())) || couponToPurchase.getEnd_date().equals(Date.valueOf(LocalDate.now()))){
        throw new CouponExpiredException();
    }

    COUPON_REPO.addCouponPurchase(couponId,customerId);
    couponToPurchase.setAmount(couponToPurchase.getAmount()-1);
    COUPON_REPO.saveAndFlush(couponToPurchase);
    System.out.println("successfully purchased");

    }

    /**
     * Getting all customer coupons
     * @return - List of coupon object that belongs to the current customer.
     * @throws CouponException - Will be thrown if there are no coupons.
     */
    @Override
    public List<Coupon> getAllCustomerCoupon() throws CouponException {
        List<Coupon> customerCoupons = COUPON_REPO.findCouponsBelongToCustomer(customerId);
        if(customerCoupons.isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }

        return customerCoupons;
    }

    /**
     * Getting Company coupons By category
     * @param categoryId -Getting the category id
     * @return List of coupons with the exact category that the user chose.
     * @throws CouponException - Will be thrown if there are no such coupons.
     */
    @Override
    public List<Coupon> getCustomerCouponByCategory(int categoryId) throws CouponException {
        if(COUPON_REPO.couponsByCategoryAndCustomer(customerId,categoryId).isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }
        return COUPON_REPO.couponsByCategoryAndCustomer(customerId,categoryId);
    }
    /**
     * Getting coupons by max price.
     * @param maxPrice - The max price that we choose.
     * @return - List of coupons with that max priced.
     * @throws CouponException -  Will be thrown if there are no coupons with this max price.
     */
    @Override
    public List<Coupon> getCustomerCouponByMaxPriced(int maxPrice) throws CouponException {
        if(COUPON_REPO.customerCouponsMaxPrice(customerId,maxPrice).isEmpty()){
            throw new CouponException("coupons are not exists for this customer");
        }
        return COUPON_REPO.customerCouponsMaxPrice(customerId,maxPrice);
    }

    /**
     *
     * @return - Customer object initialized with current customer values.
     * @throws CustomerException - Will be thrown if there are problem with current user.
     */
    @Override
    public Customer getCustomerDetails() throws CustomerException {
        if(thisCustomer == null){
            throw new CustomerException("customer does not exists");
        }

        try {
            thisCustomer.setCoupons(new HashSet<>(getAllCustomerCoupon()));
        } catch (CouponException e) {
            System.out.println(e.getMessage());
        }
        Customer customerToReturn = thisCustomer;
        customerToReturn.setPassword("{secret}");
        return customerToReturn;
    }

    /**
     * log out
     */
    @Override
    public void logOut(){
        isLogged = false;
        token = null;
    }


    /**
     *
     * @return -true if is logged
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     *
     * @return - string with token if is logged
     */
    public String getToken() {
        if(isLogged){
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.CUSTOMER.name()));
            User userDetails = new User(thisCustomer.getEmail(), thisCustomer.getPassword(),authorities);
            token = JWT.create().withSubject(userDetails.getUsername())
                    .withClaim("id",thisCustomer.getId())
                    .withExpiresAt(new Date(System.currentTimeMillis() +30 *1000))
                    .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
        }
        return token;
    }

    /**
     * get this company
     * @return - object of this customer
     */
    public Customer getThisCustomer() {
        return thisCustomer;
    }
}
