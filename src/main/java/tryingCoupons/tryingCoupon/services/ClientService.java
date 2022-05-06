package tryingCoupons.tryingCoupon.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.exceptions.LoginException;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;

@Service
@RequiredArgsConstructor
public abstract class ClientService {
    /**
     * Log in manager
     * @param email - String with value of email/username
     * @param password - string with value of password
     * @return - true if login successes
     */
    public abstract boolean login(String email, String password);
    protected final CompanyRepo COMPANY_REPO;
    protected final CustomerRepo CUSTOMER_REPO;
    protected final CouponRepo COUPON_REPO;
}
