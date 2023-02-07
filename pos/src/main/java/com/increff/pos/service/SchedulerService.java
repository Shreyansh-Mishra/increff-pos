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
    private SchedulerDao schedulerDao;

    @Transactional
    public void insertScheduler(SchedulerPojo scheduler) {
        schedulerDao.insert(scheduler);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<SchedulerPojo> selectSchedulerData(Instant startDate, Instant endDate) throws ApiException {
        if(startDate.isAfter(endDate))
            throw new ApiException("Start date cannot be after end date");
        return schedulerDao.select(startDate, endDate);
    }
}
