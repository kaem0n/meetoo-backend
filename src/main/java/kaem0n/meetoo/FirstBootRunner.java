package kaem0n.meetoo;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserPermissions;
import kaem0n.meetoo.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FirstBootRunner implements CommandLineRunner {
    @Autowired
    private UserDAO ud;
    @Autowired
    private PasswordEncoder bcrypt;

    @Override
    public void run(String... args) throws Exception {
        if (!ud.existsByUsername("admin")) {
            User defaultAdminAccount = new User(null, "admin", bcrypt.encode("admin"));
            defaultAdminAccount.setPermissions(UserPermissions.ADMIN);
            ud.save(defaultAdminAccount);
        }
    }
}
