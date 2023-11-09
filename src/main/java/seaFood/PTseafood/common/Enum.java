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
    public enum OrderStatus {
        PENDING_CONFIRMATION("Chờ xác nhận"),
        SHIPPING("Đang giao"),
        COMPLETED("Hoàn thành"),
        CANCELED("Đã hủy");

        private final String name;

        OrderStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public enum PaymentStatus {
        PAID("Đã thanh toán"),
        UNPAID("Chưa thanh toán");

        private final String name;

        PaymentStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
