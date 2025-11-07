package com.app.shopsense.doas.entities.user;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;
    // Email verification

    private String pendingEmail;
    private boolean emailVerified;


    private int failedAttempts = 0;
    private boolean accountLocked = false;

    public static final int MAX_FAILED_ATTEMPTS = 5;

    public static final long LOCK_TIME_DURATION = 15 * 60 * 1000;
    private Instant lockTime;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    private Set<String> roles = new HashSet<>();


    public User(Long id, String email, String password, String pendingEmail, boolean emailVerified, int failedAttempts, boolean accountLocked, Instant lockTime, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.pendingEmail = pendingEmail;
        this.emailVerified = emailVerified;
        this.failedAttempts = failedAttempts;
        this.accountLocked = accountLocked;
        this.lockTime = lockTime;
        this.roles = roles;
    }

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPendingEmail() {
        return this.pendingEmail;
    }

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    public boolean isAccountLocked() {
        return this.accountLocked;
    }

    public Instant getLockTime() {
        return this.lockTime;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPendingEmail(String pendingEmail) {
        this.pendingEmail = pendingEmail;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setLockTime(Instant lockTime) {
        this.lockTime = lockTime;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$pendingEmail = this.getPendingEmail();
        final Object other$pendingEmail = other.getPendingEmail();
        if (this$pendingEmail == null ? other$pendingEmail != null : !this$pendingEmail.equals(other$pendingEmail))
            return false;
        if (this.isEmailVerified() != other.isEmailVerified()) return false;
        if (this.getFailedAttempts() != other.getFailedAttempts()) return false;
        if (this.isAccountLocked() != other.isAccountLocked()) return false;
        final Object this$lockTime = this.getLockTime();
        final Object other$lockTime = other.getLockTime();
        if (this$lockTime == null ? other$lockTime != null : !this$lockTime.equals(other$lockTime)) return false;
        final Object this$roles = this.getRoles();
        final Object other$roles = other.getRoles();
        if (this$roles == null ? other$roles != null : !this$roles.equals(other$roles)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $pendingEmail = this.getPendingEmail();
        result = result * PRIME + ($pendingEmail == null ? 43 : $pendingEmail.hashCode());
        result = result * PRIME + (this.isEmailVerified() ? 79 : 97);
        result = result * PRIME + this.getFailedAttempts();
        result = result * PRIME + (this.isAccountLocked() ? 79 : 97);
        final Object $lockTime = this.getLockTime();
        result = result * PRIME + ($lockTime == null ? 43 : $lockTime.hashCode());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", email=" + this.getEmail() + ", password=" + this.getPassword() + ", pendingEmail=" + this.getPendingEmail() + ", emailVerified=" + this.isEmailVerified() + ", failedAttempts=" + this.getFailedAttempts() + ", accountLocked=" + this.isAccountLocked() + ", lockTime=" + this.getLockTime() + ", roles=" + this.getRoles() + ")";
    }
}
