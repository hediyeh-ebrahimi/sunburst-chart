package com.example.chart.model;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Table(name = "department")
@Entity
@Data
public class Department {

    @Id
    @SequenceGenerator(name = "deptSeq",sequenceName = "dept_seq",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "deptSeq")
    private Long id;
    @Column(columnDefinition = "varchar2(15)")
    @Size(min = 3,max = 15,message = "نام دپارتمان بین 3 تا 15 کاراکتر مورد نظر می باشد.")
//    @Unique(message = "نام دپارتمان یکتا می باشد")
    private String name;
    @Lob
    private String description;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "department")
    private List<Item> itemList;
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

    public Department(Long id,@Size(min = 3, max = 15, message = "نام دپارتمان بین 3 تا 15 کاراکتر مورد نظر می باشد.") String name) {
        this.id=id;
        this.name = name;
    }

    public Department() {
    }
}
