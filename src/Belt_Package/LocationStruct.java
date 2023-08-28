package Belt_Package;

public class LocationStruct {
	public int position;
	public int side;
	public Belt belt;
	
	public LocationStruct(int position, int side, Belt belt){
		this.position = position;
		this.side = side;
		this.belt = belt;
	}
	public String toString(){
		return "<pos: "+position+", s: " + side+", b: "+belt+">";
	}
}
