package tryingCoupons.tryingCoupon.exceptions;

public class CustomerCouponException extends Exception{
    /**
     * Will be thrown if there's problem with a customer
     * @param message - Accept string which represent the error.
     */
    public CustomerCouponException(String message){
        super(message);
    }

    /**
     * Will be thrown if there's problem with a customer with default message.
     */
    public CustomerCouponException(){
        super("problem accrued while trying to purchase");
    }
}
