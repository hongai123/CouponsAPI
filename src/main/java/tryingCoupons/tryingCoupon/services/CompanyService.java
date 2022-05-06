package tryingCoupons.tryingCoupon.services;

import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.exceptions.*;

import java.util.List;

public interface CompanyService {

    /**
     * Adding coupon.
     * @param coupon - Getting coupon object
     * @throws CouponOutOfAmountException - Will be thrown if coupon amount is less than 1.
     * @throws CouponException - Will be thrown if there's a problem with the Coupon object
     * @throws CouponExpiredException - Will be thrown if the coupon is initialized with expired date.
     * @throws CompanyException - Will be thrown if Company already holding this coupon.
     */
    public void addCoupon(Coupon coupon, String token) throws CompanyException, CouponException, CouponOutOfAmountException, CouponExpiredException;
    /**
     * Update a coupon.
     * @param coupon - Getting Coupon object with updated details.
     * @throws CouponException - Will be thrown if there is a problem with the coupon object.
     */
    public void updateCoupon(Coupon coupon, String token) throws CouponException;
    /**
     * Deleting coupon.
     * @param couponId - Getting coupon id.
     * @throws CouponException - Will be thrown if the coupon does not exist.
     */
    public void deleteCoupon(int couponId, String token) throws CouponException;
    /**
     * Getting coupons that belongs to the company.
     * @return -  List of Coupon object.
     * @throws CouponException - will be thrown if there are no coupons.
     */
    public List<Coupon> getAllCompanyCoupons(String token) throws CouponException;
    /**
     * Getting Company coupons By category
     * @param category -Getting the category id
     * @return List of coupons with the exact category that the user chose.
     * @throws CouponException - Will be thrown if there are no such coupons.
     */
    public List<Coupon> companyCouponsByCategory(int category, String token) throws CouponException;
    /**
     * Getting coupons by max price.
     * @param maxPrice - The max price that we choose.
     * @return - List of coupons with that max priced.
     * @throws CouponException -  Will be thrown if there are no coupons with this max price.
     */
    public List<Coupon> companyCouponsByMaxPrice(int maxPrice, String token) throws CouponException;
    /**
     * Getting company details
     * @return - Company object with the current company values
     * @throws LoginException -  Will be thrown if the user is not logged.
     */
    public Company companyDetails(String token) throws LoginException;

    /**
     * log out
     */
    public void logOut();
}
