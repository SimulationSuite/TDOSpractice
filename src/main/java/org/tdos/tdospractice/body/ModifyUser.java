package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ModifyUser {

    @NotBlank(message = "user_id can not null")
    @JsonProperty("user_id")
    public String useID;

    @NotBlank(message = "name can not null")
    @JsonProperty("name")
    public String name;

    @NotNull(message = "gender can not null")
    @JsonProperty("gender")
    public int gender;

    @NotBlank(message = "phone can not null")
    @JsonProperty("phone")
    public String phone;

    @NotBlank(message = "identification_number can not null")
    @JsonProperty("identification_number")
    public String identificationNumber;
}
