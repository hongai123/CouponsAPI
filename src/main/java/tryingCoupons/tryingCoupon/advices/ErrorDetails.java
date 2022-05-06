package tryingCoupons.tryingCoupon.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * @param error - error name.
 * @param description - describe error.
 */
public class ErrorDetails {
    private String error;
    private String description;
}
