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

/*        Map<String,List<Children> > listMap =new HashMap<>();
        listMap.put("ali",
                new ArrayList<Children>(){{
                    add(new Children("ss",10D));
                    add(new Children("dd",20D));
        }});
        listMap.put("reza",
                new ArrayList<Children>(){{
                    add(new Children("zzzzz",10D));
                }});*/

     /*   ArrayList<Children> child1 = new ArrayList<Children>() {{
            add(new Children("a", 10D));
            add(new Children("b", 10D));
        }};

        ArrayList<Children> child2 = new ArrayList<Children>() {{
            add(new Children("c", 10D));
        }};*/
     /*   Parent parent1=new Parent("amir",child1);
        Parent parent2=new Parent("mahdi",child2);*/
//        List<Parent> parents=new ArrayList<Parent>(){{add(parent1);add(parent2);}};
        List<Parent> parents=new ArrayList<>();


//        listMap.entrySet().stream().map(e->e.getKey())


        model.addAttribute("department",new Department());
        model.addAttribute("item",new Item());
        List<Department> departmentList = departmentRepository.findIdAndName();
        model.addAttribute("deptall",departmentList);
       departmentList.forEach(dept -> {
            List<Item> itemValues = itemRepository.findByDeptId(dept.getId());
            List<Children> childrenList = itemValues.stream().map(item -> new Children(item.getName(), item.getPercent())).collect(Collectors.toList());
            Parent parent = new Parent(dept.getName(),childrenList);
            System.out.println(" -> dep : " + dept.getName());
            parents.add(parent);
        });

        /*List<String> detNames = departmentRepository.findNames();
        String chartData = "[{\n" ;
        String chartData = "[{" ;
        String chartData = "[{" ;
        int j=0;
        departmentList.stream().forEach(e->e.get);
        for (Department obj:departmentList) {
            String depName = obj.getName();
            Long depId = obj.getId();
//            chartData += "name:\""+depName+"\",\n children:[{";
            chartData += "\"name\":\""+depName+"\",\"children\":[{";
//            List<Object[]> itemValues = itemRepository.findByDepartment(depId);
            List<Item> itemValues = itemRepository.findByDepartment(depId);
            int i=0;
            for (Object[] item:itemValues) {
                String itemName = item[0].toString();
                if(item[1].toString().equals("") || item[1].toString().equals("null")){
                    item[1] = "0";
                }
                Double itemPercent = Double.valueOf(item[1].toString());
                chartData += "\"name\":\""+itemName+"\",\"value\":"+itemPercent;
                i++;

//                chartData +="}\n";
                chartData +="}";
                if(i<itemValues.size()){
                    chartData +=",{";

                }
                if(i == itemValues.size()){
                    chartData +="]";
                }

            }
            if(j<departmentList.size()){
                chartData +="}";
            }

            j++;
            if(j<departmentList.size()){
                chartData +=",{";
            }


        }

            chartData +="]";
        model.addAttribute("chartData",chartData.replaceAll("&quot;","\""));
*/
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(parents);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("JSON OUT "+ json);
        model.addAttribute("chartData",json);
        return "index";
    }

    @RequestMapping(value = "/adddept",method = RequestMethod.POST)
    public String addDept(@ModelAttribute("department") @Valid Department department, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if(!bindingResult.hasErrors()){
//            model.addAttribute("res_success","دپارتمان با موفقیت افزوده شد");
//            redirectAttributes.addAttribute("res_success","دپارتمان با موفقیت افزوده شد");
            redirectAttributes.addFlashAttribute("res_success","دپارتمان با موفقیت افزوده شد");
            departmentRepository.save(department);
            return "redirect:/";
//            return "index";
        }else{
            return "index";
        }

    }

    @RequestMapping(value = "/additem",method = RequestMethod.POST)
    public String addItem(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        if(!bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("res_success","آیتم با موفقیت افزوده شد");
            item.setDepartment(item.getDepartment());
            itemRepository.save(item);
            return "redirect:/";
        }else{
            return "index";
        }

    }
}
