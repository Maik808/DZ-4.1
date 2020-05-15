package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


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
    void shouldSendFormWithNonCorrectPhoneNumber(){
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


}


