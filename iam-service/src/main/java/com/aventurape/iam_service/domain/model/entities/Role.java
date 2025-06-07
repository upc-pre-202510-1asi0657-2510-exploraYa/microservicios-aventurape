package com.aventurape.iam_service.domain.model.entities;

import com.aventurape.iam_service.domain.model.valueobjects.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    public String getStringName() {
        return name.name();
    }
    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_ADVENTUROUS);
    }
    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }
    @Override
    public String getAuthority() {
        return name.name();
    }
}