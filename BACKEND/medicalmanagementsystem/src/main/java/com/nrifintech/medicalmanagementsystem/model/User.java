package com.nrifintech.medicalmanagementsystem.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.core.annotation.Order;

//import com.rishabh.converse.utility.annotations.EitherPatientorDoctor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Order(2)
@Data
@Entity
@Table(name="`user`")
public class User {
    
  @Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
@SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
  private Long id;
    
    
    
    @NotBlank( message = "Username must not be blank")
    @Column(unique=true, length=50, nullable=false)
    private String userName;

    
    @NotBlank( message = "Password must not be blank")
    @Column(nullable=false)
    private String password;


    


    

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id", referencedColumnName = "id", nullable=false)
    private Role role;
    
}
