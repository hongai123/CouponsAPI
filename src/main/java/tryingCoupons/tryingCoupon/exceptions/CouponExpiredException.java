package tryingCoupons.tryingCoupon.exceptions;

public class CouponExpiredException extends Exception{
    /**
     * thrown if coupon expired.
     */
    public CouponExpiredException(){
        super("coupon is out of date!");
    }
}
