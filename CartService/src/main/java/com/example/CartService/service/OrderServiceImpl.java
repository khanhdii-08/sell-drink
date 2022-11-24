package com.example.CartService.service;

import com.example.CartService.dto.WaterDto;
import com.example.CartService.entity.*;
import com.example.CartService.respository.CartItemRepository;
import com.example.CartService.respository.CartRepository;
import com.example.CartService.respository.OrderDetailRepository;
import com.example.CartService.respository.OrderRepository;
import com.example.CartService.utils.JwtTokenProvider;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository itemRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${app.host-product}")
	public String hostProduct;

    @Override
    public Order addOrder(Cart cart) throws Exception {

        boolean flag = true;

        List<CartItem> listItem = cart.getCartItems();

        Order order = Order.builder()
                .userId(cart.getUserId())
                .totalPrice(cart.getTotalPrice())
                .build();

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        List<Long> ids = new ArrayList<Long>();

        listItem.forEach(item -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
            ids.add(item.getProductId());
            listOrderDetail.add(orderDetail);
        });

        List<WaterDto> waterDtos = getListProductByIds(ids);

        for(int i =0; i< listOrderDetail.size(); ++i) {
            int quantity = listOrderDetail.get(i).getQuantity();
            int oldQuantity = waterDtos.get(i).getQuantity();
            if(oldQuantity - quantity < 0){
                flag = false;
            }
        }
        Order od = new Order();
        if(flag){
            od = orderRepository.save(order);

            List<OrderDetail> list = orderDetailRepository.saveAll(listOrderDetail);
            od.setOrderDetails(list);


            for(int i =0; i< listOrderDetail.size(); ++i) {
                int quantity = listOrderDetail.get(i).getQuantity();
                int oldQuantity = waterDtos.get(i).getQuantity();
                if(oldQuantity - quantity < 0){
                    throw new Exception("Khong du so luong");
                }
                waterDtos.get(i).setQuantity(oldQuantity - quantity);
            }
            updateWater(waterDtos);
            cart.setTotalPrice(0);
            cartRepository.save(cart);
            itemRepository.deleteAllByCartId(cart.getId());
        }
        return od;
    }

    public List<WaterDto> getListProductByIds(List<Long> ids){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + JwtTokenProvider.tokenJwt());
        Gson gson = new Gson();
        HttpEntity<String> water = new HttpEntity<String>(gson.toJson(ids),headers);

        WaterDto[] dtoArray= restTemplate.postForObject("http://"+hostProduct+":4000/v1/api/waters/ids", water, WaterDto[].class);
        List<WaterDto> result = Arrays.asList(dtoArray);
        return result;
    }


    public boolean updateWater( List<WaterDto> waterDtos){
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + JwtTokenProvider.tokenJwt());
        HttpEntity<List<WaterDto>> waters = new HttpEntity<List<WaterDto>>(waterDtos,headers);
        try {
            restTemplate.put("http://"+hostProduct+":4000/v1/api/waters", waters, WaterDto[].class);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }



    @Override
    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return null;
    }

    @Override
    public List<OrderDetail> saveOrderDetails(List<OrderDetail> orders) {
        return null;
    }

    @Override
    public boolean removeOrderDetail(OrderDetailPK detailPK) {
        return false;
    }

    @Override
    public boolean deleteOrder(Long orderId) {
        return false;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Order> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }
}
