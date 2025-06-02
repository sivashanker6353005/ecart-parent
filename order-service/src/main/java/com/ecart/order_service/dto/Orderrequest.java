package com.ecart.order_service.dto;

import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orderrequest {

	private List<orderLineItemsDto> orderLineItemsdto = new ArrayList<>();
}
