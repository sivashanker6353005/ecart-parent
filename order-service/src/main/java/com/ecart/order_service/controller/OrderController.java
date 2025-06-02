package com.ecart.order_service.controller;

import com.ecart.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ecart.order_service.dto.Orderrequest;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String orderRequest(@RequestBody Orderrequest orderrequest) {

		orderService.placeOrder(orderrequest);
		return "Orders Placed successfully ";
	}
}
