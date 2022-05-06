package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tryingCoupons.tryingCoupon.beans.Customer;

import javax.transaction.Transactional;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    //Customer findByFirstNameLike(String first_name);

    /**
     * Find customer by email
     * @param email - String contains value of email
     * @return - customer object by email
     */
    Customer findByEmailLike(String email);

    /**
     * check if customer exists
     * @param email -String contains value of email
     * @param password -String contains value of password
     * @return true if exists
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM customer WHERE BINARY customer.email = ? AND BINARY customer.password = ?", nativeQuery = true)
    boolean isCustomerExists(String email, String password);

    /**
     * getting customer by id
     * @param id - Integer contains value of id
     * @return  Customer object by id
     */
    @Query(value = "SELECT * FROM customer WHERE id = ?", nativeQuery = true)
    Customer getOneCustomer(int id);

    /**
     * delete customer coupons
     * @param customerId - Integer contains value of customerID
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM customer_vs_coupons WHERE customer_id = ?",nativeQuery = true)
    void deleteCustomerCoupons(int customerId);

    /**
     * update customer
     * @param email - String contains value of email
     * @param firstName - String contains value of first name
     * @param lastName - String contains value of last name
     * @param password - String contains value of password
     * @param id - Integer contains value of id
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE customer SET email = ?, first_name =? , last_name = ?, password = ? WHERE id = ?",nativeQuery = true)
    void updateCustomer(String email,String firstName, String lastName, String password, int id);


}
