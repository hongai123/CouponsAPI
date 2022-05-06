package tryingCoupons.tryingCoupon.exceptions;

public class CustomerException extends Exception {
    /**
     * Will be thrown if there's a problem with a customer with default message
     */
    public CustomerException(){
        super("customer is already exists");
    }

    /**
     * Will be thrown if there's a problem with a customer
     * @param message - Accept string with error message.
     */
    public CustomerException(String message){
        super(message);
    }
}
