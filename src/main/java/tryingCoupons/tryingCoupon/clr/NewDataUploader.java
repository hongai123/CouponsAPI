package tryingCoupons.tryingCoupon.clr;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tryingCoupons.tryingCoupon.beans.*;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;
import tryingCoupons.tryingCoupon.services.AdminServicesService;
import tryingCoupons.tryingCoupon.services.CompanyServiceMPL;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NewDataUploader implements CommandLineRunner {

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
        params.put("roles", Roles.ADMIN.name());
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
                .email("company1@gmail.com")
                .name("CouponsRus")
                .build();
        Company company2 = Company.builder()
                .password("12345")
                .email("company2@gmail.com")
                .name("CouponsAreThey")
                .build();
        Company company3 = Company.builder()
                .password("12345")
                .email("company3@gmail.com")
                .name("CouponsAreMe")
                .build();
        Company company4 = Company.builder()
                .password("12345")
                .email("company4@gmail.com")
                .name("CouponsAreYou")
                .build();



        adminServicesMPL.addCompany(company1, jwtTokenAdmin);
        adminServicesMPL.addCompany(company2, jwtTokenAdmin);
        adminServicesMPL.addCompany(company3, jwtTokenAdmin);
        adminServicesMPL.addCompany(company4, jwtTokenAdmin);




        Map<String,String> params2 = new HashMap<>();
        params2.put("roles",Roles.COMPANY.name());
        String loginCompany = "http://localhost:8080/token/log/{roles}";
        UserProp userProp2 = UserProp.builder()
                .username("company1@gmail.com")
                .password("12345")
                .role(Roles.COMPANY)
                .build();
        ResponseEntity<String> response2 = restTemplate.postForEntity(loginCompany,userProp2,String.class,params2);
        String jwtTokenCompany = response2.getHeaders().getFirst("Authorization");
        Coupon coupon1 = Coupon.builder()
                .title("Wheels")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://149410163.v2.pressablecdn.com/wp-content/uploads/2020/10/Supreme-Power-Wheels-BBS-RS-Gold-Diamond-Cut.png")
                .price(1000)
                .description("BBS best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon2 = Coupon.builder()
                .title("Toyota Supra")
                .amount(20)
                .image("https://aaajapan.com/images/Toyota%20Supra%20V12/toyota-supra-v12-top-secret-4.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon3 = Coupon.builder()
                .title("Honda CBR1000")
                .amount(3)
                .image("https://honda.org.il/wp-content/uploads/2019/06/83526_17YM_CBR1000RR_Fireblade.jpg")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("GET YOUR BIKE ON DISCOUNT TODAY!")
                .build();
        Coupon coupon4 = Coupon.builder()
                .title("Van Gogh Painting")
                .amount(1)
                .image("https://www.paint.org/wp-content/uploads/2019/11/Van-Gogh-The-Starry-Night-adult-coloring-page-1.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Rare van goch paiting for sale")
                .build();
        Coupon coupon5 = Coupon.builder()
                .title("Yacht For The Rich")
                .amount(3)
                .image("https://www.wartsila.com/images/default-source/marine-pictures/yachts/super-intelligent-yachts.tmb-1920x690.jpg?sfvrsn=355a4043_1")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("AMAZING YACHT FOR YOU AND YOUR FAMILY")
                .build();
        Coupon coupon6 = Coupon.builder()
                .title("Esr wheels ")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://audiocityusa.com/shop/images/P/esr-wheels-cr5-gloss-black-rims-audiocityusa-0-02.jpg")
                .price(2000)
                .description("ESR best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon7 = Coupon.builder()
                .title("Toyota Supra MK5")
                .amount(20)
                .image("https://www.carscoops.com/wp-content/uploads/2021/08/Wald-Supra-1-00006.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon8 = Coupon.builder()
                .title("R1")
                .amount(3)
                .image("https://ae01.alicdn.com/kf/H8ea4a1b5d3a142eea75d28c7b6131ad9A/Motorcycle-Fairings-Kit-Fit-For-Yzf-R1-2009-2010-2011-Bodywork-Set-High-Quality-ABS-Injection.jpg_Q90.jpg_.webp")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("special R1")
                .build();
        Coupon coupon9 = Coupon.builder()
                .title("TheMonaLiza")
                .amount(1)
                .image("https://static.dw.com/image/41371444_303.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Get The Mona Liza!")
                .build();
        Coupon coupon10 = Coupon.builder()
                .title("Titanic")
                .amount(3)
                .image("https://cdn.britannica.com/79/4679-050-BC127236/Titanic.jpg")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Not the best choice")
                .build();
        companyServiceMPL.addCoupon(coupon1,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon2,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon3,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon4,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon5,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon6,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon7,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon8,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon9,jwtTokenCompany);
        companyServiceMPL.addCoupon(coupon10,jwtTokenCompany);






        Map<String,String> params3 = new HashMap<>();
        params3.put("roles",Roles.COMPANY.name());
        String loginCompany3 = "http://localhost:8080/token/log/{roles}";
        UserProp userProp3 = UserProp.builder()
                .username("company2@gmail.com")
                .password("12345")
                .role(Roles.COMPANY)
                .build();
        ResponseEntity<String> response3 = restTemplate.postForEntity(loginCompany3,userProp3,String.class,params3);
        String jwtTokenCompany3 = response3.getHeaders().getFirst("Authorization");
        Coupon coupon11 = Coupon.builder()
                .title("Wheels")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://149410163.v2.pressablecdn.com/wp-content/uploads/2020/10/Supreme-Power-Wheels-BBS-RS-Gold-Diamond-Cut.png")
                .price(1000)
                .description("BBS best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon12 = Coupon.builder()
                .title("Toyota Supra")
                .amount(20)
                .image("https://aaajapan.com/images/Toyota%20Supra%20V12/toyota-supra-v12-top-secret-4.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon13 = Coupon.builder()
                .title("Honda CBR1000")
                .amount(3)
                .image("https://honda.org.il/wp-content/uploads/2019/06/83526_17YM_CBR1000RR_Fireblade.jpg")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("GET YOUR BIKE ON DISCOUNT TODAY!")
                .build();
        Coupon coupon14 = Coupon.builder()
                .title("Van Gogh Painting")
                .amount(1)
                .image("https://www.paint.org/wp-content/uploads/2019/11/Van-Gogh-The-Starry-Night-adult-coloring-page-1.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Rare van goch paiting for sale")
                .build();
        Coupon coupon15 = Coupon.builder()
                .title("Yacht For The Rich")
                .amount(3)
                .image("https://www.wartsila.com/images/default-source/marine-pictures/yachts/super-intelligent-yachts.tmb-1920x690.jpg?sfvrsn=355a4043_1")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("AMAZING YACHT FOR YOU AND YOUR FAMILY")
                .build();
        Coupon coupon16 = Coupon.builder()
                .title("Esr wheels ")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://audiocityusa.com/shop/images/P/esr-wheels-cr5-gloss-black-rims-audiocityusa-0-02.jpg")
                .price(2000)
                .description("ESR best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon17 = Coupon.builder()
                .title("Toyota Supra MK5")
                .amount(20)
                .image("https://www.carscoops.com/wp-content/uploads/2021/08/Wald-Supra-1-00006.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon18 = Coupon.builder()
                .title("R1")
                .amount(3)
                .image("https://ae01.alicdn.com/kf/H8ea4a1b5d3a142eea75d28c7b6131ad9A/Motorcycle-Fairings-Kit-Fit-For-Yzf-R1-2009-2010-2011-Bodywork-Set-High-Quality-ABS-Injection.jpg_Q90.jpg_.webp")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("special R1")
                .build();
        Coupon coupon19 = Coupon.builder()
                .title("TheMonaLiza")
                .amount(1)
                .image("https://static.dw.com/image/41371444_303.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Get The Mona Liza!")
                .build();
        Coupon coupon20 = Coupon.builder()
                .title("Titanic")
                .amount(3)
                .image("https://cdn.britannica.com/79/4679-050-BC127236/Titanic.jpg")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Not the best choice")
                .build();

        companyServiceMPL.addCoupon(coupon11,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon12,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon13,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon14,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon15,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon16,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon17,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon18,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon19,jwtTokenCompany3);
        companyServiceMPL.addCoupon(coupon20,jwtTokenCompany3);



        Map<String,String> params4 = new HashMap<>();
        params4.put("roles",Roles.COMPANY.name());
        String loginCompany4 = "http://localhost:8080/token/log/{roles}";
        UserProp userProp4 = UserProp.builder()
                .username("company3@gmail.com")
                .password("12345")
                .role(Roles.COMPANY)
                .build();
        ResponseEntity<String> response4 = restTemplate.postForEntity(loginCompany4,userProp4,String.class,params4);
        String jwtTokenCompany4 = response4.getHeaders().getFirst("Authorization");
        Coupon coupon21 = Coupon.builder()
                .title("Wheels")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://149410163.v2.pressablecdn.com/wp-content/uploads/2020/10/Supreme-Power-Wheels-BBS-RS-Gold-Diamond-Cut.png")
                .price(1000)
                .description("BBS best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon22 = Coupon.builder()
                .title("Toyota Supra")
                .amount(20)
                .image("https://aaajapan.com/images/Toyota%20Supra%20V12/toyota-supra-v12-top-secret-4.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon23 = Coupon.builder()
                .title("Honda CBR1000")
                .amount(3)
                .image("https://honda.org.il/wp-content/uploads/2019/06/83526_17YM_CBR1000RR_Fireblade.jpg")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("GET YOUR BIKE ON DISCOUNT TODAY!")
                .build();
        Coupon coupon24 = Coupon.builder()
                .title("Van Gogh Painting")
                .amount(1)
                .image("https://www.paint.org/wp-content/uploads/2019/11/Van-Gogh-The-Starry-Night-adult-coloring-page-1.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Rare van goch paiting for sale")
                .build();
        Coupon coupon25 = Coupon.builder()
                .title("Yacht For The Rich")
                .amount(3)
                .image("https://www.wartsila.com/images/default-source/marine-pictures/yachts/super-intelligent-yachts.tmb-1920x690.jpg?sfvrsn=355a4043_1")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("AMAZING YACHT FOR YOU AND YOUR FAMILY")
                .build();
        Coupon coupon26 = Coupon.builder()
                .title("Esr wheels ")
                .amount(230)
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(3)
                .image("https://audiocityusa.com/shop/images/P/esr-wheels-cr5-gloss-black-rims-audiocityusa-0-02.jpg")
                .price(2000)
                .description("ESR best wheels that you can find")
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now().minusDays(30)))
                .build();
        Coupon coupon27 = Coupon.builder()
                .title("Toyota Supra MK5")
                .amount(20)
                .image("https://www.carscoops.com/wp-content/uploads/2021/08/Wald-Supra-1-00006.jpg")
                .company_id_sql(companyRepo.findById(1).get())
                .category_id_bynum(1)
                .end_date(Date.valueOf(LocalDate.now().plusDays(3)))
                .start_date(Date.valueOf(LocalDate.now()))
                .price(23000)
                .description("get 20% discount on this car")
                .build();
        Coupon coupon28 = Coupon.builder()
                .title("R1")
                .amount(3)
                .image("https://ae01.alicdn.com/kf/H8ea4a1b5d3a142eea75d28c7b6131ad9A/Motorcycle-Fairings-Kit-Fit-For-Yzf-R1-2009-2010-2011-Bodywork-Set-High-Quality-ABS-Injection.jpg_Q90.jpg_.webp")
                .category_id_bynum(5)
                .company_id_sql(companyRepo.findById(1).get())
                .price(25000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("special R1")
                .build();
        Coupon coupon29 = Coupon.builder()
                .title("TheMonaLiza")
                .amount(1)
                .image("https://static.dw.com/image/41371444_303.jpg")
                .category_id_bynum(2)
                .company_id_sql(companyRepo.findById(1).get())
                .price(2_000_000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Get The Mona Liza!")
                .build();
        Coupon coupon30 = Coupon.builder()
                .title("Titanic")
                .amount(3)
                .image("https://cdn.britannica.com/79/4679-050-BC127236/Titanic.jpg")
                .category_id_bynum(4)
                .company_id_sql(companyRepo.findById(1).get())
                .price(250000)
                .end_date(Date.valueOf(LocalDate.now().plusDays(1)))
                .start_date(Date.valueOf(LocalDate.now()))
                .description("Not the best choice")
                .build();

        companyServiceMPL.addCoupon(coupon21,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon22,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon23,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon24,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon25,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon26,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon27,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon28,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon29,jwtTokenCompany4);
        companyServiceMPL.addCoupon(coupon30,jwtTokenCompany4);



        Customer customer = Customer.builder()
                .firstName("Kanye")
                .lastName("West")
                .email("kanye@gmail.com")
                .password("12345")
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Justin")
                .lastName("Bieber")
                .email("justin@gmail.com")
                .password("12345")
                .build();





        customerRepo.save(customer);
        customerRepo.save(customer2);
        couponRepo.addCouponPurchase(1,1);
        couponRepo.addCouponPurchase(2,1);
        couponRepo.addCouponPurchase(3,1);
        couponRepo.addCouponPurchase(2,2);


    }
}
