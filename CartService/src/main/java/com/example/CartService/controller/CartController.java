package com.example.CartService.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.CartService.dto.CartItemDto;
import com.example.CartService.dto.UserDto;
import com.example.CartService.dto.WaterDto;
import com.example.CartService.entity.Order;
import com.example.CartService.respository.CartItemRepository;
import com.example.CartService.service.OrderService;
import com.example.CartService.utils.JwtTokenProvider;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;
import com.example.CartService.service.CartService;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v2/api")
public class CartController {
	@Autowired
	CartService cartService;
	int count =0;

	@Autowired
	private CartItemRepository itemRepository;

	@Autowired
	private OrderService orderService;
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/carts")
	@RateLimiter(name = "water")
	public ResponseEntity<Cart> getCartByUserId( HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Cart cart = cartService.getCartByUserId(userDto.getId());
		if(cart == null){
			Cart cartNew = new Cart();
			cartNew.setUserId(userDto.getId());
			cartNew.setTotalPrice(0);
			cartNew.setCartItems(null);
			cartService.addCart(cartNew);
			return ResponseEntity.ok(cartNew);
		}
		return ResponseEntity.ok(cart);

	}
	
	@PostMapping("/carts/items")
//	@Retry(name = "water")
	@CircuitBreaker(name = "water")
	public ResponseEntity<CartItem> saveOrUpdate(@RequestBody CartItemDto cartItemDto) throws Exception {
		System.out.println("retry "+ (count++));
		try {
	        System.out.println("time retry "+ Thread.currentThread().getName() + "...running  " +
	                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	        
	    } catch (Exception e){
	    	System.out.println("test");
	        System.out.println(e.getLocalizedMessage());
	    }


		CartItem cartItem = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Cart cart = cartService.getCartByUserId(userDto.getId());

		if(cart != null) {
				CartItem itemNew = new CartItem(cart, cartItemDto.getProductId(), cartItemDto.getQuantity());
				cartItem = cartService.saveItem(itemNew);
		}
		else {

			Cart cartNew = new Cart();
			cartNew.setUserId(userDto.getId());
			cartNew.setTotalPrice(0);
			cartNew.setCartItems(null);
			cartService.addCart(cartNew);
			CartItem itemNew = new CartItem(cartNew, cartItemDto.getProductId(), cartItemDto.getQuantity());
			cartItem = cartService.saveItem(itemNew);
		}

		return ResponseEntity.ok(cartItem);

	}

	@DeleteMapping("/carts/items/{productId}")
	public Cart removeItem(@PathVariable Long productId) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Cart cart = cartService.getCartByUserId(userDto.getId());
		Optional<CartItem> cartItem =  itemRepository.findById(new CartItemPK(cart.getId(), productId));
		double oldPrice = cart.getTotalPrice();
		cart.setTotalPrice(oldPrice - (cartItem.get().getQuantity() * cartItem.get().getPrice()));
		cartService.removeItem(new CartItemPK(cart.getId(), productId));
		cartService.saveCart(cart);

		return cart;
	}

	@DeleteMapping("/carts/items")
	@CircuitBreaker(name = "water")
	public Cart removeItems(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Cart cart = cartService.getCartByUserId(userDto.getId());
		List<CartItem> items = cart.getCartItems();
		if(!items.isEmpty()){
			cartService.removeItems(cart.getId());
			cart.setTotalPrice(0);
			cartService.saveCart(cart);

			cart = cartService.getCartByUserId(userDto.getId());
		}
		return cart;
	}

	@PostMapping("/orders")
	@RateLimiter(name = "water")
	public Order orderWater() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Cart cart = cartService.getCartByUserId(userDto.getId());
		if(cart == null)
			throw new Exception("CART cart of user id " + userDto.getId());
		if(cart.getCartItems().size() == 0)
			throw new Exception("Cart Item not found any items of cart " +cart.getId()  );

		return orderService.addOrder(cart);
	}

	@GetMapping("/orders")
	public List<Order> getOrdersByUser() {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UserDto userDto = restTemplate.exchange("http://localhost:8001"
						.concat("/api/user/").concat(String.valueOf(JwtTokenProvider.usernameJwt())),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();

		Long id = userDto.getId();
		return orderService.findAllByUserId(id);
	}


}
