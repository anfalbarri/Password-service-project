package passwordservices;


import java.util.Scanner;

public class PasswordServices {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("\tWELCOM TO OUR PASSWORD SERVICES PROJECT");
        int choose;
        double length;
        do {
            System.out.println("--------------------------------------------------------------");
            System.out.println("To genrate password , please enter 1.");
            System.out.println("To check the strength of your password, please enter 2.");
            System.out.println("To genrate password and check their strength , please enter 3.");
            System.out.println("To exit the program , please enter 0.");

            choose = input.nextInt();

            switch (choose) {
                case 1:
                    System.out.println("Enter the password length: ");
                    length = input.nextDouble();

                    while ((length % 1 != 0) || (length <= 0)) { //to chick the length is not negative number or double
                        System.out.println("Password length should be a positive integer");
                        length = input.nextDouble();
                    }//end while

                    String[] password = generatePassword(length);
                    printPassword(password);

                    break;
                //--------------------------------------------------------------------
                case 2:
                    System.out.println("Enter your password: ");
                    String pass = input.next();

                    printStrength(checkStrength(pass));
                    break;
                //--------------------------------------------------------------------
                case 3:
                    System.out.println("Enter the password length: ");
                    length = input.nextDouble();

                    while ((length % 1 != 0) || (length <= 0)) { //to chick the length is not negative number or double
                        System.out.println("Password length should be a positive integer");
                        length = input.nextDouble();
                    }//end while

                    String[] arrand = generatePassword(length);
                    int[] arr = new int[3];
                    for (int i = 0; i < 3; i++) {
                        arr[i] = checkStrength(arrand[i]);
                    }//end for

                    printPasswords(arrand, arr);

                    break;
                //--------------------------------------------------------------------
                case 0:
                    System.out.println("Massege: program ended ");
                    break;
                //---------------------------------------------------------------------
                default:
                    System.out.println("Error: Invalid entry");
            }//end switch

        }//end do
        while (choose != 0);

    }//end main   

    /**
     * method generatePasswords uses the Math.random() method to randomly
     * generate three passwords of the chosen length
     *
     * @param len
     * @return
     */

    public static String [] generatePassword(double len) {//this method generate the password according to the length that user entered
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";    //upper means uppercase letter
        String lower = "abcdefghijklmnopqrstuvwxyz";    //lower means lowercase letter
        String num = "0123456789";                      //num means number
        String symp = "+-_)(*&^%$#@!";                  //symb means symbol
        String ch = upper + lower + num + symp;         //ch combine all variables into one variable

        String[] password = new String[3];
        String pass = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < len; j++) {/*This for loop is used to grnerate the password
                                              according to the length entered by the user*/
                pass += ch.charAt((int) (Math.random() * ch.length()));
            }
            password[i] = pass;
            pass = "";
        }
        return password;

    }//end generatePassword

    /**
     * method printPasswords displays the content of the array of strings
     *
     * @param password: String array
     */

    public static void printPassword(String[] password) {
        System.out.println("Here is a few options:");

        for (int i = 0; i < password.length; i++) {
            System.out.println(password[i]);
        }//end for

    }//end  printPassword

    /**
     * method checkStrength calculates and returns the score of the given
     * password
     *
     * @param s
     * @return
     */

    public static int checkStrength(String s) {
        boolean upper = false;                 //upper means uppercase letter
        boolean symb = false;                  //symb means symbol
        boolean num = false;                   //num means number
        boolean lower = false;                 //lower means lowercase letter

        int score = 0; //initial value
        for (int i = 0; i < s.length(); i++) {

            char y = s.charAt(i);
            if (Character.isUpperCase(y)) {
                upper = true;
            } else if (Character.isLowerCase(y)) {
                lower = true;
            } else if (Character.isDigit(y)) {
                num = true;
            } else {
                symb = true;
            }
        }//end for

        if (upper) {
            score++;
        }
        if (lower) {
            score++;
        }
        if (num) {
            score++;
        }
        if (symb) {
            score++;
        }

        if (s.length() >= 8) {//because the length must be greater than 8
            score++;
        }

        return score;
    }//end checkStrength

    /**
     * method printStrength prints the corresponding strength to the given score
     *
     * @param score
     */
    public static void printStrength(int score) { //Method selects the sentence according to the score
        if (score == 5) {
            System.out.println("this is a very good password !");
        } else if (score == 4) {
            System.out.println("this is a good password ,but you can still do better");
        } else if (score == 3) {
            System.out.println("this is a medium password ,try making it better");
        } else {
            System.out.println("this is a weak password ,you should find a new one !");
        }
    }//end printStrength

    /**
     * method printPasswords displays the content of the array of strings
     *
     * @param passwords: String array
     * @param score
     */

    public static void printPasswords(String[] passwords, int[] score) {
        System.out.println("Here a few options:");

        for (int i = 0; i < passwords.length; i++) {
            System.out.print(passwords[i] + "  ");
            printStrength(score[i]);
        }
    }//end printPasswords

}//end class

