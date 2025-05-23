package kr.co.pei.pei_app.domain.repository.users;

import kr.co.pei.pei_app.admin.application.dto.users.UsersFindDTO;
import kr.co.pei.pei_app.domain.entity.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

    @Modifying
    @Query("update Users u set u.password = :password where u.username = :username")
    int updateTempPassword(@Param("password") String password, @Param("username") String username);

    @Query("select u.username from Users u where u.mail = :mail")
    String findUsernameByMail(@Param("mail") String mail);

    @Query("select u.username from Users u where u.phone = :phone")
    String findUsernameByPhone(@Param("phone") String phone);

    @Query("select new kr.co.pei.pei_app.admin.application.dto.users.UsersFindDTO(" +
            "u.id, u.username, u.name, u.phone, u.mail, u.createAt, u.roleType, h.name) " +
            "from Users u left join Hospital h on u.hospital.id = h.id")
    Page<UsersFindDTO> findAllUsers(Pageable pageable);
}
