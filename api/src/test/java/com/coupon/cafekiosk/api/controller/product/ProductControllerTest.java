package com.coupon.cafekiosk.api.controller.product;


import com.coupon.cafekiosk.api.service.product.ProductService;
import com.coupon.cafekiosk.api.service.product.response.ProductResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ProductControllerTest {
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("product.http::판매할 수 있는 상품 조회")
    void getSellingProducts() throws IOException {
        // when
        List<ProductResponse> productResponses = productService.getSellingProducts();
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(productResponses.toString()));
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/api/v1/products/selling").toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException("API 호출 실패", e);
        }
        String responseBody = response.body();

        String expect = "[ProductResponse{id=1, productNumber='001', type=HANDMADE, sellingStatus=SELLING, name='아메리카노', price=4000}, " +
                        "ProductResponse{id=2, productNumber='002', type=HANDMADE, sellingStatus=HOLD, name='카페라떼', price=4500}]";

        // then
        assertThat(expect).isEqualTo(responseBody);

        mockWebServer.shutdown();
    }
}