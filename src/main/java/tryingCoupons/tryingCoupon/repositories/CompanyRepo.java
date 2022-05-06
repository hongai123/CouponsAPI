package tryingCoupons.tryingCoupon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tryingCoupons.tryingCoupon.beans.Company;

import javax.transaction.Transactional;

@Repository

public interface CompanyRepo extends JpaRepository<Company,Integer> {
    /**
     * Find company by email
     * @param email - String contains value of email
     * @return - Company object.
     */
    Company findByEmailLike(String email);

    /**
     * Find company by email and password
     * @param email - String contains value of email
     * @param password -String contains value of password
     * @return - Company object.
     */
    @Query(value = "SELECT * FROM companies WHERE BINARY email = ? AND password = ?", nativeQuery = true)
    Company findByEmailLikeAndPassword(String email,String password);

    /**
     * Check if company exists
     * @param email  - String contains value of email
     * @param password -String contains value of password
     * @return - true if exists
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE BINARY companies.email = ? AND BINARY companies.password = ?", nativeQuery = true)
    boolean isCompanyExistsByEmailAndPassWord(String email,String password);

    /**
     * Find one company
     * @param id - Integer contains value of id
     * @return - Company object
     */
    @Query(value = "SELECT * FROM companies WHERE companies.id =?",nativeQuery = true)
    Company findOneCompany(int id);

    /**
     * Check if company exists by name.
     * @param name - String contains value of name
     * @return -true if exists
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE companies.name = ?" , nativeQuery = true)
    boolean isCompanyExistsByName(String name);

    /**
     * Check if company exists by email.
     * @param email -String contains value of email
     * @return true if exists.
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM companies WHERE companies.email = ?" , nativeQuery = true)
    boolean isCompanyExistsByEmail(String email);

    /**
     * update a company
     * @param email - String contains value of email
     * @param password - String contains value of password
     * @param id - String contains value of id
     */
    @Modifying
    @Query(value = "UPDATE companies SET email = ? , password = ? WHERE id = ?" ,nativeQuery = true)
    @Transactional
    void updateCompany(String email, String password,int id);
}
