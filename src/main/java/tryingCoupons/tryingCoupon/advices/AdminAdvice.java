package tryingCoupons.tryingCoupon.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tryingCoupons.tryingCoupon.exceptions.CompanyException;
import tryingCoupons.tryingCoupon.exceptions.CustomerException;

@RestController
@ControllerAdvice
public class AdminAdvice {
    /**
     * Handle a company exception
     * @param err - Get's an exception
     * @return - ErrorDetails object - which contains 2 String values to describe the error.
     */
    @ExceptionHandler(value = {CompanyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleCompanyException(Exception err){
        return new ErrorDetails(err.getClass().getName(),err.getMessage());
    }

}
