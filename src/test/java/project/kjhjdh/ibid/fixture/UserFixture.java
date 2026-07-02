package project.kjhjdh.ibid.fixture;

import project.kjhjdh.ibid.user.domain.User;

public class UserFixture {

    public static UserBuilder user() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private String email = "user@example.com";
        private String password = "encoded-password";
        private String username = "tester";

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public User build() {
            return User.create(email, password, username);
        }
    }
}
