public class Main {
    public static boolean recursePalindrome(String str, int pL, int pR)
    {
        // exit condition (no non-match terminated the recurse as of yet)
        // must go first to avoid incorrect false condition
        if(pL >= pR)
            return true;

        // both converging pointers must match by-value
        if(str.charAt(pL) != str.charAt(pR))
            return false;

        return recursePalindrome(str, pL + 1, pR - 1);
    }

    public static void main(String[] args) {
        int leftptr = 0, rightptr = 0;

        String expectCase = "racecar";

        // set rptr var
        rightptr = expectCase.length() - 1;

        // should be true
        System.out.println(recursePalindrome(expectCase, leftptr, rightptr));

        String dontExpectCase = "Me? A Palindrome? Hah!";

        // reset pointer vars
        leftptr = 0;
        rightptr = dontExpectCase.length() - 1;

        // should be false
        System.out.println(recursePalindrome(dontExpectCase, leftptr, rightptr));
    }
}