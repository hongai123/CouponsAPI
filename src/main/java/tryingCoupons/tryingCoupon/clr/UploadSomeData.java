package tryingCoupons.tryingCoupon.clr;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tryingCoupons.tryingCoupon.beans.*;
import tryingCoupons.tryingCoupon.exceptions.*;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import tryingCoupons.tryingCoupon.services.AdminServicesService;
import tryingCoupons.tryingCoupon.services.CompanyServiceMPL;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


//@Component
@RequiredArgsConstructor
/**
 * A component that uploads some mock data
 */
public class UploadSomeData implements CommandLineRunner {

    private final CustomerServiceMPL customerServiceMPL;
    private final CompanyRepo companyRepo;
    private final CouponRepo couponRepo;
    private final CustomerRepo customerRepo;
    private final AdminServicesService adminServicesMPL;
    private final CompanyServiceMPL companyServiceMPL;
    private final RestTemplate restTemplate;





    @Override
    public void run(String... args) throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("roles",Roles.ADMIN.name());
        String login = "http://localhost:8080/token/log/{roles}";
        UserProp userProp = UserProp.builder()
                .username("admin@admin.com")
                .password("admin")
                .role(Roles.ADMIN)
                .build();
        ResponseEntity<String> response1 = restTemplate.postForEntity(login,userProp,String.class,params);
        String jwtTokenAdmin = response1.getHeaders().getFirst("Authorization");
        System.out.println(jwtTokenAdmin);





        Company company1 = Company.builder()
                .password("12345")
                .email("huyn3211")
                .name("CouponRus")
                .build();
        Company company2 = Company.builder()
                .password("12345")
                .email("justin122")
                .name("CouponsAreThey")
                .build();



        adminServicesMPL.addCompany(company1, jwtTokenAdmin);
        adminServicesMPL.addCompany(company2, jwtTokenAdmin);

        try{
            adminServicesMPL.addCompany(company1, jwtTokenAdmin);
        }catch (CompanyException err){
            System.out.println(err.getMessage());
        }



        Map<String,String> params2 = new HashMap<>();
        params2.put("roles",Roles.COMPANY.name());
        String loginCompany = "http://localhost:8080/token/log/{roles}";
        UserProp userProp2 = UserProp.builder()
                .username("huyn3211")
                .password("12345")
                .role(Roles.COMPANY)
                .build();
        ResponseEntity<String> response2 = restTemplate.postForEntity(loginCompany,userProp2,String.class,params2);
        String jwtTokenCompany = response2.getHeaders().getFirst("Authorization");

        companyServiceMPL.login("huyn3211","12345");

        Coupon coupon1 = Coupon.builder()
                .title("Wheels")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(2)
                .image("https://149410163.v2.pressablecdn.com/wp-content/uploads/2020/10/Supreme-Power-Wheels-BBS-RS-Gold-Diamond-Cut.png")
                .price(1000)
                .description("BBS best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon2 = Coupon.builder()
                .title("Toyota Supra")
                .amount(5)
                .image("https://aaajapan.com/images/Toyota%20Supra%20V12/toyota-supra-v12-top-secret-4.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(4)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon3 = Coupon.builder()
                .title("Honda CBR1000")
                .amount(3)
                .image("https://honda.org.il/wp-content/uploads/2019/06/83526_17YM_CBR1000RR_Fireblade.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("GET YOUR BIKE ON DISCOUNT TODAY!")
                .build();
        companyServiceMPL.addCoupon(coupon1,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon2,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon3,jwtTokenCompany);

        Set<Coupon> customerCoupons = new HashSet<>();
        customerCoupons.add(couponRepo.findById(1).get());
        customerCoupons.add(couponRepo.findById(2).get());
        customerCoupons.add(couponRepo.findById(3).get());

        Set<Coupon> customerCoupons2 = new HashSet<>();
        customerCoupons2.add(couponRepo.findById(1).get());
        customerCoupons2.add(couponRepo.findById(2).get());
        customerCoupons2.add(couponRepo.findById(3).get());

        Customer customer = Customer.builder()
                .firstName("TOMER")
                .lastName("OMO")
                .email("wannadie@die.com")
                .password("dieWithMe")
                .coupons(customerCoupons)
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Itzik")
                .lastName("OMO")
                .email("wannalive@live.com")
                .password("liveWithMe")
                .coupons(customerCoupons2)
                .build();





        customerRepo.save(customer);
        customerRepo.save(customer2);
        couponRepo.addCouponPurchase(1,1);
        couponRepo.addCouponPurchase(2,1);
        couponRepo.addCouponPurchase(3,1);
        couponRepo.addCouponPurchase(2,2);


        List<Coupon> checking = couponRepo.findCouponsBelongToCustomer(1);
        System.out.println(checking);
        System.out.println(companyRepo.isCompanyExistsByEmailAndPassWord("huyn3211","12345"));
        System.out.println(companyRepo.findOneCompany(1));
        System.out.println(customerRepo.isCustomerExists("wannalive@live.com","liveWithMe"));
        System.out.println(customerRepo.getOneCustomer(1));
        System.out.println(couponRepo.getOneCoupon(1));

            adminServicesMPL.login("admin@admin.com", "admin");

        company1.setEmail("justinbieber");
        company1.setPassword("DrewHouse");
        adminServicesMPL.updateCompany(company1,jwtTokenAdmin);

        try {
            adminServicesMPL.updateCompany(company1,jwtTokenAdmin);
        }catch (CompanyException err){
            System.out.println(err.getMessage());
        }

        System.out.println(adminServicesMPL.getAllCompanies(jwtTokenAdmin));


        try{
            System.out.println(adminServicesMPL.getAllCompanies(jwtTokenAdmin));
        }catch (CompanyException err){
            System.out.println(err.getMessage());
        }

        Customer customer3 = Customer.builder().password("huy").email("wannalive@live.com").firstName("huy").lastName("huy").build();

        try{
            adminServicesMPL.addCustomer(customer,jwtTokenAdmin);
        }catch (CustomerException err){
            System.out.println(err.getMessage());
        }


        try{
            adminServicesMPL.addCustomer(customer3,jwtTokenAdmin);
        }catch (CustomerException err){
            System.out.println(err.getMessage());
        }

        customer3.setEmail("customer3@gmail.com");


        try{
            adminServicesMPL.addCustomer(customer3,jwtTokenAdmin);
        }catch (CustomerException err){
            System.out.println(err.getMessage());
        }

        customer3.setPassword("justKillMeAlready");

        adminServicesMPL.updateCustomer(customer3,jwtTokenAdmin);

        //adminServicesMPL.deleteCustomer(1);

        System.out.println(adminServicesMPL.getAllCustomer(jwtTokenAdmin));
        System.out.println(adminServicesMPL.findCustomerById(2,jwtTokenAdmin));

        try{
            System.out.println(adminServicesMPL.getAllCustomer(jwtTokenAdmin));
        }catch (CustomerException err){
            System.out.println(err.getMessage());
        }

        try{
            System.out.println(adminServicesMPL.findCustomerById(5,jwtTokenAdmin));

        }catch(CustomerException err){
            System.out.println(err.getMessage());
        }

            System.out.println(companyServiceMPL.login("justin bieber", "DrewHouse"));


        Coupon coupon5 = Coupon.builder()
                .title("mazdaRX7")
                .amount(3)
                .price(200000)
                .category_id_bynum(1)
                .description("beautiful rx7")
                .end_date(Date.valueOf(LocalDate.of(2022,12,12)))
                .start_date(Date.valueOf(LocalDate.now()))
                .image("https://64.media.tumblr.com/703d6d275e9b153acec1204a328ffd50/tumblr_ponivzj7081vejkn0o1_1280.jpg")
                .build();

      try{
          companyServiceMPL.addCoupon(coupon5,jwtTokenCompany);
      }catch (CompanyException | CouponException| CouponOutOfAmountException | CouponExpiredException err){
          System.out.println(err.getMessage());
        }


//        System.out.println(coupon5.getCoupon_id());
//        System.out.println(company1.getId());
//        System.out.println(company2.getId());
//        System.out.println(customer3.getId());

        coupon5.setAmount(1);

        companyServiceMPL.updateCoupon(coupon5,jwtTokenCompany);
//      companyServiceMPL.deleteCoupon(coupon5);
        System.out.println(companyServiceMPL.companyCouponsByCategory(1,jwtTokenCompany));

        try{
            System.out.println(companyServiceMPL.companyCouponsByCategory(2,jwtTokenCompany));
        }catch (CouponException err){
            System.out.println(err.getMessage());
        }

        System.out.println(companyServiceMPL.companyCouponsByMaxPrice(70000,jwtTokenCompany));

        //System.out.println(companyServiceMPL.getAllCompanyCoupons());

        System.out.println(companyServiceMPL.companyDetails(jwtTokenCompany));

        Map<String,String> params3 = new HashMap<>();
        params3.put("roles",Roles.CUSTOMER.name());
        String login3 = "http://localhost:8080/token/log/{roles}";
        UserProp userProp3 = UserProp.builder()
                .username("just kill me already")
                .password("justKillMeAlready")
                .role(Roles.CUSTOMER)
                .build();
        ResponseEntity<String> response3 = restTemplate.postForEntity(login3,userProp3,String.class,params3);
        String customerToken = response3.getHeaders().getFirst("Authorization");

        System.out.println(customerServiceMPL.login("just kill me already","justKillMeAlready"));

        customerServiceMPL.purchaseCoupons(1,customerToken);

        System.out.println(customerServiceMPL.getAllCustomerCoupon(customerToken));
       // System.out.println(customerServiceMPL.getCustomerCouponByCategory(2));
        System.out.println(customerServiceMPL.getCustomerCouponByMaxPriced(100000,customerToken));
        System.out.println(customerServiceMPL.getCustomerDetails(customerToken));



        companyServiceMPL.logOut();
        adminServicesMPL.logOut();
        customerServiceMPL.logOut();





//        companyServiceMPL.login("justin bieber","DrewHouse");
//        companyServiceMPL.setId(companyServiceMPL.getToken());




    }
}
