package tryingCoupons.tryingCoupon.clr;


import org.springframework.beans.factory.annotation.Autowired;
import tryingCoupons.tryingCoupon.beans.Customer;
import tryingCoupons.tryingCoupon.services.AdminService;
import tryingCoupons.tryingCoupon.services.AdminServicesService;

import java.util.*;

public class RestTest {
//        double decimalUse = 10;
//        int numbers = 9;
//        double someNumber = (double)((int)(Math.random()*10));
//        System.out.println(someNumber);
//
//        for(int counter =0 ; counter <10; counter ++){
//            double addingNumber = numbers /decimalUse;
//            System.out.println(addingNumber);
//            numbers--;
//            decimalUse*=10;
//            someNumber+=addingNumber;
//            if(numbers == 0 ){
//                String theNumber = someNumber + "0";
//                someNumber = Double.parseDouble(theNumber);
//            }
//        }
//        System.out.println(someNumber);


    // Display a message, preceded by
    // the name of the current thread
//        static class Friend {
//            private final String name;
//
//            public Friend(String name) {
//                this.name = name;
//            }
//
//            public String getName() {
//                return this.name;
//            }
//
//            public synchronized void bow(Friend bower) {
//                System.out.format("%s: %s"
//                                + "  has bowed to me!%n",
//                        this.name, bower.getName());
//
//                bower.bowBack(this);
//            }
//
//            public  void bowBack(Friend bower) {
//                System.out.format("%s: %s"
//                                + " has bowed back to me!%n",
//                        this.name, bower.getName());
//            }
//        }
//
//        public static void main(String[] args) {
//            final Friend alphonse =
//                    new Friend("Alphonse");
//            final Friend gaston =
//                    new Friend("Gaston");
//            new Thread(new Runnable() {
//                public void run() {
//                    alphonse.bow(gaston);
//                }
//            }).start();
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    gaston.bow(alphonse);
//                }
//            }).start();

    public void hello(){
        System.out.println("hello");
    }


    public static void main(String[] args) {
           byte x = 64;
           int i;
           byte y;
           i= x << 4;
        System.out.println(i);
        Stack<String> stack=  new Stack<String>();
        Set<String> set = new HashSet<String>();
        Map<String,String> lol = new HashMap<>();
        set.add("lol");
        set.add("lol");
        set.add("check on me");
        System.out.println(set);


    }

    }








