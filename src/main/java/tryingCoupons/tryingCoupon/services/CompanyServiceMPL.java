package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.*;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CategoryRepo;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;




@Service
public class CompanyServiceMPL extends ClientService implements CompanyService{
    //private int companyID;
    //private Company thisCompany;
    private boolean isLogged = false;
    //private String token;

    @Autowired
    private CategoryRepo categoryRepo;

    public CompanyServiceMPL(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    /**
     * Log in manager
     * @param email - String with value of email/username
     * @param password - string with value of password
     * @return - true if login successes
     */
    @Override
    public boolean login(String email, String password)  {


        boolean isExists = COMPANY_REPO.isCompanyExistsByEmailAndPassWord(email,password);
        if(!isExists){
            isLogged = false;
            return false;

        }else {
            isLogged = true;
            //thisCompany = COMPANY_REPO.findByEmailLikeAndPassword(email, password);
            System.out.println("login successfully");
            return true;
        }
    }

    /**
     * Adding coupon.
     * @param coupon - Getting coupon object
     * @throws CouponOutOfAmountException - Will be thrown if coupon amount is less than 1.
     * @throws CouponException - Will be thrown if there's a problem with the Coupon object
     * @throws CouponExpiredException - Will be thrown if the coupon is initialized with expired date.
     * @throws CompanyException - Will be thrown if Company already holding this coupon.
     */
    @Override
    public void addCoupon(Coupon coupon , String token) throws CompanyException, CouponException, CouponOutOfAmountException, CouponExpiredException {
       Company thisCompany = setId(token);

        int companyID = thisCompany.getId();

        if(companyID == -1){
            throw new CompanyException("company is not logged!");
        }
        coupon.setCompany_id_sql(thisCompany);
        if(coupon.getEnd_date().before(Date.valueOf(LocalDate.now()))||coupon.getEnd_date().equals(Date.valueOf(LocalDate.now()))){
            throw new CouponExpiredException();
        }
        if(coupon.getAmount()<1){
            throw new CouponOutOfAmountException();
        }
        if(COUPON_REPO.isCompanyOwnTheCoupon(companyID, coupon.getTitle())){
            throw new CouponException("coupon is already exists!");
        }
        Optional<CategoryInjection> categoryInjection = categoryRepo.findById(coupon.getCategory_id_bynum());
        if(categoryInjection.isPresent()){
            coupon.setCategory(categoryInjection.get());
        }else{
            throw new CouponException("coupon category does not exists!");
        }
        coupon.setCompanyId(companyID);
        COUPON_REPO.save(coupon);
        //coupon.setCouponID(COUPON_REPO.companyCoupon(coupon.getTitle(), companyID).getCoupon_id());
        System.out.println("Successfully added coupon");
    }

    /**
     * Update a coupon.
     * @param coupon - Getting Coupon object with updated details.
     * @throws CouponException - Will be thrown if there is a problem with the coupon object.
     */
    @Override
    public void updateCoupon(Coupon coupon, String token) throws CouponException {
        Company thisCompany = setId(token);

        int companyID = thisCompany.getId();

        coupon.setCompanyId(companyID);
        Optional<Coupon> couponCheck = COUPON_REPO.findById(coupon.getCoupon_id());

        if(couponCheck.isPresent()){
            Coupon couponToCheck = couponCheck.get();
            System.out.println(couponToCheck);
            System.out.println(couponToCheck.getCompanyId());
            System.out.println(coupon.getCompanyId());
            if(couponToCheck.getCompanyId() == (coupon.getCompanyId())){
                COUPON_REPO.saveAndFlush(coupon);
                System.out.println("successfully updated");
            }
        }else{
            throw new CouponException("company does not own this coupon!");
        }
    }

    /**
     * Deleting coupon.
     * @param couponId - Getting coupon id.
     * @throws CouponException - Will be thrown if the coupon does not exist.
     */
    @Override
    public void deleteCoupon(int couponId, String token) throws CouponException {
        Company thisCompany = setId(token);
        int companyID = thisCompany.getId();

        Optional<Coupon> optional = COUPON_REPO.findById(couponId);
        Coupon coupon = null;
        if(optional.isPresent()){
            coupon = optional.get();
        }else{
                throw new CouponException("coupon does not exists!");
            }


        if(coupon.getCompany_id_sql().getId() == companyID){
            if(COUPON_REPO.existsById(coupon.getCoupon_id())){
                COUPON_REPO.deleteById(coupon.getCoupon_id());
                System.out.println("Deleted successfully");
            }else{
                throw new CouponException("Coupon does not belong to the company");
            }
        }
    }

    /**
     * Getting coupons that belongs to the company.
     * @return -  List of Coupon object.
     * @throws CouponException - will be thrown if there are no coupons.
     */
    @Override
    public List<Coupon> getAllCompanyCoupons(String token) throws CouponException {

        Company thisCompany = setId(token);

        int companyID = thisCompany.getId();


        if(COUPON_REPO.getAllCompanyCoupons(companyID).isEmpty()){
            throw new CouponException("company does not own any coupons");
        }

        List<Coupon> coupons = COUPON_REPO.getAllCompanyCoupons(companyID);
        return COUPON_REPO.getAllCompanyCoupons(companyID);
    }

    /**
     * Getting Company coupons By category
     * @param category -Getting the category id
     * @return List of coupons with the exact category that the user chose.
     * @throws CouponException - Will be thrown if there are no such coupons.
     */
    @Override
    public List<Coupon> companyCouponsByCategory(int category,String token) throws CouponException {
        Company thisCompany = setId(token);

        int companyID = thisCompany.getId();
        List<Coupon> coupons = COUPON_REPO.getCouponsByCompanyAndCategory(companyID,category);
        if(coupons.isEmpty()){
            throw new CouponException("requested coupons are not exists");
        }
        return coupons;
    }

    /**
     * Getting coupons by max price.
     * @param maxPrice - The max price that we choose.
     * @return - List of coupons with that max priced.
     * @throws CouponException -  Will be thrown if there are no coupons with this max price.
     */
    @Override
    public List<Coupon> companyCouponsByMaxPrice(int maxPrice, String token) throws CouponException {
        Company thisCompany = setId(token);

        int companyID = thisCompany.getId();
        List<Coupon> coupons = COUPON_REPO.getCouponsByMaxedPrice(companyID,maxPrice);
        if(coupons.isEmpty()){
            throw new CouponException("requested coupons are not exists");
        }
        return coupons;
    }

    /**
     * Getting company details
     * @return - Company object with the current company values
     * @throws LoginException -  Will be thrown if the user is not logged.
     */
    @Override
    public Company companyDetails(String token) throws LoginException {
        Company thisCompany = setId(token);

        int companyID = thisCompany.getId();
        if(thisCompany == null){
            throw new LoginException();
        }
//
//        thisCompany.setCoupons(new HashSet<>(COUPON_REPO.getAllCompanyCoupons(companyID)));
//        return thisCompany.toString() +"  "+  thisCompany.getCoupons().toString();
        thisCompany.setPassword("{secret}");
        return thisCompany;
    }

    /**
     * log out
     */
    @Override
    public void logOut(){
        isLogged = false;
        //token = null;
    }

    /**
     *
     * @return -true if is logged
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     *
     * @return - string with token if is logged
     */


    /**
     * get this company
     * @return - object of this company
     */
    public Company getThisCompany(String token) {
        Company thisCompany = setId(token);

        return thisCompany;
    }


    public Company setId(String token){


        DecodedJWT decodedJWT = JWT.decode(token.replace("Bearer ",""));
        int companyID = decodedJWT.getClaim("id").asInt();
        Company thisCompany = COMPANY_REPO.findOneCompany(companyID);


        return thisCompany;


    }

    public int CompanyId(String userName , String password) throws LoginException {
        Company company = COMPANY_REPO.findByEmailLikeAndPassword(userName,password);
        if(company!=null){
            return company.getId();
        }
        throw new LoginException();

    }
}
