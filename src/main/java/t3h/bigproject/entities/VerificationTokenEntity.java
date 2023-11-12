package t3h.bigproject.entities;

import t3h.bigproject.dto.BillDto;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "verification_token")
public class VerificationTokenEntity {
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@Column(name="token")
	private String token;

	@OneToOne(targetEntity = BillEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "bill_id", nullable = false, referencedColumnName = "id")
	private BillEntity billEntity;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="expiry_date")
	private Date expiryDate;

	public VerificationTokenEntity() {
		super();
	}

	public VerificationTokenEntity(final String token) {
		super();

		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	public VerificationTokenEntity(final String token, final BillEntity billEntity) {
		super();
		Calendar calendar = Calendar.getInstance();
		
		this.token = token;
		this.billEntity = billEntity;
		this.createdDate = new Date(calendar.getTime().getTime());
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public BillEntity getBillEntity() {
		return billEntity;
	}

	public void setBillEntity(final BillEntity billEntity) {
		this.billEntity = billEntity;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(calendar.getTime().getTime()));
		// calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		// calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(calendar.getTime().getTime());
	}

}
