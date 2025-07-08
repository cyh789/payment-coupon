package com.coupon.cafekiosk.api.controller.order;


import com.coupon.cafekiosk.api.service.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("order.http::주문 신규 생성")
    void createOrder() throws Exception {
        // Given
        String requestBody = """
            {
              "type": [
                "HANDMADE"
              ], "productNumbers": [
                "001"
              ], "price": [
                "4000"
              ]
            }
            """;

        // When & Then
        mockMvc.perform(post("/api/v1/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalPrice").value("4000"))
                .andExpect(jsonPath("$.products[0].type").value("HANDMADE"))
                .andDo(print());
        //Body = {"id":1,"totalPrice":4000,"registeredDateTime":"2025-07-09T00:22:53.681786","products":[{"id":1,"productNumber":"001","type":"HANDMADE","sellingStatus":"SELLING","name":"아메리카노","price":4000}]}
    }
}