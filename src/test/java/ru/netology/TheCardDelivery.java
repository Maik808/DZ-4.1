package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class TheCardDelivery {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendFormWithValidData() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=phone] input").setValue("+79038965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSendFormWithNonCorrectSurname() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Vasin234");
        $("[data-test-id=phone] input").setValue("+79038965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"name\"] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormWithNonCorrectPhoneNumber() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=phone] input").setValue("3790 38965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"phone\"] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSendFormWithNonCorrectCity() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Kostroma");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=phone] input").setValue("+79038965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"city\"] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendFormWithNonCorrectData() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").doubleClick().sendKeys("12122000");
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=phone] input").setValue("+79038965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"date\"] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithoutName() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=phone] input").setValue("+79038965656");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"name\"] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutNumber() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=\"phone\"] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        SelenideElement form = $(".form");
        $("[data-test-id=city] input").setValue("Кострома");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Василий Васин");
        $("[data-test-id=phone] input").setValue("+79038965656");
        $(".button").click();
        $(".input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldSubmitRequestWithDropDownList() {
        $("[data-test-id=city] input").setValue("Кос");
        $$(".menu-item").first().click();
        $("[data-test-id=date] input").setValue("");
        $(".input__icon").click();
        $$("td").find(exactText("10")).click();
        $("[name=name]").setValue("Василий Васин");
        $("[name=phone]").setValue("+79038965656");
        $(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }
}
