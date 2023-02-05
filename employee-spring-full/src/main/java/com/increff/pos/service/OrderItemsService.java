package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemsService {
    @Autowired
    private OrderItemDao orderItemDao;

    public void addItems(List<OrderItemPojo> orderItems, Integer id) throws ApiException {
        if(orderItems.size()==0)
            throw new ApiException("Please add atleast 1 item to place your order!");
        for(OrderItemPojo item: orderItems) {
            if(item.getQuantity()<=0)
                throw new ApiException("Quantity should be a positive value");

            if(item.getMrp()<0)
                throw new ApiException("Selling Price needs to be positive");

            item.setOrderId(id);
            OrderItemPojo isExists = orderItemDao.checkExisting(item.getProductId(),item.getOrderId());

            if(isExists!=null && !(isExists.getMrp().equals(item.getMrp())))
                throw new ApiException("Selling Price of the same product can't be different");

            else if(isExists!=null){
                isExists.setQuantity(item.getQuantity()+isExists.getQuantity());
                orderItemDao.update(isExists);
                continue;
            }
            orderItemDao.insert(item);
        }
    }

    public List<OrderItemPojo> selectItems(Integer id){
        return orderItemDao.selectItems(id);
    }

    public OrderItemPojo selectItem(Integer productId, Integer orderId) {
        return orderItemDao.selectId(productId, orderId);
    }

    public void updateItems(List<OrderItemPojo> items, Integer orderId) throws ApiException {
        if(items.size()==0)
            throw new ApiException("Please add atleast 1 item to place your order!");
        List<OrderItemPojo> orderItems = orderItemDao.selectItems(orderId);
        HashMap<Integer, Boolean> map = new HashMap<>();
        for(OrderItemPojo item: orderItems){
            map.put(item.getProductId(), false);
        }
        for(OrderItemPojo item: items){
            if(item.getQuantity()<=0)
                throw new ApiException("Quantity should be a positive value");
            if(item.getMrp()<0)
                throw new ApiException("Selling Price needs to be positive");
            OrderItemPojo isExists = orderItemDao.selectId(item.getProductId(), orderId);
            if(isExists!=null){
                isExists.setQuantity(item.getQuantity());
                isExists.setMrp(item.getMrp());
                orderItemDao.update(isExists);
            }
            else{
                item.setOrderId(orderId);
                orderItemDao.insert(item);
            }
            map.put(item.getProductId(), true);
        }
        for(Integer key: map.keySet()){
            if(!map.get(key)){
                orderItemDao.delete(key, orderId);
            }
        }
    }

}
