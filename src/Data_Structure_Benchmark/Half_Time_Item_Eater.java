package Data_Structure_Benchmark;

public class Half_Time_Item_Eater implements Belt_To_Belt{
	int interval;
	int current_count = 0;
	public Half_Time_Item_Eater(int interval){
		this.interval = interval;
	}
	@Override
	public boolean insert(int index, int item) {
		//5
		//01234-56789
		current_count = (current_count + 1) % (interval*2);
		if(current_count >= interval)
			return true;
		return false;
	}
	
	@Override
	public void iterate() {
	
	}
}
