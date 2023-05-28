package sample.cafekiosk.spring.api.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("신규 상품을 생성한다.")
    void testCreateProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(5000)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    void testCreateProductWithOutType() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(5000)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
    void testCreateProductWithOutSellingStatus() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(5000)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품명은 필수값이다.")
    void testCreateProductWithOutName() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .price(5000)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품명은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 가격은 필수값이다.")
    void testCreateProductWithNoPrice() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 0보다 커야합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("판매 상품을 조회한다.")
    void testGetSellingProducts() throws Exception {
        //given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when //then
        mockMvc.perform(
                get("/api/v1/products/selling")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

}