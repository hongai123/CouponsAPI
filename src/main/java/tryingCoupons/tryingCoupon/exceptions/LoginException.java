package tryingCoupons.tryingCoupon.exceptions;

public class LoginException extends Exception{
    /**
     * Will be thrown where there is a problem relate to Log in.
     * @param describeERR - Accept string which describe the error.
     */
    public LoginException(String describeERR){
        super(describeERR);
    }

    /**
     * Will be thrown where there is a problem relate to Log in with default message.
     */
    public LoginException(){
        super("User name or password are wrong");
    }
}
