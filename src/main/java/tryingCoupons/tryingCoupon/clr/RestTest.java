package tryingCoupons.tryingCoupon.clr;



public class RestTest  {
    public static void main(String[] args) {
        double decimalUse = 10;
        int numbers = 9;
        double someNumber = (double)((int)(Math.random()*10));
        System.out.println(someNumber);

        for(int counter =0 ; counter <10; counter ++){
            double addingNumber = numbers /decimalUse;
            System.out.println(addingNumber);
            numbers--;
            decimalUse*=10;
            someNumber+=addingNumber;
            if(numbers == 0 ){
                String theNumber = someNumber + "0";
                someNumber = Double.parseDouble(theNumber);
            }
        }
        System.out.println(someNumber);
    }

}
