package com.tecsup.minishop.service;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.model.ProductRequest;
import com.tecsup.minishop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_convierteElDtoEnEntidadYLoGuarda() {
        ProductRequest request = new ProductRequest("Laptop", 2500.0, 10);
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product resultado = productService.create(request);

        assertEquals("Laptop", resultado.getName());
        assertEquals(2500.0, resultado.getPrice());
        assertEquals(10, resultado.getStock());
    }

    @Test
    void save_conPrecioCero_lanzaIllegalArgumentException() {
        Product product = Product.builder().name("Mouse").price(0.0).stock(5).build();

        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void save_conStockNegativo_lanzaIllegalArgumentException() {
        Product product = Product.builder().name("Mouse").price(50.0).stock(-1).build();

        assertThrows(IllegalArgumentException.class, () -> productService.save(product));
    }

    @Test
    void findAll_devuelveLaListaDelRepositorio() {
        Product product = Product.builder().id(1L).name("Teclado").price(80.0).stock(3).build();
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> resultado = productService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getName());
    }

    @Test
    void findById_existente_devuelveElProducto() {
        Product product = Product.builder().id(1L).name("Monitor").price(600.0).stock(2).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product resultado = productService.findById(1L);

        assertEquals("Monitor", resultado.getName());
    }

    @Test
    void findById_inexistente_lanzaRuntimeException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findById(99L));
    }
}