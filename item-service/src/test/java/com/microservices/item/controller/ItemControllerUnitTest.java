package com.microservices.item.controller;

import com.microservices.item.entity.Item;
import com.microservices.item.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ItemControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    protected void test() throws Exception {
        Item item = new Item();
        item.setItemId(1l);
        item.setItemLeftInStock(40);
        item.setItemName("Banana");
        item.setItemCategory("Food");
        item.setItemFeatured(false);
        item.setItemPrice(BigDecimal.valueOf(1.48));

        when(itemService.getItem(1l)).thenReturn(item);

                  mockMvc.perform(MockMvcRequestBuilders.get("/items/getItem/1"))
                          .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(1l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemPrice").value(1.48))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemName").value("Banana"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemCategory").value("Food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemFeatured").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemLeftInStock").value(40))
                .andExpect(status().isOk());
    }
}
