package com.example.chart.repository;

import com.example.chart.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
//    select new com.example.chart.model.Item(i.id, i.percent) from Item i where i.d
    @Query(value = "select *  from item where dept_id=?1 ",nativeQuery = true)
//    List<Object[]> findByDepartment(Long deptId);
    List<Item> findByDeptId(Long deptId);
}
