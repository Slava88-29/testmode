package ru.netology.testmode.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static io.restassured.RestAssured.given;
import static ru.netology.testmode.data.DataGenerator.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;

class AuthTest {
    // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $(".form");
        form.$("[name=login]").setValue(registeredUser.login);
        form.$("[name=password]").setValue(registeredUser.password);
        form.$(".button").click();
        webdriver().shouldHave(url("http://localhost:9999/dashboard"));
        $x("//h2[contains(text(),'Личный кабинет')]").shouldBe(visible);

        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        SelenideElement form = $(".form");
        form.$("[name=login]").setValue(notRegisteredUser.login);
        form.$("[name=password]").setValue(notRegisteredUser.password);
        form.$(".button").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        SelenideElement form = $(".form");
        form.$("[name=login]").setValue(blockedUser.login);
        form.$("[name=password]").setValue(blockedUser.password);
        form.$(".button").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Пользователь заблокирован"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        SelenideElement form = $(".form");
        form.$("[name=login]").setValue(wrongLogin);
        form.$("[name=password]").setValue(registeredUser.password);
        form.$(".button").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        SelenideElement form = $(".form");
        form.$("[name=login]").setValue(registeredUser.login);
        form.$("[name=password]").setValue(wrongPassword);
        form.$(".button").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
    }
}
