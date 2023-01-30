package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

    @RequestMapping(value = "/ui/home")
    public ModelAndView home() {
        return mav("home.html");
    }

    @RequestMapping(value = "/ui/brands")
    public ModelAndView brand() {
        return mav("brands.html");
    }

    @RequestMapping(value = "/ui/products")
    public ModelAndView product() {
        return mav("products.html");
    }

    @RequestMapping(value = "/ui/inventory")
    public ModelAndView inventory() {
        return mav("inventory.html");
    }

    @RequestMapping(value = "/ui/orders")
    public ModelAndView orders() {
        return mav("orders.html");
    }

    @RequestMapping(value = "/ui/reports")
    public ModelAndView reports() {
        return mav("reports.html");
    }

    @RequestMapping(value = "/ui/reports/inventory")
    public ModelAndView inventoryReport() {
        return mav("inventoryReport.html");
    }

    @RequestMapping(value = "/ui/reports/brands")
    public ModelAndView brandsReport() {
        return mav("brandsReport.html");
    }

    @RequestMapping(value = "/ui/reports/sales")
    public ModelAndView salesReport() {
        return mav("salesReport.html");
    }

    @RequestMapping(value = "/ui/reports/dailyReport")
    public ModelAndView dailyReport() {
        return mav("dailyReport.html");
    }

    @RequestMapping(value = "/ui/admin")
    public ModelAndView admin() {
        return mav("user.html");
    }

}
