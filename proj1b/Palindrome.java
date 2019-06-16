public class Palindrome {
    /**
     * Given a String, wordToDeque should return a Deque
     * where the characters appear in the same order as in the String.
     * @param word a String.
     * @return a deque from word.
     */
    public Deque<Character> wordToDeque(String word) {
//      LinkedListDeque<Character> deque = new LinkedListDeque<>();
        ArrayDeque<Character> deque = new ArrayDeque<>();
        for (int i = 0; i < word.length(); ++i) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    /**
     * @param word a String.
     * @return Return true if the given word is a palindrome, and false otherwise.
     */
    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            if (deque.removeLast() != deque.removeFirst()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param word a String.
     * @param cc a CharacterComparator.
     * @return Return true if the given word is a palindrome, and false otherwise.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            if (!cc.equalChars(deque.removeLast(), deque.removeFirst())) {
                return false;
            }
        }
        return true;
    }
}
