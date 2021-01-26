package com.microservices.item.service;

import com.microservices.item.VO.ItemPathList;
import com.microservices.item.VO.OrderContentList;
import com.microservices.item.VO.PathObj;
import com.microservices.item.entity.Item;
import com.microservices.item.repository.FileLocationRepository;
import com.microservices.item.repository.ItemRepository;
import com.microservices.item.subscribechannel.InputChannels;
import com.microservices.item.wrapper.ItemList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service
@Slf4j
@EnableBinding(InputChannels.class)
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileLocationRepository fileLocationRepository;


    public ItemList getItems() {
        log.info("Inside of getItems method  of ItemService class, item-service");
        return new ItemList(itemRepository.findAll());
    }

    public Map<String, Object> getItemsByPage(int pageNo, int pageSize
            , String sortBy, Integer direction, Long min, Long max) {
        log.info("Inside of getItemsByPage method  of ItemService class, item-service");
        Map<String, Object> response = new HashMap<>();
        Sort.Direction sortDirection = direction.equals(1) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(sortDirection,sortBy);

        Pageable page = PageRequest.of(pageNo, pageSize, sort);
        Page<Item> itemPage =itemRepository.findByItemPriceBetween(page, BigDecimal.valueOf(min), BigDecimal.valueOf(max));
        response.put("data", itemPage.getContent());
        response.put("NumberOfTotalPages", itemPage.getTotalPages());
        response.put("TotalNumberOfItems", itemPage.getTotalElements());
        response.put("CurrentPage", itemPage.getNumber());

        return response;
    }

    public Item addItem(Item item, Long userId) {
        log.info("Inside of addItem method  of ItemService class, item-service");
        item.setItemOwner(userId);
        return itemRepository.save(item);
    }

    public Item updateItem(Item item, Long userId) {
        log.info("Inside of updateItem method  of ItemService class, item-service");
        item.setItemOwner(userId);
        if (item != null && itemRepository.existsByItemIdAndItemOwner(item.getItemId(), userId)) {
            return itemRepository.save(item);
        } else  {
            return null;
        }

    }

    public Item getItem(Long itemId) {
        log.info("Inside of getItem method  of ItemService class, item-service");
        return itemRepository.findById(itemId).get();
    }

    public boolean updateStock(OrderContentList orderContentList) {
        log.info("Inside of updateStock method  of ItemService class, item-service");
        AtomicBoolean updateSuccessful = new AtomicBoolean(true);
        ConcurrentLinkedQueue<Item> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        orderContentList.getOrderContentList().forEach( orderContent -> {
            Item item = itemRepository.findById(orderContent.getItemId()).get();
            if(orderContent.getItemAmount() > item.getItemLeftInStock() ) {
                updateSuccessful.set(false);
            }
            item.setItemLeftInStock(item.getItemLeftInStock() - orderContent.getItemAmount());
            concurrentLinkedQueue.add(item);
        });

        //Update database only if there is enough stock for each item
        if(updateSuccessful.get() == true) {
            concurrentLinkedQueue.forEach((item) ->
                    itemRepository.save(item)
            );
            return  true;
        } else {
            return false;
        }

    }

    public boolean deleteItem(Long itemId , Long userId) {
        log.info("Inside of deleteItem method  of ItemService class, item-service");
        if(itemRepository.existsByItemIdAndItemOwner(itemId, userId)) {
            itemRepository.deleteById(itemId);
            return true;
        } else {
            return false;
        }
    }

    @StreamListener(InputChannels.INPUT_FILE_CREATED)
    public void saveFileLocation(PathObj pathObj) {
        log.info("Inside of saveFileLocation method  of ItemService class, item-service");
        log.info(pathObj.toString());
        ItemPathList itemPathList = fileLocationRepository.findById(pathObj.getItemId()).orElse(null);
        if(itemPathList != null) {
            itemPathList.getPathList().add(pathObj.getPath());
            fileLocationRepository.save(itemPathList);
        }else {
            ItemPathList newPathList = new ItemPathList();
            newPathList.setItemId(pathObj.getItemId());
            List<String> pathList = new ArrayList<>();
            pathList.add(pathObj.getPath());
            newPathList.setPathList(pathList);

            fileLocationRepository.save(newPathList);
        }
    }

    @StreamListener(InputChannels.INPUT_FILE_DELETED)
    private void deleteFileLocation(PathObj pathObj) {
        log.info("Inside of deleteFileLocation method  of ItemService class, item-service");
        log.info(pathObj.toString());
        ItemPathList currentPathList = fileLocationRepository.findById(pathObj.getItemId()).get();
        currentPathList.setPathList(currentPathList.getPathList().stream()
                .filter(path -> !path.equals(pathObj.getPath())).collect(Collectors.toList()));

        fileLocationRepository.save(currentPathList);
    }

    public ItemPathList getItemFileLocations(Long itemId) {
        log.info("Inside of getItemFileLocations method  of ItemService class, item-service");
        ItemPathList itemPathList = fileLocationRepository.findById(itemId).orElse(null);

        return itemPathList;
    }

    public List<Item> search(String word) {
        List<Item> searchResult =
        itemRepository.findByItemNameStartingWith(word)
                .stream().limit(10).collect(Collectors.toList());
        return searchResult;
    }

    public List<Item> getUserItems(Long userId) {
       return itemRepository.findByItemOwner(userId);
    }
}
