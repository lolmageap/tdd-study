package sample.cafekiosk.spring.docs.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.domain.product.request.ProductCreateServiceRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerDocsTest extends RestDocsSupport {

    private final ProductService productService = Mockito.mock(ProductService.class);

    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @Test
    @DisplayName("신규 상품을 등록하는 api")
    void testCreateProduct() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("아메리카노")
                .price(5000)
                .build();

        BDDMockito.given(productService.createProduct(any(ProductCreateServiceRequest.class)))
                        .willReturn(ProductResponse.builder()
                                .id(1L)
                                .productNumber("001")
                                .type(ProductType.HANDMADE)
                                .sellingStatus(SellingStatus.SELLING)
                                .name("아메리카노")
                                .price(5000)
                                .build()
                        );

        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("상품의 타입"),
                                fieldWithPath("sellingStatus").type(JsonFieldType.STRING)
                                        .description("상품 판매상태"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("상품 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("상품 ID"),
                                fieldWithPath("data.productNumber").type(JsonFieldType.STRING)
                                        .description("상품 번호"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING)
                                        .description("상품의 타입"),
                                fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING)
                                        .description("상품 판매상태"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격")
                        )
                ));
    }

}
