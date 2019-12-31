package com.fast.springboot.basic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author bowen.yan
 * @since 2019-12-25
 */
@NoArgsConstructor(staticName = "of")
@Accessors(fluent = true)
@Data
public class User {
    private String userName;
    private String mobile;

    public User(User rawUser) {
        this.userName = rawUser.userName() + "-1";
        this.mobile = rawUser.mobile + "-1";
    }
}
