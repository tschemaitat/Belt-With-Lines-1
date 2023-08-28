package Data_Structure_Benchmark;

public interface Belt_To_Manager {
	public void iterate();
	public int remove(int index);
	public boolean insert(int index, int item);
	public boolean has(int index);
	public void set_not_iterated();
	public void remove_output();
	public void set_output(Belt_To_Belt output, int output_index);
	public void print_items();
	public void print_debug_info();
}
