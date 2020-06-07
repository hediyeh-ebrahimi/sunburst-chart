package com.example.chart.controller;

import com.example.chart.model.Children;
import com.example.chart.model.Department;
import com.example.chart.model.Item;
import com.example.chart.model.Parent;
import com.example.chart.repository.DepartmentRepository;
import com.example.chart.repository.ItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChartDetail {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ItemRepository itemRepository;



    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("department",new Department());
        model.addAttribute("item",new Item());
        List<Department> departmentList = departmentRepository.findIdAndName();
        model.addAttribute("deptall",departmentList);
        int deptSize = 0;
        if(departmentList.size() >0){
            deptSize = departmentList.size();
        }
        model.addAttribute("deptSize",deptSize);

        String json = createChartData();

        System.out.println("JSON OUT "+ json+"------------");
        model.addAttribute("chartData",json);
        return "index";
    }

    private String createChartData() {
        List<Parent> parents=new ArrayList<>();
        List<Department> departmentList = departmentRepository.findIdAndName();
        departmentList.forEach(dept -> {
            List<Item> itemValues = itemRepository.findByDeptId(dept.getId());
            List<Children> childrenList ;
            if(itemValues.size()>0){
                 childrenList = itemValues.stream().map(item -> new Children(item.getName(), item.getPercent())).collect(Collectors.toList());

            }else{
                childrenList = itemValues.stream().map(item -> new Children("",0D)).collect(Collectors.toList());

            }
            Parent parent = new Parent(dept.getName(),childrenList);
            System.out.println(" -> dep : " + dept.getName());
            parents.add(parent);
        });

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(parents);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @RequestMapping(value = "/adddept",method = RequestMethod.POST)
    public String addDept(@ModelAttribute("department") @Valid Department department, BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model){
        String json = createChartData();
        model.addAttribute("chartData",json);
        model.addAttribute("item",new Item());
        if(bindingResult.hasErrors()){
            return "index";

        }else{
            redirectAttributes.addFlashAttribute("res_success","دپارتمان با موفقیت افزوده شد");
            departmentRepository.save(department);
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/additem",method = RequestMethod.POST)
    public String addItem(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,Model model){
        String json = createChartData();
        model.addAttribute("chartData",json);
        model.addAttribute("department",new Department());
        Long deptId = 0L;
        if(item.getDepartment() == null ) {
             deptId = 0L;
        }else{
             deptId = item.getDepartment().getId();
        }
        if(!bindingResult.hasErrors() &&  deptId != 0){
            redirectAttributes.addFlashAttribute("res_success","آیتم با موفقیت افزوده شد");
            item.setDepartment(item.getDepartment());
            itemRepository.save(item);
            return "redirect:/";
        }else{
            if(deptId == 0){
                FieldError fieldError = new FieldError("department","department","دپارتمان مورد نظر را مشخص کنید");
                bindingResult.addError(fieldError);
//
            }
            for (Object object : bindingResult.getAllErrors()) {
                if(object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                }

                if(object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;
                }
            }
            return "index";
        }

    }
}
