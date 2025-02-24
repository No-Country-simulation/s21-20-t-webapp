package com.inventario.demo.entities.productos.controller;

import com.inventario.demo.entities.productos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;
}
