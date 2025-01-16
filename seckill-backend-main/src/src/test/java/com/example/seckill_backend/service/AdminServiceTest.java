package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.repository.ProductRepository;
import com.example.seckill_backend.repository.SeckillProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private SeckillProductRepository seckillProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> result = adminService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSeckillProducts() {
        // Arrange
        SeckillProduct seckillProduct1 = new SeckillProduct();
        seckillProduct1.setId(1L);
        SeckillProduct seckillProduct2 = new SeckillProduct();
        seckillProduct2.setId(2L);

        when(seckillProductRepository.findAll()).thenReturn(Arrays.asList(seckillProduct1, seckillProduct2));

        // Act
        List<SeckillProduct> result = adminService.getAllSeckillProducts();

        // Assert
        assertEquals(2, result.size());
        verify(seckillProductRepository, times(1)).findAll();
    }

    @Test
    void testGetSeckillProductsByGoodsId() {
        // Arrange
        Long goodsId = 1L;
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setGoodsId(goodsId);

        when(seckillProductRepository.findByGoodsId(goodsId)).thenReturn(Arrays.asList(seckillProduct));

        // Act
        List<SeckillProduct> result = adminService.getSeckillProductsByGoodsId(goodsId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(goodsId, result.get(0).getGoodsId());
        verify(seckillProductRepository, times(1)).findByGoodsId(goodsId);
    }

    @Test
    void testCreateSeckillProduct_Success() {
        // Arrange
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setGoodsId(1L);

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(seckillProductRepository.save(seckillProduct)).thenReturn(seckillProduct);

        // Act
        SeckillProduct result = adminService.createSeckillProduct(seckillProduct);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(seckillProductRepository, times(1)).save(seckillProduct);
    }

    @Test
    void testCreateSeckillProduct_ProductNotFound() {
        // Arrange
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setGoodsId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                adminService.createSeckillProduct(seckillProduct)
        );
        assertEquals("商品不存在", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateSeckillProduct_Success() {
        // Arrange
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setId(1L);

        when(seckillProductRepository.findById(1L)).thenReturn(Optional.of(seckillProduct));
        when(seckillProductRepository.save(seckillProduct)).thenReturn(seckillProduct);

        // Act
        SeckillProduct result = adminService.updateSeckillProduct(seckillProduct);

        // Assert
        assertNotNull(result);
        verify(seckillProductRepository, times(1)).findById(1L);
        verify(seckillProductRepository, times(1)).save(seckillProduct);
    }

    @Test
    void testUpdateSeckillProduct_NotFound() {
        // Arrange
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setId(1L);

        when(seckillProductRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                adminService.updateSeckillProduct(seckillProduct)
        );
        assertEquals("秒杀商品不存在", exception.getMessage());
        verify(seckillProductRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSeckillProduct() {
        // Arrange
        Long id = 1L;

        // Act
        adminService.deleteSeckillProduct(id);

        // Assert
        verify(seckillProductRepository, times(1)).deleteById(id);
    }
}
