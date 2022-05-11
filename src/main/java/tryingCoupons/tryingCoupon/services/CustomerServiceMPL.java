package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


@Service
@Getter
public class CustomerServiceMPL extends ClientService implements CustomerService{
    private int customerId;
    private Customer thisCustomer;
    private boolean isLogged = false;

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
    public void purchaseCoupons(int couponId, String token) throws CustomerCouponException, CouponException, CouponOutOfAmountException, CouponExpiredException {
        thisCustomer = setId(token);
        customerId = thisCustomer.getId();
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
    public List<Coupon> getAllCustomerCoupon(String token) throws CouponException {
        thisCustomer = setId(token);
        customerId = thisCustomer.getId();
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
    public List<Coupon> getCustomerCouponByCategory(int categoryId, String token) throws CouponException {
        thisCustomer = setId(token);
        customerId = thisCustomer.getId();
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
    public List<Coupon> getCustomerCouponByMaxPriced(int maxPrice, String token) throws CouponException {
        thisCustomer = setId(token);
        customerId = thisCustomer.getId();
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
    public Customer getCustomerDetails(String token) throws CustomerException {
        thisCustomer = setId(token);
        customerId = thisCustomer.getId();
        thisCustomer = setId(token) ;
        if(thisCustomer == null){
            throw new CustomerException("customer does not exists");
        }

        try {
            thisCustomer.setCoupons(new HashSet<>(getAllCustomerCoupon(token)));
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
    }


    /**
     *
     * @return -true if is logged
     */
    public boolean isLogged() {
        return isLogged;
    }



    /**
     * get this company
     * @return - object of this customer
     */
    public Customer getThisCustomer() {
        return thisCustomer;
    }

    public int CustomerId(String userName) throws LoginException {
        Customer customer = CUSTOMER_REPO.findByEmailLike(userName);
        if(customer!=null){
            return customer.getId();
        }
        throw new LoginException();

    }


    public Customer setId(String token) {


        DecodedJWT decodedJWT = JWT.decode(token.replace("Bearer ", ""));
        int companyID = decodedJWT.getClaim("id").asInt();
        Customer thisCustomer = CUSTOMER_REPO.findById(companyID).get();


        return thisCustomer;
    }

}
