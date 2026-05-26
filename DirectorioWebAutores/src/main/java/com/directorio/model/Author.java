package com.directorio.model;
 
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
 
@Entity
@Table(name = "author")
public class Author implements Serializable {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "name", nullable = false)
    private String name;
 
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = false)
    private Date birthDate;
 
    @Column(name = "phone", length = 9)
    private String phone;
 
    @ManyToOne
    @JoinColumn(name = "id_genre", referencedColumnName = "id")
    private LiteraryGenre genre;
 
    public Author() {}
 
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
 
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
 
    public LiteraryGenre getGenre() { return genre; }
    public void setGenre(LiteraryGenre genre) { this.genre = genre; }
}