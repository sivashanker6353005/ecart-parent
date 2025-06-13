// Project: eCart
package com.ecart.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.ecart.order_service.dto.InventoryResponseDto;
import com.ecart.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ecart.order_service.dto.Orderrequest;
import com.ecart.order_service.dto.orderLineItemsDto;
import com.ecart.order_service.entity.OrderLineItems;
import com.ecart.order_service.entity.Orders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderrepository;
	private final WebClient.Builder webClientBuilder;

	public void placeOrder(Orderrequest orderrequest) {
		Orders order = new Orders();
		order.setOrdernumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems = orderrequest.getOrderLineItemsdto().stream()
				.map(this::mapToDto)
				.toList();
		order.setOrderLineItemList(orderLineItems);
		List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItems::getSkuCode).toList();

		// Call the inventory service to check if the items are in stock
		InventoryResponseDto[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder
								.path("/api/inventory/check")
								.queryParam("skuCode", skuCodes)
								.build())

				.retrieve()
				.bodyToMono(InventoryResponseDto[].class)
				.block();

        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
				                           .allMatch(InventoryResponseDto::isInStock);

		if (allProductsInStock) {
			orderrepository.save(order);
		} else {
			throw new IllegalArgumentException("Orders cannot be placed as items are not in stock");
		}
	}

	public OrderLineItems mapToDto(orderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		orderLineItems.setId(orderLineItemsDto.getId());
		return orderLineItems;
	}
}