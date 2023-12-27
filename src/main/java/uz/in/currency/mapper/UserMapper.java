package uz.in.currency.mapper;

import org.mapstruct.*;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDtoToUser(UserDTO userDto);

}
