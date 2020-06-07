package com.example.chart.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Table(name = "item")
@Entity
@Data
public class Item {
    @Id
    @SequenceGenerator(name = "itemSeq",sequenceName = "item_seq",allocationSize = 1,initialValue = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "itemSeq")
    private Long id;

    @Column(columnDefinition = "varchar2(20)")
    @Size(
            min = 2,
            max = 15,
            message = "تعداد کاراکتر مورد نظر بین 2 تا 15 می باشد"
    )
    private String name;
    @Column(columnDefinition = "Decimal(10,2) default 0.00")
    @Min(value = 0,message = "مقدار درصد آیتم بین 0 تا 100 می باشد")
    @Max(value=100,message="مقدار درصد آیتم بین 0 تا 100 می باشد")
    private Double percent;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name="dept_id")
    private Department department;

    @Lob
    private String descripton;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    public void setCreateTime() {
        this.createTime = new Date();
    }

    @PreUpdate
    public void SetUpdateTime(){
        this.updateTime = new Date();
    }




}
