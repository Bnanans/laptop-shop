package com.shop.service;

import com.shop.entity.Laptop;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryRepair {

    private static final Logger LOG = Logger.getLogger(InventoryRepair.class);

    @Transactional
    void repairNegativeInventory(@Observes StartupEvent event) {
        long repaired = Laptop.update("quantity = 0 where quantity < 0");
        if (repaired > 0) {
            LOG.warnf("Reset inventory to zero for %d laptop(s) with negative stock", repaired);
        }
    }
}
