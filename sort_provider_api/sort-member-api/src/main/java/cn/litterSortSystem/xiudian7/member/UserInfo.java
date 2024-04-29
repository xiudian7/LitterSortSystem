package cn.litterSortSystem.xiudian7.member;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int userId;
    private String username;
    private String password;
    private int gender;


}
