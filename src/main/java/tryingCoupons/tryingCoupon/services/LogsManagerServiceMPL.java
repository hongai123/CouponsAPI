package tryingCoupons.tryingCoupon.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.beans.UserProp;
import tryingCoupons.tryingCoupon.exceptions.LoginException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogsManagerServiceMPL implements LogsManagerService{


    private final CompanyServiceMPL companyServiceMPL;
    private final CustomerServiceMPL customerServiceMPL;
    private final AdminServicesService adminServicesMPL;


    @Override
    public ResponseEntity<String> userLog(UserProp user, Roles roles) throws LoginException {
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

