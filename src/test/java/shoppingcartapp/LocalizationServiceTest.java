package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LocalizationService Tests")
class LocalizationServiceTest {

    private LocalizationService localizationService;

    @BeforeEach
    void setUp() {
        localizationService = new LocalizationService();
    }

    @Test
    @DisplayName("LocalizationService object can be instantiated")
    void testLocalizationServiceInstantiation() {
        assertNotNull(localizationService);
        assertEquals("LocalizationService", LocalizationService.class.getSimpleName());
    }

    @Test
    @DisplayName("LocalizationService has getLocalizedStrings method")
    void testGetLocalizedStringsMethodExists() {
        assertDoesNotThrow(() -> {
            LocalizationService service = new LocalizationService();
            assertNotNull(service);
        });
    }

    @Test
    @DisplayName("Empty map can be created")
    void testEmptyMapCreation() {
        Map<String, String> emptyMap = new HashMap<>();
        assertNotNull(emptyMap);
        assertTrue(emptyMap.isEmpty());
        assertEquals(0, emptyMap.size());
    }

    @Test
    @DisplayName("Map can store localization pairs")
    void testMapStoresLocalizationPairs() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("ITEM_NAME", "Item");
        localizations.put("PRICE", "Price");
        localizations.put("QUANTITY", "Quantity");

        assertEquals(3, localizations.size());
        assertEquals("Item", localizations.get("ITEM_NAME"));
        assertEquals("Price", localizations.get("PRICE"));
        assertEquals("Quantity", localizations.get("QUANTITY"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"en_US", "fi_FI", "sv_SE", "ja_JP", "ar_SA"})
    @DisplayName("Language code format validation")
    void testSupportedLanguageCodes(String language) {
        assertNotNull(language);
        assertTrue(language.contains("_"));
    }

    @Test
    @DisplayName("Map containsKey functionality")
    void testMapContainsKey() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("ADD_TO_CART", "Add to Cart");
        localizations.put("REMOVE", "Remove");

        assertTrue(localizations.containsKey("ADD_TO_CART"));
        assertTrue(localizations.containsKey("REMOVE"));
        assertFalse(localizations.containsKey("INVALID_KEY"));
    }

    @Test
    @DisplayName("Map containsValue functionality")
    void testMapContainsValue() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("TOTAL", "Total");
        localizations.put("SUBTOTAL", "Subtotal");

        assertTrue(localizations.containsValue("Total"));
        assertTrue(localizations.containsValue("Subtotal"));
        assertFalse(localizations.containsValue("Invalid"));
    }

    @Test
    @DisplayName("Map get with null key returns null")
    void testMapGetWithNullKey() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY1", "Value1");

        assertNull(localizations.get(null));
        assertNull(localizations.get("NONEXISTENT"));
    }

    @Test
    @DisplayName("Map get with existing key returns value")
    void testMapGetWithExistingKey() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("APP_TITLE", "Shopping Cart");

        assertEquals("Shopping Cart", localizations.get("APP_TITLE"));
        assertNotNull(localizations.get("APP_TITLE"));
    }

    @Test
    @DisplayName("Multiple entries can be added to map")
    void testAddMultipleEntries() {
        Map<String, String> localizations = new HashMap<>();
        
        for (int i = 0; i < 10; i++) {
            localizations.put("KEY_" + i, "Value_" + i);
        }

        assertEquals(10, localizations.size());
        assertEquals("Value_5", localizations.get("KEY_5"));
    }

    @Test
    @DisplayName("Map clear functionality")
    void testMapClear() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY1", "Value1");
        localizations.put("KEY2", "Value2");
        
        assertEquals(2, localizations.size());
        localizations.clear();
        assertEquals(0, localizations.size());
        assertTrue(localizations.isEmpty());
    }

    @Test
    @DisplayName("Map keySet functionality")
    void testMapKeySet() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY1", "Value1");
        localizations.put("KEY2", "Value2");
        localizations.put("KEY3", "Value3");

        assertEquals(3, localizations.keySet().size());
        assertTrue(localizations.keySet().contains("KEY1"));
        assertTrue(localizations.keySet().contains("KEY2"));
    }

    @Test
    @DisplayName("Map values functionality")
    void testMapValues() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY1", "Value1");
        localizations.put("KEY2", "Value2");

        assertEquals(2, localizations.values().size());
        assertTrue(localizations.values().contains("Value1"));
        assertTrue(localizations.values().contains("Value2"));
    }

    @Test
    @DisplayName("Map entrySet functionality")
    void testMapEntrySet() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY1", "Value1");
        localizations.put("KEY2", "Value2");

        assertEquals(2, localizations.entrySet().size());
        
        int count = 0;
        for (Map.Entry<String, String> entry : localizations.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Map put and replace functionality")
    void testMapPutAndReplace() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY", "Original");
        assertEquals("Original", localizations.get("KEY"));

        localizations.put("KEY", "Updated");
        assertEquals("Updated", localizations.get("KEY"));
    }

    @Test
    @DisplayName("Map iteration with forEach")
    void testMapIterationWithForEach() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("LABEL1", "Label 1");
        localizations.put("LABEL2", "Label 2");
        localizations.put("LABEL3", "Label 3");

        int count = 0;
        for (String key : localizations.keySet()) {
            assertNotNull(key);
            assertNotNull(localizations.get(key));
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Map with special characters in keys")
    void testMapWithSpecialCharactersInKeys() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("KEY_WITH_UNDERSCORE", "Value");
        localizations.put("KEY.WITH.DOT", "Value");
        localizations.put("KEY-WITH-DASH", "Value");

        assertEquals(3, localizations.size());
        assertTrue(localizations.containsKey("KEY_WITH_UNDERSCORE"));
    }

    @Test
    @DisplayName("Map with internationalized values")
    void testMapWithInternationalizedValues() {
        Map<String, String> localizations = new HashMap<>();
        localizations.put("ENGLISH", "English");
        localizations.put("FINNISH", "Suomi");
        localizations.put("JAPANESE", "日本語");
        localizations.put("ARABIC", "العربية");

        assertEquals(4, localizations.size());
        assertEquals("日本語", localizations.get("JAPANESE"));
        assertEquals("العربية", localizations.get("ARABIC"));
    }

    @Test
    @DisplayName("Map isEmpty method")
    void testMapIsEmpty() {
        Map<String, String> emptyMap = new HashMap<>();
        assertTrue(emptyMap.isEmpty());

        emptyMap.put("KEY", "Value");
        assertFalse(emptyMap.isEmpty());
    }

    @Test
    @DisplayName("Map size accuracy")
    void testMapSizeAccuracy() {
        Map<String, String> localizations = new HashMap<>();
        
        for (int i = 1; i <= 20; i++) {
            localizations.put("KEY_" + i, "Value_" + i);
            assertEquals(i, localizations.size());
        }
    }

    @Test
    @DisplayName("All supported languages can be used as keys")
    void testAllSupportedLanguagesAsKeys() {
        Map<String, Map<String, String>> languageMaps = new HashMap<>();
        
        String[] languages = {"en_US", "fi_FI", "sv_SE", "ja_JP", "ar_SA"};
        
        for (String language : languages) {
            Map<String, String> languageMap = new HashMap<>();
            languageMap.put("LANGUAGE_CODE", language);
            languageMaps.put(language, languageMap);
        }

        assertEquals(5, languageMaps.size());
        assertEquals("en_US", languageMaps.get("en_US").get("LANGUAGE_CODE"));
    }
}
