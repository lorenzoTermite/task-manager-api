package com.example.task_manager.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.task_manager.dto.UserCreateRequest;
import com.example.task_manager.dto.UserResponse;
import com.example.task_manager.entity.User;

@Component
public class UserMapper {


 public User toEntity(UserCreateRequest request){
      User user=new User();
      user.setEmail(request.email());
      user.setUsername(request.username());
      return user;

 } 

public UserResponse toResponse(User user){
     return new UserResponse(
    user.getId(),
    user.getEmail(),
    user.getUsername(),
    user.getTasks().size(),
    user.getCreatedAt()
     );
    
}

public List<UserResponse> toResponseList(List<User> users){
return users.stream().map(this::toResponse).toList();

}


}
