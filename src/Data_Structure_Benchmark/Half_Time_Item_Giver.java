package Data_Structure_Benchmark;

public class Half_Time_Item_Giver implements Belt_To_Belt{
	int interval;
	int current_count = 0;
	Belt_To_Belt output;
	int output_index;
	int item;
	public Half_Time_Item_Giver(int interval, Belt_To_Belt output, int output_index, int item){
		this.interval = interval;
		this.output = output;
		this.output_index = output_index;
		this.item = item;
	}
	@Override
	public boolean insert(int index, int item) {
		//5
		//01234-56789
		
		return false;
	}
	
	@Override
	public void iterate() {
		current_count = (current_count + 1) % (interval*2);
		if(current_count >= interval){
			output.insert(output_index, item);
		}
	}
}
