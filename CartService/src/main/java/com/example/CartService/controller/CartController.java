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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;
import com.example.CartService.service.CartService;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartService cartService;

	@Autowired
	private CartItemRepository itemRepository;

	@Autowired
	private OrderService orderService;
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/carts")
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
	public ResponseEntity<CartItem> saveOrUpdate(@RequestBody CartItemDto cartItemDto, HttpServletRequest request) throws Exception {

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
			try {
				CartItem itemNew = new CartItem(cart, cartItemDto.getProductId(), cartItemDto.getQuantity());
				cartItem = cartService.saveItem(itemNew);
			}catch (Exception e){
				System.out.println(e);
			}

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
			System.out.println("duy : " + cart.getId());
			cartService.removeItems(cart.getId());
			cart.setTotalPrice(0);
			cartService.saveCart(cart);

			cart = cartService.getCartByUserId(userDto.getId());
		}
		return cart;
	}

	@PostMapping("/orders")
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
