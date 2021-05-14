package be.app.sb.configuration.security;


import be.app.sb.db.tables.pojos.Employee;
import be.app.sb.employees.EmployeeService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeService employeeService;

    public UserDetailsServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Load a user by username with specific authorities depending on its role
     * @Override of method loadUserByUserName from UserDetailsService
     * @param s the username
     * @return UserDetails model including username, password and authority)
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        Employee employeeToLogin = this.employeeService.findEmployeeByUserName(s);
        if(employeeToLogin.getRole().equals("HR")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_HR"));
            return new User(employeeToLogin.getUserName(), employeeToLogin.getPassword(), authorities);
        }
        else if(employeeToLogin.getRole().equals("DPM")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_DPM"));
            return new User(employeeToLogin.getUserName(), employeeToLogin.getPassword(), authorities);
        }
        else {
            authorities.add(new SimpleGrantedAuthority("ROLE_EM"));
            return new User(employeeToLogin.getUserName(), employeeToLogin.getPassword(), authorities);
        }
    }
}