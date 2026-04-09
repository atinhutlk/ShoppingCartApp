package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("LanguageOption Tests")
class LanguageOptionTest {

    @Test
    @DisplayName("Stores code and display name correctly")
    void storesCodeAndDisplayName() {
        LanguageOption option = new LanguageOption("en_US", "English");

        assertEquals("en_US", option.getCode());
        assertEquals("English", option.toString());
    }

    @Test
    @DisplayName("Finnish language option")
    void testFinnishLanguageOption() {
        LanguageOption option = new LanguageOption("fi_FI", "Suomi");
        assertEquals("fi_FI", option.getCode());
        assertEquals("Suomi", option.toString());
    }

    @Test
    @DisplayName("Swedish language option")
    void testSwedishLanguageOption() {
        LanguageOption option = new LanguageOption("sv_SE", "Svenska");
        assertEquals("sv_SE", option.getCode());
        assertEquals("Svenska", option.toString());
    }

    @Test
    @DisplayName("Japanese language option")
    void testJapaneseLanguageOption() {
        LanguageOption option = new LanguageOption("ja_JP", "日本語");
        assertEquals("ja_JP", option.getCode());
        assertEquals("日本語", option.toString());
    }

    @Test
    @DisplayName("Arabic language option")
    void testArabicLanguageOption() {
        LanguageOption option = new LanguageOption("ar_SA", "العربية");
        assertEquals("ar_SA", option.getCode());
        assertEquals("العربية", option.toString());
    }

    @Test
    @DisplayName("Constructor creates non-null object")
    void testConstructorCreatesNonNullObject() {
        LanguageOption option = new LanguageOption("en_US", "English");
        assertNotNull(option);
    }

    @Test
    @DisplayName("Different language options are not equal")
    void testDifferentLanguageOptionsNotEqual() {
        LanguageOption option1 = new LanguageOption("en_US", "English");
        LanguageOption option2 = new LanguageOption("fi_FI", "Suomi");

        assertNotEquals(option1.getCode(), option2.getCode());
        assertNotEquals(option1.toString(), option2.toString());
    }

    @Test
    @DisplayName("Same language code with different display name")
    void testSameCodeDifferentName() {
        LanguageOption option1 = new LanguageOption("en_US", "English");
        LanguageOption option2 = new LanguageOption("en_US", "American English");

        assertEquals(option1.getCode(), option2.getCode());
        assertNotEquals(option1.toString(), option2.toString());
    }

    @Test
    @DisplayName("toString returns display name not code")
    void testToStringReturnsDisplayNameNotCode() {
        LanguageOption option = new LanguageOption("ja_JP", "Japanese");

        assertNotEquals(option.getCode(), option.toString());
        assertEquals("Japanese", option.toString());
    }

    @Test
    @DisplayName("Multiple calls to getCode return same value")
    void testMultipleCallsToGetCode() {
        LanguageOption option = new LanguageOption("sv_SE", "Swedish");

        assertEquals(option.getCode(), option.getCode());
        assertEquals("sv_SE", option.getCode());
    }

    @Test
    @DisplayName("Multiple calls to toString return same value")
    void testMultipleCallsToToString() {
        LanguageOption option = new LanguageOption("ar_SA", "Arabic");

        assertEquals(option.toString(), option.toString());
        assertEquals("Arabic", option.toString());
    }

    @Test
    @DisplayName("Language option with empty strings")
    void testLanguageOptionWithEmptyStrings() {
        LanguageOption option = new LanguageOption("", "");
        assertEquals("", option.getCode());
        assertEquals("", option.toString());
    }

    @Test
    @DisplayName("Language option with special characters")
    void testLanguageOptionWithSpecialCharacters() {
        LanguageOption option = new LanguageOption("pt_BR", "Português (Brasil)");
        assertEquals("pt_BR", option.getCode());
        assertEquals("Português (Brasil)", option.toString());
    }

    @Test
    @DisplayName("Multiple instances are independent")
    void testMultipleInstancesAreIndependent() {
        LanguageOption option1 = new LanguageOption("en_US", "English");
        LanguageOption option2 = new LanguageOption("fi_FI", "Suomi");
        LanguageOption option3 = new LanguageOption("ja_JP", "日本語");

        assertEquals("en_US", option1.getCode());
        assertEquals("fi_FI", option2.getCode());
        assertEquals("ja_JP", option3.getCode());

        assertEquals("English", option1.toString());
        assertEquals("Suomi", option2.toString());
        assertEquals("日本語", option3.toString());
    }

    @Test
    @DisplayName("LanguageOption with null code")
    void testLanguageOptionWithNullCode() {
        LanguageOption option = new LanguageOption(null, "English");
        assertNull(option.getCode());
    }

    @Test
    @DisplayName("LanguageOption with null display name")
    void testLanguageOptionWithNullDisplayName() {
        LanguageOption option = new LanguageOption("en_US", null);
        assertNull(option.toString());
    }

    @Test
    @DisplayName("LanguageOption with both null values")
    void testLanguageOptionWithBothNull() {
        LanguageOption option = new LanguageOption(null, null);
        assertNull(option.getCode());
        assertNull(option.toString());
    }

    @Test
    @DisplayName("LanguageOption with very long code and name")
    void testLanguageOptionWithVeryLongValues() {
        String longCode = "en_US_variant_v1_extended_code";
        String longName = "English (United States) - Extended with very long display name for testing purposes";
        
        LanguageOption option = new LanguageOption(longCode, longName);
        assertEquals(longCode, option.getCode());
        assertEquals(longName, option.toString());
    }

    @Test
    @DisplayName("LanguageOption code and display name are independent")
    void testCodeAndDisplayNameAreIndependent() {
        LanguageOption option1 = new LanguageOption("code1", "display1");
        LanguageOption option2 = new LanguageOption("code2", "display1");
        
        assertNotEquals(option1.getCode(), option2.getCode());
        assertEquals(option1.toString(), option2.toString());
    }

    @Test
    @DisplayName("LanguageOption with numeric values in strings")
    void testLanguageOptionWithNumericValues() {
        LanguageOption option = new LanguageOption("12345", "123456789");
        assertEquals("12345", option.getCode());
        assertEquals("123456789", option.toString());
    }

    @Test
    @DisplayName("LanguageOption with special Unicode characters")
    void testLanguageOptionWithUnicodeCharacters() {
        LanguageOption option = new LanguageOption("zh_CN", "中文 (中国)");
        assertEquals("zh_CN", option.getCode());
        assertEquals("中文 (中国)", option.toString());
    }

    @Test
    @DisplayName("LanguageOption getter consistency over multiple calls")
    void testGetterConsistencyOverMultipleCalls() {
        LanguageOption option = new LanguageOption("de_DE", "Deutsch");
        
        String code1 = option.getCode();
        String code2 = option.getCode();
        String code3 = option.getCode();
        
        assertEquals(code1, code2);
        assertEquals(code2, code3);
    }

    @Test
    @DisplayName("LanguageOption toString consistency over multiple calls")
    void testToStringConsistencyOverMultipleCalls() {
        LanguageOption option = new LanguageOption("it_IT", "Italiano");
        
        String display1 = option.toString();
        String display2 = option.toString();
        String display3 = option.toString();
        
        assertEquals(display1, display2);
        assertEquals(display2, display3);
    }

    @Test
    @DisplayName("LanguageOption with whitespace in code")
    void testLanguageOptionWithWhitespaceInCode() {
        LanguageOption option = new LanguageOption("en US", "English");
        assertEquals("en US", option.getCode());
    }

    @Test
    @DisplayName("LanguageOption with whitespace in display name")
    void testLanguageOptionWithWhitespaceInDisplayName() {
        LanguageOption option = new LanguageOption("en_US", "  English  ");
        assertEquals("  English  ", option.toString());
    }

    @Test
    @DisplayName("All supported languages can be instantiated")
    void testAllSupportedLanguagesInstantiation() {
        String[][] supportedLanguages = {
            {"en_US", "English"},
            {"fi_FI", "Suomi"},
            {"sv_SE", "Svenska"},
            {"ja_JP", "日本語"},
            {"ar_SA", "العربية"}
        };
        
        for (String[] lang : supportedLanguages) {
            LanguageOption option = new LanguageOption(lang[0], lang[1]);
            assertNotNull(option);
            assertEquals(lang[0], option.getCode());
            assertEquals(lang[1], option.toString());
        }
    }

    @Test
    @DisplayName("LanguageOption array operations")
    void testLanguageOptionArrayOperations() {
        LanguageOption[] options = new LanguageOption[5];
        options[0] = new LanguageOption("en_US", "English");
        options[1] = new LanguageOption("fi_FI", "Suomi");
        options[2] = new LanguageOption("sv_SE", "Svenska");
        options[3] = new LanguageOption("ja_JP", "日本語");
        options[4] = new LanguageOption("ar_SA", "العربية");
        
        assertEquals(5, options.length);
        assertEquals("en_US", options[0].getCode());
        assertEquals("العربية", options[4].toString());
    }

    @Test
    @DisplayName("LanguageOption preserves exact string values")
    void testLanguageOptionPreservesExactStringValues() {
        String code = "test_CODE_123";
        String display = "Test Display Name 123";
        
        LanguageOption option = new LanguageOption(code, display);
        
        assertEquals(code, option.getCode());
        assertEquals(display, option.toString());
        assertSame(code, option.getCode()); // Check object identity isn't necessary but getters work
    }

    @Test
    @DisplayName("LanguageOption with empty display name but non-empty code")
    void testLanguageOptionEmptyDisplayNameNonEmptyCode() {
        LanguageOption option = new LanguageOption("xx_XX", "");
        assertEquals("xx_XX", option.getCode());
        assertEquals("", option.toString());
    }

    @Test
    @DisplayName("LanguageOption toString returns display name not code")
    void testToStringMethodDefinition() {
        LanguageOption option = new LanguageOption("code123", "displayName456");
        
        String result = option.toString();
        assertEquals("displayName456", result);
        assertNotEquals("code123", result);
    }
}
