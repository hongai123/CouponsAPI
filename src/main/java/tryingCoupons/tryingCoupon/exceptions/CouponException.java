package tryingCoupons.tryingCoupon.exceptions;

public class CouponException extends Exception{

    /**
     * Will be thrown if relate to coupon.
     */
    public CouponException(){
        super("There is a problem with the coupon");
    }

    /**
     * Will be thrown if relate to coupon.
     * @param someMassage - String which accept error message.
     */
    public CouponException(String someMassage){
        super(someMassage);
    }

}
