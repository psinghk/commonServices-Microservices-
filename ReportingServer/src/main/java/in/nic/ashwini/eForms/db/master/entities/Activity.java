package in.nic.ashwini.eForms.db.master.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "activity")
@Access(value = AccessType.FIELD)
@Data
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "email")
	private String email;
	@Column(name = "message")
	private String message;
	@CreationTimestamp
	@Column(name = "datetime")
	private Timestamp datetime;
}
