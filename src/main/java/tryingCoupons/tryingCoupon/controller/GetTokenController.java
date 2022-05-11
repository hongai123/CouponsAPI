package tryingCoupons.tryingCoupon.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.beans.UserProp;
import tryingCoupons.tryingCoupon.exceptions.LoginException;
import tryingCoupons.tryingCoupon.services.AdminServicesService;
import tryingCoupons.tryingCoupon.services.CompanyServiceMPL;
import tryingCoupons.tryingCoupon.services.CustomerServiceMPL;

import javax.persistence.Enumerated;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
@CrossOrigin(exposedHeaders = "Authorization")
@RequiredArgsConstructor
/**
 * login/token/logout controller
 */
public class GetTokenController {
    private final CompanyServiceMPL companyServiceMPL;
    private final CustomerServiceMPL customerServiceMPL;
    private final AdminServicesService adminServicesMPL;

//    /**
//     * Getting token of logged user from header.
//     * @return - Response entity which contains the token.
//     * @throws LoginException - If user is not logged this exception will be thrown.
//     */
//    @GetMapping("/getToken")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<String> getToken() throws LoginException {
//        HttpHeaders responseHeader =  new HttpHeaders();
//
//        if(adminServicesMPL.isLogged()){
//            responseHeader.set("Token","Bearer "+adminServicesMPL.getToken());
//            //return "Bearer " + adminServicesMPL.getToken();
//            return ResponseEntity.accepted()
//                    .headers(responseHeader)
//                    .body("your token is in the headers!");
//        }
//
//        if(companyServiceMPL.isLogged()){
//            responseHeader.set("Token","Bearer "+companyServiceMPL.getToken());
//            //return "Bearer " + companyServiceMPL.getToken();
//            return ResponseEntity.accepted()
//                    .headers(responseHeader)
//                    .body("your token is in the headers!");
//        }
//
//        if(customerServiceMPL.isLogged()){
//            responseHeader.set("Token","Bearer "+customerServiceMPL.getToken());
//            //return "Bearer " + customerServiceMPL.getToken();
//            return ResponseEntity.accepted()
//                    .headers(responseHeader)
//                    .body("your token is in the headers!");
//        }
//
//        else{
//            throw new LoginException("Please log in first!");
//        }
//    }


    /**
     * Logout
     * @return - ModelAndView Object which redirect browser to a specific route.
     * @throws LoginException - If the user is not logged this exception will be thrown.
     */
    @RequestMapping(value = "/lognout", method = RequestMethod.GET)
    public ModelAndView logOut() throws LoginException {
        if(adminServicesMPL.isLogged()){
            adminServicesMPL.logOut();
            System.out.println("in admin logout");
            return new ModelAndView("redirect:" + "http://localhost:8080/login");
        }

        if(companyServiceMPL.isLogged()){
            companyServiceMPL.logOut();
            System.out.println("in company log out");
            return new ModelAndView("redirect:" + "http://localhost:8080/login");
        }

        if(customerServiceMPL.isLogged()){
            customerServiceMPL.logOut();
            System.out.println("in customer log out");
            return new ModelAndView("redirect:" + "http://localhost:8080/login");
        }

        else{
            throw new LoginException("Please log in first!");

        }
    }

    /**
     * Login with controller
     * @param user - User object which contains username and password.
     * @return - boolean if logged.
     * @throws LoginException - Will be thrown if username or password are invalid.
     */
    @RequestMapping(value = "/log/{roles}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> log(@RequestBody UserProp user, @PathVariable Roles roles) throws LoginException {
        System.out.println(user);
        user.setRole(roles);
        HttpHeaders responseHeader =  new HttpHeaders();

        if(user.getRole().name().equals("ADMIN")) {
            if (adminServicesMPL.login(user.getUsername(), user.getPassword())) {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + Roles.ADMIN.name()));
                User userDetails = new User("admin@admin.com", "admin", authorities);

                String token = JWT.create().withSubject(userDetails.getUsername())
                        .withClaim("id", 1)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 10000))
                        .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
                responseHeader.set("Authorization", "Bearer " + token);
                //return "Bearer " + adminServicesMPL.getToken();
                return ResponseEntity.accepted()
                        .headers(responseHeader)
                        .body("your token is in the headers!");

            }else{
                throw new LoginException("wrong username or password");
            }

        }

        if(user.getRole().name().equals("COMPANY")) {
            if (companyServiceMPL.login(user.getUsername(), user.getPassword())) {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + Roles.COMPANY.name()));
                User userDetails = new User(user.getUsername(), user.getPassword(), authorities);
                String token = JWT.create().withSubject(userDetails.getUsername())
                        .withClaim("id", companyServiceMPL.CompanyId(user.getUsername(), user.getPassword()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 10000))
                        .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
                responseHeader.set("Authorization", "Bearer " + token);
                return ResponseEntity.accepted()
                        .headers(responseHeader)
                        .body("your token is in the headers!");

            }else {
                throw new LoginException("wrong username or password");
            }
        }

        if(user.getRole().name().equals("CUSTOMER"))
            if(customerServiceMPL.login(user.getUsername(), user.getPassword())){
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + Roles.CUSTOMER.name()));
                User userDetails = new User(user.getUsername(), user.getPassword(), authorities);
                String token = JWT.create().withSubject(userDetails.getUsername())
                        .withClaim("id", customerServiceMPL.CustomerId(user.getUsername()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 10000))
                        .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
                responseHeader.set("Authorization", "Bearer " + token);
                return ResponseEntity.accepted()
                        .headers(responseHeader)
                        .body("your token is in the headers!");
        }


            throw new LoginException("wrong username or password");


    }

}
