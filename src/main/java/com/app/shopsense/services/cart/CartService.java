package com.app.shopsense.services.cart;

import com.app.shopsense.doas.entities.cart.Cart;
import com.app.shopsense.doas.entities.cart.CartItem;
import com.app.shopsense.doas.entities.product.Product;
import com.app.shopsense.doas.repositories.cart.CartItemRepository;
import com.app.shopsense.doas.repositories.cart.CartRepository;
import com.app.shopsense.doas.repositories.product.ProductRepository;
import com.app.shopsense.dtos.cart.CartDto;
import com.app.shopsense.dtos.cart.CartItemDto;
import com.app.shopsense.exceptions.AppException;
import com.app.shopsense.mappers.CartMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

@Service
@Validated
public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional(readOnly = true)
    public CartDto getCartByUserId(Long userId) {
        log.debug("Fetching cart for userId: {}", userId);

        Optional<Cart> userCart = cartRepository.findByUserId(userId);

        if (userCart.isEmpty()) {
            log.debug("No cart found for userId: {}", userId);
            return createEmptyCartDto(userId);
        }

        Cart cart = userCart.get();

        // Consolidate duplicate products
        Map<Long, CartItemDto> consolidatedItems = consolidateCartItems(cart.getCartItems());

        // Enrich with product images
        enrichWithProductData(consolidatedItems);

        // Calculate total
        BigDecimal totalPrice = calculateTotalPrice(consolidatedItems.values());

        CartDto cartDto = cartMapper.toDto(cart);
        cartDto.setCartItems(new ArrayList<>(consolidatedItems.values()));
        cartDto.setTotalPrice(totalPrice);

        return cartDto;
    }

    @Transactional(readOnly = true)
    public int getNumberOfItemsInCart(Long userId) {
        log.debug("Counting items in cart for userId: {}", userId);

        Optional<Cart> userCart = cartRepository.findByUserId(userId);

        return userCart.map(cart -> cart.getCartItems().stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum())
                .orElse(0);
    }

    @Transactional
    public CartDto addItemToCart(Long userId, Long productId,
                                 @Min(1) @Max(999) int quantity) {
        log.info("Adding item to cart: userId={}, productId={}, quantity={}",
                userId, productId, quantity);

        // Validate product exists and has sufficient stock
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        validateStockAvailability(product, quantity);

        // Get or create cart
        Cart cart = getOrCreateCart(userId);

        // Check if product already exists in cart
        Optional<CartItem> existingItem = findCartItemByProductId(cart, productId);

        if (existingItem.isPresent()) {
            updateExistingCartItem(existingItem.get(), quantity, product);
        } else {
            addNewCartItem(cart, product, quantity);
        }

        // Recalculate and save total
        updateCartTotal(cart);
        cartRepository.save(cart);

        log.info("Item added successfully to cart for userId: {}", userId);
        return getCartByUserId(userId);
    }

    @Transactional
    public CartDto updateCartItemQuantity(Long userId, Long productId,
                                          @Min(1) @Max(999) int newQuantity) {
        log.info("Updating cart item quantity: userId={}, productId={}, newQuantity={}",
                userId, productId, newQuantity);

        CartDto cartDto = getCartByUserId(userId);
        Cart cart = cartMapper.toEntity(cartDto);


        CartItem cartItem = findCartItemByProductId(cart, productId)
                .orElseThrow(() -> new AppException("Cart item not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        validateStockAvailability(product, newQuantity);

        cartItem.setQuantity(newQuantity);
        cartItem.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        cartItemRepository.save(cartItem);

        updateCartTotal(cart);
        cartRepository.save(cart);

        return getCartByUserId(userId);
    }

    @Transactional
    public CartDto removeItemFromCart(Long userId, Long productId) {
        log.info("Removing item from cart: userId={}, productId={}", userId, productId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException("Cart not found", HttpStatus.NOT_FOUND));

        // Remove ALL items with this productId (in case of duplicates)
        List<CartItem> itemsToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .toList();

        if (itemsToRemove.isEmpty()) {
            throw new AppException("Cart item not found", HttpStatus.NOT_FOUND);
        }

        cart.getCartItems().removeAll(itemsToRemove);
        cartItemRepository.deleteAll(itemsToRemove);

        updateCartTotal(cart);
        cartRepository.save(cart);

        log.info("Item removed successfully from cart for userId: {}", userId);
        return getCartByUserId(userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        log.info("Clearing cart for userId: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        "Cart not found for user id: " + userId, HttpStatus.NOT_FOUND));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        log.info("Cart cleared successfully for userId: {}", userId);
    }

    @Transactional(readOnly = true)
    public Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        "Cart not found for user id: " + userId, HttpStatus.NOT_FOUND));
    }

    // ==================== Private Helper Methods ====================

    private Cart getOrCreateCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }

        log.debug("Creating new cart for userId: {}", userId);
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setTotalPrice(BigDecimal.ZERO);
        newCart.setCartItems(new ArrayList<>());
        return cartRepository.save(newCart);
    }

    private Optional<CartItem> findCartItemByProductId(Cart cart, Long productId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void updateExistingCartItem(CartItem cartItem, int additionalQuantity, Product product) {
        log.debug("Updating existing cart item: productId={}", cartItem.getProductId());

        int newQuantity = cartItem.getQuantity() + additionalQuantity;
        validateStockAvailability(product, newQuantity);

        cartItem.setQuantity(newQuantity);
        cartItem.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        cartItemRepository.save(cartItem);
    }

    private void addNewCartItem(Cart cart, Product product, int quantity) {
        log.debug("Adding new cart item: productId={}, quantity={}", product.getId(), quantity);

        CartItem newItem = new CartItem();
        newItem.setProductId(product.getId());
        newItem.setProductName(product.getName());
        newItem.setQuantity(quantity);
        newItem.setPrice(product.getPrice()); // Store unit price
        newItem.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        newItem.setCart(cart);

        cartItemRepository.save(newItem);
        cart.getCartItems().add(newItem);
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
    }

    private Map<Long, CartItemDto> consolidateCartItems(List<CartItem> cartItems) {
        Map<Long, CartItemDto> consolidatedMap = new HashMap<>();

        for (CartItem item : cartItems) {
            Long productId = item.getProductId();
            CartItemDto dto = cartMapper.toDto(item);

            if (consolidatedMap.containsKey(productId)) {
                CartItemDto existing = consolidatedMap.get(productId);
                existing.setQuantity(existing.getQuantity() + dto.getQuantity());
                existing.setSubTotal(existing.getSubTotal().add(dto.getSubTotal()));
            } else {
                consolidatedMap.put(productId, dto);
            }
        }

        return consolidatedMap;
    }

    private void enrichWithProductData(Map<Long, CartItemDto> cartItemMap) {
        for (CartItemDto itemDto : cartItemMap.values()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new AppException(
                            "Product not found: " + itemDto.getProductId(), HttpStatus.NOT_FOUND));

            itemDto.setProduct(cartMapper.toProductDto(product));
        }
    }

    private BigDecimal calculateTotalPrice(Collection<CartItemDto> cartItems) {
        return cartItems.stream()
                .map(CartItemDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateStockAvailability(Product product, int requestedQuantity) {
        if (product.getQuantity() != 0 && product.getQuantity() < requestedQuantity) {
            throw new AppException(
                    String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                            product.getName(), product.getQuantity(), requestedQuantity),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private CartDto createEmptyCartDto(Long userId) {
        CartDto emptyCart = new CartDto();
        emptyCart.setUserId(userId);
        emptyCart.setCartItems(new ArrayList<>());
        emptyCart.setTotalPrice(BigDecimal.ZERO);
        return emptyCart;
    }
}