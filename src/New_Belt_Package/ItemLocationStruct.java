package New_Belt_Package;

public class ItemLocationStruct {
	public Item_In_List item;
	public int position;
	public int side;
	public Belt belt;
	
	public ItemLocationStruct(Item_In_List item, int position, int side, Belt belt){
		this.item = item;
		this.position = position;
		this.side = side;
		this.belt = belt;
	}
	
}
