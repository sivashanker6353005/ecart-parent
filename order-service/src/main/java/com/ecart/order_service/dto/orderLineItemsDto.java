package com.ecart.order_service.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class orderLineItemsDto {

	private long id;
	private String skuCode;
	private BigDecimal price;
	private Integer quantity;
}
