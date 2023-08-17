package com.ky.inventoryservice.service;

import com.ky.amqp.dto.DeleteInventoryRequest;
import com.ky.amqp.dto.InventoryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {
    Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RabbitListener(queues = "${rabbitmq.queues.create-inventory}")
    public void createInventoryConsumer(InventoryRequest inventoryRequest) {
        logger.info("Consumed {} from create-inventory queue", inventoryRequest);
        inventoryService.addProductToInventory(inventoryRequest);
    }

    @RabbitListener(queues = "${rabbitmq.queues.delete-inventory}")
    public void deleteInventoryConsumer(DeleteInventoryRequest deleteInventoryRequest) {
        logger.info("Consumed {} from delete-inventory queue", deleteInventoryRequest);
        inventoryService.deleteProductFromInventory(deleteInventoryRequest);
    }

    @RabbitListener(queues = "${rabbitmq.queues.update-inventory}")
    public void updateInventoryConsumer(InventoryRequest inventoryRequest) {
        logger.info("Consumed {} from update-inventory queue", inventoryRequest);
        inventoryService.updateProductFromInventory(inventoryRequest);
    }


}
