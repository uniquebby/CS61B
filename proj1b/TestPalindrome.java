import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        /* test the first function of isPalindrome.*/
        String a = "a";
        String b = "racecar";
        String c = "horse";
        String d = "";

        assertTrue(palindrome.isPalindrome(a));
        assertTrue(palindrome.isPalindrome(b));
        assertFalse(palindrome.isPalindrome(c));
        assertTrue(palindrome.isPalindrome(d));

        /* test the second function of isPalindrome.*/
        CharacterComparator cc = new OffByOne();
        CharacterComparator cc2 = new OffByN(1);
        b = "flake";
        c = "yangbinbin";
        /* test with offByOne */
        assertTrue(palindrome.isPalindrome(a, cc));
        assertTrue(palindrome.isPalindrome(b, cc));
        assertFalse(palindrome.isPalindrome(c, cc));
        assertTrue(palindrome.isPalindrome(d, cc));

        /* test with offByN */
        assertTrue(palindrome.isPalindrome(a, cc2));
        assertTrue(palindrome.isPalindrome(b, cc2));
        assertFalse(palindrome.isPalindrome(c, cc2));
        assertTrue(palindrome.isPalindrome(d, cc2));

    }


}