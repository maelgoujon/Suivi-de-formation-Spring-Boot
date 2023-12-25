package com.webapp.ytb.webappytp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.webapp.ytb.webappytp.modele.palindrome;

@SpringBootTest
class WebappytpApplicationTests {

	@Test
	public void whenEmptyString_thenAccept() {
		palindrome palindromeTester = new palindrome();
		assertTrue(palindromeTester.isPalindrome(""));
	}

	@Test
	public void whenPalindrom_thenAccept() {
		palindrome palindromeTester = new palindrome();
		assertTrue(palindromeTester.isPalindrome("noon"));
	}

}
