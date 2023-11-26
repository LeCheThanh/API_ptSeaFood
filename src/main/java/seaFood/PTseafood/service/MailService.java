package seaFood.PTseafood.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.utils.NumberUtil;

import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private CartService cartService;
    public void sendConfirmationEmail(Order order, User user) {
        String receiverEmail = order.getReceiverEmail();
        String receiverName = order.getReceiverName();
        String receiverPhone = order.getReceiverPhone();
        String receiverAddress = order.getReceiverAddress();
        Double finalPrice = order.getFinalPrice();
        String code = order.getCode();
        StringBuilder strProduct = new StringBuilder();
        double totalPrice = 0.0;
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();
            double price = cartItem.getTotal();
            totalPrice += price;
            strProduct.append("<tr>");
            strProduct.append("<td>").append(variant.getName()).append("</td>");
            strProduct.append("<td>").append(cartItem.getQuantity()).append("</td>");
            strProduct.append("<td>").append(NumberUtil.formatNumber(price)).append("</td>");
            strProduct.append("</tr>");
        }


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(receiverEmail);
            helper.setSubject("Xác nhận đơn hàng");
            Resource resource = new ClassPathResource("templates/order/confirmation_template.html");
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String htmlTemplate = new String(byteArray, StandardCharsets.UTF_8);
            htmlTemplate = htmlTemplate.replace("${customerName}", receiverName);
            htmlTemplate = htmlTemplate.replace("${customerPhone}", receiverPhone);
            htmlTemplate = htmlTemplate.replace("${customerEmail}", receiverEmail);
            htmlTemplate = htmlTemplate.replace("${customerAddress}", receiverAddress);
            htmlTemplate = htmlTemplate.replace("${code}", code);
            htmlTemplate = htmlTemplate.replace("${orderVariants}", strProduct);
            htmlTemplate = htmlTemplate.replace("${tongTien}", NumberUtil.formatNumber(finalPrice));
            helper.setText(htmlTemplate, true);

            // Gửi email
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
        }
    }
}
