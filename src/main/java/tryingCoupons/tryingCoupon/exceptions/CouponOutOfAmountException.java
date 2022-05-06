package tryingCoupons.tryingCoupon.exceptions;

public class CouponOutOfAmountException extends Exception{
    /**
     * will be thrown if coupon out of amount
     */
    public CouponOutOfAmountException(){
        super("Coupon is out of amount!");
    }
}
