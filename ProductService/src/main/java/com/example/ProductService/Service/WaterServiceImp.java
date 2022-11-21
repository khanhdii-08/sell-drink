package com.example.ProductService.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.example.ProductService.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Water;
import com.example.ProductService.repository.WatterRepository;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class WaterServiceImp implements WaterService, UserDetailsService {
	
	@Autowired
	private WatterRepository watterRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Water getOneWatter(Long id) {
		// TODO Auto-generated method stub
		return watterRepository.findById(id).get();
	}

	@Override
	public List<Water> getAllWatter() {
		// TODO Auto-generated method stub
		return watterRepository.findAll();
	}

	@Override
	public Water addOrUppdateWatter(Water watter) {
		// TODO Auto-generated method stub
		return watterRepository.save(watter);
	}

	@Override
	public boolean deleteWate(Long id) {
		// TODO Auto-generated method stub
		Water wt = watterRepository.findById(id).get();
		if(wt != null) {
			watterRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		UserDto userDto = restTemplate.exchange("http://localhost:8001/".concat("/").concat(String.valueOf(username)),
				HttpMethod.GET,
				entity,
				UserDto.class
		).getBody();


		if(userDto == null){
			throw new UsernameNotFoundException("User not found in the database");
		}

		System.out.printf("dto" + userDto);

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		userDto.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(userDto.getUsername(), userDto.getPassword(), authorities);

	}
}
