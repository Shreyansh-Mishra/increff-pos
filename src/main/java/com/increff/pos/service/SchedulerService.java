package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SchedulerPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private SchedulerDao schedulerDao;

    //trigger automatically at 00:05 AM everyday
    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void insertScheduler() {
        SchedulerPojo scheduler = new SchedulerPojo();
        Instant instant = Instant.now();
        List<OrderPojo> o = orderDao.selectByDate(instant);
        double revenue = 0;
        int itemcount = 0;
        for(OrderPojo order: o){
            List<OrderItemPojo> items = orderItemDao.selectItems(order.getId());
            for(OrderItemPojo item: items){
                revenue += item.getSellingPrice()*item.getQuantity();
                itemcount++;
            }
        }
        scheduler.setRevenue(revenue);
        scheduler.setInvoiced_items_count(itemcount);
        scheduler.setInvoiced_orders_count(o.size());
        scheduler.setDate(instant);
        schedulerDao.insert(scheduler);
    }

    @Transactional
    public List<SchedulerPojo> selectSchedulerData() {
        return schedulerDao.select();
    }
}
