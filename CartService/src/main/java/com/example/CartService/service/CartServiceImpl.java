package com.example.CartService.service;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.CartService.dto.UserDto;
import com.example.CartService.dto.WaterDto;
import com.example.CartService.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;
import com.example.CartService.respository.CartItemRepository;
import com.example.CartService.respository.CartRepository;
import org.springframework.web.client.RestTemplate;
import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class CartServiceImpl implements CartService, UserDetailsService {
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository itemRepository;
	@Autowired
	private RestTemplate restTemplate;


	@Override
	public Cart addCart(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.saveAndFlush(cart);
	}
	@Override
	public CartItem saveItem(CartItem cartItem) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization","Bearer " + JwtTokenProvider.tokenJwt());
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		WaterDto waterDto = restTemplate.exchange("http://localhost:4000".concat("/api/watters/").concat(String.valueOf(cartItem.getProductId())),
				HttpMethod.GET,
				entity,
				WaterDto.class
		).getBody();

		if(waterDto != null ) {
			cartItem.setPrice(waterDto.getPrice());
		}else {
			throw new Exception("Water not found");
		}

		if(cartItem.getQuantity()  > waterDto.getQuantity()){
			throw new Exception("Quantity water exceeds quantity in store");
		}else{
			CartItemPK cartItemPK = new CartItemPK(cartItem.getCart().getId(), cartItem.getProductId());
			Optional<CartItem> oldCartItem =  itemRepository.findById(cartItemPK);
			if(oldCartItem.isPresent()){
				double oldPrice = cartItem.getCart().getTotalPrice();
				CartItem oldCI =  oldCartItem.get();
				cartItem.getCart().setTotalPrice(oldPrice - (oldCI.getQuantity()* oldCI.getPrice()) + (cartItem.getQuantity()*cartItem.getPrice()));
			}else{
				double oldPrice = cartItem.getCart().getTotalPrice();
				cartItem.getCart().setTotalPrice(oldPrice + cartItem.getQuantity()* waterDto.getPrice());
			}
		}

		CartItem item = itemRepository.save(cartItem);
		cartRepository.save(cartItem.getCart());
		return item;
	}
	@Override
	public boolean removeItem(CartItemPK cartItemPK) {
		try {
			Optional<CartItem> cartItem =  itemRepository.findById(cartItemPK);
			if(cartItem.isPresent()){
				itemRepository.deleteById(cartItemPK);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean removeItems(List<CartItem> items) {
		try {
			items.stream().forEach(i -> {
				System.out.println(i.getCart().getId());
				System.out.println(i.getProductId());

				itemRepository.deleteById(new CartItemPK(i.getCart().getId(), i.getProductId()));
			});
			return true;
		}catch (Exception e){
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean deleteCart(Long cartId) {
		try {
			cartRepository.deleteById(cartId);
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).get();
		return cart;
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		return cart;
	}

	@Override
	public Cart saveCart(Cart cart) {
		return cartRepository.save(cart);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		UserDto userDto = restTemplate.exchange("http://localhost:8001".concat("/api/user/").concat(String.valueOf(username)),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();


		if(userDto == null){
			throw new UsernameNotFoundException("User not found in the database");
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		userDto.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(userDto.getUsername(), userDto.getPassword(), authorities);

	}
}