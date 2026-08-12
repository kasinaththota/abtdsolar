package com.abtd.solarbackend.inventory.repository;

import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProduct(Product product);

    boolean existsByProductId(Long productId);

    Page<Inventory> findByStatus(
            InventoryStatus status,
            Pageable pageable);

    Page<Inventory> findByWarehouseLocationIgnoreCase(
            String warehouseLocation,
            Pageable pageable);

    Page<Inventory> findByStatusAndWarehouseLocationIgnoreCase(
            InventoryStatus status,
            String warehouseLocation,
            Pageable pageable);

    // ================= Dashboard =================

    @Query("""
            SELECT COUNT(i)
            FROM Inventory i
            WHERE i.availableQuantity <= i.minimumStock
            """)
    long getLowStockProducts();

    @Query("""
            SELECT COUNT(i)
            FROM Inventory i
            WHERE i.availableQuantity = 0
            """)
    long getOutOfStockProducts();

    @Query("""
            SELECT i
            FROM Inventory i
            ORDER BY i.product.name
            """)
    List<Inventory> findAllForReport();
}