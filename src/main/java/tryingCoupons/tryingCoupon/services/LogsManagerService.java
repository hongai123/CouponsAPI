package tryingCoupons.tryingCoupon.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.beans.UserProp;
import tryingCoupons.tryingCoupon.exceptions.LoginException;

public interface LogsManagerService {
    public ResponseEntity<String> userLog(UserProp user, Roles roles) throws LoginException;
}
