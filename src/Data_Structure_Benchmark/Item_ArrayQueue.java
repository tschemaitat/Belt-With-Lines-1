package Data_Structure_Benchmark;

public class Item_ArrayQueue implements Belt_To_Belt, Belt_To_Manager{
	public static final int empty_id = -1;
	public static int id_count = 1;
	
	private int[] items;
	private int start = 0;
	private int end;
	private boolean iterated = false;
	public String name = null;
	
	Belt_To_Belt output_item_array;
	int out_item_index;
	
	public Item_ArrayQueue(int length){
		name = ""+id_count;
		id_count++;
		items = new int[length];
		end = 0;
		for(int i = 0; i < items.length; i++){
			items[i] = empty_id;
		}
	}
	
	public void set_output(Belt_To_Belt output, int output_index){
		this.output_item_array = output;
		this.out_item_index = output_index;
	}
	
	public void remove_output(){
		this.output_item_array = null;
		this.out_item_index = empty_id;
	}
	
	public void set_not_iterated(){
		iterated = false;
	}
	
	public int index(int index){
		return (start + index) % items.length;
	}
	
	public boolean has(int index) {
		return empty_id == items[index(index)];
	}
	
	public void print_debug_info(){
		System.out.print("\t(start: " + start + "), (end: " + end + "), (items: ");
		System.out.print("\t");
		for(int i = 0; i < items.length; i++){
			System.out.print(item_string(items[i]));
		}
		System.out.println(")");
	}
	
	@Override
	public boolean insert(int index, int item) {
		System.out.println("("+name+")<inserting> (index: " + index + "), " + "(item: " + item + ")");
		print_debug_info();
		int item_index = index(index);
		if(items[item_index] != empty_id){
			System.out.println("\t<end inserting>");
			return false;
		}
		
		items[item_index] = item;
		if(end >= start){
			if(item_index > end || item_index < start){
				end = item_index;
			}
		}else{
			if(item_index > end && item_index < start){
				end = item_index;
			}
		}
		print_debug_info();
		System.out.println("\t<end inserting>");
		return true;
	}
	
	public int remove(int index) {
		int item_index = index(index);
		int item = items[item_index];
		items[item_index] = empty_id;
		return item;
	}
	
	@Override
	public void iterate() {
		
		if(iterated){
			return;
		}
		System.out.println("("+name+")<iterating>");
		print_debug_info();
		iterated = true;
		//if first item is empty, move all items
		if(items[start] == empty_id){
			System.out.println("\t"+name+"First spot empty");
			start = (start + 1)%items.length;
			return;
		}
		//try to output, if we outputted, move all items
		if(output_item_array != null){
			System.out.println("\t("+name+")iterating output array: ");
			output_item_array.iterate();
			boolean outputted = output_item_array.insert(out_item_index, items[start]);
			if(outputted){
				items[start] = empty_id;
				start = (start + 1)%items.length;
				System.out.println("\t("+name+")outputted");
				return;
			}
		}
		//now we know that at least the first item cannot move
		int first_empty_index = -1;
		finding_first_empty:
		if(end > start){
			for(int i = start; i <= end; i++){
				if(items[i] == empty_id){
					first_empty_index = i;
					break finding_first_empty;
				}
			}
		}else{
			for(int i = start; i < items.length; i++){
				if(items[i] == empty_id){
					first_empty_index = i;
					break finding_first_empty;
				}
			}
			for(int i = 0; i <= end; i++){
				if(items[i] == empty_id){
					first_empty_index = i;
					break finding_first_empty;
				}
			}
		}
		if(first_empty_index == -1)
			return;
		if(end > first_empty_index){
			for(int i = first_empty_index + 1; i <= end; i++){
				items[i - 1] = items[i];
			}
		}else{
			for(int i = first_empty_index + 1; i < items.length; i++){
				items[i - 1] = items[i];
			}
			items[items.length - 1] = items[0];
			for(int i = 1; i <= end; i++){
				items[i - 1] = items[i];
			}
		}
		end--;
		System.out.println("\t<finished iterating>");
		print_debug_info();
	}
	
	public boolean iterated() {
		return false;
	}
	
	public int size(){
		return items.length;
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
	
	public void print_items(){
		System.out.println("("+name+")<printing items>");
		int active_spots;
		if(end >= start)
			active_spots = 1 + end - start;
		else
			active_spots = 1 + size() - (start - end);
		System.out.print("\t");
		if(end >= start){
			for(int i = start; i <= end; i++){
				System.out.print(item_string(items[i]));
			}
		}else{
			for(int i = start; i < items.length; i++){
				System.out.print(item_string(items[i]));
			}
			for(int i = 0; i <= end; i++){
				System.out.print(item_string(items[i]));
			}
		}
		System.out.println();
		System.out.print("\t");
		for(int i = 0; i < active_spots; i++){
			if(i == 0){
				System.out.print(" s");
				continue;
			}
			if(i == active_spots - 1){
				System.out.print(" e");
				continue;
			}
			System.out.print("  ");
		}
		System.out.println();
		
	}
}
