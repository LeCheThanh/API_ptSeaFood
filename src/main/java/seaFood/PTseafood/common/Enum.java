package seaFood.PTseafood.common;

public class Enum {
    public enum Rank {
        unRank, Silver, Gold, Platinum
    }
    public enum Role{
        User("User"),

        Admin("Admin"),

        Manager("Manager");

        private String name;

        Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Provider{
        LOCAL,
        GOOGLE
    }
}
