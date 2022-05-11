package tryingCoupons.tryingCoupon.services;

import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.exceptions.*;

import java.util.List;

public interface CustomerService {
    /**
     * Customer buying coupon.
     * @param couponId -The coupon id.
     * @throws CouponOutOfAmountException - Will be thrown if coupon is out of amount.
     * @throws CouponException - will be thrown if coupon does not exist.
     * @throws CouponExpiredException - will be thrown if coupon is expired
     * @throws CustomerCouponException - Will be thrown if customer already own this coupon.
     */
    public void purchaseCoupons(int couponId, String token) throws CustomerCouponException, CouponException, CouponOutOfAmountException, CouponExpiredException;
    /**
     * Getting all customer coupons
     * @return - List of coupon object that belongs to the current customer.
     * @throws CouponException - Will be thrown if there are no coupons.
     */
    public List<Coupon> getAllCustomerCoupon(String token) throws CouponException;
    /**
     * Getting Company coupons By category
     * @param categoryId -Getting the category id
     * @return List of coupons with the exact category that the user chose.
     * @throws CouponException - Will be thrown if there are no such coupons.
     */
    public List<Coupon> getCustomerCouponByCategory(int categoryId, String token) throws CouponException;
    /**
     * Getting coupons by max price.
     * @param maxPrice - The max price that we choose.
     * @return - List of coupons with that max priced.
     * @throws CouponException -  Will be thrown if there are no coupons with this max price.
     */
    public List<Coupon> getCustomerCouponByMaxPriced(int maxPrice, String token) throws CouponException;
    /**
     *
     * @return - Customer object initialized with current customer values.
     * @throws CustomerException - Will be thrown if there are problem with current user.
     */
    public Customer getCustomerDetails(String token) throws CustomerException;

    /**
     * log out
     */
    public void logOut();
}
