package seaFood.PTseafood.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;

import javax.xml.transform.Transformer;
import java.lang.annotation.Native;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class IRoleCustomRepository {
        @PersistenceContext
        private EntityManager entityManager;

//        public List<Role> getRole(User user){
//                StringBuilder sql = new StringBuilder().append("SELECT t.name as name from users u join user_role ur on u.id = ur.user_id " +
//                        "join roles r on r.id = ur.role_id");
//                sql.append("Where 1=1");
//                if(user.getEmail()!=null){
//                        sql.append(" and email=: email");
//                }
//                NativeQuery<Role> query = ((Session)entityManager.getDelegate()).createNativeQuery(sql.toString());
//                if(user.getEmail()!=null){
//                        query.setParameter("email", user.getEmail());
//                }
//                query.addScalar("name", StandardBasicTypes.STRING);
//                query.setResultTransformer(Transformers.aliasToBean(Role.class));
//                return query.list();
//        }
        public List<Role> getRole(User user) {
                StringBuilder sql = new StringBuilder()
                        .append("SELECT r.name as name FROM users u ")
                        .append("JOIN user_role ur ON u.id = ur.user_id ")
                        .append("JOIN roles r ON r.id = ur.role_id ")
                        .append("WHERE 1=1");

                if (user.getEmail() != null) {
                        sql.append(" AND u.email = :email");
                }

                NativeQuery<Role> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());

                if (user.getEmail() != null) {
                        query.setParameter("email", user.getEmail());
                }

                query.addScalar("name", StandardBasicTypes.STRING);
                query.setResultTransformer(Transformers.aliasToBean(Role.class));

                return query.list();
        }

}
