package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tryingCoupons.tryingCoupon.beans.CategoryInjection;
import tryingCoupons.tryingCoupon.beans.Coupon;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    /**
     * Finding coupon that belongs to a specific customer
     * @param id Integer contains value of id
     * @return List of coupon that belongs to that customer
     */
    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ?",nativeQuery = true)
    List<Coupon> findCouponsBelongToCustomer(Integer id);

    /**
     * getting one coupon by id
     * @param id Integer contains value of id
     * @return - Coupon object
     */
    @Query(value = "SELECT * FROM coupons WHERE id = ?", nativeQuery = true)
    Coupon getOneCoupon(int id);

    /**
     * adding a coupon purchase
     * @param couponID - Integer contains value of couponID
     * @param customerID - Integer contains value of customerID
     */
    @Modifying
    @Query(value = "INSERT INTO customer_vs_coupons (coupon_id, customer_id) VALUES (?, ?)",nativeQuery = true)
    @Transactional
    void addCouponPurchase(int couponID, int customerID);

    /**
     * Check if company own s certain coupon
     * @param id - Integer contains value of id
     * @param title - String contains value of title
     * @return - true if owns.
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM coupons WHERE coupons.company_id = ? AND coupons.title = ?",nativeQuery = true)
    boolean isCompanyOwnTheCoupon(int id, String title);

    /**
     * Getting coupon by title and company id
     * @param title - String contains value of title
     * @param CompanyId - Integer contains value of companyID
     * @return - Coupon object
     */
    @Query(value = "SELECT coupons FROM Coupon coupons WHERE coupons.title = ?1 AND company_id = ?2")
    Coupon companyCoupon(String title, int CompanyId);

    /**
     * Coupon by category and company
     * @param companyId - Integer contains value of companyID
     * @param category - Integer contains value of companyID
     * @return - List of coupons.
     */
    @Query(value = "SELECT * FROM coupons WHERE company_id = ? AND category_id= ?", nativeQuery = true)
    List<Coupon> getCouponsByCompanyAndCategory(int companyId, int category);

    /**
     * Coupons by max price
     * @param companyID - Integer contains value of companyID
     * @param price -Integer contains value of price
     * @return List of coupons with max price
     */
    @Query(value = "SELECT * FROM coupons WHERE company_id= ? AND price < ?",nativeQuery = true)
    List<Coupon> getCouponsByMaxedPrice(int companyID, int price);

    /**
     * all company coupons
     * @param companyId - Integer contains value of companyID
     * @return - List of coupons that belongs to the company.
     */
    @Query(value = "SELECT c FROM Coupon c WHERE company_id =?1")
    List<Coupon> getAllCompanyCoupons(int companyId);

    /**
     * Check if customer own coupon
     * @param couponId -Integer contains value of couponID
     * @param CustomerID - Integer contains value of customerID
     * @return true if own
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM customer_vs_coupons WHERE customer_vs_coupons.coupon_id = ? AND customer_id = ?",nativeQuery = true)
    boolean isCustomerOwnCoupon(int couponId, int CustomerID);

    /**
     * coupons by category and customer
     * @param customerID Integer contains value of customerId
     * @param categoryID - Integer contains value of CategoryID
     * @return - List of coupons by those 2 attributes
     */
    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ? AND coupons.category_id = ?",nativeQuery = true)
    List<Coupon> couponsByCategoryAndCustomer(int customerID, int categoryID);

    /**
     * Coupons by max price
     * @param customerID - Integer contains value of customerID
     * @param maxPrice - Integer contains value of maxPrice
     * @return - List of coupons with max price and customer ID
     */
    @Query(value = "SELECT * FROM customer_vs_coupons INNER JOIN coupons\n" +
            "on coupons.id = customer_vs_coupons.coupon_id where\n" +
            "customer_vs_coupons.customer_id = ? AND coupons.price < ?",nativeQuery = true)
    List<Coupon> customerCouponsMaxPrice(int customerID, int maxPrice);

    /**
     * Delete coupons
     * @param expiredDate - Local date of today
     */
    @Modifying
    @Query(value = "DELETE FROM coupons WHERE end_date <= ?",nativeQuery = true)
    @Transactional
    void deleteCouponsByDate(Date expiredDate);

    /**
     * Get all Available coupons
     * @param expired local date of today
     * @return List of all coupons that are available
     */
    @Query(value = "SELECT * FROM coupons WHERE end_date > ? AND amount > 0", nativeQuery = true)
    List<Coupon> getAllAvailableCoupons(Date expired);

/*
    shirelle - i love you :3
    will you marry me?
    hoang- yes i will
    shirelle - omg!!! im so happy!

 */



}
