package ru.yandex.praktikum.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    protected RequestSpecification spec() {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured());
    }
}
