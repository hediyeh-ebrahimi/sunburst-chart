package com.example.chart.repository;

import com.example.chart.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    @Query(value = "select new com.example.chart.model.Department(d.id, d.name) from Department d ")
    List<Department> findIdAndName();
    @Query(value = "select name from department",nativeQuery = true)
    List<String> findNames();
}
