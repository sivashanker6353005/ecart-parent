package com.ecart.order_service;

import com.ecart.order_service.dto.InventoryResponseDto;
import com.ecart.order_service.dto.Orderrequest;
import com.ecart.order_service.dto.orderLineItemsDto;
import com.ecart.order_service.entity.OrderLineItems;
import com.ecart.order_service.entity.Orders;
import com.ecart.order_service.repository.OrderRepository;
import com.ecart.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Orderrequest createSampleOrderRequest() {
        orderLineItemsDto item = new orderLineItemsDto();
        item.setId(1L);
        item.setSkuCode("sku123");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(2);

        Orderrequest request = new Orderrequest();
        request.setOrderLineItemsdto(List.of(item));
        return request;
    }

    @Test
    void testPlaceOrder_allItemsInStock_shouldSaveOrder() {
        Orderrequest orderRequest = createSampleOrderRequest();
        InventoryResponseDto[] inventoryResponse = {new InventoryResponseDto("sku123", true)};

        when(webClientBuilder.build()).thenReturn(webClient);
        //when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponseDto[].class)).thenReturn(Mono.just(inventoryResponse));

        orderService.placeOrder(orderRequest);

        verify(orderRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void testPlaceOrder_itemsOutOfStock_shouldThrowException() {
        Orderrequest orderRequest = createSampleOrderRequest();
        InventoryResponseDto[] inventoryResponse = {new InventoryResponseDto("sku123", false)};

        when(webClientBuilder.build()).thenReturn(webClient);
        //when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponseDto[].class)).thenReturn(Mono.just(inventoryResponse));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        assertEquals("Orders cannot be placed as items are not in stock", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testPlaceOrder_nullInventoryResponse_shouldThrowException() {
        Orderrequest orderRequest = createSampleOrderRequest();

        when(webClientBuilder.build()).thenReturn(webClient);
        //when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponseDto[].class)).thenReturn(Mono.justOrEmpty(null));

        assertThrows(NullPointerException.class, () -> orderService.placeOrder(orderRequest));
    }

    @Test
    void testMapToDto_validInput_shouldReturnCorrectEntity() {
        orderLineItemsDto dto = new orderLineItemsDto();
        dto.setId(10L);
        dto.setSkuCode("abc-123");
        dto.setPrice(BigDecimal.TEN);
        dto.setQuantity(5);

        OrderLineItems entity = new OrderService(null, null).mapToDto(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getSkuCode(), entity.getSkuCode());
        assertEquals(dto.getPrice(), entity.getPrice());
        assertEquals(dto.getQuantity(), entity.getQuantity());
    }
}
