package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemsService {
    @Autowired
    private OrderItemDao orderItemDao;

    public void addItems(List<OrderItemPojo> orderItems, int id) throws ApiException {
        if(orderItems.size()==0)
            throw new ApiException("Please add atleast 1 item to place your order!");
        for(OrderItemPojo o: orderItems) {
            if(o.getBarcode().isEmpty()||o.getQuantity()==0||o.getSellingPrice()==0) {
                throw new ApiException("All fields are mandatory, please check again!");
            }
            if(o.getQuantity()<0) {
                throw new ApiException("Quantity should be a positive value");
            }
            if(o.getSellingPrice()<0) {
                throw new ApiException("Selling Price needs to be positive");
            }
            o.setOrderId(id);
            orderItemDao.insert(o);
        }
    }

    public List<OrderItemPojo> selectItems(int id){
        return orderItemDao.selectItems(id);
    }


}
