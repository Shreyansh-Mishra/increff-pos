package com.increff.pos.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;

import java.util.ArrayList;
import java.util.List;

public class ObjectUtil {
    public static <T,R> R objectMapper(T object, Class<R> destinClass){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        R newObject = mapper.convertValue(object, destinClass);
        return newObject;
    }

    public static InvoicePojo convert(Integer id, String path){
        InvoicePojo invoice = new InvoicePojo();
        invoice.setId(id);
        invoice.setPath(path);
        return invoice;
    }

    public static OrderData convert(OrderPojo order) {
        OrderData data = new OrderData();
        String time = order.getTime().toString();
        data.setId(order.getId());
        data.setTime(time);
        return data;
    }

    public static List<OrderItemPojo> convert(List<OrderForm> form){
        List<OrderItemPojo> list= new ArrayList<>();
        for(OrderForm i: form) {
            OrderItemPojo o = ObjectUtil.objectMapper(i,OrderItemPojo.class);
            o.setMrp(RefactorUtil.round(i.getMrp(), 2));
            list.add(o);
        }
        return list;
    }

}
