package chat;

import java.util.Objects;

public class User {
    // Хранит имя пользователя(никнейм)
    private String userName;

    protected User(String userName) {
        this.userName = userName;
    }

    protected String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
