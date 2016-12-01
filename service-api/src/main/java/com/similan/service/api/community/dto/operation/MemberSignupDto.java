package com.similan.service.api.community.dto.operation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.service.api.base.dto.operation.OperationDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberSignupDto extends OperationDto {
  String firstName;
  String lastName;
  String username;
  String password;
  String email;
}
