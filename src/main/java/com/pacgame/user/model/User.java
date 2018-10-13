package com.pacgame.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pacgame.main.model.ResourceInterface;
import com.pacgame.main.validation.FieldMatch;
import com.pacgame.main.validation.UniqueUsername;
import com.pacgame.main.validation.ValidChangePassword;
import com.pacgame.main.validation.group.PasswordChange;
import com.pacgame.main.validation.group.Registration;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table( name = "user" )
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize()
@NamedQuery(name="User.findAllWhereIsAsdInFirstName", query="SELECT u from User u left join u.userDetails ud where ud.firstName LIKE '%asd%'")
//@JsonIgnoreProperties({"id", "firstName"})
//@JsonPropertyOrder({ "name", "id" })
@FieldMatch(first = "password", second = "confirmPassword", message = "{validation.user.fieldMatch}", groups = {Registration.class, PasswordChange.class})
@ValidChangePassword(oldPasswordField = "oldPassword", passwordField = "password", confirmPasswordField = "confirmPassword", groups = {PasswordChange.class})
public class User implements ResourceInterface, Serializable {

    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Version
    private Long version;

    @Transient
    @NotEmpty(groups = {PasswordChange.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String oldPassword;

    @NotEmpty(groups = {Registration.class}, message = "{validation.user.password.notEmpty}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @NotEmpty(groups = {Registration.class, PasswordChange.class}, message = "{validation.user.confirmPassword.notEmpty}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    @NotEmpty(groups = {Registration.class} ,message = "{validation.user.username.notEmpty}")
    @UniqueUsername(groups = {Registration.class}, message = "{validation.user.username.unique}")
    private String username;

    @NotNull
    private boolean enabled;

    private boolean tokenExpired;

    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false, orphanRemoval = true)
    private UserDetails userDetails;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "user_has_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles;

    public User() {
        setRoles(new HashSet<Role>());
    }

    public User(String username, String password, boolean enabled, UserDetails userDetails, Set<Role> roles) {
        this();
        this.password = password;
        this.username = username;
        this.enabled = enabled;
        setUserDetails(userDetails);
        setRoles(roles);
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore()
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore()
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore()
    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public User setRoles(Set<Role> roles)
    {
        this.roles = roles;

        return this;

    }

    public User addRole(Role role)
    {
        roles.add(role);

        return this;

    }

    public User removeRole(Role role)
    {
        roles.remove(role);

        return this;

    }

    @JsonIgnore
    public Set<Role> getRoles()
    {
        return roles;

    }

    public String getUserRoles()
    {
        String rolesStr = roles.stream().map(e -> e.getName()).collect(
            Collectors.joining(", ")
        );

        return rolesStr;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        userDetails.setUser(this);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
