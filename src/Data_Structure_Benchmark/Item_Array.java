package Data_Structure_Benchmark;

public class Item_Array implements Belt_To_Belt, Belt_To_Manager{
	private static int id_count = 1;
	private int[] items;
	boolean iterated = false;
	Belt_To_Belt output_belt = null;
	int output_belt_index = -1;
	int empty_id = -1;
	String name;
	
	public Item_Array(int length){
		
		name = ""+id_count;
		id_count++;
		items = new int[length];
		for(int i = 0; i < items.length; i++){
			items[i] = empty_id;
		}
	}
	@Override
	public boolean insert(int index, int item) {
		if(items[index] != empty_id){
			System.out.println("(" + name + ") cannot insert at ("+index+") not empty");
			return false;
		}
		System.out.println("(" + name + ") inserting at ("+index+")");
		items[index] = item;
		return true;
	}
	
	@Override
	public boolean has(int index) {
		return items[index] != empty_id;
	}
	
	@Override
	public void set_not_iterated() {
		iterated = false;
	}
	
	@Override
	public void remove_output() {
		output_belt = null;
		output_belt_index = -1;
	}
	
	@Override
	public void set_output(Belt_To_Belt output, int output_index) {
		this.output_belt = output;
		this.output_belt_index = output_index;
	}
	
	@Override
	public void print_items() {
		System.out.print("\t");
		System.out.print("(items: ");
		for(int i = 0; i < items.length; i++){
			System.out.print(item_string(items[i]));
		}
		System.out.println(")");
	}
	
	private String item_string(int item){
//		if(item < 0)
//			return ""+(item);
//		return " " + item;
//
		if(item < 0){
			return " -";
		}
		return " *";
	}
	
	@Override
	public void print_debug_info() {
		System.out.println("("+name+")<printing items>");
		System.out.print("\t");
		System.out.print("(items: ");
		for(int i = 0; i < items.length; i++){
			System.out.print(item_string(items[i]));
		}
		System.out.println(")");
	}
	
	@Override
	public void iterate() {
		if(iterated)
			return;
		System.out.println("("+name+")<iterating>");
		iterated = true;
		if(items[0] == empty_id){
			System.out.println("\t"+name+"First spot empty");
			for(int i = 0; i < items.length - 1; i++){
				items[i] = items[i + 1];
			}
			items[items.length - 1] = empty_id;
			return;
		}
		
		if(output_belt != null){
			System.out.println("\t("+name+")iterating output array: ");
			output_belt.iterate();
			boolean outputted = output_belt.insert(output_belt_index, items[0]);
			if(outputted){
				for(int i = 0; i < items.length - 1; i++){
					items[i] = items[i + 1];
				}
				items[items.length - 1] = empty_id;
				System.out.println("\t("+name+")outputted");
				return;
			}
		}
		int first_empty_index = -1;
		for(int i = 1; i < items.length; i++){
			if(items[i] == empty_id){
				first_empty_index = i;
				break;
			}
		}
		if(first_empty_index == -1)
			return;
		for(int i = first_empty_index; i < items.length - 1; i++){
			items[i] = items[i + 1];
		}
		items[items.length - 1] = -1;
		
		
	}
	
	@Override
	public int remove(int index) {
		int item = items[index];
		items[index] = -1;
		return item;
	}
}
