package com.microservices.item.controller;

import com.microservices.item.VO.OrderContent;
import com.microservices.item.VO.OrderContentList;
import com.microservices.item.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
class ItemControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    protected void getItem()  {
        ResponseEntity<Item> responseEntity =  testRestTemplate.getForEntity("/items/getItem/1", Item.class);

        assertEquals(1l,responseEntity.getBody().getItemId());
        assertEquals("Banana",responseEntity.getBody().getItemName());
        assertTrue(responseEntity.getBody().getItemPrice().compareTo(BigDecimal.valueOf(1.45)) == 0);
        assertEquals(43,responseEntity.getBody().getItemLeftInStock());
        assertEquals("Food",responseEntity.getBody().getItemCategory());
        assertEquals(false,responseEntity.getBody().isItemFeatured());
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));


    }

    @Test
    protected void addItemTest() {
        Item item = new Item();
        item.setItemPrice(BigDecimal.valueOf(32.16));
        item.setItemFeatured(true);
        item.setItemCategory("Electronics");
        item.setItemName("Keyboard");
        item.setItemLeftInStock(35);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> httpEntity = new HttpEntity<>(item, httpHeaders);

        ResponseEntity<Item> responseEntity = testRestTemplate.postForEntity("/items/addItem", httpEntity, Item.class );

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
        assertEquals(6l, responseEntity.getBody().getItemId());
        assertTrue(responseEntity.getBody().getItemPrice().compareTo(BigDecimal.valueOf(32.16)) == 0);
        assertTrue(responseEntity.getBody().getItemName().equals("Keyboard"));
        assertEquals(35, responseEntity.getBody().getItemLeftInStock());
        assertTrue(responseEntity.getBody().getItemCategory().equals("Electronics"));
        assertTrue(responseEntity.getBody().isItemFeatured() == true);
    }

    @Test
    protected void updateItemTest() {
        Item item = new Item();
        item.setItemId(6l);
        item.setItemPrice(BigDecimal.valueOf(44.69));
        item.setItemFeatured(false);
        item.setItemCategory("Electronics");
        item.setItemName("Keyboard");
        item.setItemLeftInStock(25);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> itemHttpEntity = new HttpEntity<>(item, httpHeaders);

       ResponseEntity<Item> responseEntity = testRestTemplate.postForEntity("/items/updateItem", itemHttpEntity, Item.class);

       log.info(responseEntity.getStatusCode().toString());
     //  assertTrue(responseEntity.getStatusCode().isError());

        assertFalse(responseEntity.getStatusCode().isError());
        assertTrue(responseEntity.getBody().isItemFeatured() == false);
        assertTrue(responseEntity.getBody().getItemLeftInStock() == 25);
        assertTrue(responseEntity.getBody().getItemPrice().compareTo(BigDecimal.valueOf(44.69)) == 0);
        assertTrue(responseEntity.getBody().getItemId().equals(6l));

    }

    @Test
    protected void updateStock() {

        OrderContentList orderContentList = new OrderContentList(
                Arrays.asList(
                        new OrderContent(5l, 10),
                        new OrderContent(6l, 10)
                )
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
/*
        HttpEntity<OrderContentList> httpEntity= new HttpEntity<>(orderContentList, httpHeaders);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/items/updateStock", httpEntity, String.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        ResponseEntity<Item> updatedItem1 = testRestTemplate.getForEntity("/items/getItem/5", Item.class);
        ResponseEntity<Item> updatedItem2 = testRestTemplate.getForEntity("/items/getItem/6", Item.class);

        assertTrue(updatedItem1.getStatusCode().is2xxSuccessful());
        assertTrue(updatedItem2.getStatusCode().is2xxSuccessful());
        assertTrue(updatedItem1.getBody().getItemLeftInStock() == 38);
        assertTrue(updatedItem2.getBody().getItemLeftInStock() == 15);
*/

        OrderContentList outOfStockTest = new OrderContentList(
          Arrays.asList(
                  new OrderContent(5l, 39),
                  new OrderContent(6l,20)
          )
        );

        HttpEntity<OrderContentList> failUpdate = new HttpEntity<>(outOfStockTest, httpHeaders);

        ResponseEntity<String> responseEntityFail = testRestTemplate.postForEntity("/items/updateStock", failUpdate, String.class);

        assertTrue(responseEntityFail.getStatusCode().isError());

        ResponseEntity<Item> updatedItemFailed1 = testRestTemplate.getForEntity("/items/getItem/5", Item.class);
        ResponseEntity<Item> updatedItemFailed2 = testRestTemplate.getForEntity("/items/getItem/6", Item.class);
        // Nothing should be changed
        assertTrue(updatedItemFailed1.getStatusCode().is2xxSuccessful());
        assertTrue(updatedItemFailed2.getStatusCode().is2xxSuccessful());
        assertTrue(updatedItemFailed1.getBody().getItemLeftInStock() == 38);
        assertTrue(updatedItemFailed2.getBody().getItemLeftInStock() == 15);
    }
}