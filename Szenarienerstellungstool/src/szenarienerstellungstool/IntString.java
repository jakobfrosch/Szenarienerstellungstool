package szenarienerstellungstool;

public class IntString {
	private String name;
    private long id;
    private int level;
    public IntString(String name,long l,int level) {
        this.setName(name);
        this.setId(l);
        this.setLevel(level);
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	

}
