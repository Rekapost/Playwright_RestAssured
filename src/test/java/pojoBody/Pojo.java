package pojoBody;

public class Pojo {
	private String id;
	private String name;
	private String email;
	private String gender;
	private String status;
	
	
	// Cannot construct instance of `pojoBody.Pojo` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
	public Pojo() {
		
	}
	
	public Pojo( String name, String email, String gender, String status) {
		
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Pojo {" +
	            " id='" + id +'\''+
				", name='" + name + '\''+
				", email='" + email + '\''+
				", gender='" + gender + '\''+
				", status='" + status + '\''+
				 '}';
	}

	

	
	
	
}
