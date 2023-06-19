package ru.jiehk;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VprokTests {
    @BeforeAll
    static void setUp() {
        Configuration.pageLoadTimeout=1000000;
    }

    @ValueSource(strings = {"Мясо, птица, колбасы", "Рыба, икра"})
    @ParameterizedTest(name = "Проверка перехода из бургер меню в раздел каталога {0}")
    void redirectToCategoryPageFromCatalogTest(String catalogCategoryName) {
        open("https://www.vprok.ru/");
        $(".Burger_burger__bNSA_").click();
        $$(".CatalogMenuLink_parentLink__5IG3T").find(text(catalogCategoryName)).click();
        $(".TitlePage_title__YjxvC").shouldHave(text(catalogCategoryName));
    }

    @CsvSource(value = {
            "Доставка по Санкт-Петербургу, Доставка продуктов на дом в Санкт-Петербурге и Ленинградской области",
            "Доставка по Москве и МО, Условия доставки по Москве и Московской области"
    })
    @ParameterizedTest(name = "Проверка перехода на инфостраницу {0}")
    void redirectToDeliveryInfoPage(String mainBannerPromoName, String promoPageTitle) {
        open("https://www.vprok.ru/info");
        $$(".Navigation_item__uFKMP").find(text(mainBannerPromoName)).click();
        $(".UiPageInfoPost_html__7rpWl").shouldHave(text(promoPageTitle));

    }

    static Stream<Arguments> catalogSubCategoryListTest() {
        return Stream.of(
                Arguments.of("Мясо, птица, колбасы", List.of("Наше производство", "Мясо", "Птица", "Стейки",
                        "Деликатесы и колбасные изделия", "Печень, сердце, желудок, субпродукты")),
                Arguments.of("Чипсы, снеки, орехи", List.of("Чипсы", "Lay's", "Овощные, фруктовые и злаковые снеки",
                        "Попкорн", "Кукурузные снеки", "Семечки, сухофрукты", "Сухарики", "Орехи"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Проверка отображения в каталоге подкатегорий для категории {0}")
    void catalogSubCategoryListTest(String catalogCategoryName, List<String> subCategoriesNames) {
        open("https://www.vprok.ru/");
        $(".Burger_burger__bNSA_").click();
        $$(".CatalogMenuLink_parentLink__5IG3T").find(text(catalogCategoryName)).hover();
        $$(".CategoryList_headerLink__8BIfP").filter(visible).shouldHave(CollectionCondition.texts(subCategoriesNames));
    }
}
