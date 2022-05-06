package tryingCoupons.tryingCoupon.exceptions;


public class CompanyException extends Exception {

    /**
     * Will be thrown if relate to company.
     */
    public CompanyException(){
        super("company is already exists");
    }

    /**
     * Will be thrown if relate to company
     * @param message - Accept string which represent the error message.
     */
    public CompanyException(String message){
        super(message);
    }
}
