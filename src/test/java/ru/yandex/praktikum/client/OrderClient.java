package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.Order;

import java.util.Map;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";
    private static final String CANCEL_PATH = "/api/v1/orders/cancel";
    private static final String TRACK_PATH = "/api/v1/orders/track";
    private static final String ACCEPT_PATH = "/api/v1/orders/accept/";

    @Step("Создать заказ")
    public ValidatableResponse create(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Отменить заказ с трек-номером {track}")
    public ValidatableResponse cancel(int track) {
        return spec()
                .body(Map.of("track", track))
                .when()
                .put(CANCEL_PATH)
                .then();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getList() {
        return spec()
                .when()
                .get(ORDERS_PATH)
                .then();
    }

    @Step("Получить заказ по трек-номеру {track}")
    public ValidatableResponse getByTrack(int track) {
        return spec()
                .queryParam("t", track)
                .when()
                .get(TRACK_PATH)
                .then();
    }

    @Step("Получить заказ без трек-номера")
    public ValidatableResponse getWithoutTrack() {
        return spec()
                .when()
                .get(TRACK_PATH)
                .then();
    }

    @Step("Принять заказ {orderId} курьером {courierId}")
    public ValidatableResponse accept(int orderId, int courierId) {
        return spec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_PATH + orderId)
                .then();
    }

    @Step("Принять заказ {orderId} без id курьера")
    public ValidatableResponse acceptWithoutCourier(int orderId) {
        return spec()
                .when()
                .put(ACCEPT_PATH + orderId)
                .then();
    }

    @Step("Принять заказ без номера заказа курьером {courierId}")
    public ValidatableResponse acceptWithoutOrder(int courierId) {
        return spec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_PATH)
                .then();
    }

    @Step("Получить id заказа по трек-номеру {track}")
    public int getOrderId(int track) {
        return getByTrack(track).extract().path("order.id");
    }
}
