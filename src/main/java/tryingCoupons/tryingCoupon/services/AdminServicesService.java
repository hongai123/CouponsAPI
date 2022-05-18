package tryingCoupons.tryingCoupon.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tryingCoupons.tryingCoupon.beans.Company;
import tryingCoupons.tryingCoupon.beans.Coupon;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.beans.Roles;
import tryingCoupons.tryingCoupon.exceptions.CompanyException;
import tryingCoupons.tryingCoupon.exceptions.CustomerException;
import tryingCoupons.tryingCoupon.repositories.CompanyRepo;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;
import tryingCoupons.tryingCoupon.repositories.CustomerRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdminServicesService extends ClientService implements AdminService {
    private final String EMAIL = "admin@admin.com";
    private final String PASSWORD = "admin";
    private String token;
    private boolean isLogged = false;


    public AdminServicesService(CompanyRepo COMPANY_REPO, CustomerRepo CUSTOMER_REPO, CouponRepo COUPON_REPO) {
        super(COMPANY_REPO, CUSTOMER_REPO, COUPON_REPO);
    }

    /**
     * login
     * @param email - String with value of email/username
     * @param password - string with value of password
     * @return - true if login successes
     */
    @Override
    public boolean login(String email, String password)  {
        if(!(this.EMAIL.equals(email) && this.PASSWORD.equals(password))){
           // isLogged = false;
            return false;
        }
        else{
            //isLogged = true;
            System.out.println("successful login");
            return true;
        }
    }

    /**
     * adding company
     * @param company - Getting Company object.
     * @throws CompanyException - thrown if there's a problem with login
     */
    @Override
    public void addCompany(Company company, String token) throws CompanyException {
        if(COMPANY_REPO.isCompanyExistsByName(company.getName()) || COMPANY_REPO.isCompanyExistsByEmail(company.getEmail())){
            throw new CompanyException();
        }
        else{
            COMPANY_REPO.save(company);
            //company.setId(COMPANY_REPO.findByNameLike(company.getName()).getId());
            System.out.println("company is successfully added id: " + company.getId() );

        }
    }


    /**
     * update company
     * @param company - Getting Company object.
     * @throws CompanyException - Thrown if there's a problem with the company object
     */
    @Override
    public void updateCompany(Company company, String token) throws CompanyException {
        if(COMPANY_REPO.existsById(company.getId())){
            if(COMPANY_REPO.isCompanyExistsByEmail(company.getEmail())){
                throw new CompanyException("Email has been taken");
            }
            COMPANY_REPO.updateCompany(company.getEmail(), company.getPassword(), company.getId());
            System.out.println("successfully updated company id: " + company.getId());
        }
        else{
            throw new CompanyException("Company does not exists!");
        }
    }

    /**
     * delete company
     * @param id - Getting an id of a company.
     * @throws CompanyException - Thrown if company does not exist
     */
    @Override
    public void deleteCompany(int id, String token) throws CompanyException {
        if(COMPANY_REPO.existsById(id)){
            COMPANY_REPO.deleteById(id);
            System.out.printf("Company %d is successfully deleted", id );
        }
        else{
            System.out.println();
            throw new CompanyException("Company does not exist");
        }
    }

    /**
     * get all companies
     * @return - all companies
     * @throws CompanyException - thrown if companies does not exists
     */
    @Override
    public List<Company> getAllCompanies(String token) throws CompanyException {
            List<Company> companies = COMPANY_REPO.findAll();
        if(companies.isEmpty()){
            throw new CompanyException("There are now companies exits!");
        }
        companies.forEach(c-> c.setPassword("{secret}"));
        return companies;
    }

    /**
     * company by id
     * @param id - Asking for an id of a company.
     * @return - object of company by id
     * @throws CompanyException - Thrown if company does not exist.
     */
    @Override
    public Company getCompanyByID(int id, String token) throws CompanyException {
        Company company = COMPANY_REPO.findOneCompany(id);
        if(company == null){
            throw new CompanyException("company does not exists");
        }
        company.setPassword("{secret}");
        return company;
    }

    /**
     * adding customer
     * @param customer - Getting a customer object to upload.
     * @throws CustomerException - thrown if customer already exists
     */
    @Override
    public void addCustomer(Customer customer, String token) throws CustomerException {
        if(CUSTOMER_REPO.isCustomerExists(customer.getEmail(), customer.getPassword())){
            throw new CustomerException("customer is already exists");
        }
        else if(CUSTOMER_REPO.findByEmailLike(customer.getEmail()) != null){
            throw new CustomerException("Email is already exists in the system!");
        }
        else{
            CUSTOMER_REPO.save(customer);
            System.out.println("Customer is successfully saved! id: " + customer.getId());
        }
    }

    /**
     * update customer
     * @param customer - Getting Customer object.
     * @throws CustomerException - thrown if there's a problem with customer object
     */
    @Override
    public void updateCustomer(Customer customer, String token) throws CustomerException {
        Optional<Customer> customerToUpdate = CUSTOMER_REPO.findById(customer.getId());
        if(customerToUpdate.isPresent()){
            if(customer.getFirstName().equals("")){
                customer.setFirstName(customerToUpdate.get().getFirstName());
            }
            if(customer.getLastName().equals("")){
                customer.setLastName(customerToUpdate.get().getLastName());
            }
            if(customer.getEmail().equals("")){
                customer.setEmail(customerToUpdate.get().getEmail());
            }
            if(customer.getPassword().equals("")){
                customer.setPassword(customerToUpdate.get().getPassword());
            }



            CUSTOMER_REPO.updateCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getPassword(), customer.getId());
            System.out.println("customer is successfully updated id: " + customer.getId());
        }
        else{
            throw new CustomerException("customer does not exists!");
        }
    }

    /**
     * delete a customer
     * @param id - Getting customer ID.
     * @throws CustomerException - Thrown if customer does not exist.
     */
    @Override
    public void deleteCustomer(int id, String token) throws CustomerException {
        if(CUSTOMER_REPO.existsById(id)){
            CUSTOMER_REPO.deleteCustomerCoupons(id);
            CUSTOMER_REPO.deleteById(id);
            System.out.println("successfully deleted customer : " + id);
        }
        else{
            throw new CustomerException("customer does not exist");
        }
    }

    /**
     * get all customers
     * @return - list of all customer
     * @throws CustomerException - thrown if there are no customers
     */
    @Override
    public List<Customer> getAllCustomer(String token) throws CustomerException {
        List<Customer> customer = CUSTOMER_REPO.findAll();
        if(customer.isEmpty()){
            throw new CustomerException("there are no customers!");
        }
        customer.forEach(c-> c.setPassword("{secret}"));
        return CUSTOMER_REPO.findAll();
    }

    /**
     * Get one customer by id
     * @param id - Getting customer id
     * @return - customer object by id
     * @throws CustomerException - thrown if no such customer
     */
    @Override
    public Customer findCustomerById(int id, String token) throws CustomerException {
        Optional<Customer> customer = CUSTOMER_REPO.findById(id);
        if(customer.isEmpty()){
            throw new CustomerException("customer does not exists");
        }
        else{
            customer.get().setPassword("{secret}");
            return customer.get();
        }
    }

    /**
     * fina all available coupons
     * @return - all available coupons
     */
    @Override
    public List<Coupon> findAllAvailableCoupons() {
        return COUPON_REPO.getAllAvailableCoupons(Date.valueOf(LocalDate.now()));
    }

    /**
     * log out
     */
    @Override
    public void logOut(){
        isLogged = false;
        token = null;
    }


    /**
     * getting toke
     * @return - string which contains token
     */
    public String getToken() {
        return token;
    }

    /**
     * is logged?
     * @return - return true if logged
     */
    public boolean isLogged() {
        if(isLogged){
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ Roles.ADMIN.name()));
            User userDetails = new User("admin@admin.com", "admin", authorities);

            token = JWT.create().withSubject(userDetails.getUsername())
                    .withClaim("id", 1)
                    .withExpiresAt(new Date(System.currentTimeMillis() +  30 * 1000))
                    .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
        }
        return isLogged;
    }

    /**
     *
     * @return - string with email
     */
    public String getEMAIL() {
        return EMAIL;
    }

    /**
     *
     * @return string with password
     */
    public String getPASSWORD() {
        return PASSWORD;
    }
}
