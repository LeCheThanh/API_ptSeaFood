package seaFood.PTseafood;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
//import seaFood.PTseafood.service.UserService;

import java.math.BigInteger;
import java.util.HashSet;
@SpringBootApplication
public class PTseafoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(PTseafoodApplication.class, args);
	}
//	@Bean
//	BCryptPasswordEncoder bCryptPasswordEncoder(){
//		return new BCryptPasswordEncoder();
//	}
//	@Bean
//	CommandLineRunner run(UserService userService){
//		return args -> {
//			userService.saveRole(new Role(null,"User"));
//			userService.saveRole(new Role(null,"Manager"));
//			userService.saveRole(new Role(null,"Admin"));
//
//			userService.saveUser(
//					new User(null,"chethanh3112@gmail.com","CheThanh Le",
//							"123456","091831213",false,
//							"quan 9","Vang",
//							10,new BigInteger("200000000"),new HashSet<>()));
//			userService.saveUser(
//					new User(null,"chethanh@gmail.com","CheThanh",
//							"123456789","091831213",true,
//							"quan 9","Vang",
//							20,new BigInteger("200000000"),new HashSet<>()));
//			userService.addtoUser("chethanh3112@gmail.com","User");
//			userService.addtoUser("chethanh@gmail.com","Admin");
//		};
//	}
}
